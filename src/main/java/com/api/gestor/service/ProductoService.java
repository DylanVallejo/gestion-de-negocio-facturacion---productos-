package com.api.gestor.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductoService {

    ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap);
}
