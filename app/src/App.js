import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import SuggestedMovies from "./SuggestedMovies";
import WatchedMovies from "./WatchedMovies";
import AddWatchedMovie from "./AddWatchedMovie";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/new-movies/:genre" element={<SuggestedMovies/>}/>
                <Route exact path="/old-movies" element={<WatchedMovies/>}/>
                <Route exact path="/old-movies/add" element={<AddWatchedMovie/>}/>
            </Routes>
        </Router>
    )
}

export default App;
