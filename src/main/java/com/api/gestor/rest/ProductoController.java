package com.api.gestor.rest;


import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.service.ProductoService;
import com.api.gestor.util.FacturaUtils;
import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    @Autowired
    ProductoService productoService;


    @PostMapping("/add")
    ResponseEntity<String> agregarProducto(@RequestBody Map<String, String> requestMap){
        try {
           return productoService.addNuevoProducto(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list")
    ResponseEntity<List<ProductoWrapper>> getAllProducts(){
        try{
            return productoService.getAllProducts();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<List<ProductoWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    ResponseEntity<String> actualizarProducto(@RequestBody Map<String, String> requestMap){
        try {
            return productoService.actualizarProducto(requestMap);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/updatePut/{id}")
    ResponseEntity<String> actualizarProductoParcial(@PathVariable("id") Integer id ,@RequestBody ProductoWrapper productoWrapper){
        try {
            return productoService.actualizarProductoParcial(productoWrapper,id);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> eliminarProducto(@PathVariable("id") Integer id){
        try {
            return productoService.eliminarProducto(id);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/status/{id}")
    ResponseEntity<String> updateStatus(@RequestBody ProductoWrapper status , @PathVariable("id") Integer id){
        try {
            return productoService.updateStatus(status, id);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/nombre")
    ResponseEntity<ProductoWrapper> buscarProductoPorNombre(@RequestParam String nombreProducto){
        try {
            return productoService.buscarProductoPorNombre(nombreProducto);
        } catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list/orden")
    ResponseEntity<List<ProductoWrapper>> obtenerPorOrdenDeFecha(@RequestParam String orden) {
        try{
            return productoService.obtenerPorOrdenDeFecha(orden);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
