package com.api.gestor.service;

import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductoService {

    ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap);

    ResponseEntity<List<ProductoWrapper>> getAllProducts();
}
