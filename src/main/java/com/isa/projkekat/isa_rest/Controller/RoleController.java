package com.isa.projkekat.isa_rest.Controller;

import com.isa.projkekat.isa_rest.Model.Role;
import com.isa.projkekat.isa_rest.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleRepo roleRepo;

    @GetMapping
    public List<Role> getAllRoles(){
        return roleRepo.findAll();
    }

    @PostMapping
    public String saveRole(@RequestBody Role role){
        roleRepo.save(role);
        return "Role created";
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable long id){
        return roleRepo.findById(id).get();
    }

    @PutMapping("/{id}")
    public String updateRole(@PathVariable long id, @RequestBody Role role){
        Role updatedRole = roleRepo.findById(id).get();
        updatedRole.setName(role.getName());
        return "Role updated";

    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable long id){
        roleRepo.deleteById(id);
        return "Role deleted";
    }

}
