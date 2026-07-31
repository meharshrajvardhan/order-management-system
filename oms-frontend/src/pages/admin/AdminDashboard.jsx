import { useCallback, useEffect, useMemo, useState } from 'react'
import api from '../../services/api'
import LogoutButton from '../../components/LogoutButton'
import '../../App.css'

const PAGE_SIZE = 10

const ORDER_STATUSES = [
  'PENDING',
  'PROCESSING',
  'CONFIRMED',
  'SHIPPED',
  'DELIVERED',
  'CANCELLED',
]

function AdminDashboard() {
  const username = localStorage.getItem('username')

  const [orders, setOrders] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)

  const [search, setSearch] = useState('')
  const [statusFilter, setStatusFilter] = useState('')

  const [loading, setLoading] = useState(true)
  const [updatingId, setUpdatingId] = useState(null)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const loadOrders = useCallback(async () => {
    setLoading(true)
    setError('')

    try {
      const response = await api.get('/api/orders', {
        params: {
          page,
          size: PAGE_SIZE,
          sortBy: 'id',
          direction: 'desc',
        },
      })

      setOrders(response.data.content || [])
      setTotalPages(response.data.totalPages || 0)
      setTotalElements(response.data.totalElements || 0)
    } catch (requestError) {
      if (requestError.response?.status === 403) {
        setError('You do not have administrator permission.')
      } else {
        setError('Unable to load orders.')
      }
    } finally {
      setLoading(false)
    }
  }, [page])

  /* eslint-disable react-hooks/set-state-in-effect */
  useEffect(() => {
    loadOrders()
  }, [loadOrders])
  /* eslint-enable react-hooks/set-state-in-effect */

  const filteredOrders = useMemo(() => {
    const normalizedSearch = search.trim().toLowerCase()

    return orders.filter((order) => {
      const matchesStatus =
        !statusFilter || order.orderStatus === statusFilter

      const searchableText = [
        order.id,
        order.customerName,
        order.productName,
      ]
        .join(' ')
        .toLowerCase()

      const matchesSearch =
        !normalizedSearch ||
        searchableText.includes(normalizedSearch)

      return matchesStatus && matchesSearch
    })
  }, [orders, search, statusFilter])

  const updateStatus = async (orderId, newStatus) => {
    setUpdatingId(orderId)
    setError('')
    setSuccess('')

    try {
      await api.put(`/api/orders/${orderId}/status`, null, {
        params: {
          status: newStatus,
        },
      })

      setOrders((currentOrders) =>
        currentOrders.map((order) =>
          order.id === orderId
            ? {
                ...order,
                orderStatus: newStatus,
              }
            : order,
        ),
      )

      setSuccess(`Order #${orderId} status updated.`)
    } catch {
      setError(`Unable to update order #${orderId}.`)
    } finally {
      setUpdatingId(null)
    }
  }

  const deleteOrder = async (orderId) => {
    const confirmed = window.confirm(
      `Delete order #${orderId}? This cannot be undone.`,
    )

    if (!confirmed) {
      return
    }

    setUpdatingId(orderId)
    setError('')
    setSuccess('')

    try {
      await api.delete(`/api/orders/${orderId}`)

      setSuccess(`Order #${orderId} deleted.`)

      /*
       * If the final order on a page is deleted,
       * return to the previous available page.
       */
      if (orders.length === 1 && page > 0) {
        setPage((currentPage) => currentPage - 1)
      } else {
        await loadOrders()
      }
    } catch {
      setError(`Unable to delete order #${orderId}.`)
    } finally {
      setUpdatingId(null)
    }
  }

  const activeOnPage = orders.filter((order) =>
    [
      'PENDING',
      'PROCESSING',
      'CONFIRMED',
      'SHIPPED',
    ].includes(order.orderStatus),
  ).length

  const deliveredOnPage = orders.filter(
    (order) => order.orderStatus === 'DELIVERED',
  ).length

  const pageOrderValue = orders
    .filter((order) => order.orderStatus !== 'CANCELLED')
    .reduce(
      (total, order) => total + Number(order.amount || 0),
      0,
    )

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

  const goToPreviousPage = () => {
    if (page > 0) {
      setPage((currentPage) => currentPage - 1)
      setSearch('')
      setStatusFilter('')
      setSuccess('')
    }
  }

  const goToNextPage = () => {
    if (page + 1 < totalPages) {
      setPage((currentPage) => currentPage + 1)
      setSearch('')
      setStatusFilter('')
      setSuccess('')
    }
  }

  return (
    <div className="dashboard-page">
      <header className="dashboard-header">
        <div className="dashboard-brand">
          <span className="dashboard-logo">OMS</span>

          <div>
            <strong>Order Management System</strong>
            <small>Administration Portal</small>
          </div>
        </div>

        <div className="dashboard-user">
          <div>
            <strong>{username}</strong>
            <small>Administrator</small>
          </div>

          <LogoutButton />
        </div>
      </header>

      <main className="dashboard-main">
        <section className="dashboard-welcome">
          <div>
            <p className="eyebrow">Administration</p>
            <h1>Order operations</h1>
            <p>Review and manage all customer orders.</p>
          </div>

          <button
            className="secondary-button"
            type="button"
            onClick={loadOrders}
            disabled={loading}
          >
            {loading ? 'Refreshing...' : 'Refresh orders'}
          </button>
        </section>

        <section className="summary-grid admin-summary-grid">
          <article className="summary-card">
            <span>Total orders</span>
            <strong>{totalElements}</strong>
            <small>All database orders</small>
          </article>

          <article className="summary-card">
            <span>Visible orders</span>
            <strong>{orders.length}</strong>
            <small>Orders on the current page</small>
          </article>

          <article className="summary-card">
            <span>Active / delivered</span>
            <strong>
              {activeOnPage} / {deliveredOnPage}
            </strong>
            <small>Current page status</small>
          </article>

          <article className="summary-card">
            <span>Page order value</span>
            <strong className="revenue-value">
              {formatCurrency(pageOrderValue)}
            </strong>
            <small>Current page, excluding cancelled</small>
          </article>
        </section>

        <section className="orders-panel">
          <div className="panel-heading admin-panel-heading">
            <div>
              <h2>All orders</h2>
              <p>
                Showing page {page + 1} of{' '}
                {Math.max(totalPages, 1)}.
              </p>
            </div>

            <div className="filter-controls">
              <input
                type="search"
                value={search}
                onChange={(event) =>
                  setSearch(event.target.value)
                }
                placeholder="Search this page"
              />

              <select
                value={statusFilter}
                onChange={(event) =>
                  setStatusFilter(event.target.value)
                }
              >
                <option value="">All statuses</option>

                {ORDER_STATUSES.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </div>
          </div>

          {error && (
            <div className="error-message">{error}</div>
          )}

          {success && (
            <div className="success-message">{success}</div>
          )}

          {loading && (
            <p className="table-message">Loading orders...</p>
          )}

          {!loading && filteredOrders.length === 0 && (
            <div className="empty-state">
              <h3>No matching orders</h3>
              <p>Try changing the current-page filters.</p>
            </div>
          )}

          {!loading && filteredOrders.length > 0 && (
            <div className="table-wrapper">
              <table className="orders-table">
                <thead>
                  <tr>
                    <th>Order</th>
                    <th>Customer</th>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Amount</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th>Action</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredOrders.map((order) => (
                    <tr key={order.id}>
                      <td>#{order.id}</td>
                      <td>{order.customerName}</td>
                      <td>{order.productName}</td>
                      <td>{order.quantity}</td>

                      <td>
                        {formatCurrency(order.amount)}
                      </td>

                      <td>
                        <select
                          className="status-select"
                          value={order.orderStatus}
                          disabled={updatingId === order.id}
                          onChange={(event) =>
                            updateStatus(
                              order.id,
                              event.target.value,
                            )
                          }
                        >
                          {ORDER_STATUSES.map((status) => (
                            <option
                              key={status}
                              value={status}
                            >
                              {status}
                            </option>
                          ))}
                        </select>
                      </td>

                      <td>{formatDate(order.createdDate)}</td>

                      <td>
                        <button
                          className="delete-button"
                          type="button"
                          disabled={updatingId === order.id}
                          onClick={() => deleteOrder(order.id)}
                        >
                          {updatingId === order.id
                            ? 'Working...'
                            : 'Delete'}
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}

          <div className="pagination">
            <button
              className="pagination-button"
              type="button"
              onClick={goToPreviousPage}
              disabled={page === 0 || loading}
            >
              Previous
            </button>

            <span className="pagination-info">
              Page <strong>{page + 1}</strong> of{' '}
              <strong>{Math.max(totalPages, 1)}</strong>
            </span>

            <button
              className="pagination-button"
              type="button"
              onClick={goToNextPage}
              disabled={
                page + 1 >= totalPages || loading
              }
            >
              Next
            </button>
          </div>
        </section>
      </main>
    </div>
  )
}

export default AdminDashboard