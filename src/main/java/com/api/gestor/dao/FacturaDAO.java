package com.api.gestor.dao;

import com.api.gestor.pojo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaDAO extends JpaRepository<Factura, Integer> {



}
