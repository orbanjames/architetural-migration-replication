package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SynodDAO;
import com.ecomapp.dao.UserDAO;
import com.ecomapp.models.Synod;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.UserStatusEnum;
import com.ecomapp.services.synod.SynodService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class SynodServiceImpl implements SynodService {

    @Resource
    private SynodDAO synodDAO;

    @Resource
    private UserDAO userDAO;

    @Override
    public Flux<Synod> getAll() {
        return synodDAO.findAll();
    }

    @Override
    public Mono<Synod> getByID(int id) {
        return synodDAO.findById(id);
    }

    @Override
    public Flux<User> getSynodMembers(int synodId) {
        return userDAO.findSynodUserBySynod(synodId);
    }

    @Override
    public Flux<User> getRegistrars(int synodId) {
        return userDAO.findRegistrarUserBySynod(synodId);
    }


    @Override
    public Mono<Boolean> isUserRegisterToSynod(int synodId, int userId) {
        return synodDAO.isUserRegisterToSynod(synodId, userId)
                .map(count -> count.equals(1));
    }

    @Override
    public Mono<Boolean> registerToSynod(int synodId, int userId) {
        return synodDAO.registerToSynod(synodId, userId, UserStatusEnum.PENDING_APPLICANT_REGISTRATION.toString())
                .map(count -> count.equals(1));
    }

    @Override
    public Flux<Synod> findSynodsForUser(User user) {
        return null;
    }
}