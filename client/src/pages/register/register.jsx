import React, { useState } from "react";
import { Link } from "react-router-dom";
import { onPostData } from "../../api";
import "./register.css";

// Import notification context
import { useNotifications } from "../../context/NotificationContext";

// Register component definition
export default function Register() {
  // State variables for username and password
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // Notification context
  const { createNotification } = useNotifications();

  // Handle registration form submission
  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      // Make a POST request to the server with user data
      const res = await onPostData("project/signup", {
        username,
        password,
      });

      // Check the response status and show appropriate notification
      if (res.data.status === "success") {
        createNotification("success", "User Account Created!", "Success");

        // Clear the form fields upon successful registration
        setUsername("");
        setPassword("");
      }
    } catch (err) {
      // Handle errors and display an error notification
      createNotification("error", "User already exists!", "Error");
    }
  };

  // Render the registration form
  return (
    <div className="register-container">
      <h1>Register</h1>
      <br />
      <form className="register-form" onSubmit={handleRegister}>
        <br />
        {/* Input field for username */}
        <input
          className="register-input"
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <br />
        {/* Input field for password */}
        <input
          className="register-input"
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <br />
        {/* Submit button */}
        <button className="register-button" type="submit">
          Register
        </button>
      </form>
      <br />
      {/* Link to the login page */}
      <Link to="/login">Login</Link>
    </div>
  );
}
