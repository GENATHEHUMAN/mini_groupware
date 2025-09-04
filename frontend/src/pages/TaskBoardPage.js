"use client"

import { useState, useEffect } from "react"
import axios from "axios"
import { useAuth } from "../context/AuthContext"
import "./TaskBoardPage_style.css"

function TaskBoardPage() {
  const { user } = useAuth()
  const [tasks, setTasks] = useState([])
  const [isLoading, setIsLoading] = useState(true)

  const fetchTasks = async () => {
    setIsLoading(true)
    try {
      const response = await axios.get("/api/tasks/my-tasks", { withCredentials: true })
      setTasks(response.data)
    } catch (error) {
      console.error("업무 목록을 불러오는데 실패했습니다.", error)
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    if (user) {
      fetchTasks()
    }
  }, [user])

  const handleStatusChange = async (taskId, newStatus) => {
    try {
      await axios.patch(`/api/tasks/${taskId}/status`, { status: newStatus }, { withCredentials: true })
      // 상태 변경 성공 시, 업무 목록을 다시 불러와 화면을 갱신합니다.
      fetchTasks()
    } catch (error) {
      console.error("업무 상태 변경에 실패했습니다.", error)
      alert("업무 상태 변경에 실패했습니다.")
    }
  }

  if (isLoading) return <p className="loading-message">로딩 중...</p>

  // 상태별로 업무 필터링
  const todoTasks = tasks.filter((task) => task.status === "TODO")
  const inProgressTasks = tasks.filter((task) => task.status === "IN_PROGRESS")
  const doneTasks = tasks.filter((task) => task.status === "DONE")

  return (
    <div className="task-board-container">
      <div className="board-header">
        <h1 className="board-title">업무 관리 보드</h1>
        <p className="board-subtitle">{user.username}님의 업무 목록입니다.</p>
      </div>
      <div className="kanban-board">
        {/* TODO 컬럼 */}
        <div className="kanban-column">
          <h3 className="column-title todo">할 일 (TODO)</h3>
          <div className="task-list">
            {todoTasks.map((task) => (
              <div key={task.taskId} className="task-card">
                <h4 className="task-title">{task.title}</h4>
                <select
                  value={task.status}
                  onChange={(e) => handleStatusChange(task.taskId, e.target.value)}
                  className="status-select"
                >
                  <option value="TODO">할 일</option>
                  <option value="IN_PROGRESS">진행 중</option>
                  <option value="DONE">완료</option>
                </select>
              </div>
            ))}
          </div>
        </div>
        {/* IN_PROGRESS 컬럼 */}
        <div className="kanban-column">
          <h3 className="column-title in-progress">진행 중 (IN_PROGRESS)</h3>
          <div className="task-list">
            {inProgressTasks.map((task) => (
              <div key={task.taskId} className="task-card">
                <h4 className="task-title">{task.title}</h4>
                <select
                  value={task.status}
                  onChange={(e) => handleStatusChange(task.taskId, e.target.value)}
                  className="status-select"
                >
                  <option value="TODO">할 일</option>
                  <option value="IN_PROGRESS">진행 중</option>
                  <option value="DONE">완료</option>
                </select>
              </div>
            ))}
          </div>
        </div>
        {/* DONE 컬럼 */}
        <div className="kanban-column">
          <h3 className="column-title done">완료 (DONE)</h3>
          <div className="task-list">
            {doneTasks.map((task) => (
              <div key={task.taskId} className="task-card">
                <h4 className="task-title">{task.title}</h4>
                <select
                  value={task.status}
                  onChange={(e) => handleStatusChange(task.taskId, e.target.value)}
                  className="status-select"
                >
                  <option value="TODO">할 일</option>
                  <option value="IN_PROGRESS">진행 중</option>
                  <option value="DONE">완료</option>
                </select>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default TaskBoardPage
