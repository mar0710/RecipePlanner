import React, { useEffect, useState } from 'react';
import api from './axiosConfig';

const AddToPlannerButton = ({ id }) => {
    const [showForm, setShowForm] = useState(false);
    const [selectedDay, setSelectedDay] = useState('');
    const [mealType, setMealType] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const mealTypes = ['Breakfast', 'Lunch', 'Dinner', 'Supper'];

    const handleAdd = () => {
        if (!selectedDay || !mealType) {
            setError('Please select both day and meal type.');
            return;
        }

        api.post(`/api/recipes/${id}/planner/${selectedDay}/${mealType}`)
            .then(() => {
                setSuccess('Added to planner!');
                setError(null);
                setShowForm(false);
            })
            .catch(() => {
                setError('Failed to add to planner.');
                setSuccess(null);
            });
    };

    return (
        <div style={{ marginBottom: '1rem' }}>
            <button className="add-to-planner-button" onClick={() => setShowForm(!showForm)}>
                {showForm ? 'Cancel' : 'Add to Planner'}
            </button>

            {showForm && (
                <div style={{ marginTop: '0.5rem' }}>
                    <select className="add-to-planner-button" value={selectedDay} onChange={(e) => setSelectedDay(e.target.value)}>
                        <option value="">Select Day</option>
                        {daysOfWeek.map(day => (
                            <option key={day} value={day}>{day}</option>
                        ))}
                    </select>

                    <select className="add-to-planner-button" value={mealType} onChange={(e) => setMealType(e.target.value)} style={{ marginLeft: '0.5rem' }}>
                        <option value="">Select Meal</option>
                        {mealTypes.map(type => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>

                    <button className="add-to-planner-button" onClick={handleAdd} style={{ marginLeft: '0.5rem' }}>
                        Add
                    </button>
                </div>
            )}

            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
        </div>
    );
};
export default AddToPlannerButton;
