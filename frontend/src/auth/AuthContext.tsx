import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from 'react'
import axios from 'axios'
import client, { setAccessToken } from '../api/client'

export interface AuthUser {
  username: string
  role: string
}

interface AuthContextValue {
  user: AuthUser | null
  loading: boolean
  login: (username: string, password: string) => Promise<void>
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(null)
  const [loading, setLoading] = useState(true)

  // On mount, try to restore the session using the httpOnly refresh cookie.
  useEffect(() => {
    axios
      .post('/api/auth/refresh', null, { withCredentials: true })
      .then(({ data }) => {
        setAccessToken(data.token)
        setUser({ username: data.username, role: data.role })
      })
      .catch(() => {
        setAccessToken(null)
        setUser(null)
      })
      .finally(() => setLoading(false))
  }, [])

  async function login(username: string, password: string) {
    const { data } = await client.post('/api/auth/login', { username, password })
    setAccessToken(data.token)
    setUser({ username: data.username, role: data.role })
  }

  async function logout() {
    try {
      await client.post('/api/auth/logout')
    } catch {
      // ignore — clear local state regardless
    }
    setAccessToken(null)
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return ctx
}
