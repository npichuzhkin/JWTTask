package com.npichuzhkin.JWTTask.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.npichuzhkin.JWTTask.dto.UserDTO;
import com.npichuzhkin.JWTTask.enums.Role;
import com.npichuzhkin.JWTTask.services.UserService;
import com.npichuzhkin.JWTTask.views.Views;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @JsonView(Views.Public.class)
    @Secured({"ROLE_USER","ROLE_MODERATOR","ROLE_SUPER_ADMIN"})
    public ResponseEntity<List<UserDTO>> showAll(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @JsonView(Views.Public.class)
    @Secured({"ROLE_USER","ROLE_MODERATOR","ROLE_SUPER_ADMIN"})
    public ResponseEntity<UserDTO> showOne(@PathVariable String username){
        return new ResponseEntity<>(userService.findByUserName(username), HttpStatus.OK);
    }

    @PostMapping("/add")
    @Secured("ROLE_SUPER_ADMIN")
    public ResponseEntity<HttpStatus> addNew(@RequestBody @Valid UserDTO newUser){
        userService.save(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/put/{username}")
    @Secured({"ROLE_MODERATOR","ROLE_SUPER_ADMIN"})
    public ResponseEntity<HttpStatus> update(@PathVariable String username,
                                             @RequestBody @Valid UserDTO updatedUser){
        userService.update(username, updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/remove/{username}")
    @Secured("ROLE_SUPER_ADMIN")
    public ResponseEntity<HttpStatus> remove(@PathVariable String username){
        userService.delete(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/unlock/{username}")
    @Secured({"ROLE_MODERATOR","ROLE_SUPER_ADMIN"})
    public ResponseEntity<HttpStatus> unlockUser(@PathVariable String username) {
        userService.unlockUserAccount(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
