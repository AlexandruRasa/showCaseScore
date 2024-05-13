import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getAllUsers, deleteUser } from '../api/AuthenticationApi';
import { saveMovies } from '../api/MovieApi';
import "./ProfilePage.css";

const ProfilePage = () => {
    const [profileInfo, setProfileInfo] = useState({});
    const [usersList, setUsersList] = useState([]);
    const [pageRole, setPageRole] = useState([]);

    const fetchProfileInfo = async () => {
        try {
            const response = localStorage.getItem("userData");
            const userData = JSON.parse(response);
            setProfileInfo(userData);
            console.log(userData)
        } catch (error) {
            console.error('Error fetching profile information:', error);
        }
    };

    useEffect(() => {
        const response = localStorage.getItem("userData");
        const userData = JSON.parse(response);
        setPageRole(userData.role)
        console.log(pageRole)
        fetchProfileInfo();
    }, [pageRole]);

    const handleSaveMovies = async (id) => {
        try {
            const confirmDelete = window.confirm("This operation might take a while, do you want to continue ?");
            if (!confirmDelete) {
                return;
            }
            const token = localStorage.getItem("token");
            await saveMovies(id, token);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    useEffect(() => {
        const fetchAllUsers = async () => {
            try {
                const token = localStorage.getItem("token");
                console.log(token)
                const response = await getAllUsers(token);
                setUsersList(response.userList); 
                console.log(response.userList);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };
        fetchAllUsers();
    }, []);

    const handleDelete = async (userId) => {
        try {
            const token = localStorage.getItem("token");
            const confirmDelete = window.confirm("Are you sure you want to delete this user ?");
            if (!confirmDelete) {
                return;
            }
            await deleteUser(userId, token);
            const updatedUsersList = await getAllUsers(token);
            setUsersList(updatedUsersList.userList);
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };
    
    return (
        <div className="profile-page-container">
            <h2>Profile Information</h2>
            <p>First Name: {profileInfo.firstName}</p>
            <p>Last Name: {profileInfo.lastName}</p>
            <p>Email: {profileInfo.email}</p>
            <button style={{ fontSize: '14px'}} ><Link to={`/user/update`}>Update Profile</Link></button>
            <p></p>
            {pageRole === "ADMIN" && (
            <>
            <h2>Manage movies</h2>
            <button  style={{ fontSize: '14px'}} onClick={() => handleSaveMovies(1)}>Save top rated movies</button>
            <button style={{ fontSize: '14px'}} onClick={() => handleSaveMovies(2)}>Save most popular movies</button>
            <h2>Manage Users</h2>
            <div className="user-list-container">
                <ul>
                    {usersList.map((user, index) => (
                        <li key={index}>
                            <p className="user-info">
                                User ID: {user.id} Username: {user.username}
                                <button 
                                onClick={() => handleDelete(user.id)}
                                style={{ fontSize: '12px', float: 'right'  }} 
                                className="edit-button">Delete
                                </button>
                            </p>
                        </li>
                    ))}
                </ul>
                </div>
                </>
            )}
        </div>
    );
}

export default ProfilePage;