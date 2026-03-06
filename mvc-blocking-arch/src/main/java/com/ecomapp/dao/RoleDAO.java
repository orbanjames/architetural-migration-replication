package com.ecomapp.dao;

import com.ecomapp.models.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

public interface RoleDAO {

    String BASIC_QUERY = "SELECT u.* FROM user_role u";

    @Query(BASIC_QUERY + " WHERE u.name = :name")
    Public Role findByName(@Param("name") String name);
}

