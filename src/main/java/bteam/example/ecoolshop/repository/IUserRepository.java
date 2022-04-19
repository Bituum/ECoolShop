package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select user_id from custom_user where username = :var", nativeQuery = true)
    Optional<Integer> findIdByUsername(@Param("var") String username);

    void deleteUserByUsername(String username);

    Optional<User> findUserByUsername(String username);
}
