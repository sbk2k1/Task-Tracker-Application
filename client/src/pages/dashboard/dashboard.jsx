import React, { useState, useEffect, useRef } from "react";
import {
  onGetData,
  isUser,
  onPostData,
  onDeleteData,
  onPutData,
  getUsername,
  removeData,
} from "../../api";
import { useNotifications } from "../../context/NotificationContext";
import "./dashboard.css";
import { over } from "stompjs";
import SockJS from "sockjs-client";

import TasksSection from "./TaskSection";
import CreateTaskSection from "./CreateTaskSection";

var stompClient = null;
const pageSize = 5;
export default function Dashboard() {
  const [tasks, setTasks] = useState([]);
  const [page, setPage] = useState("dashboard");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [completed, setCompleted] = useState(false);
  const [dueDate, setDueDate] = useState("");
  const [graphLoad, setGraphLoad] = useState(false);
  const [active, setActive] = useState({});

  // set page number as ref to persist value
  const pageNumber = useRef(0);

  // loader
  const [loading, setLoading] = useState(true);

  const { createNotification } = useNotifications();

  useEffect(() => {
    if (!isUser()) {
      createNotification("error", "Please Login First", "Error");
      removeData();
      // set href to /home
      window.location.href = "/login";
    }
    async function fetchData() {
      try {
        const res = await onGetData(`tasks?pageNumber=0&pageSize=${pageSize}`);
        if (res.data.status === "success") {
          setTasks(res.data.data);
          setGraphLoad((graphLoad) => !graphLoad);
          createNotification(
            "success",
            "Tasks fetched Successfully!",
            "Success",
          );
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

    // ------------------------- WEBSOCKET INIT-------------------------
    const sock = new SockJS("/ws");
    stompClient = over(sock);
    stompClient.debug = null;
    stompClient.connect({}, onConnected, onError);
  }, []);

  // ------------------------- WEBSOCKET -------------------------

  const onConnected = () => {
    stompClient.subscribe("changes", onTaskChanged);
  };

  const onError = (err) => {
    console.log(err);
  };

  const onTaskChanged = (msg) => {
    const data = JSON.parse(msg.body);
    if (data.task.username !== getUsername()) {
      return;
    } else {
      switch (data.changeType) {
        case "CREATED":
          // if task length is less than pageSize, add task to tasks
          setTasks((prevTasks) => {
            if (prevTasks.length < pageSize) {
              return [...prevTasks, data.task];
            }
            return prevTasks;
          });
          break;
        case "UPDATED":
          // if data.task.id is present in tasks, update it
          setTasks((prevTasks) => {
            const updatedTasks = prevTasks.map((task) =>
              task.id === data.task.id ? { ...task, ...data.task } : task,
            );
            return updatedTasks;
          });
          break;
        case "DELETED":
          // if data.task.id is present in tasks, delete it
          setTasks((prevTasks) => {

            // change active to null if active task is deleted
            if (active.id === data.task.id) {
              // remove active button class
              try {
                setActive({});
                document.getElementsByClassName("active-button connection-button")[0].classList.remove("active-button");
              } catch (err) {
                console.log(err);
              }
            }

            return prevTasks.filter((task) => task.id !== data.task.id);
          });
          break;
        default:
          break;
      }
    }
  };

  // ------------------------- PAGINATION -------------------------

  const getNextPage = async () => {
    try {
      const res = await onGetData(
        "tasks?pageNumber=" +
        (pageNumber.current + 1) +
        "&pageSize=" +
        pageSize,
      );
      if (res.data.status === "success") {
        setTasks(res.data.data);
        setGraphLoad((graphLoad) => !graphLoad);
        // createNotification("success", "Tasks fetched Successfully!", "Success");
        setActive(res.data.data[0]);

        // if page number is 0, enable previous button
        if (pageNumber.current === 0) {
          // enable previous button
          document
            .getElementsByClassName("pagination")[0]
            .childNodes[0].classList.remove("disabled");
        }

        // increment page number ref
        pageNumber.current = Number(pageNumber.current) + 1;

        // if tasks length is less than pageSize, disable next button
        if (res.data.data.length < pageSize) {
          // disable next button
          document
            .getElementsByClassName("pagination")[0]
            .childNodes[1].classList.add("disabled");
        }
      } else {
        // create a notification
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      // create a notification
      if (err.response.data.status === "not found") {
        // inform user that no connection is present
        createNotification("info", "No More Tasks in Workspace", "Information");
      } else {
        createNotification("error", err.data.message, "Error");
      }
    }
  };

  const getPreviousPage = async () => {
    try {
      const res = await onGetData(
        "tasks?pageNumber=" +
        (pageNumber.current - 1) +
        "&pageSize=" +
        pageSize,
      );
      if (res.data.status === "success") {
        setTasks(res.data.data);
        setGraphLoad((graphLoad) => !graphLoad);
        // createNotification("success", "Tasks fetched Successfully!", "Success");
        setActive(res.data.data[0]);

        // if page number is 1, disable previous button
        if (pageNumber.current === 1) {
          // disable previous button
          document
            .getElementsByClassName("pagination")[0]
            .childNodes[0].classList.add("disabled");
        }

        // decrement page number ref
        pageNumber.current = Number(pageNumber.current) - 1;

        // if tasks length is pageSize, enable next button
        if (res.data.data.length === pageSize) {
          // enable next button
          document
            .getElementsByClassName("pagination")[0]
            .childNodes[1].classList.remove("disabled");
        }
      } else {
        // create a notification
        createNotification("error", res.data.message, "Error");
      }
    } catch (err) {
      console.log(err);
    }
  };

  // ------------------------- HANDLERS -------------------------

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
  };

  const handleActiveCompleted = (e) => {
    e.preventDefault();
    setActive({ ...active, completed: e.target.value });
  };

  const handleActiveDueDate = (e) => {
    e.preventDefault();
    setActive({ ...active, dueDate: e.target.value });
  };

  // ------------------------- API CALLS -------------------------

  const handleEdit = async () => {
    try {
      const res = await onPutData("tasks", {
        id: active.id,
        title: active.title,
        description: active.description,
        completed: active.completed,
        dueDate: active.dueDate,
      });
      if (res.data.status === "success") {
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
        window.location.reload();
        setGraphLoad((graphLoad) => !graphLoad);
      } else {
        createNotification("error", err.data.message, "Error");
      }
    }
  };

  const handleDelete = async () => {
    try {
      const res = await onDeleteData("tasks/" + active.id);
      if (res.data.status === "deleted") {
        createNotification("success", "Task Deleted Successfully!", "Success");
        // remove task from tasks without reloading
        setTasks((prevTasks) => {
          // change active task to first task
          if (prevTasks.length === 1) {
            window.location.reload();
          } else {
            setActive({});
          }
          return prevTasks.filter((task) => task.id !== active.id);
        });
        try {
          document.getElementsByClassName("active-button connection-button")[0].classList.remove("active-button");
        }
        catch (err) {
          console.log(err);
        }
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
      const res = await onPostData("tasks", {
        title,
        description,
        completed,
        dueDate: dueDate + "T00:00:00.000+00:00",
      });
      if (res.data.status === "success") {
        createNotification("success", "Task Created Successfully!", "Success");

        // add task to tasks without reloading
        setTasks(
          (prevTasks) => {
            setActive(res.data.data);
            if (prevTasks.length !== 0) { return [...prevTasks, res.data.data]; }
            else { return [res.data.data]; }
          }
        );

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

  // ---- Function to handle task click ----------

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

  // ------------------------- RENDER -------------------------

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
        Create Task
      </button>

      {page === "dashboard" && (
        <>
          <TasksSection
            loading={loading}
            tasks={tasks}
            graphLoad={graphLoad}
            active={active}
            handleTask={handleTask}
            handleActiveTitle={handleActiveTitle}
            handleActiveDescription={handleActiveDescription}
            handleActiveCompleted={handleActiveCompleted}
            handleActiveDueDate={handleActiveDueDate}
            handleEdit={handleEdit}
            handleDelete={handleDelete}
          />
        </>
      )}

      {page === "dashboard" && (
        <div className="pagination">
          <button
            onClick={getPreviousPage}
            className={pageNumber.current === 0 ? "disabled" : ""}
          >
            {"<"}
          </button>
          <button onClick={getNextPage}>{">"}</button>
        </div>
      )}

      {page === "create" && (
        <CreateTaskSection
          handleTitle={handleTitle}
          handleDescription={handleDescription}
          handleCompleted={handleCompleted}
          handleDueDate={handleDueDate}
          createTaskHandle={createTaskHandle}
          title={title}
          description={description}
          completed={completed}
          dueDate={dueDate}
        />
      )}
    </div>
  );
}
