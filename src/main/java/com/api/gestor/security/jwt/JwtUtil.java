package com.api.gestor.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private Key secret;
    /*
        Este método es anotado con @PostConstruct, lo que significa que se ejecutará después de que la
        instancia de la clase haya sido construida y todas las dependencias hayan sido inyectadas.
        En este método, se inicializa la clave secreta que se utilizará para firmar y verificar los
        tokens JWT. Se genera una clave aleatoria utilizando SecureRandom() y se asigna a la variable secret.
        ahora la llave secreta se generara automaticamente mediante los metodos proporcionados
    */
    @PostConstruct
    protected void init(){
        byte[] apiKeySecretBytes = new byte[64]; //512 bits
        new SecureRandom().nextBytes(apiKeySecretBytes);
        secret = Keys.hmacShaKeyFor(apiKeySecretBytes);
    }

    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /*
        Este método verifica y extrae todos los reclamos del token JWT. Utiliza la clave secreta para
        verificar la firma del token y luego extrae los reclamos del payload.
    */

//firmamos el token  validamos el token y obtenemos su cuerpo
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getEncoded()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//validamos fecha
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //genera un token
    public String generateToken(String username, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 100 * 60 *60 *10))
                .signWith(Keys.hmacShaKeyFor(secret.getEncoded())).compact();
    }

//    validacion vigencia del token.
    public Boolean validateToken(String token , UserDetails userDetails){
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired((token)));
    }

}
