package bteam.example.ecoolshop.controller;

import bteam.example.ecoolshop.entity.Role;
import bteam.example.ecoolshop.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestRoleController {
    private final RoleService service;

    public TestRoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping("/getRoles")
    public List<Role> getRoles() {
        return service.getAllRoles();
    }
}
