import React, {useEffect, useState} from 'react';
import {Table} from 'reactstrap';

const WatchedMovies = () => {

    const [loading, setLoading] = useState(false);
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        setLoading(true);

        fetch('api/watched/movies')
            .then(response => response.json())
            .then(data => {
                setMovies(data);
                setLoading(false);
            })
    }, []);

    const remove = async (code) => {
        await fetch(`#`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedMovies = [...movies].filter(i => i.code !== code);
            setMovies(updatedMovies);
        });
    }

    if (loading) {
        return <p>Loading...</p>;
    }

    const movieList = movies.map(movie => {
        return <tr key={movie.code}>
            <td style={{whiteSpace: 'nowrap'}}>{movie.title}</td>
            <td>{movie.release}</td>
            <td>{movie.code}</td>
        </tr>
    });

    return (
        <Table>
            <thead>
            <tr>
                <th>Title</th>
                <th>Release</th>
                <th>Code</th>
            </tr>
            </thead>
            <tbody>
            {movieList}
            </tbody>
        </Table>
    );

};

export default WatchedMovies;
