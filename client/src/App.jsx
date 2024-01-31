import React, { useState, useEffect } from "react";
import AddTask from "./components/AddTask";
import Header from "./components/Header";
import Tasks from "./components/Tasks";
import "./styles.css";

const getLocalStorage = () => {
  let tasks = localStorage.getItem("myTasks");
  if (tasks) {
    return JSON.parse(localStorage.getItem("myTasks"));
  } else {
    return [];
  }
};

export default function App() {
  const [showAddTask, setShowAddTask] = useState(false);
  const [tasks, setTasks] = useState(getLocalStorage());
  // const [tasks, setTasks] = useState([
  //   {
  //     id: 1,
  //     text: "Doctors Appointment",
  //     day: "Feb 5th at 2:30pm",
  //     reminder: true
  //   },
  //   {
  //     id: 2,
  //     text: "Meeting at School",
  //     day: "Feb 8th at 9:30am",
  //     reminder: true
  //   },
  //   {
  //     id: 3,
  //     text: "Grocery Shopping",
  //     day: "Feb 10th at 12:30pm",
  //     reminder: false
  //   }
  // ]);

  // Add Task
  const addTask = (task) => {
    const id = Math.floor(Math.random() * 10000 + 1);
    const newTask = {
      id: id,
      ...task
    };

    setTasks([...tasks, newTask]);
  };

  // Delete task
  const deleteTask = (id) => {
    setTasks(tasks.filter((task) => task.id !== id));
  };

  // Toggle reminder
  const toggleReminder = (id) => {
    setTasks(
      tasks.map((task) =>
        task.id === id
          ? {
              ...task,
              reminder: !task.reminder
            }
          : task
      )
    );
  };

  // Show Form
  const showTaskForm = () => {
    setShowAddTask(!showAddTask);
  };

  useEffect(() => {
    localStorage.setItem("myTasks", JSON.stringify(tasks));
  }, [tasks]);

  return (
    <div className="container">
      <Header onAdd={showTaskForm} title="Task Tracker" showAdd={showAddTask} />
      {showAddTask && <AddTask addTask={addTask} />}
      {tasks.length > 0 ? (
        <Tasks
          tasks={tasks}
          onDelete={deleteTask}
          toggleReminder={toggleReminder}
        />
      ) : (
        "No more tasks"
      )}
    </div>
  );
}
