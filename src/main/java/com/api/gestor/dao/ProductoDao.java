package com.api.gestor.dao;

import com.api.gestor.pojo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//no es necesaria esta notacion
public interface ProductoDao extends JpaRepository<Producto, Integer> {


}
