package com.api.gestor.rest;


import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.pojo.Factura;
import com.api.gestor.service.FacturaService;
import com.api.gestor.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/factura")
public class FacturaController {

    @Autowired
    FacturaService facturaService;


    @PostMapping("/generarReporte")
    ResponseEntity<String> generarReporte(@RequestBody Map<String, Object>requestMap){
        try {
            return facturaService.generateReport(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/getFacturas")
    public ResponseEntity<List<Factura>> listarFacturas(){
        try {
            return  facturaService.getFacturas();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
