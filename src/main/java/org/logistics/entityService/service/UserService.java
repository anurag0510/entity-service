package org.logistics.entityService.service;

import org.logistics.entityService.shared.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers(boolean allActiveUsers);

    List<UserDto> getAllUsersWithUid(String value, boolean allUsers);

    List<UserDto> getAllUsersWithEmailAddress(String value, boolean all);

    List<UserDto> getAllUsersWithUserName(String value, boolean all);

    UserDto updateUserBasedOnUid(UserDto userDto, String uid);

    UserDto updateUserBasedOnUserName(UserDto userDto, String userName);

    UserDto updateUserBasedOnEmailAddress(UserDto userDto, String emailAddress);

    List<UserDto> getAllUsersWithMobileNumber(String countryCode, String mobileNumber, boolean allUsers);
}
