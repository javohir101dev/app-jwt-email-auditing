package emailauditing.controller;

import emailauditing.payload.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import emailauditing.payload.ApiResponse;
import emailauditing.payload.RegisterDto;
import emailauditing.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser( @RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/veriftEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code){
        ApiResponse apiResponse = authService.verifyEmail(email, code);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse = authService.login(loginDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 401).body(apiResponse);
    }

}
