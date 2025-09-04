import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    // 1. 앱이 시작될 때 localStorage에서 'user' 정보를 가져와 초기 상태로 설정
    const [user, setUser] = useState(() => {
        const storedUser = localStorage.getItem('user');
        try {
            return storedUser ? JSON.parse(storedUser) : null;
        } catch (e) {
            console.error("localStorage에서 사용자 정보를 파싱하는데 실패했습니다.", e);
            return null;
        }
    });

    const login = (userData) => {
        // 2. 로그인 시, React state와 localStorage에 모두 사용자 정보를 저장
        localStorage.setItem('user', JSON.stringify(userData));
        setUser(userData);
    };

    const logout = () => {
        // 3. 로그아웃 시, React state와 localStorage에서 모두 사용자 정보를 제거
        localStorage.removeItem('user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    return useContext(AuthContext);
};