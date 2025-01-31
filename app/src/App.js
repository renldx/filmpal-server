import React from 'react';
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import SuggestedMovies from "./SuggestedMovies";
import WatchedMovies from "./WatchedMovies";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<Home/>}/>
                <Route exact path="/new-movies" element={<SuggestedMovies/>}/>
                <Route exact path="/old-movies" element={<WatchedMovies/>}/>
            </Routes>
        </Router>
    )
}

export default App;
