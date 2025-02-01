import {useEffect, useState} from 'react';
import SuggestedMovie from "./SuggestedMovie";
import {useParams} from "react-router-dom";

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
        <div>
            {movies.map(m =>
                <div key={m.code}>
                    <SuggestedMovie movie={{m}}/>
                </div>
            )}
        </div>
    );

}

export default SuggestedMovies;
