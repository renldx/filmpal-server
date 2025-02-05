import React from "react";
import { Nav, NavItem, NavLink } from "reactstrap";

const NavigationMenu = () => {
    return (
        <Nav>
            <NavItem>
                <NavLink active href="/">
                    Home
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink href="/old-movies">Watched Movies</NavLink>
            </NavItem>
        </Nav>
    );
};

export default NavigationMenu;
