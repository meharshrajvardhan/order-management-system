
import { useNavigate } from 'react-router'



function LogoutButton() {
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')

    navigate('/login', { replace: true })
  }

  return (
    <button
      className="logout-button"
      type="button"
      onClick={handleLogout}
    >
      Logout
    </button>
  )
}

export default LogoutButton