package com.api.gestor.dao;

import com.api.gestor.pojo.User;
import com.api.gestor.wrapper.UserWrapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {


    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUsers();


    List<String> getAllAdmins();


    @Transactional
    @Modifying //indica que seran operaciones insert update delete no de tipo get
    Integer updateStatus(@Param("status") String status, @Param("id")Integer id);

}
