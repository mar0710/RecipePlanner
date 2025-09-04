import React, { useState } from 'react';
import "./App.css";
import { useNavigate, Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import PropTypes from 'prop-types';
export default function Login({ onLogin }){
    const [form, setForm] = useState({ username: '', password: '' });
    const navigate = useNavigate();
    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/api/auth/login', form);
            localStorage.setItem('token', res.data.token);
            onLogin();
            navigate("/");
        } catch {
            alert('Login failed');
        }
    };


    return (
        <div className={"body-login"}>
            <img src="/logo.svg" alt={"logo"} className={"login-logo"}/>
            <div className={"wrapper"}>

                <div className={"title"}>
                    Login
                </div>
                <form onSubmit={handleLogin}>
                    <div className={"field"}>
                        <input name="username" value={form.username} onChange={handleChange} placeholder="Username"/>
                    </div>
                    <div className={"field"}>
                        <input name="password" type="password" value={form.password} onChange={handleChange}
                               placeholder="Password"/>
                    </div>
                    <div className="content">
                        <div className="pass-link">
                            <a href="#">Forgot password?</a>
                        </div>
                    </div>
                    <div className="field">
                        <input type="submit" value="Login"/>
                    </div>
                    <div className="signup-link">
                        <a href="/signup">Signup now</a>
                    </div>
                </form>


            </div>

        </div>
    );
};
Login.propTypes = {
    onLogin: PropTypes.func.isRequired,
};
