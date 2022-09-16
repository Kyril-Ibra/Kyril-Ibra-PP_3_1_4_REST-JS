package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RESTController {

    public final UserService userService;

    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable int id) {
        Optional<User> user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {  //в теле метода отправляем user
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>("User with ID = " + id + " was deleted", HttpStatus.OK);
    }

    @GetMapping("/user-view")
    public ResponseEntity<User> showLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>((User) auth.getPrincipal(), HttpStatus.OK);
    }

    @GetMapping("user/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = userService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
