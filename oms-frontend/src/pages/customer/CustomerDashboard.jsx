import { useNavigate } from 'react-router'
import { useEffect, useState } from 'react'
import api from '../../services/api'
import LogoutButton from '../../components/LogoutButton'
import '../../App.css'


function CustomerDashboard() {
  const username = localStorage.getItem('username')
  const navigate = useNavigate()

  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    const loadOrders = async () => {
      try {
        const response = await api.get(
          '/api/orders/my-orders',
        )

        setOrders(response.data)
      } catch (requestError) {
        const status = requestError.response?.status

        if (status === 401) {
          setError('Your session has expired. Please log in again.')
        } else {
          setError('Unable to load your orders.')
        }
      } finally {
        setLoading(false)
      }
    }

    loadOrders()
  }, [])

  const pendingOrders = orders.filter((order) =>
    ['PENDING', 'PROCESSING', 'CONFIRMED'].includes(
      order.orderStatus,
    ),
  ).length

  const deliveredOrders = orders.filter(
    (order) => order.orderStatus === 'DELIVERED',
  ).length

  const formatCurrency = (value) =>
    new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
    }).format(value)

  const formatDate = (value) =>
    new Intl.DateTimeFormat('en-IN', {
      dateStyle: 'medium',
      timeStyle: 'short',
    }).format(new Date(value))

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <div className="dashboard-brand">
          <span className="dashboard-logo">OMS</span>

          <div>
            <strong>Order Management System</strong>
            <small>Customer Portal</small>
          </div>
        </div>

        <div className="dashboard-user">
          <div>
            <strong>{username}</strong>
            <small>Customer</small>
          </div>

          <LogoutButton />
        </div>
      </header>

      <main className="dashboard-main">
        <section className="dashboard-welcome">
          <div>
            <p className="eyebrow">Customer dashboard</p>
            <h1>Welcome, {username}</h1>
            <p>
              Review your orders and follow their current status.
            </p>
          </div>

		  <button
		    className="primary-action"
		    type="button"
		    onClick={() => navigate('/customer/orders/new')}
		  >
		    + Create order
		  </button>
        </section>

        <section className="summary-grid">
          <article className="summary-card">
            <span>Total orders</span>
            <strong>{orders.length}</strong>
            <small>All your orders</small>
          </article>

          <article className="summary-card">
            <span>In progress</span>
            <strong>{pendingOrders}</strong>
            <small>Pending or processing</small>
          </article>

          <article className="summary-card">
            <span>Delivered</span>
            <strong>{deliveredOrders}</strong>
            <small>Successfully completed</small>
          </article>
        </section>

        <section className="orders-panel">
          <div className="panel-heading">
            <div>
              <h2>My orders</h2>
              <p>Only orders connected to your account appear here.</p>
            </div>
          </div>

          {loading && (
            <p className="table-message">Loading your orders...</p>
          )}

          {error && (
            <div className="error-message">{error}</div>
          )}

          {!loading && !error && orders.length === 0 && (
            <div className="empty-state">
              <h3>No orders yet</h3>
              <p>Create your first order to see it here.</p>
            </div>
          )}

          {!loading && !error && orders.length > 0 && (
            <div className="table-wrapper">
              <table className="orders-table">
                <thead>
                  <tr>
                    <th>Order</th>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Created</th>
                  </tr>
                </thead>

                <tbody>
                  {orders.map((order) => (
                    <tr key={order.id}>
                      <td>#{order.id}</td>
                      <td>{order.productName}</td>
                      <td>{order.quantity}</td>
                      <td>{formatCurrency(order.price)}</td>
                      <td>{formatCurrency(order.amount)}</td>
                      <td>
                        <span
                          className={`status-badge status-${order.orderStatus.toLowerCase()}`}
                        >
                          {order.orderStatus}
                        </span>
                      </td>
                      <td>{formatDate(order.createdDate)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </main>
    </div>
  )
}

export default CustomerDashboard