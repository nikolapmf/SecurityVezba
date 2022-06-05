package com.example.userservice.service;

import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        log.info("Saving new user");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role");
        return roleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role to user");
        User u = userRepo.findByUsername(username);
        Role r = roleRepo.findByName(roleName);
        u.getRoles().add(r);
    }

    public User getUser(String username) {
        log.info("Fetching user by username");
        return userRepo.findByUsername(username);
    }

    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username);
        if (u == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        u.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), authorities);
    }
}
