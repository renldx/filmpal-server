import React, {useEffect, useState} from 'react';
import logo from './logo.svg';
import './App.css';

const App = () => {

    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);

        fetch('api/watched/movies')
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
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <div className="App-intro">
                    <h2>Movie List</h2>
                    {movies.map(movie =>
                        <div key={movie.id}>
                            {movie.title}
                        </div>
                    )}
                </div>
            </header>
        </div>
    );
}

export default App;
