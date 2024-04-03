package com.api.gestor.rest;


import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.pojo.Categoria;
import com.api.gestor.service.CategoriaService;
import com.api.gestor.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/categoria")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;


    @GetMapping("/getAll")
    public ResponseEntity<List<Categoria>> getAllCategorias(@RequestBody(required = false) String valueFilter){
        try{
            return  categoriaService.getAllCategorias(valueFilter);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add")
    public ResponseEntity<String>  agregarCategoria(@RequestBody(required = true) Map<String, String> requestMap){

        try{
            return  categoriaService.agregarCategoria(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
