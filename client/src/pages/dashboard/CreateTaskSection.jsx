// CreateTaskSection.js
import React from "react";

// component for creating a task

function CreateTaskSection({
  handleTitle,
  handleDescription,
  handleCompleted,
  handleDueDate,
  createTaskHandle,
  title,
  description,
  completed,
  dueDate,
}) {
  return (
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
  );
}

export default CreateTaskSection;
