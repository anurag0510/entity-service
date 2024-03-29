package org.logistics.entityService.controller;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.request.CreateUserRequestModel;
import org.logistics.entityService.model.request.UpdateUserPasswordRequestModel;
import org.logistics.entityService.model.request.UpdateUserRequestModel;
import org.logistics.entityService.model.request.ValidateUserPasswordRequestModel;
import org.logistics.entityService.model.response.CreateUserResponseModel;
import org.logistics.entityService.model.response.GetAllUsersResponseModel;
import org.logistics.entityService.model.response.ValidatePasswordResponseModel;
import org.logistics.entityService.service.UserService;
import org.logistics.entityService.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<GetAllUsersResponseModel> getUsers(
            @RequestParam(value = "all", defaultValue = "false", required = false) boolean all
    ) {
        log.info("Request received to retrieve all users with flag for all set to : {} ", all);
        GetAllUsersResponseModel returnValue = new GetAllUsersResponseModel();
        returnValue.setSuccess(true);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<UserDto> userDtoList = userService.getAllUsers(all);
        Type listType = new TypeToken<List<CreateUserResponseModel>>() {
        }.getType();
        List<CreateUserResponseModel> usersList = modelMapper.map(userDtoList, listType);
        returnValue.setData(usersList);
        log.info("Returning Details : {}", returnValue);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails,
                                                              @RequestHeader(name = "x-requester-id", required = false) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId) {
        log.info("Request received to create user with details : {} ", userDetails.toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        CreateUserResponseModel createUserResponseModel = modelMapper.map(userService.createUser(userDto, requesterId), CreateUserResponseModel.class);
        log.info("Returning Details : {}", createUserResponseModel);
        return new ResponseEntity<>(createUserResponseModel, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{filter}/{value}")
    public ResponseEntity<GetAllUsersResponseModel> getUser(
            @RequestParam(value = "all", defaultValue = "false", required = false) boolean all,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value
    ) {
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        GetAllUsersResponseModel returnValue = new GetAllUsersResponseModel();
        returnValue.setSuccess(true);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Type listType = new TypeToken<List<CreateUserResponseModel>>() {
        }.getType();
        if (filter.matches("(?i)uid")) {
            List<CreateUserResponseModel> usersList = modelMapper.map(userService.getAllUsersWithUid(value, all), listType);
            if (usersList.size() == 0)
                throw new EntityServiceException("No such user is present/active in system with uid : " + value);
            returnValue.setData(usersList);
        }
        if (filter.matches("(?i)user_name")) {
            List<CreateUserResponseModel> usersList = modelMapper.map(userService.getAllUsersWithUserName(value, all), listType);
            if (usersList.size() == 0)
                throw new EntityServiceException("No such user is present/active in system with user_name : " + value);
            returnValue.setData(usersList);
        }
        if (filter.matches("(?i)email_address")) {
            List<CreateUserResponseModel> usersList = modelMapper.map(userService.getAllUsersWithEmailAddress(value, all), listType);
            if (usersList.size() == 0)
                throw new EntityServiceException("No such user is present/active in system with email_address : " + value);
            returnValue.setData(usersList);
        }
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PutMapping(value = "/{filter}/{value}")
    public ResponseEntity<CreateUserResponseModel> updateUser(
            @Valid @RequestBody UpdateUserRequestModel userDetails,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value,
            @RequestHeader(name = "x-requester-id", required = true) @NotNull @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId
    ) {
        log.info(requesterId);
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        CreateUserResponseModel returnValue = null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        if (filter.matches("(?i)uid")) {
            returnValue = modelMapper.map(userService.updateUserBasedOnUid(userDto, value, requesterId), CreateUserResponseModel.class);
        }
        if (filter.matches("(?i)user_name")) {
            returnValue = modelMapper.map(userService.updateUserBasedOnUserName(userDto, value, requesterId), CreateUserResponseModel.class);
        }
        if (filter.matches("(?i)email_address")) {
            returnValue = modelMapper.map(userService.updateUserBasedOnEmailAddress(userDto, value, requesterId), CreateUserResponseModel.class);
        }
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{filter}/{value}")
    public ResponseEntity<CreateUserResponseModel> deleteUser(
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value
    ) {
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        CreateUserResponseModel returnValue = null;
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        if (filter.matches("(?i)uid")) {
            returnValue = modelMapper.map(userService.deleteUserBasedOnUid(value), CreateUserResponseModel.class);
        }
        if (filter.matches("(?i)user_name")) {
            returnValue = modelMapper.map(userService.deleteUserBasedOnUserName(value), CreateUserResponseModel.class);
        }
        if (filter.matches("(?i)email_address")) {
            returnValue = modelMapper.map(userService.deleteUserBasedOnEmailAddress(value), CreateUserResponseModel.class);
        }
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping(value = "/{filter}/{value}/validatePassword")
    public ResponseEntity<ValidatePasswordResponseModel> validatePassword(
            @Valid @RequestBody ValidateUserPasswordRequestModel passwordDetails,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value
    ) {
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        ValidatePasswordResponseModel returnValue = new ValidatePasswordResponseModel();
        if (filter.matches("(?i)uid")) {
            returnValue.setSuccess(userService.validateUserPasswordBasedOnUid(value, passwordDetails));
        }
        if (filter.matches("(?i)user_name")) {
            returnValue.setSuccess(userService.validateUserPasswordBasedOnUserName(value, passwordDetails));
        }
        if (filter.matches("(?i)email_address")) {
            returnValue.setSuccess(userService.validateUserPasswordBasedOnEmailAddress(value, passwordDetails));
        }
        if (!returnValue.isSuccess())
            throw new EntityServiceException("Provided password is wrong!");
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping(value = "/{filter}/{value}/updatePassword")
    public ResponseEntity<ValidatePasswordResponseModel> updatePassword(
            @Valid @RequestBody UpdateUserPasswordRequestModel updateUserPasswordDetails,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value
    ) {
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        ValidatePasswordResponseModel returnValue = new ValidatePasswordResponseModel();
        if (filter.matches("(?i)uid")) {
            returnValue.setSuccess(userService.updateUserPasswordBasedOnUid(value, updateUserPasswordDetails.getOldPassword(), updateUserPasswordDetails.getNewPassword()));
        }
        if (filter.matches("(?i)user_name")) {
            returnValue.setSuccess(userService.updateUserPasswordBasedOnUserName(value, updateUserPasswordDetails.getOldPassword(), updateUserPasswordDetails.getNewPassword()));
        }
        if (filter.matches("(?i)email_address")) {
            returnValue.setSuccess(userService.updateUserPasswordBasedOnEmailAddress(value, updateUserPasswordDetails.getOldPassword(), updateUserPasswordDetails.getNewPassword()));
        }
        if (!returnValue.isSuccess())
            throw new EntityServiceException("Provided password is wrong!");
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PostMapping(value = "/{filter}/{value}/resetPassword")
    public ResponseEntity<ValidatePasswordResponseModel> resetPassword(
            @Valid @RequestBody ValidateUserPasswordRequestModel resetUserPasswordDetails,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "value", required = true) String value
    ) {
        if (!filter.matches("(?i)uid|user_name|email_address"))
            throw new EntityServiceException("Only allowed to filter via : uid, user_name, email_address");
        ValidatePasswordResponseModel returnValue = new ValidatePasswordResponseModel();
        if (filter.matches("(?i)uid")) {
            returnValue.setSuccess(userService.resetUserPasswordBasedOnUid(value, resetUserPasswordDetails.getPassword()));
        }
        if (filter.matches("(?i)user_name")) {
            returnValue.setSuccess(userService.resetUserPasswordBasedOnUserName(value, resetUserPasswordDetails.getPassword()));
        }
        if (filter.matches("(?i)email_address")) {
            returnValue.setSuccess(userService.resetUserPasswordBasedOnEmailAddress(value, resetUserPasswordDetails.getPassword()));
        }
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }
}
