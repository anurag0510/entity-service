package org.logistics.entityService.service;

import org.logistics.entityService.shared.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers(boolean allActiveUsers);
}
