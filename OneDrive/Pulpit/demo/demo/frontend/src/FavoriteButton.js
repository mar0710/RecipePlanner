import React, { useEffect, useState } from 'react';
import api from './axiosConfig';

const FavoriteButton = ({ id }) => {
    const [isFavorited, setIsFavorited] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        api.get(`/api/recipes/${id}/is-favorited`)
            .then((res) => setIsFavorited(res.data))
            .catch(() => setError("Could not check favorite status"));
    }, [id]);

    const toggleFavorite = () => {
            if (isFavorited) {
                api.delete(`/api/recipes/${id}/unfavorite`)
                    .then(() => setIsFavorited(false))
                    .catch(() => console.log("Failed to unfavorite"));
            } else {
                api.post(`/api/recipes/${id}/favorite`)
                    .then(() => setIsFavorited(true))
                    .catch(() => console.log("Failed to favorite"));
            }
        };
    return (
        <div>
            <button className="favorite-button" onClick={toggleFavorite}>
                {isFavorited ? '💔 Unfavorite' : '❤️ Favorite'}
            </button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default FavoriteButton;
