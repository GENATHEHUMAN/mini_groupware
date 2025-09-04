import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
    const { user } = useAuth();

    if (!user) {
        // user 상태가 null이면(로그인하지 않았으면) 로그인 페이지로 리다이렉트.
        console.log("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        return <Navigate to="/" />;
    }

    // user 상태가 존재하면(로그인했으면) 요청된 페이지(children) 보여줌.
    return children;
}

export default ProtectedRoute;