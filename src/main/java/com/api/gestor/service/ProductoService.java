package com.api.gestor.service;

import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface ProductoService {

    ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap);

    ResponseEntity<List<ProductoWrapper>> getAllProducts();

    ResponseEntity<String> actualizarProducto(@RequestBody Map<String, String> requestMap);

    ResponseEntity<String> actualizarProductoParcial(ProductoWrapper productoWrapper, Integer id);

    ResponseEntity<String> eliminarProducto(Integer id);

    ResponseEntity<String> updateStatus(ProductoWrapper status, Integer id);

    ResponseEntity<ProductoWrapper> buscarProductoPorNombre(String nombreProducto);

    ResponseEntity<List<ProductoWrapper>> obtenerPorOrdenDeFecha(String ordenar);

}
