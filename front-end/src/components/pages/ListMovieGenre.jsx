import React from 'react';
import { getFilteredMoviesByGenre } from '../api/MovieApi';
import MovieGrid from './MovieGrid';

const ListMovieGenre = () => {
    const filterOptions = [
        { value: 'Action', label: 'Action' },
        { value: 'Adventure', label: 'Adventure' },
        { value: 'Animation', label: 'Animation' },
        { value: 'Comedy', label: 'Comedy' },
        { value: 'Crime', label: 'Crime' },
        { value: 'Drama', label: 'Drama' },
        { value: 'Fantasy', label: 'Fantasy' },
        { value: 'Horror', label: 'Horror' },
        { value: 'Sci-Fi', label: 'Sci-Fi' },
        { value: 'Thriller', label: 'Thriller' }
    ];

    return <MovieGrid title="Movies by Genre" fetchMovies={getFilteredMoviesByGenre} filterOptions={filterOptions} />;
};

export default ListMovieGenre;
