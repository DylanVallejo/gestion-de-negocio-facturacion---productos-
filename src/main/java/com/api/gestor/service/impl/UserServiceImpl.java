package com.api.gestor.service.impl;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.UserDAO;
import com.api.gestor.pojo.User;
import com.api.gestor.service.UserService;
import com.api.gestor.util.FacturaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDAO userDAO;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("registro interno de un usuario {}", requestMap);
        try{
            if(validateSignUpMap((requestMap))){
                User user = userDAO.findByEmail(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDAO.save(getUserFromMap(requestMap));
                    return FacturaUtils.getResponseEntity("Usuario registrado con exito", HttpStatus.CREATED);
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


    private boolean validateSignUpMap(Map<String, String> requestMap){
        if (
                requestMap.containsKey("nombre")
                && requestMap.containsKey("numeroDeContacto")
                && requestMap.containsKey("email")
                && requestMap.containsKey("pasword")
        ){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setNombre(requestMap.get("nombre"));
        user.setNumeroDeContacto(requestMap.get("numeroDeContacto"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("pasword"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }


}
