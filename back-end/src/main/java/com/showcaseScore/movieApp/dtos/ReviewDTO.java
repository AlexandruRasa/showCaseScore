package com.showcaseScore.movieApp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    @JsonProperty("rating")
    private String rating;
    @JsonProperty("text")
    private String text;
    @JsonProperty("userEmail")
    private String userEmail;
}
