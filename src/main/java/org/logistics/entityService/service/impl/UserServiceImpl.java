package org.logistics.entityService.service.impl;

import org.logistics.entityService.data.UserEntity;
import org.logistics.entityService.data.UsersRepository;
import org.logistics.entityService.exceptions.UserServiceException;
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
            throw new UserServiceException("User With userName " + userEntity.getUserName() + " already exists in system.");
        if (usersRepository.findByEmailAddress(userEntity.getEmailAddress()) > 0)
            throw new UserServiceException("User With emailAddress " + userEntity.getEmailAddress() + " already exists in system.");
        if (usersRepository.findByMobileNumber(userEntity.getMobileNumber(), userEntity.getCountryCode()) > 0)
            throw new UserServiceException("User With mobileNumber " + userEntity.getMobileNumber() + " and country code " + userEntity.getCountryCode() + " already exists in system.");
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
}
