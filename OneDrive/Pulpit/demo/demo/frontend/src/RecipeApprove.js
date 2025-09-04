
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import api from "./axiosConfig";
import { Button} from 'reactstrap';
import { jwtDecode } from "jwt-decode";

const token = localStorage.getItem("token");

let isAdmin = false;

if (token) {
    const decoded = jwtDecode(token);
    let roles = decoded.roles;
    if (!Array.isArray(roles)) {
        roles = [roles];
    }
    isAdmin = roles.includes("ROLE_ADMIN");
}
const RecipeApprove = () => {
    const [recipes, setRecipes] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        api
            .get("/api/recipes/approve")
            .then((response) => setRecipes(response.data))
            .catch((err) => setError('Could not fetch recipes.'));

    }, []);

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1>Recipes to review and approve</h1>
            <div className="recipe-list">
                {recipes.map((recipe) => (
                    <div key={recipe.id} className="recipe-card">
                        <Link to={`/recipes/approve/${recipe.id}`} className="recipe-button">
                            <div className="recipe-name">
                                <h2>{recipe.name}</h2>
                            </div>
                            <div className="recipe-description">
                                <p>{recipe.description}</p>
                            </div>
                            <div className="recipe-button-img">
                                {recipe.imgName && (
                                    <img src={recipe.imgName} alt={recipe.imgName} width="300"/>
                                )}
                            </div>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
};


export default RecipeApprove;