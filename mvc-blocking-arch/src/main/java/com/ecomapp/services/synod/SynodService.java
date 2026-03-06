package com.ecomapp.services.synod;

import com.ecomapp.models.Synod;
import com.ecomapp.models.User;
import com.ecomapp.services.GenericService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SynodService extends GenericService<Synod> {

    Flux<User> getSynodMembers(int synodId);

    Flux<User> getRegistrars(int synodId);

    Mono<Boolean> isUserRegisterToSynod(int synodId, int userId);

    Mono<Boolean> registerToSynod(int synodId, int userId);

    Flux<Synod> findSynodsForUser(User user);
}
