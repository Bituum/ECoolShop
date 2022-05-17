package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.UserRegistrationApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<UserRegistrationApply, Integer> {
    Optional<UserRegistrationApply> getUserRegistrationAppliesByUsername(String username);

    void deleteByUsername(String username);
}
