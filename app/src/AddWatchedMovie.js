import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

const AddWatchedMovie = () => {

    const [formData, setFormData] = useState({
        title: "",
        release: ""
    });

    let navigate = useNavigate();

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData((prevState) => ({...prevState, [name]: value}));
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        setFormData({
            ...formData
        });

        addMovie();
    }

    const addMovie = () => {
        (async () => {
            const rawResponse = await fetch("/api/watched/movie", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"title": formData.title, "release": formData.release})
            });
        })();

        navigate("/old-movies");
    }

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="title">Title</Label>
                    <Input onChange={handleChange} id="title" name="title"/>
                    <Label for="release">Release</Label>
                    <Input onChange={handleChange} id="release" name="release" type="date"/>
                </FormGroup>
                <Button type="submit">Submit</Button>
            </Form>
        </Container>
    );
}

export default AddWatchedMovie;
