package com.api.gestor.dao;

import com.api.gestor.pojo.Producto;
import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductoDao extends JpaRepository<Producto, Integer> {

    List<ProductoWrapper> getAllProducts();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    ProductoWrapper getProductoPorNombre(@Param("nombre") String nombre);

    List<ProductoWrapper>   getProductosAsc();

    List<ProductoWrapper>   getProductosDesc();
}
