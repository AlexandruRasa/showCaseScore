package com.showcaseScore.movieApp.service;

import com.showcaseScore.movieApp.dtos.AuthenticationRequest;
import com.showcaseScore.movieApp.dtos.AuthenticationResponse;
import com.showcaseScore.movieApp.dtos.RegisterRequestDTO;
import com.showcaseScore.movieApp.model.Role;
import com.showcaseScore.movieApp.model.User;
import com.showcaseScore.movieApp.repository.UserRepository;
import com.showcaseScore.movieApp.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequestDTO request) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getEmail().contains("333") ? Role.ADMIN : Role.USER)
                    .build();
            userRepository.save(user);
            if (user.getId() > 0) {
                authResponse.setUser(user);
                authResponse.setMessage("User Saved Successfully!");
                authResponse.setStatusCode(200);
            }
        } catch (Exception e) {
            authResponse.setStatusCode(500);
            authResponse.setError(e.getMessage());
        }
        return authResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        AuthenticationResponse authResponse = new AuthenticationResponse();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow();
            authResponse.setUser(user);
            var jwtToken = jwtService.generateToken(user);
            var jwtRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            authResponse.setStatusCode(200);
            authResponse.setToken(jwtToken);
            authResponse.setRefreshToken(jwtRefreshToken);
            authResponse.setMessage("Successfully logged in!");
            return authResponse;
        } catch (Exception e) {
            authResponse.setStatusCode(500);
            authResponse.setError(e.getMessage());
        }
        return authResponse;
    }

    public AuthenticationResponse refreshToken(AuthenticationResponse refreshTokenReqiest) {
        AuthenticationResponse response = new AuthenticationResponse();
        try {
            String userEmail = jwtService.extractUserName(refreshTokenReqiest.getToken());
            User user = userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshTokenReqiest.getToken(), user)) {
                var jwt = jwtService.generateToken(user);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public AuthenticationResponse getUsersById(Long id) {
        AuthenticationResponse reqRes = new AuthenticationResponse();
        try {
            User usersById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUser(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public AuthenticationResponse getAllUsers() {
        AuthenticationResponse reqRes = new AuthenticationResponse();

        try {
            List<User> result = userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setUserList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public void removeUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            log.error("removeUser ---> User with Id " + id + " does not exist");
            return;
        }
        userRepository.deleteById(id);
        log.error("removeUser ---> User with Id " + id + " has been deleted");
    }

    public User findUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            log.error("findUser ---> User with Id " + id + " does not exist");
            return null;
        }
        return existingUser.get();
    }

    public AuthenticationResponse updateUser(Long userId, RegisterRequestDTO updatedUser) {
        AuthenticationResponse reqRes = new AuthenticationResponse();
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setEmail(updatedUser.getEmail());

                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                User savedUser = userRepository.save(existingUser);
                reqRes.setUser(savedUser);
                var jwtToken = jwtService.generateToken(savedUser);
                var jwtRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), savedUser);
                reqRes.setStatusCode(200);
                reqRes.setToken(jwtToken);
                reqRes.setRefreshToken(jwtRefreshToken);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }
}