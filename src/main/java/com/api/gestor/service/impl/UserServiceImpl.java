package com.api.gestor.service.impl;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.UserDAO;
import com.api.gestor.pojo.User;
import com.api.gestor.security.CustomerDetailsService;
import com.api.gestor.security.jwt.JwtFilter;
import com.api.gestor.security.jwt.JwtUtil;
import com.api.gestor.service.UserService;
import com.api.gestor.util.FacturaUtils;
import com.api.gestor.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("registro interno de un usuario {}", requestMap);
        try{
            if(validateSignUpMap((requestMap))){
                User user = userDAO.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    User userdb = userDAO.save(getUserFromMap(requestMap));
                    return FacturaUtils.getResponseEntity("Usuario registrado con exito: " + userdb.getNombre(), HttpStatus.CREATED);
                }else{
                    return FacturaUtils.getResponseEntity("El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);
                }

            }else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Dentro de login");
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );

            if(authentication.isAuthenticated()){
                if(customerDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>(
                            "{\"token\":\"" +
                                    jwtUtil.generateToken(customerDetailsService.getUserDetail().getEmail(),
                                            customerDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>("{\"mensaje\":\""+" Espera la aprobación del administrador "+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception exception){
            log.error("{}",exception);
        }
        return new ResponseEntity<String>("{\"mensaje\":\""+" Credenciales incorrectas "+"\"}",HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {

        try {
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDAO.getAllUsers(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> optionalUser = userDAO.findById(Integer.parseInt(requestMap.get("id")));
                if(!optionalUser.isEmpty()){
                    userDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return FacturaUtils.getResponseEntity("Status del usuario actualizado", HttpStatus.OK);
                }else {
                    FacturaUtils.getResponseEntity("El usuario no existe", HttpStatus.NOT_FOUND);
                }

            }else {
                FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();

        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if (
                requestMap.containsKey("nombre")
                && requestMap.containsKey("numero_de_contacto")
                && requestMap.containsKey("email")
                && requestMap.containsKey("password")
        ){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setNombre(requestMap.get("nombre"));
        user.setNumero_de_contacto(requestMap.get("numero_de_contacto"));
        user.setEmail(requestMap.get("email"));
/*        se debe encriptar el password al guardarlo en base los usuarios enteriores sin encriptar haran que salte la excepion credenciales incorrectas */
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

}
