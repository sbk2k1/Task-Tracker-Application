import React, { useState, useEffect, useRef } from "react";
import { Redirect } from "react-router-dom";
import {
  onGetData,
  isUser,
  onPostData,
  onDeleteData,
  removeData,
  onPutData,
} from "../../api";
import { useNotifications } from "../../context/NotificationContext";
import "./dashboard.css";
import { Line } from "react-chartjs-2";
import { Dna } from "react-loader-spinner";

export default function Dashboard(props) {
  const [logout, setLogout] = useState(false);
  const [redirect, setRedirect] = useState(false);
  const [tasks, setTasks] = useState([]);
  const [page, setPage] = useState("dashboard");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [completed, setCompleted] = useState(false);
  const [dueDate, setDueDate] = useState("");
  const [graphLoad, setGraphLoad] = useState(false);
  const [active, setActive] = useState({});

  // loader
  const [loading, setLoading] = useState(true);

  const { createNotification } = useNotifications();

  useEffect(() => {
    if (!isUser()) {
      createNotification("error", "Please Login First", "Error");
      setLogout(true);
    }
    async function fetchData() {
      try {
        const res = await onGetData("/tasks");
        if (res.data.status == "success") {
          setTasks(res.data.data);
          setGraphLoad((graphLoad) => !graphLoad);
          createNotification("success", "Tasks fetched Successfully!", "Success");
          setActive(res.data.data[0]);
        } else {
          // create a notification
          createNotification("error", res.data.message, "Error");
        }
      } catch (err) {
        // create a notification
        if (err.response.data.status === "not found") {
          // inform user that no connection is present
          createNotification("info", "No Tasks in Workspace", "Information");
          setTasks([]);
          setGraphLoad((graphLoad) => !graphLoad);
        } else {
          createNotification("error", err.data.message, "Error");
        }
      }
      setLoading(false);
    }

    fetchData();
  }, []);

  useEffect(() => {
    setActive({});
  }, [tasks]);

  if (logout) {
    return <Redirect to="/login" />;
  }

  if (redirect) {
    return <Redirect to={redirect} />;
  }

  const handleTitle = (e) => {
    e.preventDefault();
    setTitle(e.target.value);
  };

  const handleDescription = (e) => {
    e.preventDefault();
    setDescription(e.target.value);
  };

  const handleCompleted = (e) => {
    e.preventDefault();
    setCompleted(e.target.value);
  };

  const handleDueDate = (e) => {
    e.preventDefault();
    setDueDate(e.target.value);
  };

  const handleActiveTitle = (e) => {
    e.preventDefault();
    setActive({ ...active, title: e.target.value });
  };

  const handleActiveDescription = (e) => {
    e.preventDefault();
    setActive({ ...active, description: e.target.value });
  }

  const handleActiveCompleted = (e) => {
    e.preventDefault();
    setActive({ ...active, completed: e.target.value });
  }

  const handleActiveDueDate = (e) => {
    e.preventDefault();
    setActive({ ...active, dueDate: e.target.value });
  }

  const handleEdit = async (e) => {
    try {
      const res = await onPutData("/tasks", active);
      if (res.data.status == "success") {
        createNotification("success", "Task Updated Successfully!", "Success");
      } else {
        // create a notification
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      // create a notification
      if (err.response.data.status === "not found") {
        // inform user that no connection is present
        createNotification("info", "No Tasks in Workspace", "Information");
        setTasks([]);
        setGraphLoad((graphLoad) => !graphLoad);
      } else {
        createNotification("error", err.data.message, "Error");
      }
    }
  };
  const handleDelete = async (e) => {
    try {
      const res = await onDeleteData("/tasks/" + active.id);
      if (res.data.status == "deleted") {
        createNotification("success", "Task Deleted Successfully!", "Success");
        // remove task from tasks without reloading
        setTasks(tasks.filter((task) => task.id !== active.id));
      } else {
        // create a notification
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      // create a notification
      createNotification("info", err.message, "Information");
    }
  };

  const createTaskHandle = async (e) => {
    e.preventDefault();
    try {
      const res = await onPostData("/tasks", {
        title,
        description,
        completed,
        dueDate: dueDate + "T00:00:00.000+00:00",
      });
      if (res.data.status == "success") {
        createNotification("success", "Task Created Successfully!", "Success");

        // add task to tasks without reloading
        setTasks([...tasks, res.data.data]);
        // reset the form
        setTitle("");
        setDescription("");
        setCompleted(false);
        setDueDate("");
      } else {
        // create a notification
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      // create a notification
      createNotification("error", err.message, "Error");
    }
  };




  const handleSubmit = async (e) => { };

  const handleTask = async (e, task) => {
    setActive(task);
    if (document.getElementsByClassName("active-button connection-button")[0]) {
      document
        .getElementsByClassName("active-button connection-button")[0]
        .classList.remove("active-button");
    }

    // add active-button class to clicked button
    e.target.classList.add("active-button");
  };

  return (
    <div className="text-center">
      <h1>Tasks</h1>
      <button
        onClick={() => {
          setPage("dashboard");
        }}
        className={page === "dashboard" ? "active-button" : ""}
      >
        Dashboard
      </button>
      <button
        onClick={() => {
          setPage("create");
        }}
        className={page === "create" ? "active-button" : ""}
      >
        Create Connection
      </button>

      {page === "dashboard" && (
        <div className="choice">
          <h3>Choose a Task</h3>

          {loading && (
            <Dna
              visible={true}
              height="80"
              width="80"
              ariaLabel="dna-loading"
              wrapperStyle={{}}
              wrapperClass="dna-wrapper"
              className="connections"
            />
          )}

          {!loading && tasks.length == 0 && (
            <>
              <br />
              <p>No Connection in Workspace</p>
            </>
          )}

          {!loading && (
            <div className="connections">
              <div className="connection">
                {tasks &&
                  tasks.map((task, index) => (
                    <button
                      key={tasks.id}
                      onClick={(e) => {
                        handleTask(e, task);
                      }}
                      // make first button active by default
                      className={
                        index === 0
                          ? "active-button connection-button"
                          : "connection-button"
                      }
                    >
                      {task.title}
                    </button>
                  ))}
              </div>
              {(graphLoad || !graphLoad) && active.title && (<div className="details">
                <input
                  type="text"
                  placeholder="Title"
                  onChange={handleActiveTitle}
                  value={active.title}
                  required
                />
                <input
                  type="text"
                  placeholder="Description"
                  onChange={handleActiveDescription}
                  value={active.description}
                  required
                />
                <select
                  type="text"
                  placeholder="Completed"
                  onChange={handleActiveCompleted}
                  value={active.completed}
                  required
                >
                  <option value="true">True</option>
                  <option value="false">False</option>
                </select>
                <input
                  type="text"
                  placeholder="Due Date"
                  onChange={handleActiveDueDate}
                  value={
                    active.dueDate.toString().split("T")[0]
                  }
                  required
                />
                {/* two buttons to update and delete with external onclicks */}
                <button
                  onClick={handleEdit}
                >
                  Update Task
                </button>

                <button
                  onClick={handleDelete}
                >
                  Delete Task
                </button>



              </div>)}
            </div>
          )}
        </div>

      )}

      {page === "create" && (
        <div className="choice">
          <div className="create">
            <h3>Create a Task</h3>
            <input
              type="text"
              placeholder="Title"
              onChange={handleTitle}
              value={title}
              required
            />
            <input
              type="text"
              placeholder="Description"
              onChange={handleDescription}
              value={description}
              required
            />
            <select
              type="text"
              placeholder="Completed"
              onChange={handleCompleted}
              value={completed}
              required
            >
              <option value="true">True</option>
              <option value="false">False</option>
            </select>
            <input
              type="date"
              placeholder="Due Date"
              onChange={handleDueDate}
              value={dueDate}
              required
            />
            <button onClick={createTaskHandle}>Create Task</button>
          </div>
        </div>
      )}
    </div >
  );
}
