"use client"

import { useState, useEffect } from "react"
import { useParams, useNavigate } from "react-router-dom"
import axios from "axios"
import { useAuth } from "../context/AuthContext"
import "./ApprovalDetailPage_style.css"

function ApprovalDetailPage() {
  const { docId } = useParams()
  const [doc, setDoc] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)
  const { user } = useAuth() // 현재 로그인한 사용자 정보 가져오기
  const navigate = useNavigate()

  useEffect(() => {
    const fetchDoc = async () => {
      try {
        const response = await axios.get(`/api/approvals/${docId}`, { withCredentials: true })
        setDoc(response.data)
      } catch (err) {
        setError("문서 정보를 불러오는데 실패했습니다.")
        console.error(err)
      } finally {
        setIsLoading(false)
      }
    }
    fetchDoc()
  }, [docId])

  // 상신 버튼 클릭 핸들러 (새로 추가)
  const handleSubmit = async () => {
    if (!window.confirm("정말 상신하시겠습니까?")) return
    try {
      await axios.patch(`/api/approvals/${docId}/submit`, {}, { withCredentials: true })
      alert("문서가 상신되었습니다.")
      navigate("/dashboard") // 처리 후 대시보드로 이동
    } catch (error) {
      console.error("상신 처리 중 오류 발생:", error)
      alert("상신 처리 중 오류가 발생했습니다.")
    }
  }

  // 승인 버튼 클릭 핸들러
  const handleApprove = async () => {
    if (!window.confirm("정말 승인하시겠습니까?")) return
    try {
      await axios.patch(`/api/approvals/${docId}/approve`, {}, { withCredentials: true })
      alert("결재가 승인되었습니다.")
      navigate("/dashboard") // 처리 후 대시보드로 이동
    } catch (error) {
      console.error("승인 처리 중 오류 발생:", error)
      alert("승인 처리 중 오류가 발생했습니다.")
    }
  }

  // 반려 버튼 클릭 핸들러
  const handleReject = async () => {
    if (!window.confirm("정말 반려하시겠습니까?")) return
    try {
      await axios.patch(`/api/approvals/${docId}/reject`, {}, { withCredentials: true })
      alert("결재가 반려되었습니다.")
      navigate("/dashboard") // 처리 후 대시보드로 이동
    } catch (error) {
      console.error("반려 처리 중 오류 발생:", error)
      alert("반려 처리 중 오류가 발생했습니다.")
    }
  }

  if (isLoading) return <p className="loading-message">로딩 중...</p>
  if (error) return <p className="error-message">{error}</p>
  if (!doc) return <p className="error-message">문서 정보가 없습니다.</p>

  // 버튼 표시를 위한 조건들
  const isDrafter = user && user.username === doc.drafterUsername
  const isApprover = user && user.username === doc.approverUsername
  const canSubmit = isDrafter && doc.status === "DRAFT"
  const canProcess = isApprover && doc.status === "PENDING"

  return (
    <div className="detail-container">
      <div className="detail-card">
        <h1 className="detail-title">결재 문서 상세보기</h1>
        <div className="document-info">
          <p className="info-item">
            <strong>문서 번호:</strong> {doc.docId}
          </p>
          <p className="info-item">
            <strong>제목:</strong> {doc.title}
          </p>
          <p className="info-item">
            <strong>기안자:</strong> {doc.drafterName}
          </p>
          <p className="info-item">
            <strong>결재자:</strong> {doc.approverName}
          </p>
          <p className="info-item">
            <strong>상태:</strong>{" "}
            <span className={`status-badge status-${doc.status.toLowerCase()}`}>{doc.status}</span>
          </p>
        </div>

        <div className="document-content">
          <p className="content-label">
            <strong>내용:</strong>
          </p>
          <div className="content-box">
            <p>{doc.content || "내용 없음"}</p>
          </div>
        </div>

        <div className="action-buttons">
          {/* 기안자에게만 보이는 상신 버튼 */}
          {canSubmit && (
            <button onClick={handleSubmit} className="btn btn-submit">
              상신하기
            </button>
          )}

          {/* 결재자에게만 보이는 승인/반려 버튼 */}
          {canProcess && (
            <div className="approval-buttons">
              <button onClick={handleApprove} className="btn btn-approve">
                승인
              </button>
              <button onClick={handleReject} className="btn btn-reject">
                반려
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default ApprovalDetailPage
