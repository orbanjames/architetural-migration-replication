package com.ecomapp.services;

import com.ecomapp.models.Role;
import reactor.core.publisher.Mono;

public interface RoleService extends GenericService<Role> {

    Mono<Role> getByName(String roleName);
}

