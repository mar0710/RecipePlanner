import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from "./axiosConfig";
import {Button} from "reactstrap";

const Profile = () => {
    const [profile, setProfile] = useState(null);
    const [error, setError] = useState(null);
    const [recipes, setRecipes] = useState([]);

    useEffect(() => {
        api
            .get("/api/user")
            .then((response) => setProfile(response.data))
            .catch((err) => {
                if (err.response && err.response.data) {
                    setError(err.response.data);
                } else {
                    setError({error: "Could not fetch user data", status: null});
                }
            });
    }, []);
    const showMyRecipes = (e) => {

        api.get(`/api/recipes/myrecipes`)
            .then((res) => {
                console.log("My Recipes Response:", res.data);
                setRecipes(res.data);
            })
            .catch(() => setError("Failed to load my recipes"));
    };
    const showFavoriteRecipes = (e) => {

        api.get(`/api/recipes/favoriterecipes`)
            .then((res) => {
                console.log("My Favorite Recipes Response:", res.data);
                setRecipes(res.data);
            })
            .catch(() => setError("Failed to load my favorite recipes"));
    };

    if (error) {
        return <div>{error.error && (
                <div className="error-message">
                    {error.error} (status: {error.status})
                </div>
            )}</div>;
    }

    return (
        <div>
            <h1>Profile</h1>
            {profile ? (
                <>
                    <p>{profile.username}</p>
                    <p>{profile.email}</p>
                </>
            ) : (
                <p>Loading profile...</p>
            )}

            <div>
                <Button color="secondary" onClick={showMyRecipes}>
                    My Recipes
                </Button>
            </div>
            <div>
                <Button color="secondary" onClick={showFavoriteRecipes}>
                    Favorite Recipes
                </Button>
            </div>
            <div className="recipe-list">
                {recipes.map((recipe) => (
                    <div key={recipe.id} className="recipe-card">
                        <Link to={`/recipes/${recipe.id}`} className="recipe-button">
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

export default Profile;