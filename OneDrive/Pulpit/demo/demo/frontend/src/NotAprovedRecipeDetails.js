import React, { useEffect, useState } from 'react';
import {Link, useParams} from 'react-router-dom';
import axios from 'axios';
import api from "./axiosConfig";
import {Button} from "reactstrap";

const NotApprovedRecipeDetail = () => {
    const { id } = useParams();
    const [recipe, setRecipe] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        api
            .get(`/api/recipes/approve/${id}`)
            .then((response) => setRecipe(response.data))
            .catch((err) => setError('Recipe not found.'));
    }, [id]);
    const handleApprove = (id) => {
        api.put(`/api/recipes/approve/${id}`)
            .then(() => alert("Approved!"))
            .catch(() => alert("\"approve\" action failed."));
    };
    const handleDelete = (id) => {
        api.delete(`/api/recipes/approve/${id}`)
            .then((res) => alert("Deleted!"))
            .catch(() => alert("Delete failed."));
    };
    if (error) {
        return <div>{error}</div>;
    }

    if (!recipe) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>{recipe.name} {recipe.rating}</h1>
            {recipe.imgName && (
                <img src={recipe.imgName} alt={recipe.name} width="400"/>
            )}
            <p>{recipe.description}</p>
            <h2>Ingredients</h2>
            {recipe.ingredientDtos?.map((ingredientDto) => (
                <div key={ingredientDto.product} className="ingredient">
                    <p>{ingredientDto.amount} {ingredientDto.product}</p>
                </div>
            ))}
            <Button color="secondary" onClick={() => handleApprove(id)}>
                approve
            </Button>
            <Button color="secondary" onClick={() => handleDelete(id)}>
            delete
            </Button>


        </div>
    );
};

export default NotApprovedRecipeDetail;