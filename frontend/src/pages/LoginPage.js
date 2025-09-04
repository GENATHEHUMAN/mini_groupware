"use client"

import { useState } from "react"
import axios from "axios"
import { useNavigate } from "react-router-dom" // 페이지 이동을 위한 Hook
import { useAuth } from "../context/AuthContext" // AuthContext Hook
import "./LoginPage_style.css"

function LoginPage() {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const navigate = useNavigate() // navigate 함수 사용 준비
  const { login } = useAuth() // AuthContext의 login 함수 사용 준비

  const handleLogin = async (e) => {
    e.preventDefault()
    const params = new URLSearchParams()
    params.append("username", username)
    params.append("password", password)

    try {
      const response = await axios.post("/api/users/login", params, {
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      })

      console.log("로그인 성공:", response.data)
      alert("로그인에 성공했습니다!")

      // Context에 사용자 정보 저장
      login({ username: username })

      // '/dashboard' 경로로 페이지 이동
      navigate("/dashboard")
    } catch (error) {
      console.error("로그인 실패:", error)
      alert("아이디 또는 비밀번호가 일치하지 않습니다.")
    }
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <h2 className="login-title">로그인</h2>
        <form onSubmit={handleLogin} className="login-form">
          <div className="form-group">
            <input
              type="text"
              placeholder="아이디"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="form-input"
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
            />
          </div>
          <button type="submit" className="login-button">
            로그인
          </button>
        </form>
      </div>
    </div>
  )
}

export default LoginPage
