package com.api.gestor.security.jwt;


import com.api.gestor.security.CustomerDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    Claims claims = null;

    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        "/api/v1/user/login","/api/v1/user/signUp","/api/v1/user/forgotPassword"
        if(request.getServletPath().matches("api/v1/user/login|api/v1/user/forgotPassword|api/v1/user/signUp")){
            filterChain.doFilter(request, response);;
        }else {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUserName(token);
                claims = jwtUtil.extractAllClaims(token);
            }
//el aplicativo se daña si se solicita incorrectamnete a un endpoint verificar esto ya que pide un token
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = customerDetailsService.loadUserByUsername(userName);
                if(jwtUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public Boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }

    public Boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }

    public String getCurrentUser(){
        return userName;
    }
}
