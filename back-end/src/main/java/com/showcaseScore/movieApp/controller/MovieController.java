package com.showcaseScore.movieApp.controller;

import com.showcaseScore.movieApp.dtos.MovieDTO;
import com.showcaseScore.movieApp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/get/{imdbId}")
    public ResponseEntity<MovieDTO> getMovieByTitle(@PathVariable String imdbId) {
        MovieDTO movie = movieService.getMovie(imdbId);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable String genre) {
        List<MovieDTO> movies = movieService.getMoviesByGenre(genre);
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/awards")
    public ResponseEntity<List<MovieDTO>> getMoviesByAwards() {
        List<MovieDTO> movies = movieService.getMoviesByAwards();
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/boxOffice")
    public ResponseEntity<List<MovieDTO>> getMoviesByBoxOffice() {
        List<MovieDTO> movies = movieService.getMoviesByBoxOffice();
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/rating")
    public ResponseEntity<List<MovieDTO>> getMoviesByRating() {
        List<MovieDTO> movies = movieService.getMoviesByRating();
        if (movies != null && !movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/saveMovieByImdbId")
    public ResponseEntity<Void> saveMovieByImdbId(@RequestParam String imdbId) {
        movieService.saveMovieByImdbId(imdbId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/saveMovieList/{id}")
    public ResponseEntity<Void> saveMovieList(@PathVariable Integer id) {
        movieService.saveMovieList(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{imdbId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String imdbId) {
        try {
            movieService.deleteMovie(imdbId);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete() {
        movieService.delete();
        return ResponseEntity.noContent().build();
    }
}
