package org.logistics.entityService.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.logistics.entityService.data.UserEntity;
import org.logistics.entityService.data.UsersRepository;
import org.logistics.entityService.exceptions.UserServiceException;
import org.logistics.entityService.model.request.CreateUserRequestModel;
import org.logistics.entityService.model.request.UpdateUserRequestModel;
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
        Assertions.assertEquals(2, ((Collection<?>) userController.getUsers(true).getBody().getData()).size());
    }

    @Test
    public void getActiveUsersTest() {
        when(usersRepository.findAllActiveUsers()).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null),
                        new UserEntity(2L, "USR-7ebedc0c-4062-43b6-887d-ecaeb3db44fc", "anurag", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231239", "abrakadabra", "saltdabra", "Audogodi, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(2, ((Collection<?>) userController.getUsers(false).getBody().getData()).size());
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
    public void createUserWithPreExistingMobileNumberTest() {
        when(usersRepository.findByMobileNumber("1231231234", "91")).
                thenReturn(1L);
        try {
            userController.createUser(new CreateUserRequestModel("anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "9889258114", "91", "1231231234", "India", "Bangalore", "Electronic City, Bangalore"));
        } catch (UserServiceException ex) {
            Assertions.assertEquals("User With mobileNumber 1231231234 and country code 91 already exists in system.", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnUidTest() {
        when(usersRepository.findAllUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals("USR-ddb39364-23f9-4571-af60-d29d6a84bab3", userController.getUser(true, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().iterator().next().getUid());
    }

    @Test
    public void getAllUserBasedOnUserNameTest() {
        when(usersRepository.findAllUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "user_name", "anurag0510").getBody().getData()).size());
        Assertions.assertEquals("anurag0510", userController.getUser(true, "user_name", "anurag0510").getBody().getData().iterator().next().getUserName());
    }

    @Test
    public void getAllUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(true, "email_address", "anurag0510@outlook.com").getBody().getData()).size());
        Assertions.assertEquals("anurag0510@outlook.com", userController.getUser(true, "email_address", "anurag0510@outlook.com").getBody().getData().iterator().next().getEmailAddress());
    }

    @Test
    public void getAllActiveUserBasedOnUidTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData()).size());
        Assertions.assertEquals("USR-ddb39364-23f9-4571-af60-d29d6a84bab3", userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getData().iterator().next().getUid());
    }

    @Test
    public void getAllActiveUserBasedOnUserNameTest() {
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "user_name", "anurag0510").getBody().getData()).size());
        Assertions.assertEquals("anurag0510", userController.getUser(false, "user_name", "anurag0510").getBody().getData().iterator().next().getUserName());
    }

    @Test
    public void getAllActiveUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        Assertions.assertEquals(1, ((Collection<?>) userController.getUser(false, "email_address", "anurag0510@outlook.com").getBody().getData()).size());
        Assertions.assertEquals("anurag0510@outlook.com", userController.getUser(false, "email_address", "anurag0510@outlook.com").getBody().getData().iterator().next().getEmailAddress());
    }

    @Test
    public void getAllUserBasedOnInvalidFilter() {
        try {
            userController.getUser(true, "invalid_filter", "anurag0510@outlook.com");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingUidTest() {
        try {
            userController.getUser(false, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingUserNameTest() {
        try {
            userController.getUser(false, "user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void getAllUserBasedOnNonExistingEmailAddressTest() {
        try {
            userController.getUser(false, "email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No such user is present/active in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnUidTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser( userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser( userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3").getBody().getLastName());
    }

    @Test
    public void updateUserBasedOnUserNameTest() {
        when(usersRepository.findAllActiveUsersWithUserName("anurag0510")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser( userDetaisToBeUpdated, "user_name", "anurag0510").getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser( userDetaisToBeUpdated, "user_name", "anurag0510").getBody().getLastName());
    }

    @Test
    public void updateUserBasedOnEmailAddressTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setFirstName("Updated First");
        userDetaisToBeUpdated.setLastName("Updated Last");
        Assertions.assertEquals("Updated First", userController.updateUser( userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com").getBody().getFirstName());
        Assertions.assertEquals("Updated Last", userController.updateUser( userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com").getBody().getLastName());
    }

    @Test
    public void updateUserWithPreExistingUserNameInSystemTest() {
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag0510@outlook.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        when(usersRepository.findAllActiveUsersWithUserName("anurag21st")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setUserName("anurag21st");
        try {
            userController.updateUser( userDetaisToBeUpdated, "email_address", "anurag0510@outlook.com");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("Can't update user with the user_name : anurag21st as system already has a user with same user_name present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserWithPreExistingEmailAddressInSystemTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        when(usersRepository.findAllActiveUsersWithEmailAddress("anurag@gmail.com")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setEmailAddress("anurag@gmail.com");
        try {
            userController.updateUser( userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("Can't update user with the email_address : anurag@gmail.com as system already has a user with same email address present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserWithPreExistingMobileNumberInSystemTest() {
        when(usersRepository.findAllActiveUsersWithUid("USR-ddb39364-23f9-4571-af60-d29d6a84bab3")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bab3", "anurag0510", "Anurag", "Dubey", "anurag0510@outlook.com", "91", "1231231234", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        when(usersRepository.findAllActiveUsersWithMobileNumber("91", "1231231237")).
                thenReturn(new ArrayList<>(Arrays.asList(
                        new UserEntity(1L, "USR-ddb39364-23f9-4571-af60-d29d6a84bae5", "anurag21st", "Anurag", "Dubey", "anurag@gmail.com", "91", "1231231237", "abrakadabra", "saltdabra", "Electronic City, Bangalore", "India", "Bangalore", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), true, false, null)
                )));
        UpdateUserRequestModel userDetaisToBeUpdated = new UpdateUserRequestModel();
        userDetaisToBeUpdated.setCountryCode("91");
        userDetaisToBeUpdated.setMobileNumber("1231231237");
        try {
            userController.updateUser( userDetaisToBeUpdated, "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("Can't update user with the mobile_number : 1231231237 as system already has a user with same mobile number present.", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingUidTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "uid", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No user exist in system with uid : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingUserNameTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "user_name", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No user exist in system with user_name : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnNonExistingEmailAddressTest() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "email_address", "USR-ddb39364-23f9-4571-af60-d29d6a84bab3");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("No user exist in system with email_address : USR-ddb39364-23f9-4571-af60-d29d6a84bab3", ex.getMessage());
        }
    }

    @Test
    public void updateUserBasedOnInvalidFilter() {
        try {
            userController.updateUser(new UpdateUserRequestModel(), "invalid_filter", "anurag0510@outlook.com");
        } catch (UserServiceException ex) {
            Assertions.assertEquals("Only allowed to filter via : uid, user_name, email_address", ex.getMessage());
        }
    }
}
