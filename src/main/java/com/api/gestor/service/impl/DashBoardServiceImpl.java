package com.api.gestor.service.impl;

import com.api.gestor.dao.CategoriaDAO;
import com.api.gestor.dao.FacturaDAO;
import com.api.gestor.dao.ProductoDao;
import com.api.gestor.service.DashBoardService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    ProductoDao productoDao;

    @Autowired
    CategoriaDAO categoriaDAO;

    @Autowired
    FacturaDAO facturaDAO;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {

        Map<String, Object> map = new HashMap<>();
//        count es un metdo que extiende de jpa y nos permite obtener la cantidad de registros
        map.put("producto", productoDao.count());
        map.put("categorias", categoriaDAO.count());
        map.put("facturas", facturaDAO.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
