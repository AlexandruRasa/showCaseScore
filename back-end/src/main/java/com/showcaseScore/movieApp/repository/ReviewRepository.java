package com.showcaseScore.movieApp.repository;

import com.showcaseScore.movieApp.model.Movie;
import com.showcaseScore.movieApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.showcaseScore.movieApp.model.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findById(Long id);
    Optional<Review> findByUserIdAndMovieId(User userId, Long movie_id);
    @Query("SELECT review FROM REVIEW review WHERE review.movie.id = :movieId")
    List<Review> findAllById(Long movieId);
}
