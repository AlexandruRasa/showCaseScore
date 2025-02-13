import React, { useState, useEffect, useCallback } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import HomeComponent from './components/moviesearch/HomeComponent';
import NavBar from './components/navbar/NavBar';
import MoviePage from './components/moviepage/MoviePage';
import MovieList from './components/pages/MovieList';
import ListMovieGenre from './components/pages/ListMovieGenre';
import LoginForm from './components/authentication/LoginForm';
import RegistrationPage from './components/registration/RegistrationPage';
import ProfilePage from './components/profile/ProfilePage';
import UpdateUser from './components/profile/UpdateUser';
import { jwtDecode } from 'jwt-decode';

const App = () => {
  const navigate = useNavigate();
  const [searchVal, setSearchVal] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem('isLoggedIn') === 'true';
  });

  // Function to decode the token and get the expiration time
  const getTokenExpiration = (token) => {
    try {
      const decodedToken = jwtDecode(token);
      return decodedToken.exp * 1000;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  };

  // Memoized logout function
  const handleLogout = useCallback(() => {
    setIsLoggedIn(false);
    localStorage.setItem('isLoggedIn', 'false');
    localStorage.removeItem('userData');
    localStorage.removeItem('token');
    navigate('/');
  }, [navigate]);

  // Set auto logout based on token expiration
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return;

    const expirationTime = getTokenExpiration(token);
    if (!expirationTime) return;

    const timeUntilExpiration = expirationTime - Date.now();
    if (timeUntilExpiration > 0) {
      const timer = setTimeout(() => {
        handleLogout(); // Auto logout when the token expires
      }, timeUntilExpiration);

      return () => clearTimeout(timer);
    }
  }, [handleLogout]); 

  // Check isLoggedIn state on mount
  useEffect(() => {
    const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
    setIsLoggedIn(loggedIn);
  }, []);

  return (
    <div className="App">
      {isLoggedIn && <NavBar setSearchVal={setSearchVal} onLogout={handleLogout} />}
      <Routes>
        {!isLoggedIn ? (
          <>
            <Route exact path="/" element={<LoginForm setIsLoggedIn={setIsLoggedIn} />} />
            <Route exact path="/register" element={<RegistrationPage />} />
          </>
        ) : (
          <>
            <Route path="/movie/:imdbId" element={<MoviePage />} />
            <Route path="/" element={<HomeComponent searchVal={searchVal} setSearchVal={setSearchVal} />} />
            <Route path="/movieList" element={<MovieList />} />
            <Route path="/listMovieGenre" element={<ListMovieGenre />} />
            <Route path="/user/profile" element={<ProfilePage />} />
            <Route path="/user/update" element={<UpdateUser />} />
          </>
        )}
      </Routes>
    </div>
  );
};

export default App;
