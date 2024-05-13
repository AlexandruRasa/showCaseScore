import React, { useState, useEffect } from 'react';
import { Routes, Route, useNavigate } from 'react-router-dom';
import HomeComponent from './components/moviesearch/HomeComponent';
import NavBar from './components/navbar/NavBar';
import MoviePage from './components/moviepage/MoviePage';
import MovieList from './components/movielists/MovieList';
import ListMovieGenre from './components/movielists/ListMovieGenre';
import LoginForm from './components/authentication/LoginForm';
import RegistrationPage from './components/registration/RegistrationPage';
import ProfilePage from './components/profile/ProfilePage';
import UpdateUser from './components/profile/UpdateUser';

const App = () => {
  const [searchVal, setSearchVal] = useState('');
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem('isLoggedIn') === 'true';
  });

  useEffect(() => {
    console.log(localStorage.getItem('isLoggedIn'))
    if (localStorage.getItem('isLoggedIn') === 'true') {
      setIsLoggedIn(true);
    }
  }, []);

  useEffect(() => {
    console.log(localStorage.getItem('isLoggedIn'))
    localStorage.setItem('isLoggedIn', isLoggedIn.toString());
  }, [isLoggedIn]);

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.setItem("isLoggedIn", "false")
    localStorage.removeItem("userData")
    localStorage.removeItem("token")
    navigate('/');
  };

  return (
    <div className="App">
      {isLoggedIn && (
        <>
          <NavBar setSearchVal={setSearchVal} onLogout={handleLogout} />
        </>
      )}
      <Routes>
      {!isLoggedIn && (
        <>
          <Route exact path="/" element={<LoginForm setIsLoggedIn={setIsLoggedIn} />} />
          <Route exact path="/register" element={<RegistrationPage />} />
        </>
        )}
      {isLoggedIn && (
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
