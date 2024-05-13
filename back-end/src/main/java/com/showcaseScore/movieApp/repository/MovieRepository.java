package com.showcaseScore.movieApp.repository;

import com.showcaseScore.movieApp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findByImdbID(String imdbId);

    @Query("SELECT movie FROM MOVIE movie WHERE LOWER(movie.genre) LIKE %:lowercaseKeyword%")
    List<Movie> findAllByGenre(String lowercaseKeyword);

    @Query("SELECT movie FROM MOVIE movie WHERE LOWER(movie.awards) LIKE %:lowercaseWin% OR LOWER(movie.awards) LIKE %:lowercaseWon%")
    List<Movie> findAllByAwards(String lowercaseWin, String lowercaseWon);

    @Query("SELECT movie FROM MOVIE movie ORDER BY movie.imdbRating DESC")
    List<Movie> findAllByImdbRating();

    @Query("SELECT movie FROM MOVIE movie ORDER BY CAST(REPLACE(REPLACE(movie.boxOffice, '$', ''), ',', '') AS DOUBLE) DESC")
    List<Movie> findAllOrderByBoxOffice();
}
