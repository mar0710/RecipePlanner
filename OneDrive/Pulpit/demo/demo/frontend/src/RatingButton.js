import React, { useEffect, useState } from 'react';
import api from './axiosConfig';

const RatingButton = ({ id, initialRating }) => {
    const [isRated, setIsRated] = useState(-1.0);
    const [rating, setRating] = useState(initialRating || '');
    const [error, setError] = useState(null);

    useEffect(() => {
        api.get(`/api/recipes/${id}/is-rated`)
            .then((res) => setIsRated(res.data))
            .catch(() => setError("Could not check rating status"));
    }, [id]);

    const handleRatingChange = (e) => {
        setRating(e.target.value);
    };

    const rateRecipe = () => {
        const numericRating = parseFloat(rating);
        if (numericRating >= 1 && numericRating <= 5) {
            api.post(`/api/recipes/${id}/rate/${numericRating}`)
                .then(() => {
                    setIsRated(numericRating);
                    setError(null);
                })
                .catch(() => setError("Failed to rate"));


        } else {
            setError("Please enter a rating between 1 and 5.");
        }
    };

    return (
        <div className="rating-container ms-auto">
            {/*{isRated === -1.0 && <p>Please enter your rating:</p>}*/}

            <input
                className="rating-box"
                type="number"
                min="1"
                max="5"
                value={rating}
                onChange={handleRatingChange}
                placeholder="Rate 1–5"
            />

            <button className="rate-button" onClick={rateRecipe}>
                {isRated === -1.0 ? 'Rate' : `Change your rating (${isRated})`}
            </button>

            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default RatingButton;
