package org.logistics.entityService.service;

import org.logistics.entityService.model.request.ValidateUserPasswordRequestModel;
import org.logistics.entityService.shared.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto, String requesterId);

    List<UserDto> getAllUsers(boolean allActiveUsers);

    List<UserDto> getAllUsersWithUid(String value, boolean allUsers);

    List<UserDto> getAllUsersWithEmailAddress(String value, boolean all);

    List<UserDto> getAllUsersWithUserName(String value, boolean all);

    UserDto updateUserBasedOnUid(UserDto userDto, String uid, String requesterId);

    UserDto updateUserBasedOnUserName(UserDto userDto, String userName, String requesterId);

    UserDto updateUserBasedOnEmailAddress(UserDto userDto, String emailAddress, String requesterId);

    List<UserDto> getAllUsersWithMobileNumber(String countryCode, String mobileNumber, boolean allUsers);

    UserDto deleteUserBasedOnUid(String uid);

    UserDto deleteUserBasedOnUserName(String userName);

    UserDto deleteUserBasedOnEmailAddress(String emailAddress);

    boolean validateUserPasswordBasedOnUid(String uid, ValidateUserPasswordRequestModel passwordDetails);

    boolean validateUserPasswordBasedOnUserName(String userName, ValidateUserPasswordRequestModel passwordDetails);

    boolean validateUserPasswordBasedOnEmailAddress(String emailAddress, ValidateUserPasswordRequestModel passwordDetails);

    boolean updateUserPasswordBasedOnUid(String uid, String oldPassword, String newPassword);

    boolean updateUserPasswordBasedOnUserName(String userName, String oldPassword, String newPassword);

    boolean updateUserPasswordBasedOnEmailAddress(String emailAddress, String oldPassword, String newPassword);

    boolean resetUserPasswordBasedOnUid(String uid, String password);

    boolean resetUserPasswordBasedOnUserName(String userName, String password);

    boolean resetUserPasswordBasedOnEmailAddress(String emailAddress, String password);
}
