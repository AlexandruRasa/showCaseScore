package com.showcaseScore.movieApp.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MovieDTO {

    @JsonProperty("Title")
    private String title;
    @JsonProperty("Plot")
    private String plot;
    @JsonProperty("Genre")
    private String genre;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Actors")
    private String actors;
    @JsonProperty("Year")
    private String year;
    @JsonProperty("Ratings")
    private List<RatingDTO> ratings;
    @JsonProperty("Reviews")
    private List<ReviewDTO> reviews;
    @JsonProperty("Awards")
    private String awards;
    @JsonProperty("Runtime")
    private String runtime;
    @JsonProperty("Released")
    @JsonFormat(pattern = "dd MMM yyyy")
    private String releaseDate;
    @JsonProperty("Poster")
    private String poster;
    @JsonProperty("Trailer")
    private String trailer;
    @JsonProperty("BoxOffice")
    private String boxOffice;
    @JsonProperty("imdbRating")
    private String imdbRating;
    @JsonProperty("imdbID")
    private String imdbID;
}
