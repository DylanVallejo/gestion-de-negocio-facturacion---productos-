package com.api.gestor.service;

import com.api.gestor.pojo.Factura;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FacturaService {

    ResponseEntity<String> generateReport(Integer id) throws Exception;

    ResponseEntity<List<Factura>> getFacturas();

    ResponseEntity<String> guardarFactura(Map<String, Object> requestMap);

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    ResponseEntity<String> deleteFactura(Integer id);

}
