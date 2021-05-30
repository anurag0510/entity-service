package org.logistics.entityService.data;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "SELECT COUNT(u) FROM users u WHERE u.user_name LIKE ?1 AND u.is_active = true",
            nativeQuery = true)
    Long findByUserName(String userName);

    @Query(value = "SELECT COUNT(u) FROM users u WHERE u.email_address LIKE ?1 AND u.is_active = true",
            nativeQuery = true)
    Long findByEmailAddress(String emailAddress);

    @Query(value = "SELECT COUNT(u) FROM users u WHERE u.mobile_number LIKE ?1 AND u.country_code LIKE ?2 AND u.is_active = true",
            nativeQuery = true)
    Long findByMobileNumber(String mobileNumber, String countryCode);

    @Query(value = "SELECT * FROM users u WHERE u.is_active = true AND u.is_deleted = false",
            nativeQuery = true)
    Iterable<UserEntity> findAllActiveUsers();

    @Query(value = "SELECT * FROM users u WHERE u.uid = ?1 and u.is_active = true AND u.is_deleted = false",
            nativeQuery = true)
    Iterable<UserEntity> findAllActiveUsersWithUid(String uid);

    @Query(value = "SELECT * FROM users u WHERE u.uid = ?1",
            nativeQuery = true)
    Iterable<UserEntity> findAllUsersWithUid(String uid);

    @Query(value = "SELECT * FROM users u WHERE u.user_name LIKE ?1 and u.is_active = true AND u.is_deleted = false",
            nativeQuery = true)
    Iterable<UserEntity> findAllActiveUsersWithUserName(String userName);

    @Query(value = "SELECT * FROM users u WHERE u.user_name LIKE ?1",
            nativeQuery = true)
    Iterable<UserEntity> findAllUsersWithUserName(String userName);

    @Query(value = "SELECT * FROM users u WHERE u.email_address LIKE ?1 and u.is_active = true AND u.is_deleted = false",
            nativeQuery = true)
    Iterable<UserEntity> findAllActiveUsersWithEmailAddress(String emailAddress);

    @Query(value = "SELECT * FROM users u WHERE u.email_address LIKE ?1",
            nativeQuery = true)
    Iterable<UserEntity> findAllUsersWithEmailAddress(String emailAddress);

    @Query(value = "SELECT * FROM users u WHERE u.mobile_number LIKE ?1 and u.country_code LIKE ?2 and u.is_active = true AND u.is_deleted = false",
            nativeQuery = true)
    Iterable<UserEntity> findAllActiveUsersWithMobileNumber(String countryCode, String mobileNumber);

    @Query(value = "SELECT * FROM users u WHERE u.mobile_number LIKE ?1 and u.country_code LIKE ?2",
            nativeQuery = true)
    Iterable<UserEntity> findAllUsersWithMobileNumber(String countryCode, String mobileNumber);

    @Modifying
    @Query(value = "UPDATE users SET user_name = ?1, first_name = ?2, last_name = ?3, email_address = ?4, mobile_number = ?5, country_code = ?6, country = ?7, city = ?8, address = ?9, updated_at = CURRENT_TIMESTAMP WHERE uid = ?10 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updateUserBasedOnUid(String userName, String firstName, String lastName, String emailAddress, String mobileNumber, String countryCode, String country, String city, String address, String uid);

    @Modifying
    @Query(value = "UPDATE users SET user_name = ?1, first_name = ?2, last_name = ?3, email_address = ?4, mobile_number = ?5, country_code = ?6, country = ?7, city = ?8, address = ?9, updated_at = CURRENT_TIMESTAMP WHERE user_name = ?10 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updateUserBasedOnUserName(String userName, String firstName, String lastName, String emailAddress, String mobileNumber, String countryCode, String country, String city, String address, String oldUserName);

    @Modifying
    @Query(value = "UPDATE users SET user_name = ?1, first_name = ?2, last_name = ?3, email_address = ?4, mobile_number = ?5, country_code = ?6, country = ?7, city = ?8, address = ?9, updated_at = CURRENT_TIMESTAMP WHERE email_address = ?10 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updateUserBasedOnEmailAddress(String userName, String firstName, String lastName, String emailAddress, String mobileNumber, String countryCode, String country, String city, String address, String emailAddress1);

    @Modifying
    @Query(value = "UPDATE users SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE uid = ?1 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void deleteUserBasedOnUid(String uid);

    @Modifying
    @Query(value = "UPDATE users SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE user_name = ?1 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void deleteUserBasedOnUserName(String userName);

    @Modifying
    @Query(value = "UPDATE users SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP, updated_at = CURRENT_TIMESTAMP WHERE email_address = ?1 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void deleteUserBasedOnEmailAddress(String emailAddress);

    @Modifying
    @Query(value = "UPDATE users SET password = ?1, updated_at = CURRENT_TIMESTAMP WHERE uid = ?2 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updatePasswordBasedOnUid(String newEncryptedPassword, String uid);

    @Modifying
    @Query(value = "UPDATE users SET password = ?1, updated_at = CURRENT_TIMESTAMP WHERE user_name = ?2 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updatePasswordBasedOnUserName(String newEncryptedPassword, String userName);

    @Modifying
    @Query(value = "UPDATE users SET password = ?1, updated_at = CURRENT_TIMESTAMP WHERE email_address = ?2 AND is_deleted = false AND is_active = true", nativeQuery = true)
    void updatePasswordBasedOnEmailAddress(String newEncryptedPassword, String emailAddress);
}
