package com.showcaseScore.movieApp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieListDTO {

    @JsonProperty("Search")
    private List<MovieDTO> movies;
    @JsonProperty("totalResults")
    private String totalResults;
}
