import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import api from "./axiosConfig";
import { Button} from 'reactstrap';
import { jwtDecode } from "jwt-decode";

const FavoriteRecipeList = () => {
    const [recipes, setRecipes] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        api
            .get("/api/recipes/favorite_recipes")
            .then((response) => setRecipes(response.data))
            .catch((err) => setError('Could not fetch recipes.'));

    }, []);


    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1>Favorite Recipes</h1>
            <div className="recipe-list">
                {recipes.map((recipe) => (
                    <div key={recipe.id} className="recipe-card">
                        <Link to={`/recipes/${recipe.id}`}>
                            <h2>{recipe.name}</h2>

                            <p>{recipe.description}</p>
                            {recipe.imgName && (
                                <img src={recipe.imgName} alt={recipe.imgName} width="200"/>
                            )}
                            <h2>{recipe.id}</h2>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default FavoriteRecipeList;