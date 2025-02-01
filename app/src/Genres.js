import {useEffect, useState} from "react";
import {Button} from "reactstrap";
import {useNavigate} from "react-router-dom";

const Genres = () => {

    const [loading, setLoading] = useState(false);
    const [genres, setGenres] = useState([]);

    const navigate = useNavigate();
    const pickGenre = (e) => {
        navigate(`/new-movies/${e.target.value}`)
    }

    useEffect(() => {
        setLoading(true);

        fetch('api/genres')
            .then(response => response.json())
            .then(data => {
                setGenres(data);
                setLoading(false);
            })
    }, []);

    if (loading) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            {genres.map(g =>
                <Button key={g} value={g} onClick={(e) => pickGenre(e)}>{g}</Button>
            )}
        </div>
    );

}

export default Genres;
