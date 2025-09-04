"use client"

import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import axios from "axios"
import "./ApprovalCreatePage_style.css"

function ApprovalCreatePage() {
  const [users, setUsers] = useState([])
  const [title, setTitle] = useState("")
  const [content, setContent] = useState("")
  const [approverId, setApproverId] = useState("")
  const navigate = useNavigate()

  // 페이지 로드 시 전체 사용자 목록 불러오기
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get("/api/users", { withCredentials: true })
        setUsers(response.data)
      } catch (error) {
        console.error("사용자 목록을 불러오는데 실패했습니다.", error)
      }
    }
    fetchUsers()
  }, [])

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!approverId) {
      alert("결재자를 선택해주세요.")
      return
    }
    try {
      await axios.post("/api/approvals", { title, content, approverId: Number(approverId) }, { withCredentials: true })
      alert("결재 문서가 성공적으로 생성되었습니다.")
      navigate("/dashboard") // 성공 후 대시보드로 이동
    } catch (error) {
      console.error("결재 문서 생성에 실패했습니다.", error)
      alert("결재 문서 생성에 실패했습니다.")
    }
  }

  return (
    <div className="create-container">
      <div className="create-card">
        <h2 className="create-title">새 결재 문서 작성</h2>
        <form onSubmit={handleSubmit} className="create-form">
          <div className="form-group">
            <label className="form-label">결재자 선택: </label>
            <select value={approverId} onChange={(e) => setApproverId(e.target.value)} className="form-select">
              <option value="">-- 결재자를 선택하세요 --</option>
              {users.map((user) => (
                <option key={user.userId} value={user.userId}>
                  {user.name} ({user.username})
                </option>
              ))}
            </select>
          </div>
          <div className="form-group">
            <input
              type="text"
              placeholder="제목"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
              className="form-input"
            />
          </div>
          <div className="form-group">
            <textarea
              placeholder="내용"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              required
              rows="10"
              className="form-textarea"
            />
          </div>
          <button type="submit" className="submit-button">
            생성하기
          </button>
        </form>
      </div>
    </div>
  )
}

export default ApprovalCreatePage
