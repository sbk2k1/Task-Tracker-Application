import React from "react";
import Task from "./Task";

const Tasks = ({ tasks, onDelete, toggleReminder }) => {
  return (
    <>
      {tasks.map((task) => {
        return (
          <Task
            key={task.id}
            task={task}
            handleDelete={onDelete}
            toggleReminder={toggleReminder}
          />
        );
      })}
    </>
  );
};

export default Tasks;
