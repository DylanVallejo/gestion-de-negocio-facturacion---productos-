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


    @PostMapping("/generarReporte/{id}")
    ResponseEntity<String> generarReporte(@PathVariable("id") Integer id){
        try {
            return facturaService.generateReport(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/guardarFactura")
    ResponseEntity<String> guardarFactura(@RequestBody Map<String, Object> requestBody){
        try {
            return facturaService.guardarFactura(requestBody);
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

    @PostMapping("/getPdf")
    public ResponseEntity<byte[]> obtenerPdf(@RequestBody Map<String,Object> requestMap){
        try {
            return  facturaService.getPdf(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
//en lugar de eliminar una factura deberia existir un estado de activa o inactiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> borrarFactura(@PathVariable("id") Integer id){
        try {
            return facturaService.deleteFactura(id);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
