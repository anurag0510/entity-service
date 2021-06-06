package org.logistics.entityService.service.impl;

import org.logistics.entityService.data.UserEntity;
import org.logistics.entityService.data.UsersRepository;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.request.ValidateUserPasswordRequestModel;
import org.logistics.entityService.service.UserService;
import org.logistics.entityService.shared.UserDto;
import org.logistics.entityService.shared.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UsersRepository usersRepository;
    private Utils utils;

    @Autowired
    public UserServiceImpl(Utils utils, UsersRepository usersRepository) {
        this.utils = utils;
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUid("USR-" + utils.getUniqueId());
        String salt = utils.getEncryptedValue(utils.getUniqueId() + new Date().getTime(), "MD5");
        userDto.setSalt(salt);
        userDto.setPassword(utils.getEncryptedValue(salt + userDto.getPassword(), "SHA-512"));
        userDto.setActive(true);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        if (usersRepository.findByUserName(userEntity.getUserName()) > 0)
            throw new EntityServiceException("User With userName " + userEntity.getUserName() + " already exists in system.");
        if (usersRepository.findByEmailAddress(userEntity.getEmailAddress()) > 0)
            throw new EntityServiceException("User With emailAddress " + userEntity.getEmailAddress() + " already exists in system.");
        if (usersRepository.findByMobileNumber(userEntity.getMobileNumber(), userEntity.getCountryCode()) > 0)
            throw new EntityServiceException("User With mobileNumber " + userEntity.getMobileNumber() + " and country code " + userEntity.getCountryCode() + " already exists in system.");
        usersRepository.save(userEntity);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers(boolean allUsers) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserEntity> post = allUsers ? (List<UserEntity>) usersRepository.findAll() : (List<UserEntity>) usersRepository.findAllActiveUsers();
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<UserDto> getAllUsersWithUid(String uid, boolean allUsers) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserEntity> post = !allUsers ? (List<UserEntity>) usersRepository.findAllActiveUsersWithUid(uid) : (List<UserEntity>) usersRepository.findAllUsersWithUid(uid);
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<UserDto> getAllUsersWithUserName(String userName, boolean allUsers) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserEntity> post = !allUsers ? (List<UserEntity>) usersRepository.findAllActiveUsersWithUserName(userName) : (List<UserEntity>) usersRepository.findAllUsersWithUserName(userName);
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<UserDto> getAllUsersWithEmailAddress(String emailAddress, boolean allUsers) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserEntity> post = !allUsers ? (List<UserEntity>) usersRepository.findAllActiveUsersWithEmailAddress(emailAddress) : (List<UserEntity>) usersRepository.findAllUsersWithEmailAddress(emailAddress);
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public UserDto updateUserBasedOnUid(UserDto userDto, String uid) {
        List<UserDto> users = getAllUsersWithUid(uid, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with uid : " + uid);
        UserDto systemUser = users.get(0);
        checkExceptions(userDto, systemUser);
        setUserDtoData(userDto, systemUser);
        usersRepository.updateUserBasedOnUid(userDto.getUserName(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmailAddress(), userDto.getMobileNumber(), userDto.getCountryCode(), userDto.getCountry(), userDto.getCity(), userDto.getAddress(), uid);
        return userDto;
    }

    @Override
    public UserDto updateUserBasedOnUserName(UserDto userDto, String userName) {
        List<UserDto> users = getAllUsersWithUserName(userName, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with user_name : " + userName);
        UserDto systemUser = users.get(0);
        checkExceptions(userDto, systemUser);
        setUserDtoData(userDto, systemUser);
        usersRepository.updateUserBasedOnUserName(userDto.getUserName(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmailAddress(), userDto.getMobileNumber(), userDto.getCountryCode(), userDto.getCountry(), userDto.getCity(), userDto.getAddress(), userName);
        return userDto;
    }

    @Override
    public UserDto updateUserBasedOnEmailAddress(UserDto userDto, String emailAddress) {
        List<UserDto> users = getAllUsersWithEmailAddress(emailAddress, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with email_address : " + emailAddress);
        UserDto systemUser = users.get(0);
        checkExceptions(userDto, systemUser);
        setUserDtoData(userDto, systemUser);
        usersRepository.updateUserBasedOnEmailAddress(userDto.getUserName(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmailAddress(), userDto.getMobileNumber(), userDto.getCountryCode(), userDto.getCountry(), userDto.getCity(), userDto.getAddress(), emailAddress);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsersWithMobileNumber(String countryCode, String mobileNumber, boolean allUsers) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserEntity> post = !allUsers ? (List<UserEntity>) usersRepository.findAllActiveUsersWithMobileNumber(countryCode, mobileNumber) : (List<UserEntity>) usersRepository.findAllUsersWithMobileNumber(countryCode, mobileNumber);
        Type listType = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public UserDto deleteUserBasedOnUid(String uid) {
        List<UserDto> users = getAllUsersWithUid(uid, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with uid : " + uid);
        UserDto systemUser = users.get(0);
        usersRepository.deleteUserBasedOnUid(uid);
        systemUser.setDeleted(true);
        return systemUser;
    }

    @Override
    public UserDto deleteUserBasedOnUserName(String userName) {
        List<UserDto> users = getAllUsersWithUserName(userName, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with user_name : " + userName);
        UserDto systemUser = users.get(0);
        usersRepository.deleteUserBasedOnUserName(userName);
        systemUser.setDeleted(true);
        return systemUser;
    }

    @Override
    public UserDto deleteUserBasedOnEmailAddress(String emailAddress) {
        List<UserDto> users = getAllUsersWithEmailAddress(emailAddress, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with email_address : " + emailAddress);
        UserDto systemUser = users.get(0);
        usersRepository.deleteUserBasedOnEmailAddress(emailAddress);
        systemUser.setDeleted(true);
        return systemUser;
    }

    @Override
    public boolean validateUserPasswordBasedOnUid(String uid, ValidateUserPasswordRequestModel passwordDetails) {
        List<UserDto> users = getAllUsersWithUid(uid, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with uid : " + uid);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + passwordDetails.getPassword(), "SHA-512")))
            return true;
        return false;
    }

    @Override
    public boolean validateUserPasswordBasedOnUserName(String userName, ValidateUserPasswordRequestModel passwordDetails) {
        List<UserDto> users = getAllUsersWithUserName(userName, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with user_name : " + userName);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + passwordDetails.getPassword(), "SHA-512")))
            return true;
        return false;
    }

    @Override
    public boolean validateUserPasswordBasedOnEmailAddress(String emailAddress, ValidateUserPasswordRequestModel passwordDetails) {
        List<UserDto> users = getAllUsersWithEmailAddress(emailAddress, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with email_address : " + emailAddress);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + passwordDetails.getPassword(), "SHA-512")))
            return true;
        return false;
    }

    @Override
    public boolean updateUserPasswordBasedOnUid(String uid, String oldPassword, String newPassword) {
        List<UserDto> users = getAllUsersWithUid(uid, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with uid : " + uid);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + oldPassword, "SHA-512"))) {
            String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + newPassword, "SHA-512");
            usersRepository.updatePasswordBasedOnUid(newEncryptedPassword, uid);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPasswordBasedOnUserName(String userName, String oldPassword, String newPassword) {
        List<UserDto> users = getAllUsersWithUserName(userName, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with user_name : " + userName);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + oldPassword, "SHA-512"))) {
            String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + newPassword, "SHA-512");
            usersRepository.updatePasswordBasedOnUserName(newEncryptedPassword, userName);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPasswordBasedOnEmailAddress(String emailAddress, String oldPassword, String newPassword) {
        List<UserDto> users = getAllUsersWithEmailAddress(emailAddress, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with email_address : " + emailAddress);
        UserDto systemUser = users.get(0);
        if (systemUser.getPassword().equals(utils.getEncryptedValue(systemUser.getSalt() + oldPassword, "SHA-512"))) {
            String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + newPassword, "SHA-512");
            usersRepository.updatePasswordBasedOnEmailAddress(newEncryptedPassword, emailAddress);
            return true;
        }
        return false;
    }

    @Override
    public boolean resetUserPasswordBasedOnUid(String uid, String password) {
        List<UserDto> users = getAllUsersWithUid(uid, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with uid : " + uid);
        UserDto systemUser = users.get(0);
        String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + password, "SHA-512");
        usersRepository.updatePasswordBasedOnUid(newEncryptedPassword, uid);
        return true;
    }

    @Override
    public boolean resetUserPasswordBasedOnUserName(String userName, String password) {
        List<UserDto> users = getAllUsersWithUserName(userName, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with user_name : " + userName);
        UserDto systemUser = users.get(0);
        String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + password, "SHA-512");
        usersRepository.updatePasswordBasedOnUserName(newEncryptedPassword, userName);
        return true;
    }

    @Override
    public boolean resetUserPasswordBasedOnEmailAddress(String emailAddress, String password) {
        List<UserDto> users = getAllUsersWithEmailAddress(emailAddress, false);
        if (users.size() == 0)
            throw new EntityServiceException("No user exist in system with email_address : " + emailAddress);
        UserDto systemUser = users.get(0);
        String newEncryptedPassword = utils.getEncryptedValue(systemUser.getSalt() + password, "SHA-512");
        usersRepository.updatePasswordBasedOnEmailAddress(newEncryptedPassword, emailAddress);
        return true;
    }

    private void checkExceptions(UserDto userDto, UserDto systemUser) {
        if ((userDto.getUserName() != null && !userDto.getUserName().equals(systemUser.getUserName())) && getAllUsersWithUserName(userDto.getUserName(), false).size() != 0)
            throw new EntityServiceException("Can't update user with the user_name : " + userDto.getUserName() + " as system already has a user with same user_name present.");
        if ((userDto.getEmailAddress() != null && !userDto.getEmailAddress().equals(systemUser.getEmailAddress())) && getAllUsersWithEmailAddress(userDto.getEmailAddress(), false).size() != 0)
            throw new EntityServiceException("Can't update user with the email_address : " + userDto.getEmailAddress() + " as system already has a user with same email address present.");
        if ((userDto.getMobileNumber() != null && !userDto.getMobileNumber().equals(systemUser.getMobileNumber())) && getAllUsersWithMobileNumber(userDto.getCountryCode(), systemUser.getMobileNumber(), false).size() != 0)
            throw new EntityServiceException("Can't update user with the mobile_number : " + userDto.getMobileNumber() + " as system already has a user with same mobile number present.");
    }

    private void setUserDtoData(UserDto userDto, UserDto systemUser) {
        if (userDto.getUid() == null)
            userDto.setUid(systemUser.getUid());
        if (userDto.getUserName() == null)
            userDto.setUserName(systemUser.getUserName());
        if (userDto.getFirstName() == null)
            userDto.setFirstName(systemUser.getFirstName());
        if (userDto.getLastName() == null)
            userDto.setLastName(systemUser.getLastName());
        if (userDto.getEmailAddress() == null)
            userDto.setEmailAddress(systemUser.getEmailAddress());
        if (userDto.getMobileNumber() == null)
            userDto.setMobileNumber(systemUser.getMobileNumber());
        if (userDto.getCountryCode() == null)
            userDto.setCountryCode(systemUser.getCountryCode());
        if (userDto.getCountry() == null)
            userDto.setCountry(systemUser.getCountry());
        if (userDto.getCity() == null)
            userDto.setCity(systemUser.getCity());
        if (userDto.getAddress() == null)
            userDto.setAddress(systemUser.getAddress());
        if (userDto.getActive() != systemUser.getActive())
            userDto.setActive(systemUser.getActive());
    }
}
