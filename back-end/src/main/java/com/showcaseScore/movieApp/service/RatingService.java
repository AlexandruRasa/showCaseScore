package com.showcaseScore.movieApp.service;

import com.showcaseScore.movieApp.model.Movie;
import com.showcaseScore.movieApp.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingService {

    private final RatingRepository ratingRepository;

    public void saveRatingsForMovie(Movie movie) {
        if (movie.getRatings() != null && !movie.getRatings().isEmpty()) {
            movie.getRatings().forEach(rating -> rating.setMovie(movie));
            ratingRepository.saveAll(movie.getRatings());
        }
    }
}
