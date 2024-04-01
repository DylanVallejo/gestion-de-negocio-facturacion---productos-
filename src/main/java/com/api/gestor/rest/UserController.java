package com.api.gestor.rest;

import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.pojo.User;
import com.api.gestor.service.UserService;
import com.api.gestor.util.FacturaUtils;
import com.api.gestor.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/signUp")
    public ResponseEntity<String> registrarUsuario(@RequestBody(required = true)Map<String, String> requestMap){
            try{
                return userService.signUp(requestMap);
            }catch (Exception e){
                e.printStackTrace();
            }
            return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true)  Map<String, String> requestMap) throws Exception {

        try{
            return userService.login(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserWrapper>> getAllUser(){
        try{
            return userService.getAllUsers();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @GetMapping("/getAdmins")
//    public ResponseEntity<List<User>> getAllAdmins(){
//        try{
//            return userService.getAllAdmins();
//        }catch (Exception exception){
//            exception.printStackTrace();
//        }
//        return new ResponseEntity<List<User>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody(required = true) Map<String, String> requestMap){

        try{
            return userService.updateUser(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
