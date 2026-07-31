import { useState } from 'react'
import { useNavigate } from 'react-router'
import api from '../services/api'
import '../App.css'

function RegisterPage() {
  const navigate = useNavigate()

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: '',
  })

  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
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

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match.')
      return
    }

    setLoading(true)
    setError('')
    setSuccess('')

    try {
      const response = await api.post('/api/auth/register', {
        username: formData.username,
        password: formData.password,
      })

      setSuccess(response.data)

      setTimeout(() => {
        navigate('/login', { replace: true })
      }, 1200)
    } catch (requestError) {
      const responseMessage = requestError.response?.data

      setError(
        typeof responseMessage === 'string'
          ? responseMessage
          : 'Registration failed. Please try again.',
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

          <h1>Create your account</h1>

          <p>
            Register as a customer to create orders and securely
            track your own order history.
          </p>

          <div className="brand-features">
            <span>✓ View only your own orders</span>
            <span>✓ Secure password encryption</span>
            <span>✓ JWT-protected dashboard</span>
          </div>
        </div>
      </section>

      <section className="login-section">
        <div className="login-card">
          <div className="login-heading">
            <h2>Customer registration</h2>
            <p>Create your OMS customer account.</p>
          </div>

          {error && (
            <div className="error-message" role="alert">
              {error}
            </div>
          )}

          {success && (
            <div className="success-message" role="status">
              {success}
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
                placeholder="Choose a username"
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
                placeholder="Create a password"
                autoComplete="new-password"
                minLength="6"
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">
                Confirm password
              </label>

              <input
                id="confirmPassword"
                name="confirmPassword"
                type="password"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="Enter the password again"
                autoComplete="new-password"
                minLength="6"
                disabled={loading}
                required
              />
            </div>

            <button
              className="login-button"
              type="submit"
              disabled={loading || Boolean(success)}
            >
              {loading ? 'Creating account...' : 'Create account'}
            </button>
          </form>

          <p className="register-text">
            Already have an account?{' '}
            <button
              className="link-button"
              type="button"
              onClick={() => navigate('/login')}
            >
              Sign in
            </button>
          </p>
        </div>
      </section>
    </div>
  )
}

export default RegisterPage