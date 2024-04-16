package com.api.gestor.dao;

import com.api.gestor.pojo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FacturaDAO extends JpaRepository<Factura, Integer> {

    List<Factura> getFacturas();

    List<Factura> getFacturasByUserName(@Param("username") String username);

}
