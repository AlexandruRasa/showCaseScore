package com.showcaseScore.movieApp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LoginResponseDTO {
    private Long id;
    private String email;
    private String role;
    private String token;
}