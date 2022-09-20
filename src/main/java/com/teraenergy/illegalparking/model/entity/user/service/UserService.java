package com.teraenergy.illegalparking.model.entity.user.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;

import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface UserService {

    public User get(Integer userSeq) throws TeraException;

    public User get(String userName) throws TeraException;

    public User getByDB(String userName) throws TeraException;

    public List<User> gets() throws TeraException;

    public boolean isUser(String userName, String password) throws TeraException;

    public boolean isUser(String userName) throws TeraException;

    public void add(User user) throws TeraException;

    public void adds(List<User> users) throws TeraException;

}
