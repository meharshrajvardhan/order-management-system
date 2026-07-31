import { useState } from 'react'
import { useNavigate } from 'react-router'
import api from '../services/api'
import '../App.css'

function LoginPage() {
  const navigate = useNavigate()

  const [formData, setFormData] = useState({
    username: '',
    password: '',
  })

  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (event) => {
    const { name, value } = event.target

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }))

    setError('')
  }

  const handleSubmit = async (event) => {
    event.preventDefault()

    setLoading(true)
    setError('')

    try {
      const response = await api.post(
        '/api/auth/login',
        formData,
      )

      const { token, username, role } = response.data

      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('role', role)

      if (role === 'ADMIN') {
        navigate('/admin/dashboard', { replace: true })
      } else {
        navigate('/customer/dashboard', { replace: true })
      }
    } catch (requestError) {
      const responseMessage = requestError.response?.data

      setError(
        typeof responseMessage === 'string'
          ? responseMessage
          : 'Login failed. Please check your credentials.',
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="login-page">
      <section className="login-brand">
        <div className="brand-content">
          <span className="brand-badge">OMS</span>

          <h1>Order Management System</h1>

          <p>
            Manage customer orders, monitor fulfilment and track
            every order from creation to delivery.
          </p>

          <div className="brand-features">
            <span>✓ Secure JWT authentication</span>
            <span>✓ Role-based access</span>
            <span>✓ Real-time order tracking</span>
          </div>
        </div>
      </section>

      <section className="login-section">
        <div className="login-card">
          <div className="login-heading">
            <h2>Welcome back</h2>
            <p>Sign in to access your OMS dashboard.</p>
          </div>

          {error && (
            <div className="error-message" role="alert">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="username">Username</label>

              <input
                id="username"
                name="username"
                type="text"
                value={formData.username}
                onChange={handleChange}
                placeholder="Enter your username"
                autoComplete="username"
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>

              <input
                id="password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter your password"
                autoComplete="current-password"
                disabled={loading}
                required
              />
            </div>

            <button
              className="login-button"
              type="submit"
              disabled={loading}
            >
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
          </form>

          <p className="register-text">
            Don&apos;t have an account?{' '}
            <button
              className="link-button"
              type="button"
              onClick={() => navigate('/register')}
            >
              Create account
            </button>
          </p>
        </div>
      </section>
    </div>
  )
}

export default LoginPage