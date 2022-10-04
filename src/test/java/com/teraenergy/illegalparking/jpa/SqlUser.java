package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlUser {

    @Autowired
    UserService userService;

    @Test
    public void insert() throws TeraException {
        List<User> users = Lists.newArrayList();

        User user = new User();
        user.setIsDel(false);
        user.setUsername("admin");

        user.setPassword("qwer1234");
        user.setRole(Role.ADMIN);
        user.setUserCode(1234l);
        user.setName("관리자");
        users.add(user);

        User user2 = new User();
        user2.setUsername("hong@gmail.com");
        user2.setPassword("qwer1234");
        user2.setRole(Role.USER);
        user2.setUserCode(1234l);
        user2.setIsDel(false);
        user2.setName("홍길동");
        users.add(user2);

        userService.sets(users);
    }

    @Test
    public void authentication() throws TeraException {

        if ( userService.isUser("admin") ) {
            System.out.println("is ... ");
        } else  {
            System.out.println("is not .... ");
        }

    }
}
