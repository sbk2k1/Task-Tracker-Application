// TasksSection.js
import React from "react";
import { Dna } from "react-loader-spinner";

function TasksSection({
  loading,
  tasks,
  graphLoad,
  active,
  handleTask,
  handleActiveTitle,
  handleActiveDescription,
  handleActiveCompleted,
  handleActiveDueDate,
  handleEdit,
  handleDelete,
}) {
  return (
    <div className="choice">

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

      {!loading && tasks.length === 0 && (
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
                  key={task.id}
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
  );
}

export default TasksSection;
