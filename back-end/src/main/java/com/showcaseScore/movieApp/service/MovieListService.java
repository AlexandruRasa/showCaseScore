package com.showcaseScore.movieApp.service;

import com.showcaseScore.movieApp.dtos.MovieListDTO;
import com.showcaseScore.movieApp.model.MovieList;
import com.showcaseScore.movieApp.model.mapper.ModelMapper;
import com.showcaseScore.movieApp.repository.MovieListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieListService {

    private final MovieListRepository movieListRepository;

    public MovieListDTO getMovieList(Integer id) {
        MovieList movieList = movieListRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return ModelMapper.map(movieList, MovieListDTO.class);
    }

    public void saveMovieList(MovieListDTO movieListDto) {
        MovieList movieList = ModelMapper.map(movieListDto, MovieList.class);
        movieListRepository.save(movieList);
    }
}
