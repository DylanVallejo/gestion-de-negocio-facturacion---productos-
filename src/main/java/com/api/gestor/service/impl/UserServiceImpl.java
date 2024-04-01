package com.api.gestor.service.impl;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.UserDAO;
import com.api.gestor.pojo.User;
import com.api.gestor.security.CustomerDetailsService;
import com.api.gestor.security.jwt.JwtFilter;
import com.api.gestor.security.jwt.JwtUtil;
import com.api.gestor.service.UserService;
import com.api.gestor.util.EmailUtils;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private EmailUtils emailUtils;

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
                    enviarCorreoAdministradores(requestMap.get("status"), optionalUser.get().getEmail(), userDAO.getAllAdmins());
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

    @Override
    public ResponseEntity<String> checkToken() {
        return FacturaUtils.getResponseEntity("true", HttpStatus.OK);
    }


// explicar la falla y la correccion
    @Override
    public ResponseEntity<String> newPassword(Map<String, String> requestMap) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        try{
            User user = userDAO.findByEmail(jwtFilter.getCurrentUser());
            if (!user.equals(null)){
                if (encoder.matches(requestMap.get("oldPassword"), user.getPassword())){
/*                    esto trata de comaprar el password codificado contra el no codificado por lo que no pasa se instancia la clase
                    BCryptPasswordEncoder que posee el metodo matches que recibe el password directo y lo compara contra el encriptado */
//                if(encoder.user.getPassword().equals(encoder.encode(requestMap.get("oldPassword")))){
                    user.setPassword(encoder.encode(requestMap.get("newPassword")));
                    userDAO.save(user);
                    return FacturaUtils.getResponseEntity("Contraseña actualizada con exito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity("Contraseña incorrecta", HttpStatus.BAD_REQUEST);
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.BAD_REQUEST);
        }catch (Exception exception){
            exception.printStackTrace();

        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @Override
//    public ResponseEntity<List<User>> getAllAdmins() {
//            try{
//                if(jwtFilter.isAdmin()){
//                    return new ResponseEntity<>(userDAO.getAllAdmins(), HttpStatus.OK);
//                }else{
//                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
//                }
//            }catch (Exception exception){
//                exception.printStackTrace();
//            }
//        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    private void enviarCorreoAdministradores(String status, String user, List<String> allAdmins){
        allAdmins.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Cuenta aprobada", "USUARIO : "+ user + "\n es aprobado por  \nADMIN :"+jwtFilter.getCurrentUser(), allAdmins );
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Cuenta no aprobada", "USUARIO : "+ user + "\n es no aprobado por  \nADMIN :"+jwtFilter.getCurrentUser(), allAdmins );
        }

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
