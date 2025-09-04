import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { AuthProvider } from './context/AuthContext'; // AuthProvider import

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <AuthProvider> {/* App 전체를 AuthProvider로 감싸줍니다. */}
      <App />
    </AuthProvider>
  </React.StrictMode>
);