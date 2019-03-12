package com.hotch.Rest;

import com.hotch.Entities.User;
import com.hotch.Service.UserNotFoundException;
import com.hotch.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ResponseBody
    public User add(@RequestBody User newUser){
        return userService.add(newUser);
    }

    @PutMapping("users/{id}")
    @ResponseBody
    public User replace(@RequestBody User newUser, @PathVariable int id){
        return userService.edit(id,newUser);
    }

    @GetMapping("/users/{id}")
    @ResponseBody
    public User show(@PathVariable int id){
        try {
            return userService.show(id);
        } catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> list(){
        return userService.list();
    }

    @DeleteMapping("/users/{id}")
    @ResponseBody
    public void delete(@PathVariable int id){
        try {
            userService.delete(id);
        } catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find user to delete", e);
        }
    }


}
