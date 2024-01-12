package com.badeling.msbot.infrastructure.user.service;

import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final ConstRepository constRepository;

    public boolean aboveManager(long qq) {
        return qq == constRepository.getMasterQQ()
                || constRepository.getManagerQQ().contains(qq);
    }

    public void checkManager(long qq) throws IlleagleUserException {
        if (!aboveManager(qq)) {
            throw new IlleagleUserException();
        }
    }

    public void checkManager(long qq, String msg) throws IlleagleUserException {
        if (!aboveManager(qq)) {
            throw new IlleagleUserException(msg);
        }
    }

    public void checkMaster(long qq) throws IlleagleUserException {
        if (!isManager(qq)) {
            throw new IlleagleUserException();
        }
    }

    public void checkMaster(long qq, String msg) throws IlleagleUserException {
        if (!isManager(qq)) {
            throw new IlleagleUserException(msg);
        }
    }

    public boolean isManager(long qq) {
        return qq == constRepository.getMasterQQ();
    }
}
