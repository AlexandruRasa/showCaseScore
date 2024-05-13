package com.showcaseScore.movieApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MOVIE")
public class Movie {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MOVIE_SEQUENCE"
    )
    @SequenceGenerator(
            name = "MOVIE_SEQUENCE",
            sequenceName = "SEQ_MOVIE_ID",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "plot", length = 3000)
    private String plot;
    @Column(name = "genre")
    private String genre;
    @Column(name = "director")
    private String director;
    @Column(name = "year")
    private String year;
    @Column(name="actors")
    private String actors;
    @Column(name = "awards")
    private String awards;
    @Column(name = "runtime")
    private String  runtime;
    @Column(name = "release_date")
    private String releaseDate;
    @Column(name = "poster")
    private String poster;
    @Column(name = "trailer")
    private String trailer;
    @Column(name = "box_office")
    private String boxOffice;
    @Column(name = "imdb_rating")
    private String imdbRating;
    @Column(name = "imdb_id", unique = true)
    private String imdbID;

    @ManyToMany(
            mappedBy = "movies",
            cascade = CascadeType.ALL
    )
    @JsonBackReference
    private List<MovieList> movieLists;
    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonBackReference
    private List<Rating> ratings;
    @OneToMany(
            mappedBy = "movie",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Review> reviews;
}
