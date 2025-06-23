package com.isa.projkekat.isa_rest.Controller;

import com.isa.projkekat.isa_rest.Dto.AuthResponse;
import com.isa.projkekat.isa_rest.Dto.LoginRequest;
import com.isa.projkekat.isa_rest.Dto.RegisterRequest;
import com.isa.projkekat.isa_rest.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
