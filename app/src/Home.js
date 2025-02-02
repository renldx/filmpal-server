import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {Container} from 'reactstrap';
import Genres from "./Genres";

const Home = () => {
    return (
        <Container fluid style={{marginTop: '1rem'}}>
            <Genres/>
        </Container>
    );
}

export default Home;
