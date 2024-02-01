import React, { useState } from "react";
import { Link, Redirect } from "react-router-dom";
import { onPostData, setData } from "../../api";
import "./register.css";

// notification
import { useNotifications } from "../..../../../context/NotificationContext";

export default function Register() {
  // state variables
  const [name, setName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [redirect, setRedirect] = useState(false);

  // notification
  const { createNotification } = useNotifications();

  // handle register
  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const res = await onPostData("/project/signup", {
        username,
        password,
      });
      if (res.data.status == "success") {
        createNotification("success", "User Account Created!", "Success");
        //clear forms
        setUsername("");
        setPassword("");
      } else {
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      console.log(err);
      createNotification("error", err.message, "Error");
    }
  };

  // redirect to home if logged in
  if (redirect) {
    return <Redirect to="/" />;
  }

  return (
    <div className="register-container">
      <h1>Register</h1>
      <br />
      <form className="register-form" onSubmit={handleRegister}>
        <br />
        <input
          className="register-input"
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <br />
        <input
          className="register-input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <br />
        <button className="register-button" type="submit">
          Register
        </button>
      </form>
      <br />
      <Link to="/login">Login</Link>
    </div>
  );
}
