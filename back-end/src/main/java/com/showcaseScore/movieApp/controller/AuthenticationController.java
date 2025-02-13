package com.showcaseScore.movieApp.controller;


import com.showcaseScore.movieApp.dtos.AuthenticationRequest;
import com.showcaseScore.movieApp.dtos.AuthenticationResponse;
import com.showcaseScore.movieApp.dtos.RegisterRequestDTO;
import com.showcaseScore.movieApp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/authenticate")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequestDTO requestDTO) {
        return ResponseEntity.ok(authenticationService.register(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authRequest));
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<AuthenticationResponse> getAllUsers(){
        return ResponseEntity.ok(authenticationService.getAllUsers());

    }

    @GetMapping("/get-users/{userId}")
    public ResponseEntity<AuthenticationResponse> getUSerByID(@PathVariable Long userId){
        return ResponseEntity.ok(authenticationService.getUsersById(userId));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody AuthenticationResponse authenticationResponse) {
        return ResponseEntity.ok(authenticationService.refreshToken(authenticationResponse));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<AuthenticationResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody RegisterRequestDTO reqres
    ){
        return ResponseEntity.ok(authenticationService.updateUser(userId, reqres));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        authenticationService.removeUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}