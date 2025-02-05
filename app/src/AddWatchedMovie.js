import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Container, Form, FormGroup, Input, Label } from "reactstrap";

const AddWatchedMovie = () => {
    const [formData, setFormData] = useState({
        title: "",
        release: "",
        errors: {},
    });

    let navigate = useNavigate();

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData((prevState) => ({ ...prevState, [name]: value }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        if (validateForm()) {
            setFormData({
                ...formData,
            });

            addMovie();
        }
    };

    const validateForm = () => {
        const errors = {};

        if (!formData.title) {
            errors.title = "Title is required";
        }

        if (!formData.release) {
            errors.release = "Release date is required";
        }

        setFormData((prevState) => ({ ...prevState, errors }));

        return Object.keys(errors).length === 0;
    };

    const addMovie = () => {
        (async () => {
            const rawResponse = await fetch("/api/watched/movie", {
                method: "POST",
                headers: {
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: formData.title,
                    release: formData.release,
                }),
            });

            navigate("/old-movies");
        })();
    };

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="title">
                        Title
                        {formData.errors.title && (
                            <span
                                style={{ color: "red", marginLeft: "0.5rem" }}>
                                {formData.errors.title}
                            </span>
                        )}
                    </Label>
                    <Input onChange={handleChange} id="title" name="title" />
                    <Label for="release">
                        Release
                        {formData.errors.release && (
                            <span
                                style={{ color: "red", marginLeft: "0.5rem" }}>
                                {formData.errors.release}
                            </span>
                        )}
                    </Label>
                    <Input
                        onChange={handleChange}
                        id="release"
                        name="release"
                        type="date"
                    />
                </FormGroup>
                <Button type="submit">Submit</Button>
            </Form>
        </Container>
    );
};

export default AddWatchedMovie;
