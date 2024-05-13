import axios from 'axios';

export async function fetchMovieData(imdbId, token) {
  console.log("movieapi - this is the token " + token)
  try {
    const response = await axios.get(`http://localhost:8080/movie/get/${imdbId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function getFilteredMovies(selectedOption, token) {
  try {
    let url;
    switch (selectedOption) {
      case 'topRated':
        url = 'http://localhost:8080/movie/rating';
        break;
      case 'awarded':
        url = 'http://localhost:8080/movie/awards';
        break;
      case 'boxOffice':
        url = 'http://localhost:8080/movie/boxOffice';
        break;
      default:
        url = 'http://localhost:8080/movie/rating';
        break;
    }
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function getFilteredMoviesByGenre(anotherSelectedOption, token) {
  try {
    let url;
    switch (anotherSelectedOption) {
      case 'Action':
        url = 'http://localhost:8080/movie/genre/action';
        break;
      case 'Adventure':
        url = 'http://localhost:8080/movie/genre/adventure';
        break;
      case 'Animation':
        url = 'http://localhost:8080/movie/genre/animation';
        break;  
      case 'Comedy':
        url = 'http://localhost:8080/movie/genre/comedy';
        break;  
      case 'Crime':
        url = 'http://localhost:8080/movie/genre/crime';
        break;
      case 'Drama':
        url = 'http://localhost:8080/movie/genre/drama';
        break;
      case 'Fantasy':
        url = 'http://localhost:8080/movie/genre/fantasy';
        break;
      case 'Horror':
        url = 'http://localhost:8080/movie/genre/horror';
        break;  
      case 'Sci-Fi':
        url = 'http://localhost:8080/movie/genre/sci-fi';
        break;  
      case 'Thriller':
        url = 'http://localhost:8080/movie/genre/thriller';
        break;         
      default:
        url = 'http://localhost:8080/movie/genre/action';
        break;
    }
    const response = await axios.get(url, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function getMovies(searchVal) {
  const url = `https://www.omdbapi.com/?s=${searchVal}&apikey=934f342f`;

  try {
    const response = await axios.get(url);
    const responseJson = response.data;

    if (responseJson.Search) {
      return responseJson.Search;
    } else {
      return [];
    }
  } catch (error) {
    console.error('Error fetching movies:', error);
    throw new Error('Error fetching movies. Please try again later.');
  }
}

export async function saveMovies(id, token) {
  console.log("movieapi - this is the token " + token);
  try {
    const response = await axios.post(
      `http://localhost:8080/movie/saveMovieList/${id}`,
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function saveReview(imdbId, userId, reviewData, token) {
  console.log("movieapi - this is the token " + token);
  try {
    const response = await axios.post(
      `http://localhost:8080/review/saveReview/${imdbId}/${userId}`, reviewData,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}

export async function deleteReview(imdbId, userEmail, token) {
  try {
    const response = await axios.delete(`http://localhost:8080/review/deleteReview/${imdbId}/${userEmail}`, {
      headers: { Authorization: `Bearer ${token}` },
    });
    return response.data;
  } catch (error) {
    console.error('Error login data:', error);
    throw new Error('Error fetching data. Please try again later.');
  }
}
