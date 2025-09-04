import React from 'react';
import {BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import DashboardPage from './pages/DashboardPage'; 
import './App.css';
import ProtectedRoute from './components/ProtectedRoute';
import ApprovalCreatePage from './pages/ApprovalCreatePage';
import ApprovalDetailPage from './pages/ApprovalDetailPage';
import TaskBoardPage from './pages/TaskBoardPage';

function App() {
  return (
      <Router>
        <div className="App">
          <Routes>
            <Route path="/" element={<LoginPage/>} />
            <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
            <Route path="/approvals/new" element={<ProtectedRoute><ApprovalCreatePage /></ProtectedRoute>} />
            <Route path="/approvals/:docId" element={<ProtectedRoute><ApprovalDetailPage /></ProtectedRoute>} />
            <Route path="/tasks" element={<ProtectedRoute><TaskBoardPage /></ProtectedRoute>} />
          </Routes>
        </div>
      </Router>
  );
}

export default App;
