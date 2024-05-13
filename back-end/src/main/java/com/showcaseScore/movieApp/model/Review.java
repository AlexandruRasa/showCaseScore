package com.showcaseScore.movieApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "REVIEW_SEQUENCE"
    )
    @SequenceGenerator(
            name = "REVIEW_SEQUENCE",
            sequenceName = "SEQ_REVIEW_ID",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "rating")
    private String rating;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    @JsonManagedReference
    private User userId;
    @ManyToOne()
    @JoinColumn(
            name = "movie_id"
    )
    @JsonBackReference
    private Movie movie;
}
