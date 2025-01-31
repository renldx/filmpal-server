import React from 'react';
import './App.css';
import {Container} from 'reactstrap';
import Genres from "./Genres";

const Home = () => {
    return (
        <div>
            <Container fluid>
                <Genres/>
            </Container>
        </div>
    );
}

export default Home;
