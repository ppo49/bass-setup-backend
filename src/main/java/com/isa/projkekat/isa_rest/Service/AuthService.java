package com.isa.projkekat.isa_rest.Service;

import com.isa.projkekat.isa_rest.Dto.AuthResponse;
import com.isa.projkekat.isa_rest.Dto.LoginRequest;
import com.isa.projkekat.isa_rest.Dto.RegisterRequest;
import com.isa.projkekat.isa_rest.Repository.UserRepo;
import com.isa.projkekat.isa_rest.Model.Role;
import com.isa.projkekat.isa_rest.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists.");
        }


        Role.Roles userRole;
        try {
            userRole = Role.Roles.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            userRole = Role.Roles.USER;
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();
        var savedUser = userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", savedUser.getId());
        extraClaims.put("role", savedUser.getRole().name());

        var jwtToken = jwtService.generateToken(extraClaims, savedUser);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());

        var jwtToken = jwtService.generateToken(extraClaims, user);

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}