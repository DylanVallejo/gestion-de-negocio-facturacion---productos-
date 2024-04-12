package com.api.gestor.service;

import com.api.gestor.dao.FacturaDAO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface FacturaService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

}
