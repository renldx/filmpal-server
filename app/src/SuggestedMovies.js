import {useEffect, useState} from 'react';
import SuggestedMovie from "./SuggestedMovie";

const SuggestedMovies = () => {

    const [loading, setLoading] = useState(false);
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        setLoading(true);

        fetch('api/suggested/ACTION')
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
