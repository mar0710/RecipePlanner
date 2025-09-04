import React, { useEffect, useState } from 'react';
import api from "./axiosConfig";
import {Button} from "reactstrap";

const DeleteCommentButton = ({id, commentId}) => {
    const [error, setError] = useState(null);
    const handleDelete = () => {
        api.delete(`/api/recipes/${id}/deletecomment/${commentId}`)
            .then((res) => alert("Deleted!"))
            .catch(() => alert("Delete failed."));
    };
    if (error) {
        return <div>{error}</div>;
    }
    return (
        <div>
            <Button className="basic-button" onClick={handleDelete}>
                delete
            </Button>
        </div>
    );
};
export default DeleteCommentButton;