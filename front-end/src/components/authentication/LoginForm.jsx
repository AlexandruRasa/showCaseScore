import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { login } from '../api/AuthenticationApi';
import "./LoginForm.css";

const LoginForm = ({ setIsLoggedIn }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const userData = await login(email, password);
            if (userData.statusCode === 200) {
                localStorage.setItem("isLoggedIn", "true")
                localStorage.setItem("userData", JSON.stringify(userData.user));
                localStorage.setItem("token", userData.token)
                setIsLoggedIn(localStorage.getItem('isLoggedIn') === 'true');
                navigate('/');
            } else {
                setEmail('');
                setPassword('');
                setError('Invalid email or password');
            }
        } catch (error) {
            console.log(error);
            setError('Error logging in. Please try again later.');
        }
    };

    return (
        <div className="auth-container">
            <h2>Login</h2>
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Email: </label>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </div>
                <div className="form-group">
                    <label>Password: </label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <div className="button-container">
                    <button type="submit" className="login-button">Login</button>
                </div>
                <div className="register-button-container">
                    <Link className="register-link" to="/register">
                        Don't have an account? Sign up
                    </Link>
                </div>
            </form>
        </div>
    );
};

export default LoginForm;
