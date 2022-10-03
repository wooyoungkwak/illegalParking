package com.teraenergy.illegalparking.model.entity.user.service;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.exception.EncryptedException;
import com.teraenergy.illegalparking.exception.enums.EncryptedExceptionCode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
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
    public User get(Integer userSeq) throws TeraException {
        try {
            Optional<User> optional = userRepository.findById(userSeq);
            if ( optional.isEmpty()) {
                return null;
            }
            User user = optional.get();
//            user.setDecryptPassword();
            return user;
        } catch (EncryptedException e) {
            throw new TeraException(EncryptedExceptionCode.DECRYPT_FAILURE.getMessage(), e);
        }
    }

    @Override
    public User get(String userName) throws TeraException {
        try {
            User user = queryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .fetchOne();

            if (user == null) {
                return null;
            }
            return user;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public List<User> gets() throws TeraException {
        try {
            List<User> users = userRepository.findAll();
            if (users == null) {
                users = Lists.newArrayList();
            }
            return users;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN, e);
        }
    }

    @Override
    public boolean isUser(String userName, String password) throws TeraException {
        try {
            String _password = YoungEncoder.encrypt(password);
            if (queryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.password.eq(_password))
                    .fetchOne() != null) {
                return true;
            }
        } catch (EncryptedException e) {
            throw new TeraException(EncryptedExceptionCode.ENCRYPT_FAILURE.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isUser(String userName) throws TeraException {
        try {
            if (queryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .fetchOne() != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public User set(User user) throws TeraException {
        try {
            user.setEncyptPassword();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL, e);
        }
    }

    @Override
    public List<User> sets(List<User> users) throws TeraException {
        try {
            for (User user : users)
                user.setEncyptPassword();
            return userRepository.saveAll(users);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }
    }

}
