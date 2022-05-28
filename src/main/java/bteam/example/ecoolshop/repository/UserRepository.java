package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.AUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AUser, Integer> {
    @Query(value = "select user_id from custom_user where username = :var", nativeQuery = true)
    Optional<Integer> findIdByUsername(@Param("var") String username);

    void deleteUserByUsername(String username);

    Optional<AUser> findUserByUsername(String username);

    //wont work
    @Modifying
    @Query(value = "update custom_user u set u.username = ?1, u.email = ?2, u.user_password = ?3, u.birthday = ?4" +
            " where u.id = ?5", nativeQuery = true)
    void updateUserInfoById(String username, String email, char[] password, LocalDateTime birthday, int id);
}
