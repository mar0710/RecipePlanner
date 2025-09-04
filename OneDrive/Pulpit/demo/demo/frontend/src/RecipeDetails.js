import React, { useEffect, useState } from 'react';
import {Link, useParams} from 'react-router-dom';
import api from "./axiosConfig";
import FavoriteButton from './FavoriteButton';
import AddToPlannerButton from './AddToPlannerButton'
import RatingButton from './RatingButton'
import DeleteCommentButton from './DeleteCommentButton'
import {jwtDecode} from "jwt-decode";

const RecipeDetail = () => {
    const { id } = useParams();
    const [recipe, setRecipe] = useState(null);
    const [error, setError] = useState(null);
    const [content, setContent] = useState("");
    const [isAdmin, setIsAdmin] = useState(false);
    const [showComments, setShowComments] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = jwtDecode(token);
                const roles = decoded.roles || [];
                setIsAdmin(roles.includes("ROLE_ADMIN"));
            } catch (err) {
                console.error("Invalid token", err);
            }
        }
    }, []);

    useEffect(() => {
        api
            .get(`/api/recipes/${id}`)
            .then((response) => setRecipe(response.data))
            .catch((err) => setError('Recipe not found.'));

    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        const formData = new FormData();
        formData.append('content', content);

        api.post(`/api/recipes/${id}/comment`, formData)
            .then((res) => {
                setContent("");
            })
            .catch(() => setError("Failed to post comment"));
    };
    const toggleComments = () => {
        setShowComments((prev) => !prev);
    };
    if (error) {
        return <div>{error}</div>;
    }

    if (!recipe) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <div className="recipe-details-top">
                <div className="recipe-details-name">
                    <p>{recipe.name}</p>
                </div>
                <div className="recipe-details-rating">
                    <p>{(recipe.rating).toFixed(2)}</p>
                    <img
                        src='/star.svg'
                        padding-right='10px'
                        height='46'
                        alt=''
                        loading='lazy'
                    />
                </div>

                <RatingButton id={recipe.id}/>
            </div>
            <div className="upload-date">
                <p>Added:{recipe.postTime}</p>
            </div>
            <div className="recipe-details-img">
                {recipe.imgName && (
                    <img src={recipe.imgName} alt={recipe.name} width="600"/>
                )}
            </div>
            <div className="recipe-details-functions">
                <FavoriteButton id={recipe.id}/>
                <AddToPlannerButton id={recipe.id}/>

            </div>
            <div className="recipe-details-description">
                <p>{recipe.description}</p>
            </div>
            <h2>Ingredients</h2>
            <div className="recipe-ingredients">
                {recipe.ingredientDtos?.map((ingredientDto) => (
                    <div key={ingredientDto.product} className="ingredient">
                        <p>{ingredientDto.amount} {ingredientDto.product}</p>
                    </div>
                ))}
            </div>


            <form className="add-comment-container" onSubmit={handleSubmit}>
                <div>
                    <input
                        className="add-comment-input-box"
                        type="text"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="add comment ..."
                    />
                </div>
                <button className="basic-button" type="submit">comment</button>
            </form>
            <button className="show-comments-button" onClick={toggleComments}>
                {showComments ? "Hide Comments" : "Show Comments"}
            </button>
            {showComments && (
                <div>
                    {recipe.commentDtos?.map((commentDto) => (
                        <div key={commentDto.id} className="comment-container">
                            <div className="comment-top">
                                    <p className="comment-author">{commentDto.userName}</p>
                                    <p className="comment-date">{commentDto.created_at}</p>
                            </div>
                            <p>{commentDto.content}</p>
                            {isAdmin && (
                                <DeleteCommentButton id={recipe.id} commentId={commentDto.id}/>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default RecipeDetail;