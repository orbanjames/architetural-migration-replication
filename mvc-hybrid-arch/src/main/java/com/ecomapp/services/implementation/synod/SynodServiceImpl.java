package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SynodDAO;
import com.ecomapp.dao.UserDAO;
import com.ecomapp.models.Synod;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.UserStatusEnum;
import com.ecomapp.services.synod.SynodService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class SynodServiceImpl implements SynodService {

    @Resource
    private SynodDAO synodDAO;

    @Resource
    private UserDAO userDAO;

    @Override
    @Async
    public CompletableFuture<List<Synod>> getAll() {
        return CompletableFuture.completedFuture(synodDAO.findAll());
    }

    @Override
    @Async
    public CompletableFuture<Synod> getByID(int id) {
        Optional<Synod> synod = synodDAO.findById(id);
        return CompletableFuture.completedFuture(synod.orElse(null));
    }

    @Override
    @Async
    public CompletableFuture<List<User>> getSynodMembers(int synodId) {
        return CompletableFuture.completedFuture(
                userDAO.findSynodUserBySynod(synodId)
        );
    }

    @Override
    @Async
    public CompletableFuture<List<User>> getRegistrars(int synodId) {
        return CompletableFuture.completedFuture(
                userDAO.findRegistrarUserBySynod(synodId)
        );
    }

    @Override
    @Async
    public CompletableFuture<Boolean> isUserRegisterToSynod(int synodId, int userId) {
        Integer count = synodDAO.isUserRegisterToSynod(synodId, userId);
        return CompletableFuture.completedFuture(count != null && count == 1);
    }

    @Override
    @Async
    public CompletableFuture<Boolean> registerToSynod(int synodId, int userId) {
        Integer count = synodDAO.registerToSynod(
                synodId,
                userId,
                UserStatusEnum.PENDING_APPLICANT_REGISTRATION.toString()
        );
        return CompletableFuture.completedFuture(count != null && count == 1);
    }

    @Override
    @Async
    public CompletableFuture<List<Synod>> findSynodsForUser(User user) {
        return CompletableFuture.completedFuture(
                synodDAO.findSynodsForUser(user.getId())
        );
    }
}