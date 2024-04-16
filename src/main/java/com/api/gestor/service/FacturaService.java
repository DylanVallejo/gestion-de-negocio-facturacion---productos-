package com.api.gestor.service;

import com.api.gestor.dao.FacturaDAO;
import com.api.gestor.pojo.Factura;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FacturaService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);


    ResponseEntity<List<Factura>> getFacturas();

}
