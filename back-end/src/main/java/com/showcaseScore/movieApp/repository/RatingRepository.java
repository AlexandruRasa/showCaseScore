package com.showcaseScore.movieApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.showcaseScore.movieApp.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
