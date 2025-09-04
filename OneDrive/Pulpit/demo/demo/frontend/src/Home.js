
import './App.css';
import { Button, Container } from 'reactstrap';
import React, { useEffect, useState } from "react";
import api from './api';

export default function Home() {
    const [message, setMessage] = useState("");
    const[recipePhotos,setRecipePhotos] = useState(null);

    useEffect(() => {
        api
            .get("/api/recipes/homephotos")
            .then(res => setRecipePhotos(res.data))
            .catch(() => setMessage("Not authorized"));
    }, []);

    return (
        <div>
            <Container fluid>
                <h1>Welcome to Recipe Planner – Your Personal Meal Planner & Recipe Hub!</h1>

                <p>Planning your meals has never been easier!
                    Welcome to Recipe Planner, the ultimate app to help you organize, plan, and enjoy your meals.
                    Whether you’re a busy professional, a home cook, or someone looking to eat healthier, our app makes
                    meal planning simple, fun, and stress-free.</p>
                <h> {message}</h>
                <div className="recipe-list">
                    {recipePhotos?.map((recipePhoto, index) =>
                            recipePhoto && (
                                <img
                                    key={index}
                                    src={recipePhoto}
                                    alt={recipePhoto}
                                    width="300"
                                />
                            )
                    )}
                </div>
            </Container>
        </div>
    );
}
