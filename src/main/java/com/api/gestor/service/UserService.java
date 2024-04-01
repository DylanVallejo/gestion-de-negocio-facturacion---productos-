package com.api.gestor.service;


import com.api.gestor.pojo.User;
import com.api.gestor.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<String> signUp(Map<String, String> requestMap);


    ResponseEntity<String> login(Map<String, String> requestMap);


    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> updateUser(Map<String, String> requestMap);

//    ResponseEntity<List<UserWrapper>> getAllAdmins();

//    ResponseEntity<List<String>> getAllAdmins();



}
