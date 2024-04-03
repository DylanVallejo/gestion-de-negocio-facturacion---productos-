package com.api.gestor.service.impl;


import com.api.gestor.constantes.FacturaConstantes;
import com.api.gestor.dao.CategoriaDAO;
import com.api.gestor.dao.UserDAO;
import com.api.gestor.pojo.Categoria;
import com.api.gestor.security.jwt.JwtFilter;
import com.api.gestor.service.CategoriaService;
import com.api.gestor.util.FacturaUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    UserDAO userDAO;

    @Autowired
    CategoriaDAO categoriaDAO;

    @Autowired
    JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> agregarCategoria(Map<String, String> requestMap) {
        log.info("Creando una categoria {}", requestMap);
        try{
            Categoria categoria = new Categoria();
            if (jwtFilter.isAdmin() ){
                if (validarCategoria(requestMap, false)){
                    categoriaDAO.save(getCategoriaFromMap(requestMap, false));
                }
                return FacturaUtils.getResponseEntity("Categoria " + categoria.getNombre() + " creada con exito." , HttpStatus.OK );
//                if (validarCategoria(requestMap)){
//                categoria.setNombre(requestMap.get("nombre"));
            }else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<String>(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Categoria>> getAllCategorias( String valueFilter) {

        try{
            if (!Strings.isNullOrEmpty(valueFilter) && valueFilter.equalsIgnoreCase("true")){
                return new ResponseEntity<>(categoriaDAO.findAll(), HttpStatus.OK);
            }
//            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(categoriaDAO.findAll(), HttpStatus.OK);
//            }else{
//                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
//            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validarCategoria(Map<String, String> requestMap, Boolean validateId){
        if (requestMap.containsKey("nombre")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }
            return true;
        }
        return false;
    }

    private Categoria getCategoriaFromMap(Map<String, String> requestMap, Boolean isAdd){
        Categoria categoria = new Categoria();
        if (isAdd){
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNombre(requestMap.get("nombre"));
        return categoria;

    }


}
