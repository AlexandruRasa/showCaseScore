package com.showcaseScore.movieApp.repository;

import com.showcaseScore.movieApp.model.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieListRepository extends JpaRepository<MovieList, Integer> {
}
