import React, { useState } from 'react';
import "./App.css";
import axios from "axios";
import PropTypes from "prop-types";
import {useNavigate} from "react-router-dom";

const SignUp = ({ onRegisterSuccess }) => {
    const [form, setForm] = useState({
        username: '',
        email: '',
        password: '',
        roles: ["ROLE_USER"]
    });

    const [message, setMessage] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const handleChange = e => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async e => {
        e.preventDefault();
        setError(null);
        setMessage(null);

        try {
            const response = await axios.post(
                "http://localhost:8080/api/auth/signUp",
                form
            );
            setMessage(response.data.message);
            onRegisterSuccess?.();
            navigate('/login');
        } catch (err) {setError(err.response?.data ?? { error: "Could not fetch user data", status: null });}
    };

    return (
        <div className={"wrapper"}>
            <div className={"title"}>
                Sign Up
            </div>
            <form onSubmit={handleSubmit}>
                <div className={"field"}>
                    <input
                        type="text"
                        name="username"
                        placeholder="Username"
                        value={form.username}
                        onChange={handleChange}
                    />
                </div>
                <div className={"field"}>
                    <input
                        type="text"
                        name="email"
                        placeholder="Email"
                        value={form.email}
                        onChange={handleChange}
                    />
                </div>
                <div className={"field"}>
                    <input
                        type="password"
                        name="password"
                        placeholder="Password"
                        value={form.password}
                        onChange={handleChange}
                    />
                </div>
                <div className="field">
                    <input type="submit" value="Sign Up"/>
                </div>
                {message && <p className="success-message">{message}</p>}
                {error && <p className="error-message">{error.error}</p>}
            </form>
        </div>
    );
};
SignUp.propTypes = {
    onRegisterSuccess: PropTypes.func.isRequired
};

export default SignUp;