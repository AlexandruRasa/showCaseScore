package com.showcaseScore.movieApp.service;

import com.github.javafaker.Faker;
import com.showcaseScore.movieApp.dtos.MovieDTO;
import com.showcaseScore.movieApp.exception.ApiCallException;
import com.showcaseScore.movieApp.exception.MoviesNotFoundException;
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

    /**
     * Retrieves a movie by its IMDb ID. If the movie does not exist in the database,
     * it fetches the movie from an external API and saves it.
     *
     * @param imdbId The IMDb ID of the movie.
     * @return The movie DTO.
     * @throws EntityNotFoundException If the movie is not found in the database or external API.
     * @throws IllegalArgumentException If the IMDb ID is null or empty.
     */
    public MovieDTO getMovie(String imdbId) throws EntityNotFoundException, IllegalArgumentException {
        if (imdbId == null || imdbId.trim().isEmpty()) {
            throw new IllegalArgumentException("IMDb ID cannot be null or empty.");
        }
        if (!movieRepository.existsByImdbID(imdbId)) {
            saveMovieByImdbId(imdbId);
        }
        Movie movie = movieRepository.findByImdbID(imdbId)
                .orElseThrow(EntityNotFoundException::new);
        MovieDTO movieDTO = ModelMapper.map(movie, MovieDTO.class);
        movieDTO.setReviews(reviewService.getReviewsForMovie(movie.getId()));
        return movieDTO;
    }

    /**
     * Retrieves a list of movies by genre.
     *
     * @param genreKeyword The genre to search for.
     * @return A list of movie DTOs.
     * @throws MoviesNotFoundException If no movies are found for the given genre.
     * @throws IllegalArgumentException If the genre keyword is null or empty.
     */
    public List<MovieDTO> getMoviesByGenre(String genreKeyword) throws MoviesNotFoundException,
            IllegalArgumentException {
        if (genreKeyword == null || genreKeyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre keyword cannot be null or empty.");
        }
        List<Movie> movies = movieRepository.findAllByGenre(genreKeyword.toLowerCase());
        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No movies found for genre: " + genreKeyword);
        }
        return movies.stream()
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

    /**
     * Saves a movie to the database by fetching its details from an external API using the IMDb ID.
     *
     * @param imdbId The IMDb ID of the movie.
     * @throws ApiCallException If the API call to fetch movie data fails.
     * @throws IllegalArgumentException If the IMDb ID is null or empty.
     */
    @Transactional
    public void saveMovieByImdbId(String imdbId) throws ApiCallException, IllegalArgumentException {
        if (imdbId == null || imdbId.trim().isEmpty()) {
            throw new IllegalArgumentException("IMDb ID cannot be null or empty.");
        }
        Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbId);
        if (existingMovie.isPresent()) {
            log.info("Movie with IMDb ID {} already exists", imdbId);
            return;
        }
        MovieDTO movieDto = callApi.getMovieFromOmdb(imdbId);
        if (movieDto.getImdbID() == null) {
            throw new ApiCallException("Failed to fetch movie data from OMDB for IMDb ID: " + imdbId);
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

    /**
     * Deletes a movie from the database by its IMDb ID.
     *
     * @param imdbId The IMDb ID of the movie.
     * @throws EntityNotFoundException If the movie is not found in the database.
     * @throws IllegalArgumentException If the IMDb ID is null or empty.
     */
    @Transactional
    public void deleteMovie(String imdbId) throws EntityNotFoundException, IllegalArgumentException {
        if (imdbId == null || imdbId.trim().isEmpty()) {
            throw new IllegalArgumentException("IMDb ID cannot be null or empty.");
        }
        Optional<Movie> existingMovie = movieRepository.findByImdbID(imdbId);
        if (existingMovie.isEmpty()) {
            throw new EntityNotFoundException("Movie with IMDb ID " + imdbId + " does not exist.");
        }
        movieRepository.delete(existingMovie.get());
        log.info("Movie with IMDb ID {} has been deleted", imdbId);
    }

    @Transactional
    public void delete() {
        movieRepository.deleteAll();
    }
}
