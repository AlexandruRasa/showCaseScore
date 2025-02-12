import React, { useState, useEffect, useCallback } from 'react';
import YouTube from 'react-youtube';
import { useParams } from 'react-router-dom';
import { deleteReview, fetchMovieData, saveReview } from '../api/MovieApi';
import "./MoviePage.css";

const MoviePage = () => {
  const { imdbId } = useParams();
  const [movieData, setMovieData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [pageRole, setPageRole] = useState([]);
  const [reviewFormData, setReviewFormData] = useState({
    note: '',
    text: '',
  });

  const fetchData = useCallback(async () => {
    try {
      const token = localStorage.getItem("token");
      const data = await fetchMovieData(imdbId, token);
      setMovieData(data);
      setLoading(false);
      const response = localStorage.getItem("userData");
      const userData = JSON.parse(response);
      setPageRole(userData.role);
    } catch (error) {
      setError(error.message);
      setLoading(false);
    }
  }, [imdbId]); 

  useEffect(() => {
    fetchData();
    window.scrollTo(0, 0);
  }, [fetchData]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setReviewFormData({ ...reviewFormData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const response = localStorage.getItem("userData");
      const userData = JSON.parse(response);
      await saveReview(imdbId, userData.id, reviewFormData, token);
      setReviewFormData({
        rating: '',
        text: '',
      });
      fetchData();
    } catch (error) {
      console.error('Error registering review:', error);
      alert('An error occurred while registering review');
    }
  };

  const handleDelete = async (userEmail) => {
    try {
      const confirmDelete = window.confirm("Are you sure you want to delete this review ?");
      if (!confirmDelete) {
        return;
      }
      const token = localStorage.getItem("token");
      await deleteReview(imdbId, userEmail, token);
      fetchData();
    } catch (error) {
      console.error('Error deleting review:', error);
    }
  };

  const isValidYouTubeVideoId = (videoId) => {
    return videoId && typeof videoId === 'string' && videoId.length === 11;
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <div>Loading...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <div>{error}</div>
      </div>
    );
  }

  return (
    <div>
      <div className="movie-page-container">
        <div className="movie-content">
          <div className="title-rating-container">
            <h1 className="movie-title">{movieData.Title}</h1>
            <h3>IMDb Rating: {movieData.imdbRating}</h3>
          </div>
          <h4>{movieData.Genre}</h4>
          <h5>{movieData.Year} | {movieData.Runtime}</h5>
          <div className="poster-youtube-container">
            <div className="poster-container">
              <img src={movieData.Poster} alt={movieData.Title} />
            </div>
            <div className="youtube-container">
              {movieData.Trailer && isValidYouTubeVideoId(movieData.Trailer) && (
                <YouTube videoId={movieData.Trailer} opts={{ playerVars: { autoplay: 1, mute: 1 } }} />
              )}
            </div>
          </div>
          <div className="movie-details-container">
            <p><strong>Plot:</strong> {movieData.Plot}</p>
            <p><strong>Director:</strong> {movieData.Director}</p>
            <p><strong>Actors:</strong> {movieData.Actors}</p>
            {movieData.Awards && movieData.Awards !== "N/A" && (
              <p><strong>Awards:</strong> {movieData.Awards}</p>
            )}
            <p><strong>Runtime:</strong>  {movieData.Runtime}</p>
            <p><strong>Release Date:</strong> {movieData.Released}</p>
            {movieData.BoxOffice && movieData.BoxOffice !== "N/A" && (
              <p><strong>BoxOffice:</strong> {movieData.BoxOffice}</p>
            )}
            {movieData.Ratings && movieData.Ratings.length > 0 && (
              <div>
                <strong>Ratings:</strong>
                <ul>
                  {movieData.Ratings.map((rating, index) => (
                    <li key={index}>
                      {rating.Source}: {rating.Value}
                    </li>
                  ))}
                </ul>
              </div>
            )}
            {movieData.Reviews && movieData.Reviews.length > 0 && (
              <div className='reviewsList'>
                <strong>Reviews:</strong>
                <ul>
                  {movieData.Reviews.map((review, index) => (
                    <li key={index}>
                      {review.rating}: {review.userEmail}: {review.text}
                      {pageRole === "ADMIN" && (
                        <button 
                          onClick={() => handleDelete(review.userEmail)}
                          style={{ fontSize: '12px', float: 'right' }} 
                          className="edit-button">
                          Delete
                        </button>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            )}
            <div>
              <strong>Add a Review:</strong>
              <form id="reviewForm" onSubmit={handleSubmit}>
                  <label htmlFor="rating">Rating:</label>
                  <input type="number" id="rating" name="rating" min="1" max="10" value={reviewFormData.rating} onChange={handleInputChange} required /><br />
                  <label htmlFor="reviewText">Review:</label><br />
                  <textarea id="reviewText" name="text" rows="5" cols="70" value={reviewFormData.text}  style={{ fontSize: '14px'}}  onChange={handleInputChange} required></textarea><br />
                  <button type="submit">Submit Review</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default MoviePage;
