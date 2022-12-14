package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    void saveUser(User user);
    Optional<User> getUserById(int id);
    void updateUser(User user);
    void deleteUserById(int id);
    List<User> getAllUsers();
    List<Role> getRoles();
    public Set<Role> findRolesByName(String roleName);
}
