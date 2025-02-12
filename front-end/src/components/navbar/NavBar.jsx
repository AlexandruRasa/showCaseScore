import React from "react";
import { Link, useLocation } from "react-router-dom";
import SearchBar from './SearchBar';
import './NavBar.css'; 

const NavBar = ({ setSearchVal, onLogout }) => {

    const location = useLocation();

    const resetSearchVal = () => {
        setSearchVal("");
    };

    return (
        <div className="navbar">
            <Link className="navbar-link" to="/" onClick={resetSearchVal}>
                <img src="/apple-touch-icon.png" alt="Movies Icon" />
            </Link>
            {!location.pathname.startsWith("/user") && (
            <>
                <div>
                    <Link to="/movieList" >
                        <button>Top rated</button>
                    </Link>
                    <Link to="/listMovieGenre" >
                        <button>By Genre</button>
                    </Link>
                </div>
                {location.pathname === '/' && <SearchBar  setSearchVal={setSearchVal} />}
                {location.pathname !== '/' && (
                    <Link to="/" onClick={resetSearchVal}>
                        <button className="button">Search</button>
                    </Link>
                )}
                <div>
                    <Link to="/user/profile" className="search-button-link">
                        <button className="button">Account</button>
                    </Link>
                    <button className="button" onClick={onLogout}>Logout</button>
                </div>
            </>
)}
        </div>
    );
};

export default NavBar;
