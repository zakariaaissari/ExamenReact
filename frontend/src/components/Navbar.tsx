import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

const links = [
  { to: '/', label: 'Home', end: true },
  { to: '/programs', label: 'Programs' },
  { to: '/students', label: 'Students' },
  { to: '/notes', label: 'Notes' },
]

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  async function handleLogout() {
    await logout()
    navigate('/login', { replace: true })
  }

  return (
    <header className="bg-slate-900 text-slate-100 shadow">
      <nav className="mx-auto flex max-w-6xl items-center gap-6 px-6 py-4">
        <span className="text-lg font-bold tracking-tight">🎓 Campus</span>
        <ul className="flex gap-1">
          {links.map((link) => (
            <li key={link.to}>
              <NavLink
                to={link.to}
                end={link.end}
                className={({ isActive }) =>
                  `rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                    isActive
                      ? 'bg-slate-700 text-white'
                      : 'text-slate-300 hover:bg-slate-800 hover:text-white'
                  }`
                }
              >
                {link.label}
              </NavLink>
            </li>
          ))}
        </ul>

        <div className="ml-auto flex items-center gap-3">
          {user && (
            <span className="text-sm text-slate-400">
              {user.username}
            </span>
          )}
          <button
            onClick={handleLogout}
            className="rounded-md px-3 py-2 text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white"
          >
            Logout
          </button>
        </div>
      </nav>
    </header>
  )
}
