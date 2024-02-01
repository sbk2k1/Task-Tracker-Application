import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import loadable from "@loadable/component";
import "./App.css";

export default function App() {
  return (
    <Router>
      <div>
        <Switch>
          <Route
            exact
            path="/login"
            component={loadable(() => import("./pages/login/login"))}
          />
          <Route
            exact
            path="/register"
            component={loadable(() => import("./pages/register/register"))}
          />
          <Route
            exact
            path="/dashboard"
            component={loadable(() => import("./pages/dashboard/dashboard"))}
          />
          <Route
            exact
            path="/"
            component={loadable(() => import("./pages/home/home"))}
          />
          <Route component={loadable(() => import("./pages/404"))} />
        </Switch>
      </div>
    </Router>
  );
}
