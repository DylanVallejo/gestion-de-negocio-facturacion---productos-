package com.api.gestor.service;


import com.api.gestor.pojo.Categoria;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface CategoriaService {

    ResponseEntity<String> agregarCategoria(Map<String,String> requestMap);

    ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter);


}
