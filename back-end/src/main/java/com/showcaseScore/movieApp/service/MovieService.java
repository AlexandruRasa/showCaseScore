package com.showcaseScore.movieApp.service;

import com.github.javafaker.Faker;
import com.showcaseScore.movieApp.dtos.MovieDTO;
import com.showcaseScore.movieApp.model.Movie;
import com.showcaseScore.movieApp.model.mapper.ModelMapper;
import com.showcaseScore.movieApp.repository.MovieRepository;
import com.showcaseScore.movieApp.util.CallApi;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingService ratingService;
    private final ReviewService reviewService;
    private final CallApi callApi;

    public MovieDTO getMovie(String imdbId) {
        Movie movie = movieRepository.findByImdbID(imdbId)
                .orElseGet(() -> {
                    saveMovieByImdbId(imdbId);
                    return movieRepository.findByImdbID(imdbId)
                            .orElseThrow(EntityNotFoundException::new);
                });
        MovieDTO movieDTO = ModelMapper.map(movie, MovieDTO.class);
        movieDTO.setReviews(reviewService.getReviewsForMovie(movie.getId()));
        return movieDTO;
    }

    public List<MovieDTO> getMoviesByGenre(String genreKeyword) {
        return movieRepository.findAllByGenre(genreKeyword).stream()
                .map(movie -> ModelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }


    public List<MovieDTO> getMoviesByAwards() {
        return movieRepository.findAllByAwards("win", "won").stream()
                .map(movie -> ModelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getMoviesByBoxOffice() {
        return movieRepository.findAllOrderByBoxOffice().stream()
                .map(movie -> ModelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getMoviesByRating() {
        return movieRepository.findAllByImdbRating().stream()
                .map(movie -> ModelMapper.map(movie, MovieDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMovieByImdbId(String imdbId) {
        Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbId);
        if (existingMovie.isPresent()) {
            log.info("saveMovieByImdbId ---> Movie with IMDb ID " + imdbId + " already exists");
            return;
        }
        MovieDTO movieDto = callApi.getMovieFromOmdb(imdbId);
        if (movieDto.getImdbID() == null) {
            log.error("saveMovieByImdbId ---> Error fetching movie data from OMDB for IMDb ID " + imdbId);
            return;
        }
        String urlTmdbTrailer;
        if (movieDto.getYear().contains("-") || movieDto.getYear().contains("–")) {
            urlTmdbTrailer = "https://api.themoviedb.org/3/tv/";
        } else {
            urlTmdbTrailer = "https://api.themoviedb.org/3/movie/";
        }
        String trailer = callApi.getTrailerFromTmdb(imdbId, urlTmdbTrailer);
        if (movieDto.getDirector().equals("N/A")) {
            Faker faker = new Faker();
            movieDto.setDirector(faker.harryPotter().character());
        }
        if (movieDto.getImdbRating().equals("N/A")) {
            movieDto.setImdbRating("5");
        }
        Movie movie = ModelMapper.map(movieDto, Movie.class);
        movie.setTrailer(trailer);
        movieRepository.save(movie);
        ratingService.saveRatingsForMovie(movie);
    }

    @Transactional
    public void saveMovieList(Integer id) {
        List<String> ids = callApi.getImdbIdsFromTmdb(id);
        ids.forEach(this::saveMovieByImdbId);
    }
    @Transactional
    public void deleteMovie(String imdbId) {
        Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbId);
        if (existingMovie.isEmpty()) {
            log.error("deleteMovie ---> Movie with IMDb ID " + imdbId + " does not exist");
            return;
        }
        movieRepository.delete(existingMovie.get());
        log.info("deleteMovie ---> Movie with IMDb ID " + imdbId + " has been deleted");
    }

    @Transactional
    public void delete() {
        movieRepository.deleteAll();
    }
}
