import React from "react";
import ReactDOM from "react-dom";
import App from "./App.jsx";
import { NotificationProvider } from "./context/NotificationContext";

ReactDOM.render(
  <NotificationProvider>
    <App />
  </NotificationProvider>,
  document.getElementById("root"),
);
