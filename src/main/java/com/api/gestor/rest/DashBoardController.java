package com.api.gestor.rest;


import com.api.gestor.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(path = "/api/v1/dashboard")
@CrossOrigin(origins = "*")
public class DashBoardController {

    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping("/detalles")
    public ResponseEntity<Map<String,Object>> getCount(){
        try {
            return dashBoardService.getCount();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
