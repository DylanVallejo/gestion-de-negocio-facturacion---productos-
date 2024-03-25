package com.api.gestor.dao;

import com.api.gestor.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserDAO extends JpaRepository<User, Integer> {


    User findByEmail(@Param("email") String email);

}
