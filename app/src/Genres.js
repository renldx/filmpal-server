import {useEffect, useState} from "react";
import {Button} from "reactstrap";

const Genres = () => {

    const [loading, setLoading] = useState(false);
    const [genres, setGenres] = useState([]);

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
                <Button value={g}>{g}</Button>
            )}
        </div>
    );

}

export default Genres;
