package com.showcaseScore.movieApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "MOVIELIST")
public class MovieList {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MOVIE_LIST_SEQUENCE"
    )
    @SequenceGenerator(
            name = "MOVIE_LIST_SEQUENCE",
            sequenceName = "SEQ_MOVIE_LIST_ID",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "user_id")
    private Integer userId;

    @ManyToMany()
    @JoinTable(
            name = "list_movie",
            joinColumns = @JoinColumn(name = "listofuser_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @JsonManagedReference
    private List<Movie> movies;
}
