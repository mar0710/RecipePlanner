import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from "./axiosConfig";
import DeleteFromPlannerButton from './DeleteFromPlannerButton';


const Planner = () => {
    const [recipes, setRecipes] = useState([]);
    const [error, setError] = useState(null);
    const [selectedDay, setSelectedDay] = useState('Monday');
    const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    useEffect(() => {
        api
            .get(`/api/planner/${selectedDay}`)
            .then((response) => setRecipes(response.data))
            .catch((err) => setError('Could not fetch recipes.'));

    }, []);
    const switchDay = (day) => {
            api.get(`/api/planner/${day}`)
                .then((response) => setRecipes(response.data))
                .catch((err) => setError('Could not fetch recipes.'));


    };

    if (error) {
        return <div>{error}</div>;
    }

    return (
        <div>
            <h1 className="title-card">Planner</h1>
            <div className="day-buttons">
                {days.map((day) => (
                    <button className="basic-button" key={day} onClick={() => switchDay(day)}>
                        {day}
                    </button>
                ))}
            </div>
            <div className="recipe-list">
                {recipes.map((recipe) => (
                    <div key={recipe.id} className="recipe-card">
                        <Link to={`/recipes/${recipe.recipeId}`} className="recipe-button">
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
                        <DeleteFromPlannerButton id={recipe.id}/>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Planner;