package com.solvative.book_vault.services;




import com.solvative.book_vault.entitites.auth.AuthUser;
import com.solvative.book_vault.models.request.auth.LoginRequest;
import com.solvative.book_vault.models.response.auth.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUserService authUserService;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        AuthUser user = authUserService.loadDomainUserByUsername(request.getUsername());

        String token = jwtService.generateToken(user);

        System.out.println("TOKEN = " + token);

        return new LoginResponse(token);    }
}