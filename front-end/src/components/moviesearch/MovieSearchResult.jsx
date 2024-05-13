import React from 'react';
import { Link } from 'react-router-dom';
import './MovieSearchResult.css';


const MovieSearchResult = (props) => {

    return (
        <div className="movie-container">
            {props.movies.map((movie, index) =>
                movie.Poster !== 'N/A' && (
                    <div key={index} className="poster-container">
                        <Link to={`/movie/${movie.imdbID}`} >
                            <img src={movie.Poster} alt='movie' className='poster-img' />
                        </Link>
                        <div className="movie-title-wrapper">
                            <p>{movie.Title}</p>
                        </div>
                    </div>
                )
            )}
        </div>
    );
}

export default MovieSearchResult;
