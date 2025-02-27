package com.renldx.filmpal.server.controller;

import com.renldx.filmpal.server.constant.ResponseMessages;
import com.renldx.filmpal.server.helper.JwtHelper;
import com.renldx.filmpal.server.payload.request.SigninRequest;
import com.renldx.filmpal.server.payload.request.SignupRequest;
import com.renldx.filmpal.server.payload.response.JwtResponse;
import com.renldx.filmpal.server.payload.response.MessageResponse;
import com.renldx.filmpal.server.security.UserDetailsImpl;
import com.renldx.filmpal.server.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;
    private final JwtHelper jwtHelper;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtHelper jwtHelper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userService.checkIfUsernameExists(signupRequest.username())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ResponseMessages.USERNAME_TAKEN));
        }

        userService.createUser(signupRequest.username(), signupRequest.password(), signupRequest.roles());

        return ResponseEntity.ok(null);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SigninRequest signinRequest) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.username(), signinRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var jwt = jwtHelper.generateJwt(authentication);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                "Bearer",
                jwt,
                userDetails.getUsername(),
                roles));
    }

}
