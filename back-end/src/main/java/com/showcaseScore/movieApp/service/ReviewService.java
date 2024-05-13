package com.showcaseScore.movieApp.service;

import com.showcaseScore.movieApp.dtos.ReviewDTO;
import com.showcaseScore.movieApp.model.Movie;
import com.showcaseScore.movieApp.model.Review;
import com.showcaseScore.movieApp.model.User;
import com.showcaseScore.movieApp.model.mapper.ModelMapper;
import com.showcaseScore.movieApp.repository.MovieRepository;
import com.showcaseScore.movieApp.repository.ReviewRepository;
import com.showcaseScore.movieApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public void saveReviewForMovie(String imdbId, Long userId, ReviewDTO reviewDTO) {
        Optional<Movie> movieOptional = movieRepository.findByImdbID(imdbId);
        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            Optional<User> userOptional = Optional.ofNullable(authenticationService.findUser(userId));
            if (userOptional.isPresent()
                    && reviewRepository.findByUserIdAndMovieId(
                            userOptional.get(),
                            movieOptional.get().getId()).isEmpty()
            ) {
                User user = userOptional.get();
                Review review = ModelMapper.map(reviewDTO, Review.class);
                review.setMovie(movie);
                review.setUserId(user);
                movieOptional.get().getReviews().add(review);
                reviewRepository.save(review);
            } else {
                log.error("User with ID " + userId + " does not exist.");
            }
        } else {
            log.error("Movie with IMDb ID " + imdbId + " does not exist.");
        }
    }

    public ReviewDTO getReviewById(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            ReviewDTO reviewDTO = ModelMapper.map(reviewOptional.get(), ReviewDTO.class);
            reviewDTO.setUserEmail(reviewOptional.get().getUserId().getEmail());
            return reviewDTO;
        } else {
            return null;
        }
    }

    public List<ReviewDTO> getReviewsForMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findAllById(movieId);
        List<ReviewDTO> result = new ArrayList<>();
        for (Review r: reviews) {
            ReviewDTO reviewDTO = ModelMapper.map(r, ReviewDTO.class);
            reviewDTO.setUserEmail(authenticationService.findUser(r.getUserId().getId()).getEmail());
            result.add(reviewDTO);
        }
        return result;
    }

        public void deleteReviewById(String imdbId, String userEmail) {
            Optional<Movie> movieOptional = movieRepository.findByImdbID(imdbId);
            if (movieOptional.isPresent()) {
                Optional<User> userOptional = userRepository.findByEmail(userEmail);
                Optional<Review> reviewOptional = reviewRepository.findByUserIdAndMovieId(userOptional.get(), movieOptional.get().getId());
                if (reviewOptional.isPresent()) {
                    Review review = reviewOptional.get();
                    reviewRepository.delete(review);
                    log.info("Deleted review with ID: " + reviewOptional.get().getId());
                } else {
                    log.error("Review with ID: " + reviewOptional.get().getId() + " does not exist.");
                }
            }
        }
}
