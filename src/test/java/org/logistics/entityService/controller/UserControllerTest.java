package org.logistics.entityService.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.logistics.entityService.data.UserEntity;
import org.logistics.entityService.data.UsersRepository;
import org.logistics.entityService.exceptions.UserServiceException;
import org.logistics.entityService.model.request.CreateUserRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UsersRepository usersRepository;

    @Test
    public void getAllUsersTest() {
        when(usersRepository.findAll()).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null),
                        new UserEntity(2L, "USR-7ebedc0c-4062-43b6-887d-ecaeb3db44fc", "anurag", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231239", "abrakadabra", "saltdabra", "Audogodi, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(2, ((Collection<?>)userController.getUsers(true).getBody().getData()).size());
    }

    @Test
    public void getActiveUsersTest() {
        when(usersRepository.findAllActiveUsers()).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null),
                        new UserEntity(2L, "USR-7ebedc0c-4062-43b6-887d-ecaeb3db44fc", "anurag", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231239", "abrakadabra", "saltdabra", "Audogodi, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(2, ((Collection<?>)userController.getUsers(false).getBody().getData()).size());
    }

    @Test
    public void createUserTest() {
        when(usersRepository.save(new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null))).
                thenReturn(null);
        Assertions.assertEquals("anurag0510", userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore")).getBody().getUserName());
    }

    @Test
    public void createUserWithPreExistingUsernameTest() {
        when(usersRepository.findByUserName("anurag0510")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"));
        } catch (UserServiceException ex) {
            Assertions.assertEquals("User With userName anurag0510 already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void createUserWithPreExistingEmailTest() {
        when(usersRepository.findByEmailAddress("anurag0510@outlook.com")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"));
        } catch (UserServiceException ex) {
            Assertions.assertEquals("User With emailAddress anurag0510@outlook.com already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void createUserWithPreExistingMobileNumber() {
        when(usersRepository.findByMobileNumber("1231231234", "91")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"));
        } catch (UserServiceException ex) {
            Assertions.assertEquals("User With mobileNumber 1231231234 and country code 91 already exists in system.", ex.getMessage());
        }
    }
}
