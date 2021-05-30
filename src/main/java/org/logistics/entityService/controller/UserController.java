package org.logistics.entityService.controller;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.model.request.CreateUserRequestModel;
import org.logistics.entityService.model.response.CreateUserResponseModel;
import org.logistics.entityService.model.response.GetAllUsersResponseModel;
import org.logistics.entityService.service.UserService;
import org.logistics.entityService.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/user")
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
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        log.info("Request received to create user with details : {} ", userDetails.toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        CreateUserResponseModel createUserResponseModel = modelMapper.map(userService.createUser(userDto), CreateUserResponseModel.class);
        log.info("Returning Details : {}", createUserResponseModel);
        return new ResponseEntity<>(createUserResponseModel, HttpStatus.CREATED);
    }
}
