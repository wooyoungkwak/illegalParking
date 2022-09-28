package com.teraenergy.illegalparking.model.entity.user.service;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.exception.EncryptedException;
import com.teraenergy.illegalparking.exception.EncryptedExceptionCode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.user.domain.QUser;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JPAQueryFactory queryFactory;


    @Override
    public User getByInsert(Integer userSeq) throws TeraException {
        Optional<User> optional = userRepository.findById(userSeq);
        if ( optional.isEmpty()) {
            return null;
        }
        User user = optional.get();
        return user;
    }

    @Override
    public User get(Integer userSeq) throws TeraException {
        Optional<User> optional = userRepository.findById(userSeq);
        if ( optional.isEmpty()) {
            return null;
        }
        User user = optional.get();
        try {
            user.setPassword(YoungEncoder.decrypt(user.getPassword()));
        } catch (EncryptedException e) {
            log.error("사용자 정보 가져오기 오류 : ", e);
            throw new TeraException(TeraExceptionCode.NULL);
        }
        return user;
    }

    @Override
    public User get(String userName) throws TeraException {
        return queryFactory.selectFrom(QUser.user)
                .where(QUser.user.username.eq(userName))
                .fetchOne();
    }

    @Override
    public User getByDB(String userName) throws TeraException {
        return queryFactory.selectFrom(QUser.user)
                .where(QUser.user.username.eq(userName))
                .fetchOne();
    }

    @Override
    public List<User> gets() throws TeraException {
        List<User> users = userRepository.findAll();
        if (users == null) {
            users = Lists.newArrayList();
        }
        return users;
    }

    @Override
    public boolean isUser(String userName, String password) throws TeraException {
        try {
            String passwordKey = YoungEncoder.encrypt(password);
            if (queryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.password.eq(passwordKey))
                    .fetchOne() != null) {
                return true;
            }
        } catch (EncryptedException e) {
            log.error(EncryptedExceptionCode.ENCRYPT_FAILURE.getMessage(), e);
            throw new TeraException(TeraExceptionCode.NULL);
        }
        return false;
    }

    @Override
    public boolean isUser(String userName) throws TeraException {
        if (queryFactory.selectFrom(QUser.user)
                .where(QUser.user.username.eq(userName))
                .fetchOne() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void add(User user) throws TeraException {
        try {
            user.setPassword(YoungEncoder.encrypt(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("사용자 추가 오류 : ", e);
            throw new TeraException(TeraExceptionCode.NULL);
        }
    }

    @Override
    public void adds(List<User> users) throws TeraException {
        try {
            userRepository.saveAll(users);
        } catch (Exception e) {
            log.error("사용자 추가 오류 : ", e);
            throw new TeraException(TeraExceptionCode.NULL);
        }
    }
}
