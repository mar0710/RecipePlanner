import React, { Component } from "react";
import {
    Navbar,
    NavbarToggler,
    Collapse,
    Nav,
    NavItem,
    NavLink
} from "reactstrap";
import { Link } from "react-router-dom";

class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = { isOpen: false };
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        return (
            <div className="AppNavbar">
                <Navbar expand="md" className="custom-navbar" fixed="top">
                    <img
                        src="/favicon.svg"
                        style={{ paddingRight: "20px" }}
                        height="30"
                        alt=""
                        loading="lazy"
                    />
    
                    <NavbarToggler onClick={this.toggle} />
    
                    <Collapse isOpen={this.state.isOpen} navbar>
                        <Nav className="me-auto" navbar>
                            <NavItem>
                                <NavLink tag={Link} to="/" className="nv-link">
                                    Home
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink tag={Link} to="/recipes" className="nv-link">
                                    Recipes
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink tag={Link} to="/recipes/upload" className="nv-link">
                                    Add recipe
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink tag={Link} to="/planner" className="nv-link">
                                    Planner
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink tag={Link} to="/shoppinglist" className="nv-link">
                                    ShoppingList
                                </NavLink>
                            </NavItem>
                            <NavItem>
                                <NavLink tag={Link} to="/profile" className="nv-link">
                                    Profile
                                </NavLink>
                            </NavItem>
                        </Nav>

                        <Nav className="ms-auto" navbar>
                            <NavItem>
                                <NavLink
                                    href="#"
                                    onClick={this.props.onLogout}
                                    className="nav-link"
                                >
                                    Logout
                                </NavLink>
                            </NavItem>
                        </Nav>
                    </Collapse>
                </Navbar>
            </div>
        );
    }
}

export default AppNavbar;
