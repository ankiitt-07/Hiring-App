package com.hiringapp.controller;

import com.hiringapp.model.authEntity.AuthRequest;
import com.hiringapp.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) {
        log.info("Generating token");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.userName(),
                        authRequest.password())
        );
        return jwtUtil.generateToken(authRequest.userName());
    }
}
