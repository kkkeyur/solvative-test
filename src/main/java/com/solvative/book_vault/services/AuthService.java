package com.solvative.book_vault.services;


import com.solvative.book_vault.entitites.auth.AuthUser;
import com.solvative.book_vault.models.request.auth.LoginRequest;
import com.solvative.book_vault.models.response.auth.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthUserService authUserService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       AuthUserService authUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        AuthUser user = authUserService.loadDomainUserByUsername(request.getUsername());
        log.info("user fouund {}",user);
        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}
