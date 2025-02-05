import React from "react";
import { Container } from "reactstrap";

import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

import Genres from "./Genres";

const Home = () => {
    return (
        <Container fluid>
            <h2>Genres</h2>
            <Genres />
        </Container>
    );
};

export default Home;
