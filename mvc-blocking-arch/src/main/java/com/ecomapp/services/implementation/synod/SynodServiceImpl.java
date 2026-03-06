package com.ecomapp.services.implementation.synod;

import com.ecomapp.dao.SynodDAO;
import com.ecomapp.dao.UserDAO;
import com.ecomapp.models.Synod;
import com.ecomapp.models.User;
import com.ecomapp.models.enums.UserStatusEnum;
import com.ecomapp.services.synod.SynodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SynodServiceImpl implements SynodService {

    @Resource
    private SynodDAO synodDAO;

    @Resource
    private UserDAO userDAO;

    @Override
    public List<Synod> getAll() {
        return synodDAO.findAll();
    }

    @Override
    public Synod getByID(int id) {
        return synodDAO.findById(id).orElse(null);
    }

    @Override
    public List<User> getSynodMembers(int synodId) {
        return userDAO.findSynodUserBySynod(synodId);
    }

    @Override
    public List<User> getRegistrars(int synodId) {
        return userDAO.findRegistrarUserBySynod(synodId);
    }

    @Override
    public Boolean isUserRegisterToSynod(int synodId, int userId) {
        Integer count = synodDAO.isUserRegisterToSynod(synodId, userId);
        return count != null && count == 1;
    }

    @Override
    public Boolean registerToSynod(int synodId, int userId) {
        Integer count = synodDAO.registerToSynod(
                synodId,
                userId,
                UserStatusEnum.PENDING_APPLICANT_REGISTRATION.toString()
        );
        return count != null && count == 1;
    }

    @Override
    public List<Synod> findSynodsForUser(User user) {
        return synodDAO.findSynodsForUser(user.getId());
    }
}