import { Navigate } from 'react-router'

function ProtectedRoute({ children, allowedRole }) {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')

  if (!token) {
    return <Navigate to="/login" replace />
  }

  if (allowedRole && role !== allowedRole) {
    const correctDashboard =
      role === 'ADMIN'
        ? '/admin/dashboard'
        : '/customer/dashboard'

    return <Navigate to={correctDashboard} replace />
  }

  return children
}

export default ProtectedRoute