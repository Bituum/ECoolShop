package bteam.example.ecoolshop.repository;

import bteam.example.ecoolshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "select * from db_roles join user_role on db_roles.role_id = user_role.user_id where user_id = :var ", nativeQuery = true)
    Optional<Role> getRoleByUserId(@Param("var") int id);
}
