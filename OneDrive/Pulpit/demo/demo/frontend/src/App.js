import React, { Component } from "react";
import "./App.css";
import Home from "./Home";
import Login from "./Login";
import RecipeList from "./RecipeList";
import RecipeEdit from "./RecipeEdit";
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

class App extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
        };
    }

    handleLogin = () => {
        this.setState({ isLoggedIn: true });
    };

    render() {
        const { isLoggedIn } = this.state;

        return (
            <Router>
                <Routes>
                    {!isLoggedIn && (
                        <Route path="*" element={<Login onLogin={this.handleLogin} />} />
                    )}

                    {isLoggedIn && (
                        <>
                            <Route path="/" element={<Home />} />
                            <Route path="/recipes" element={<RecipeList />} />
                            <Route path="/recipes/:id" element={<RecipeEdit />} />
                            {/* Redirect any unknown route to Home */}
                            <Route path="*" element={<Navigate to="/" />} />
                        </>
                    )}
                </Routes>
            </Router>
        );
    }
}

export default App;
