import { Navigate, Route, Routes } from 'react-router'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CustomerDashboard from './pages/customer/CustomerDashboard'
import CreateOrderPage from './pages/customer/CreateOrderPage'
import ProtectedRoute from './components/ProtectedRoute'
import AdminDashboard from './pages/admin/AdminDashboard'
import './App.css'

function App() {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={<LoginPage />} />

      <Route path="/register" element={<RegisterPage />} />

      {/* Customer routes */}
      <Route
        path="/customer/dashboard"
        element={
          <ProtectedRoute allowedRole="USER">
            <CustomerDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/customer/orders/new"
        element={
          <ProtectedRoute allowedRole="USER">
            <CreateOrderPage />
          </ProtectedRoute>
        }
      />

      {/* Admin routes */}
	  <Route
	    path="/admin/dashboard"
	    element={
	      <ProtectedRoute allowedRole="ADMIN">
	        <AdminDashboard />
	      </ProtectedRoute>
	    }
	  />

      {/* Default routes */}
      <Route
        path="/"
        element={<Navigate to="/login" replace />}
      />

      <Route
        path="*"
        element={<Navigate to="/login" replace />}
      />
    </Routes>
  )
}

export default App