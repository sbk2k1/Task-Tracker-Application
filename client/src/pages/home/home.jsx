import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { isUser, removeData } from "../../api/index";
import "./home.css";

export default function Landing() {
  const [loggedIn, setLoggedIn] = useState(false);

  useEffect(() => {
    if (isUser()) {
      setLoggedIn(true);
    } else {
      setLoggedIn(false);
    }
  }, []);

  const LogoutHandler = () => {
    removeData();
    setLoggedIn(false);
    window.location.reload();
  };

  return (
    <div className="landing">
      <header className="header">
        <h1 style={{ marginLeft: 15 }}>TrackR</h1>
        <nav>
          {!loggedIn && (
            <Link to="/login">
              <button>Login</button>
            </Link>
          )}
          {loggedIn && (
            <div>
              <Link style={{ marginRight: 15 }}>
                <button onClick={LogoutHandler}>Logout</button>
              </Link>
              <Link to="/dashboard" style={{ marginRight: 15 }}>
                <button>Tasks</button>
              </Link>
            </div>
          )}
        </nav>
      </header>
      <section className="hero">
        <div className="hero-content">
          <h3>Track your Tasks!</h3>
        </div>
      </section>
    </div>
  );
}
