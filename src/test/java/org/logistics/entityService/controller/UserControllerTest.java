package org.logistics.entityService.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.logistics.entityService.data.entities.UserEntity;
import org.logistics.entityService.data.repositories.UsersRepository;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.kafka.Producer;
import org.logistics.entityService.model.request.CreateUserRequestModel;
import org.logistics.entityService.model.request.UpdateUserPasswordRequestModel;
import org.logistics.entityService.model.request.UpdateUserRequestModel;
import org.logistics.entityService.model.request.ValidateUserPasswordRequestModel;
import org.logistics.entityService.shared.Utils;
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

    @Autowired
    private Utils utils;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private Producer producer;

    @Test
    public void getAllUsersTest() {
        when(usersRepository.findAll()).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null),
                        new UserEntity(2L, "USR-7ebedc0c-4062-43b6-887d-ecaeb3db44fc", "anurag", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231239", "abrakadabra", "saltdabra", "Audogodi, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(2, ((Collection<?>) userController.getUsers(true).getBody().getData()).size());
    }

    @Test
    public void getActiveUsersTest() {
        when(usersRepository.findAllActiveUsers()).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null),
                        new UserEntity(2L, "USR-7ebedc0c-4062-43b6-887d-ecaeb3db44fc", "anurag", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231239", "abrakadabra", "saltdabra", "Audogodi, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(2, ((Collection<?>) userController.getUsers(false).getBody().getData()).size());
    }

    @Test
    public void createUserTest() {
        when(usersRepository.save(new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null))).
                thenReturn(new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null));
        Assertions.assertEquals("anurag0510", userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"), null).getBody().getUserName());
    }

    @Test
    public void createUserWithPreExistingUsernameTest() {
        when(usersRepository.findByUserName("anurag0510")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"), null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("User With userName anurag0510 already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void createUserWithPreExistingEmailTest() {
        when(usersRepository.findByEmailAddress("anurag0510@outlook.com")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"), null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("User With emailAddress anurag0510@outlook.com already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void createUserWithPreExistingMobileNumberTest() {
        when(usersRepository.findByMobileNumber("1231231234", "91")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"), null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("User With mobileNumber 1231231234 and country code 91 already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnUidTest() {
        when(usersRepository.findAllUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals("USR-ddb39364-23f9-4571-af60-d29d6a84bab3", userController.getUser(true, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().iterator().next().getUid());
    }

    @Test
    public void getAllUserBasedOnUserNameTest() {
        when(usersRepository.findAllUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "user_name", "anurag0510").getBody().getData()).size());
        Assertions.assertEquals("anurag0510", userController.getUser(true, "user_name", "anurag0510").getBody().getData().iterator().next().getUserName());
    }

    @Test
    public void getAllUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "email_address", "anurag0510@outlook.com").getBody().getData()).size());
        Assertions.assertEquals("anurag0510@outlook.com", userController.getUser(true, "email_address", "anurag0510@outlook.com").getBody().getData().iterator().next().getEmailAddress());
    }

    @Test
    public void getAllActiveUserBasedOnUidTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals("USR-ddb39364-23f9-4571-af60-d29d6a84bab3", userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().iterator().next().getUid());
    }

    @Test
    public void getAllActiveUserBasedOnUserNameTest() {
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "user_name", "anurag0510").getBody().getData()).size());
        Assertions.assertEquals("anurag0510", userController.getUser(false, "user_name", "anurag0510").getBody().getData().iterator().next().getUserName());
    }

    @Test
    public void getAllActiveUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "email_address", "anurag0510@outlook.com").getBody().getData()).size());
        Assertions.assertEquals("anurag0510@outlook.com", userController.getUser(false, "email_address", "anurag0510@outlook.com").getBody().getData().iterator().next().getEmailAddress());
    }

    @Test
    public void getAllUserBasedOnInvalidFilter() {
        try {
            userController.getUser(true, "invalid_filter", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingUidTest() {
        try {
            userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingUserNameTest() {
        try {
            userController.getUser(false, "user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingEmailAddressTest() {
        try {
            userController.getUser(false, "email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnUidTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser(userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "YGCFSB").getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser(userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null).getBody().getLastName());
    }

    @Test
    public void updateUserBasedOnUserNameTest() {
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser(userDetaisToBeUpdated, "user_name", "anurag0510", null).getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser(userDetaisToBeUpdated, "user_name", "anurag0510", null).getBody().getLastName());
    }

    @Test
    public void updateUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser(userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com", null).getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser(userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com", null).getBody().getLastName());
    }

    @Test
    public void updateUserWithPreExistingUserNameInSystemTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        when(usersRepository.findAllActiveUsersWithUserName("anurag21st")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setUserName("anurag21st");
        try {
            userController.updateUser(userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Can't update user with the user_name : anurag21st as system already has a user with same user_name present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserWithPreExistingEmailAddressInSystemTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag@gmail.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setEmailAddress("anurag@gmail.com");
        try {
            userController.updateUser(userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Can't update user with the email_address : anurag@gmail.com as system already has a user with same email address present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserWithPreExistingMobileNumberInSystemTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        when(usersRepository.findAllActiveUsersWithMobileNumber("91", "1231231237")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setCountryCode("91");
        userDetaisToBeUpdated.setMobileNumber("1231231237");
        try {
            userController.updateUser(userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Can't update user with the mobile_number : 1231231237 as system already has a user with same mobile number present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingUidTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingUserNameTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingEmailAddressTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnInvalidFilter() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "invalid_filter", "anurag0510@outlook.com", null);
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void deleteUserBasedOnUidTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals("USR-ddb39364-23f9-4571-af60-d29d6a84bab3", userController.deleteUser("uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getUid());
    }

    @Test
    public void deleteUserBasedOnUserNameTest() {
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals("anurag0510", userController.deleteUser("user_name", "anurag0510").getBody().getUserName());
    }

    @Test
    public void deleteUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals("anurag0510@outlook.com", userController.deleteUser("email_address", "anurag0510@outlook.com").getBody().getEmailAddress());
    }

    @Test
    public void deleteNonExistingUserBasedOnUidTest() {
        try {
            userController.deleteUser("uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void deleteNonExistingUserBasedOnUserNameTest() {
        try {
            userController.deleteUser("user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void deleteNonExistingUserBasedOnEmailAddressTest() {
        try {
            userController.deleteUser("email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void deleteUserBasedOnInvalidFilter() {
        try {
            userController.deleteUser("invalid_filter", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void validatePasswordBasedOnUidTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.validatePassword(new ValidateUserPasswordRequestModel("password"), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().isSuccess());
    }

    @Test
    public void validatePasswordBasedOnUserNameTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.validatePassword(new ValidateUserPasswordRequestModel("password"), "user_name", "anurag0510").getBody().isSuccess());
    }

    @Test
    public void validatePasswordBasedOnEmailAddressTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.validatePassword(new ValidateUserPasswordRequestModel("password"), "email_address", "anurag0510@outlook.com").getBody().isSuccess());
    }

    @Test
    public void validateWrongPasswordBasedOnUidTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        try {
            userController.validatePassword(new ValidateUserPasswordRequestModel("wrong_password"), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Provided password is wrong!", ex.getMessage());
        }
    }

    @Test
    public void validateWrongPasswordBasedOnUserNameTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        try {
            userController.validatePassword(new ValidateUserPasswordRequestModel("wrong_password"), "user_name", "anurag0510");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Provided password is wrong!", ex.getMessage());
        }
    }

    @Test
    public void validateWrongPasswordBasedOnEmailAddressTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        try {
            userController.validatePassword(new ValidateUserPasswordRequestModel("wrong_password"), "email_address", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Provided password is wrong!", ex.getMessage());
        }
    }

    @Test
    public void validatePasswordBasedOnInvalidFilter() {
        try {
            userController.validatePassword(new ValidateUserPasswordRequestModel("password"), "invalid_filter", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void validatePasswordForNonExistingUserBasedOnUidTest() {
        try {
            userController.validatePassword( new ValidateUserPasswordRequestModel("password"),"uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void validatePasswordForNonExistingUserBasedOnUserNameTest() {
        try {
            userController.validatePassword( new ValidateUserPasswordRequestModel("password"),"user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void validatePasswordForNonExistingUserBasedOnEmailAddressTest() {
        try {
            userController.validatePassword(new ValidateUserPasswordRequestModel("password"),"email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updatePasswordBasedOnUidTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.updatePassword(new UpdateUserPasswordRequestModel("password", "updated_password"), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().isSuccess());
    }

    @Test
    public void updatePasswordBasedOnEmailAddressTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.updatePassword(new UpdateUserPasswordRequestModel("password", "updated_password"), "email_address", "anurag0510@outlook.com").getBody().isSuccess());
    }

    @Test
    public void updatePasswordBasedOnUserNameTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.updatePassword(new UpdateUserPasswordRequestModel("password", "updated_password"), "user_name", "anurag0510").getBody().isSuccess());
    }

    @Test
    public void updatePasswordBasedOnInvalidFilter() {
        try {
            userController.updatePassword(new UpdateUserPasswordRequestModel("password", "updated_password"), "invalid_filter", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void updateProvidingWrongCurrentPasswordBasedOnEmailAddressTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        try {
            userController.updatePassword(new UpdateUserPasswordRequestModel("wrong_password", "updated_password"), "email_address", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Provided password is wrong!", ex.getMessage());
        }
    }

    @Test
    public void updatePasswordForNonExistingUserBasedOnUidTest() {
        try {
            userController.updatePassword( new UpdateUserPasswordRequestModel("password", "updated_password"),"uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updatePasswordForNonExistingUserBasedOnUserNameTest() {
        try {
            userController.updatePassword( new UpdateUserPasswordRequestModel("password", "updated_password"),"user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updatePasswordForNonExistingUserBasedOnEmailAddressTest() {
        try {
            userController.updatePassword(new UpdateUserPasswordRequestModel("password", "updated_password"),"email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void resetPasswordBasedOnUidTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.resetPassword(new ValidateUserPasswordRequestModel("password"), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().isSuccess());
    }

    @Test
    public void resetPasswordBasedOnEmailAddressTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.resetPassword(new ValidateUserPasswordRequestModel("password"), "email_address", "anurag0510@outlook.com").getBody().isSuccess());
    }

    @Test
    public void resetPasswordBasedOnUserNameTest() {
        String encryptedValue = utils.getEncryptedValue("salt" + "password", "SHA-512");
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", encryptedValue, "salt", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null, null, null)
                )));
        Assertions.assertEquals(true, userController.resetPassword(new ValidateUserPasswordRequestModel("password"), "user_name", "anurag0510").getBody().isSuccess());
    }

    @Test
    public void resetPasswordBasedOnInvalidFilter() {
        try {
            userController.resetPassword(new ValidateUserPasswordRequestModel("password"), "invalid_filter", "anurag0510@outlook.com");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void resetPasswordForNonExistingUserBasedOnUidTest() {
        try {
            userController.resetPassword( new ValidateUserPasswordRequestModel("password"),"uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void resetPasswordForNonExistingUserBasedOnUserNameTest() {
        try {
            userController.resetPassword( new ValidateUserPasswordRequestModel("password"),"user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void resetPasswordForNonExistingUserBasedOnEmailAddressTest() {
        try {
            userController.resetPassword(new ValidateUserPasswordRequestModel("password"),"email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (EntityServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }
}
