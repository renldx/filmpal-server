import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, ButtonGroup } from "reactstrap";

const Genres = () => {
    const [loading, setLoading] = useState(false);
    const [genres, setGenres] = useState([]);

    const navigate = useNavigate();
    const pickGenre = (event) => {
        navigate(`/new-movies/${event.target.value}`);
    };

    useEffect(() => {
        setLoading(true);

        fetch("api/genres")
            .then((response) => response.json())
            .then((data) => {
                setGenres(data);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <ButtonGroup vertical>
            {genres.map((genre) => (
                <Button
                    key={genre}
                    value={genre}
                    onClick={(event) => pickGenre(event)}>
                    {genre}
                </Button>
            ))}
        </ButtonGroup>
    );
};

export default Genres;
