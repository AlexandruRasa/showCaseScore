package com.showcaseScore.movieApp.controller;

import com.showcaseScore.movieApp.dtos.ReviewDTO;
import com.showcaseScore.movieApp.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private final ReviewService reviewService;

    @GetMapping("/getReview/{id}")
    public ReviewDTO getReview(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping("/saveReview/{imdbId}/{userId}")
    public ResponseEntity<Void> saveReview(
            @PathVariable String imdbId,
            @PathVariable Long userId,
            @RequestBody ReviewDTO reviewDTO
    ) {
        reviewService.saveReviewForMovie(imdbId, userId, reviewDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteReview/{imdbId}/{userEmail}")
    public void deleteReview(
            @PathVariable String imdbId,
            @PathVariable String userEmail
            ) {
        reviewService.deleteReviewById(imdbId, userEmail);
    }
}
