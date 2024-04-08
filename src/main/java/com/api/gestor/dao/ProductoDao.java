package com.api.gestor.dao;

import com.api.gestor.pojo.Producto;
import com.api.gestor.wrapper.ProductoWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoDao extends JpaRepository<Producto, Integer> {

    List<ProductoWrapper> getAllProducts();
}
