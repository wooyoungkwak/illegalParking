package com.teraenergy.illegalparking.model.entity.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails = null;

        if ( userService.isUser(username)  ) {
            userDetails = userService.getByDB(username);
        }
        return userDetails;
    }
}

