import 'bootstrap/dist/css/bootstrap.min.css';
import {useEffect, useState} from 'react';
import {useNavigate, useParams} from "react-router-dom";
import {Button, CardGroup, Container, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";
import SuggestedMovie from "./SuggestedMovie";

const SuggestedMovies = () => {

    const {genre} = useParams();

    const [loading, setLoading] = useState(false);
    const [movies, setMovies] = useState([]);

    const [modal, setModal] = useState(false);
    const [movie, setMovie] = useState(undefined);

    const toggleModal = (event, movie) => {
        setMovie(movie);
        setModal(!modal);
    }

    let navigate = useNavigate();

    const selectMovie = () => {
        (async () => {
            const rawResponse = await fetch(`/api/watched/movie`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({"title": movie.title, "release": movie.release})
            });
        })();

        toggleModal();
        navigate("/");
    }

    const releaseYear = (date) => new Date(date).getFullYear();

    useEffect(() => {
        setLoading(true);

        fetch(`/api/suggested/${genre}`)
            .then(response => response.json())
            .then(data => {
                setMovies(data);
                setLoading(false);
            })
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <Container>
            <CardGroup>
                {movies.map(m =>
                    <SuggestedMovie key={m.code} movie={m} releaseYear={releaseYear} toggleModal={toggleModal}/>
                )}
            </CardGroup>

            <Modal isOpen={modal} toggle={toggleModal}>
                <ModalHeader toggle={toggleModal}>Confirm Selected Movie</ModalHeader>
                <ModalBody>
                    Are you sure you want to watch {movie?.title} ({releaseYear(movie?.release)})?
                </ModalBody>
                <ModalFooter>
                    <Button color="primary" onClick={selectMovie}>
                        Yes
                    </Button>{' '}
                    <Button color="secondary" onClick={toggleModal}>
                        No
                    </Button>
                </ModalFooter>
            </Modal>
        </Container>
    );

}

export default SuggestedMovies;
