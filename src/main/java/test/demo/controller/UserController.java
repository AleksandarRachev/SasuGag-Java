package test.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import test.demo.dto.RegisterUserRequest;
import test.demo.dto.UserLoginRequest;
import test.demo.dto.UserLoginResponse;
import test.demo.dto.UserResponse;
import test.demo.exception.TokenExpiredException;
import test.demo.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
@PreAuthorize("permitAll()")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(userService.registerUser(registerUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) throws TokenExpiredException {
        return ResponseEntity.ok(userService.loginUser(userLoginRequest));
    }

    @GetMapping("/token")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity isTokenValid(@RequestAttribute("userId") String userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

}
