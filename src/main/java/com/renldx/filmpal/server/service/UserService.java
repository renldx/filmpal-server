package com.renldx.filmpal.server.service;

import com.renldx.filmpal.server.constant.ExceptionMessages;
import com.renldx.filmpal.server.model.Role;
import com.renldx.filmpal.server.model.RoleCode;
import com.renldx.filmpal.server.model.User;
import com.renldx.filmpal.server.repository.RoleRepository;
import com.renldx.filmpal.server.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkIfEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void createUser(String username, String email, String password, Set<String> roleNames) {
        var user = new User(username,
                email,
                encoder.encode(password));

        var roles = getUserRoles(roleNames);
        setUserRoles(user, roles);

        userRepository.save(user);
    }

    private Set<Role> getUserRoles(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();

        if (roleNames == null) {
            Role userRole = roleRepository.findByName(RoleCode.USER)
                    .orElseThrow(() -> new RuntimeException(ExceptionMessages.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            roleNames.forEach(role -> {
                if (RoleCode.ADMIN.name().equalsIgnoreCase(role)) {
                    Role adminRole = roleRepository.findByName(RoleCode.ADMIN)
                            .orElseThrow(() -> new RuntimeException(ExceptionMessages.ROLE_NOT_FOUND));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleCode.USER)
                            .orElseThrow(() -> new RuntimeException(ExceptionMessages.ROLE_NOT_FOUND));
                    roles.add(userRole);
                }
            });
        }

        return roles;
    }

    private void setUserRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
    }

}
