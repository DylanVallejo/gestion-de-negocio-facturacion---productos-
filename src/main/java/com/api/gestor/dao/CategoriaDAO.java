package com.api.gestor.dao;


import com.api.gestor.pojo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CategoriaDAO extends JpaRepository<Categoria, Integer> {

//    List<Categoria> getAllCategorias();

}
