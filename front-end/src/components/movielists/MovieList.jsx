import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getFilteredMovies } from '../api/MovieApi';
import './MovieList.css'

const moviesPerPage = 20;

const MovieList = () => {

    const [filteredMovies, setFilteredMovies] = useState([]);
    const [selectedOption, setSelectedOption] = useState('topRated');
    const [currentPage, setCurrentPage] = useState(1);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const token = localStorage.getItem("token")
                const moviesData = await getFilteredMovies(selectedOption, token);
                setFilteredMovies(moviesData);
                setCurrentPage(1);
            } catch (error) {
                console.error('Error fetching movies:', error);
            }
        };

        fetchMovies();
    }, [selectedOption]);

    const selectChange = (event) => {
        setSelectedOption(event.target.value);
    };

    const indexOfLastMovie = currentPage * moviesPerPage;
    const indexOfFirstMovie = indexOfLastMovie - moviesPerPage;
    const currentMovies = filteredMovies.slice(indexOfFirstMovie, indexOfLastMovie);

    const nextPage = () => {
        setCurrentPage(prevPage => prevPage + 1);
    };

    const prevPage = () => {
        setCurrentPage(prevPage => prevPage - 1);
    };

    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    };

    return (
        <div className='container-movie-list'>
            <div className='movie-list-options'>
                <h1>Movies</h1>
                <div className='filterValues'>
                    <select value={selectedOption} onChange={selectChange} className='selectBox'>
                        <option value="topRated">Top Rated</option>
                        <option value="awarded">Awarded</option>
                        <option value="boxOffice">BoxOffice</option>
                    </select>
                </div>
            </div>
            <div className='pagination'>
                {filteredMovies.length > indexOfLastMovie && (
                    <button onClick={() => { nextPage(); scrollToTop(); }}>Next</button>
                )}
                {currentPage > 1 && (
                    <button onClick={() => { prevPage(); scrollToTop(); }}>Previous</button>
                )}
            </div>
            <ul className='movie-list'>
                {currentMovies.map(movie => (
                    <li key={movie.id || movie.imdbID}>
                        <div className="container-list">
                            <div className='poster-container-list'>
                                <Link to={`/movie/${movie.imdbID}`}>
                                    <img src={movie.Poster} alt={movie.Title} />
                                </Link>
                            </div>
                            <div className='details-container-list'>
                                <p><strong>Title:</strong> {movie.Title}</p>
                                <p><strong>Genre:</strong> {movie.Genre}</p>
                                <p><strong>Imdb Rating:</strong> {movie.imdbRating}</p>
                                <p><strong>Awards:</strong> {movie.Awards}</p>
                                {movie.BoxOffice && movie.BoxOffice !== "N/A" && (
                                    <p><strong>BoxOffice:</strong> {movie.BoxOffice}</p>
                                )}
                            </div>
                        </div>
                    </li>
                ))}
            </ul>
            <div className='pagination'>
                {filteredMovies.length > indexOfLastMovie && (
                    <button onClick={() => { nextPage(); scrollToTop(); }}>Next</button>
                )}
                {currentPage > 1 && (
                    <button onClick={() => { prevPage(); scrollToTop(); }}>Previous</button>
                )}
            </div>
        </div>
    );
}

export default MovieList;
