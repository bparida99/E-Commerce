package com.user.api;

import com.user.dto.CredentialDto;
import com.user.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public String authenticate(@RequestBody CredentialDto credentialDto){
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (credentialDto.email(),credentialDto.passWord()));
        return jwtUtil.generateToken(credentialDto.email());
    }
}
