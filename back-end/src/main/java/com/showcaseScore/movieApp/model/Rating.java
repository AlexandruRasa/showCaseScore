package com.showcaseScore.movieApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "RATING")
public class Rating {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RATING_SEQUENCE"
    )
    @SequenceGenerator(
            name = "RATING_SEQUENCE",
            sequenceName = "SEQ_RATING_ID",
            allocationSize = 1
    )
    @Column(name = "id")
    private Long id;
    @Column(name = "source")
    private String source;
    @Column(name = "note")
    private String note;
    @ManyToOne()
    @JoinColumn(name = "movie_id")
    @JsonManagedReference
    private Movie movie;
}
