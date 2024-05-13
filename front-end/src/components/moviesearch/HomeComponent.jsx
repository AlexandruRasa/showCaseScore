import React, { useState, useEffect } from 'react';
import MovieSearchResult from './MovieSearchResult';
import { getMovies } from '../api/MovieApi';
import './HomeComponent.css';

const HomeComponent = ({ searchVal }) => {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const moviesData = await getMovies(searchVal);
        setMovies(moviesData);
      } catch (error) {
        console.error('Error fetching movies:', error);
      }
    };
    fetchMovies();
  }, [searchVal]);

  return (
    <div className='home'>
      <div>
        <MovieSearchResult movies={movies} />
      </div>
    </div>
  );
};

export default HomeComponent;
