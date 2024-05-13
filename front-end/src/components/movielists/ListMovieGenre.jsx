import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { getFilteredMoviesByGenre } from '../api/MovieApi';
import './MovieList.css'

const moviesPerPage = 20;

const ListMovieGenre = () => {

    const [filteredMoviesByGenre, setFilteredMoviesByGenre] = useState([]);
    const [anotherSelectedOption, setAnotherSelectedOption] = useState('topRated');
    const [currentPage, setCurrentPage] = useState(1);
    const userData = useSelector(state => state.auth.userData);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const token = localStorage.getItem("token")
                const moviesData = await getFilteredMoviesByGenre(anotherSelectedOption, token);
                setFilteredMoviesByGenre(moviesData);
                setCurrentPage(1);
            } catch (error) {
                console.error('Error fetching movies:', error);
            }
        };

        fetchMovies();
    }, [anotherSelectedOption]);

    const selectChange = (event) => {
        setAnotherSelectedOption(event.target.value);
    };

    const indexOfLastMovie = currentPage * moviesPerPage;
    const indexOfFirstMovie = indexOfLastMovie - moviesPerPage;
    const currentMovies = filteredMoviesByGenre.slice(indexOfFirstMovie, indexOfLastMovie);

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
                    <select value={anotherSelectedOption} onChange={selectChange} className='selectBox'>
                        <option value="Action">Action</option>
                        <option value="Adventure">Adventure</option>
                        <option value="Animation">Animation</option>
                        <option value="Comedy">Comedy</option>
                        <option value="Crime">Crime</option>
                        <option value="Drama">Drama</option>
                        <option value="Fantasy">Fantasy</option>
                        <option value="Horror">Horror</option>
                        <option value="Sci-Fi">Sci-Fi</option>
                        <option value="Thriller">Thriller</option>
                    </select>
                </div>
            </div>
            <div className='pagination'>
                {filteredMoviesByGenre.length > indexOfLastMovie && (
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
                                <Link to={`/movie/${movie.imdbID}`} >
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
                {filteredMoviesByGenre.length > indexOfLastMovie && (
                    <button onClick={() => { nextPage(); scrollToTop(); }}>Next</button>
                )}
                {currentPage > 1 && (
                    <button onClick={() => { prevPage(); scrollToTop(); }}>Previous</button>
                )}
            </div>
        </div>
    );
}

export default ListMovieGenre;