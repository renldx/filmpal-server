package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.constant.ExceptionMessages;
import com.renldx.filmpal.server.constant.ResponseMessages;
import com.renldx.filmpal.server.model.Role;
import com.renldx.filmpal.server.model.RoleCode;
import com.renldx.filmpal.server.model.User;
import com.renldx.filmpal.server.payload.request.SigninRequest;
import com.renldx.filmpal.server.payload.request.SignupRequest;
import com.renldx.filmpal.server.payload.response.JwtResponse;
import com.renldx.filmpal.server.payload.response.MessageResponse;
import com.renldx.filmpal.server.repository.RoleRepository;
import com.renldx.filmpal.server.repository.UserRepository;
import com.renldx.filmpal.server.security.JwtUtils;
import com.renldx.filmpal.server.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ResponseMessages.USERNAME_TAKEN));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ResponseMessages.EMAIL_USED));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> roleNames = signUpRequest.getRole();
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

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(ResponseMessages.USER_REGISTERED));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

}
