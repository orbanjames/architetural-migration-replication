package com.ecomapp.dao;


import com.ecomapp.models.Catalogue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
public class CatalogueDAO<Catalogue, Integer> {

    String BASIC_QUERY =
            "SELECT c.*, cac.name as 'categoryName', cac.description as 'categoryDescription', "
                    + "u.name as 'userName', u.surname 'userSurname', u.email as 'userEmail', u.username as 'userUsername', "
                    + "u.password as 'userPassword', u.title as 'userTitle', u.country as 'userCountry', u.role as 'userRole',\n"
                    + "sy.id as 'synodId', sy.name as 'synodName', sy.town as 'synodTown', sy.closed as 'synodIsClosed', "
                    + "sy.date as 'synodDate', sy.country as 'synodCountry', sy.synodCategory as 'synodCategory' \n"
                    + "FROM `catalogue` c "
                    + "JOIN User u on c.user = u.id "
                    + "JOIN synod sy on c.synod = sy.id "
                    + "JOIN catalogue_category cac ON c.synod = cac.id";

    @Query(BASIC_QUERY)
    List<Catalogue> findAll();

    @Query(BASIC_QUERY + " WHERE c.id = :id")
    Catalogue findById(@Param("id") int id);
}
