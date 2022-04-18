package bteam.example.ecoolshop.service;

import bteam.example.ecoolshop.entity.Role;
import bteam.example.ecoolshop.repository.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final IRoleRepository roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(int id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("wrong id of role")
        );
    }
}
