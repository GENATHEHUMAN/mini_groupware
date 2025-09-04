"use client"

import { useEffect, useState } from "react"
import { useAuth } from "../context/AuthContext"
import axios from "axios"
import { Link } from "react-router-dom"
import "./DashboardPage_style.css"

function DashboardPage() {
  const { user } = useAuth()
  const [myDrafts, setMyDrafts] = useState([])
  const [docsToApprove, setDocsToApprove] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchMyDrafts = async () => {
      setIsLoading(true)
      try {
        const [draftsResponse, toApproveResponse] = await Promise.all([
          axios.get("/api/approvals/my-drafts", { withCredentials: true }),
          axios.get("/api/approvals/to-me", { withCredentials: true }),
        ])

        setMyDrafts(draftsResponse.data) // 성공 시 state에 데이터 저장
        setDocsToApprove(toApproveResponse.data) // 성공 시 state에 데이터 저장
      } catch (err) {
        setError("데이터를 불러오는데 실패했습니다.")
        console.error(err)
      } finally {
        setIsLoading(false) // 로딩 상태 종료
      }
    }

    fetchMyDrafts()
  }, [])

  return (
    <div className="dashboard-container">
      <div className="dashboard-header">
        <h1 className="dashboard-title">대시보드</h1>
        {user && <p className="welcome-message">{user.username}님, 환영합니다!</p>}
      </div>

      <div className="action-buttons">
        <Link to="/approvals/new">
          <button className="btn btn-primary">새 결재 문서 작성</button>
        </Link>
        <Link to="/tasks">
          <button className="btn btn-secondary">업무 관리 보드로 이동</button>
        </Link>
      </div>

      <div className="dashboard-content">
        <div className="document-section">
          <h2 className="section-title">내가 올린 결재 문서</h2>
          {isLoading && <p className="loading-message">로딩 중...</p>}
          {error && <p className="error-message">{error}</p>}
          <ul className="document-list">
            {myDrafts.length > 0
              ? myDrafts.map((doc) => (
                  <li key={`draft-${doc.docId}`} className="document-item">
                    <Link to={`/approvals/${doc.docId}`} className="document-link">
                      {doc.title} (결재자: {doc.approverName}, 상태: {doc.status})
                    </Link>
                  </li>
                ))
              : !isLoading && <li className="empty-message">작성한 문서가 없습니다.</li>}
          </ul>
        </div>

        <div className="document-section">
          <h2 className="section-title">내가 결재할 문서</h2>
          <ul className="document-list">
            {docsToApprove.length > 0
              ? docsToApprove.map((doc) => (
                  <li key={`approve-${doc.docId}`} className="document-item">
                    <Link to={`/approvals/${doc.docId}`} className="document-link">
                      {doc.title} (기안자: {doc.drafterName}, 상태: {doc.status})
                    </Link>
                  </li>
                ))
              : !isLoading && <li className="empty-message">결재할 문서가 없습니다.</li>}
          </ul>
        </div>
      </div>
    </div>
  )
}

export default DashboardPage


/*

useEffect: React 컴포넌트가 화면에 렌더링된 후에 특정 작업을 수행하고 싶을 때 사용하는 Hook입니다. 
API 호출과 같은 "부수 효과(Side Effect)"를 처리하기에 최적입니다.

useState: myDrafts, isLoading, error 와 같이 컴포넌트 내에서 동적으로 변하는 데이터들을 관리하기 위해 사용합니다.

withCredentials: true: 가장 중요한 옵션입니다. 
localhost:3000에서 localhost:8080으로 API를 요청할 때, 
브라우저가 보안 정책상 자동으로 보내주지 않는 **로그인 세션 쿠키(JSESSIONID)**를 함께 보내도록 허용하는 설정입니다. 
이 옵션이 없으면 백엔드에서는 우리가 로그인하지 않은 사용자로 인식하게 됩니다.


Promise.all: 여러 개의 API 호출을 동시에 실행하고, 모든 호출이 끝날 때까지 기다리게 해주는 JavaScript 기능입니다. 
my-drafts와 to-me API를 각각 순서대로 호출하는 것보다 더 효율적이고 빠릅니다.

UI 추가: return 부분에 '내가 결재할 문서' 목록을 표시하는 UI를 추가했습니다. 
데이터베이스에 현재 로그인한 사용자가 결재자로 지정된 문서가 있다면, 이 목록에 나타날 것입니다.


*/