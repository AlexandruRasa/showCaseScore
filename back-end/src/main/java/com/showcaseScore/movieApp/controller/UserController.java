package com.showcaseScore.movieApp.controller;



import com.showcaseScore.movieApp.dtos.LoginRequestDTO;
import com.showcaseScore.movieApp.dtos.RegisterRequestDTO;
import com.showcaseScore.movieApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return userService.register(registerRequestDTO);
    }

    /*
    TODO: Curs 2

    @PreAuthorize("hasAnyAuthority('admin','author','reader')")
    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestParam String email) {
        return userService.changeEmail(email);
    }

    @PreAuthorize("hasAnyAuthority('admin','author','reader')")
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam String password) {
        return userService.changePassword(password);
    }
     */
}