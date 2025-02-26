package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.constant.ResponseMessages;
import com.renldx.filmpal.server.payload.request.SigninRequest;
import com.renldx.filmpal.server.payload.request.SignupRequest;
import com.renldx.filmpal.server.payload.response.JwtResponse;
import com.renldx.filmpal.server.payload.response.MessageResponse;
import com.renldx.filmpal.server.security.JwtUtils;
import com.renldx.filmpal.server.security.UserDetailsImpl;
import com.renldx.filmpal.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userService.checkIfUsernameExists(signupRequest.username())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ResponseMessages.USERNAME_TAKEN));
        }

        if (userService.checkIfEmailExists(signupRequest.email())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ResponseMessages.EMAIL_USED));
        }

        userService.createUser(signupRequest.username(), signupRequest.email(), signupRequest.password(), signupRequest.role());

        return ResponseEntity.ok(new MessageResponse(ResponseMessages.USER_REGISTERED));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.username(), signinRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

}
