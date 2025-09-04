import React, { useEffect, useState } from 'react';
import api from './axiosConfig';

const DeleteFromPlannerButton = ({ id}) => {
    const [error, setError] = useState(null);

    const toggleDeleteFromPlanner = () => {
            api.delete(`/api/planner/delete/${id}`)
                .then()
                .catch(() => console.log("Failed to delete"));

    };
    return (
        <div>
            <button onClick={toggleDeleteFromPlanner}>
                {'Delete'}
            </button>
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
};

export default DeleteFromPlannerButton;
