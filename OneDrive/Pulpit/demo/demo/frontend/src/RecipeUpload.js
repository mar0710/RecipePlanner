import React, { useState } from 'react';
import axios from 'axios';
import api from "./axiosConfig.js";
const RecipeUpload = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [file, setFile] = useState(null);
    const [ingredients, setIngredients] = useState([{ amount: '', product: '' }]);
    const [message, setMessage] = useState('');

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleIngredientChange = (index, field, value) => {
        const newIngredients = [...ingredients];
        newIngredients[index] = { ...newIngredients[index], [field]: value };
        setIngredients(newIngredients);
    };

    const handleAddIngredient = () => {
        setIngredients([...ingredients, { amount: '', product: '' }]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!name || !description || !file) {
            setMessage('Please fill out all fields and select an image.');
            return;
        }

        const filteredIngredients = ingredients.filter(
            (ing) => ing.amount.trim() !== '' || ing.product.trim() !== ''
        );

        const formData = new FormData();
        formData.append('name', name);
        formData.append('description', description);
        formData.append('image', file);
        // Append ingredients as a JSON string
        formData.append('ingredients', JSON.stringify(filteredIngredients));

        try {
            const response = await api.post('http://localhost:8080/api/recipes/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setMessage(response.data || 'Recipe uploaded successfully!');
            setName('');
            setDescription('');
            setFile(null);
            setIngredients([{ amount: '', product: '' }]);
        } catch (error) {
            console.error(error);
            setMessage('Error uploading recipe.');
        }
    };

    return (
        <div className="upload-form">
            <h2 className="title-card">Upload a New Recipe</h2>
            <form onSubmit={handleSubmit}>
                <p>Recipe Name:</p>
                <input
                    className="name-input"
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Enter recipe name"
                />
                <div>
                    <p>Description:</p>
                    <textarea
                        className="desc-input"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter recipe description"
                    />
                </div>
                <div>
                    <p>Image:</p>
                    <input className="img-input" type="file" accept="image/*" onChange={handleFileChange} />
                </div>
                <div>
                    <p>Ingredients:</p>
                    {ingredients.map((ingredient, index) => (
                        <div key={index} style={{ display: 'flex', marginBottom: '8px' }}>
                            <input
                                className="ing-input"
                                type="text"
                                placeholder="Amount"
                                value={ingredient.amount}
                                onChange={(e) => handleIngredientChange(index, 'amount', e.target.value)}
                            />
                            <input
                                className="ing-input"
                                type="text"
                                placeholder="Product"
                                value={ingredient.product}
                                onChange={(e) => handleIngredientChange(index, 'product', e.target.value)}
                            />
                        </div>
                    ))}
                    <button className="basic-button" type="button" onClick={handleAddIngredient}>
                        +
                    </button>
                </div>
                <button className="upload-button" type="submit">Upload Recipe</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default RecipeUpload;