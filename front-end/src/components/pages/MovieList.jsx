import React from 'react';
import { getFilteredMovies } from '../api/MovieApi';
import MovieGrid from './MovieGrid';

const MovieListPage = () => {
    const filterOptions = [
        { value: 'topRated', label: 'Top Rated' },
        { value: 'awarded', label: 'Awarded' },
        { value: 'boxOffice', label: 'Box Office' }
    ];

    return <MovieGrid title="Movies" fetchMovies={getFilteredMovies} filterOptions={filterOptions} />;
};

export default MovieListPage;
