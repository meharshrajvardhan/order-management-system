import { useState } from 'react'
import { useNavigate } from 'react-router'
import api from '../../services/api'
import '../../App.css'

function CreateOrderPage() {
  const navigate = useNavigate()
  const username = localStorage.getItem('username')

  const [formData, setFormData] = useState({
    productName: '',
    quantity: 1,
    price: '',
  })

  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)

  const amount =
    Number(formData.quantity || 0) *
    Number(formData.price || 0)

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

    if (Number(formData.quantity) <= 0) {
      setError('Quantity must be greater than zero.')
      return
    }

    if (Number(formData.price) <= 0) {
      setError('Price must be greater than zero.')
      return
    }

    setLoading(true)
    setError('')
    setSuccess('')

    try {
      await api.post('/api/orders', {
        customerName: username,
        productName: formData.productName.trim(),
        quantity: Number(formData.quantity),
        price: Number(formData.price),
      })

      setSuccess('Order created successfully.')

      setTimeout(() => {
        navigate('/customer/dashboard', {
          replace: true,
        })
      }, 1000)
    } catch (requestError) {
      const responseData = requestError.response?.data

      setError(
        typeof responseData === 'string'
          ? responseData
          : responseData?.message ||
              'Unable to create the order.',
      )
    } finally {
      setLoading(false)
    }
  }

  const formatCurrency = (value) =>
    new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
    }).format(value)

  return (
    <div className="form-page">
      <div className="order-form-card">
        <div className="form-page-heading">
          <div>
            <p className="eyebrow">Customer portal</p>
            <h1>Create a new order</h1>

            <p>
              This order will be connected to your account.
            </p>
          </div>

          <button
            className="secondary-button"
            type="button"
            onClick={() =>
              navigate('/customer/dashboard')
            }
          >
            Back to dashboard
          </button>
        </div>

        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {success && (
          <div className="success-message">
            {success}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="productName">
              Product name
            </label>

            <input
              id="productName"
              name="productName"
              type="text"
              value={formData.productName}
              onChange={handleChange}
              placeholder="Example: Business Laptop"
              disabled={loading}
              required
            />
          </div>

          <div className="two-column-form">
            <div className="form-group">
              <label htmlFor="quantity">
                Quantity
              </label>

              <input
                id="quantity"
                name="quantity"
                type="number"
                min="1"
                value={formData.quantity}
                onChange={handleChange}
                disabled={loading}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="price">
                Price per unit
              </label>

              <input
                id="price"
                name="price"
                type="number"
                min="0.01"
                step="0.01"
                value={formData.price}
                onChange={handleChange}
                placeholder="0.00"
                disabled={loading}
                required
              />
            </div>
          </div>

          <div className="amount-preview">
            <span>Calculated amount</span>
            <strong>{formatCurrency(amount)}</strong>
          </div>

          <button
            className="login-button"
            type="submit"
            disabled={loading || Boolean(success)}
          >
            {loading
              ? 'Creating order...'
              : 'Place order'}
          </button>
        </form>
      </div>
    </div>
  )
}

export default CreateOrderPage