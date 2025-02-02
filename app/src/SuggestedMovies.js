import 'bootstrap/dist/css/bootstrap.min.css';
import {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {CardGroup, Col, Container, Row} from "reactstrap";
import SuggestedMovie from "./SuggestedMovie";

const SuggestedMovies = () => {

    const { genre } = useParams();
    const [loading, setLoading] = useState(false);
    const [movies, setMovies] = useState([]);

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
                    <SuggestedMovie key={m.code} movie={m}/>
                )}
            </CardGroup>
        </Container>
    );

}

export default SuggestedMovies;
