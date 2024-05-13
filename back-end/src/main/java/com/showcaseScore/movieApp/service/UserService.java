package com.showcaseScore.movieApp.service;

import com.showcaseScore.movieApp.dtos.LoginRequestDTO;
import com.showcaseScore.movieApp.dtos.RegisterRequestDTO;
import com.showcaseScore.movieApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class UserService {

    UserRepository userRepository;

    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        return null;
    }

    @Transactional
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return null;
    }
}