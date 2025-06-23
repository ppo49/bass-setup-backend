package com.isa.projkekat.isa_rest.Controller;

import com.isa.projkekat.isa_rest.Model.User;
import com.isa.projkekat.isa_rest.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
public class UserController {

    @GetMapping(value="/")
    public String getPage(){
        return "welcome";
    }

    @Autowired
    private UserRepo userRepo;

    @GetMapping(value ="/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user) {
        userRepo.save(user);
        return "saved";
    }

    @PutMapping(value = "/update/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user) {
        User updatedUser = userRepo.findById(id).get();
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        userRepo.save(updatedUser);
        return "updated";
    }

    @DeleteMapping(value ="delete/{id}")
    public String deleteUser(@PathVariable long id){
        User deletedUser = userRepo.findById(id).get();
        userRepo.delete(deletedUser);
        return  "delete user with the id: "+id;

    }

}
