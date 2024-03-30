package com.api.gestor.dao;

import com.api.gestor.pojo.User;
import com.api.gestor.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDAO extends JpaRepository<User, Integer> {


    User findByEmail(@Param("email") String email);

    List<UserWrapper> getAllUsers();

}
