import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios'

// The access token lives only in memory (module variable) — never localStorage.
// AuthContext calls setAccessToken() on login/refresh; the request interceptor
// reads it here.
let accessToken: string | null = null

export function setAccessToken(token: string | null) {
  accessToken = token
}

export function getAccessToken(): string | null {
  return accessToken
}

// Base URL is '/' because Vite proxies /programs, /students, /notes and
// /api/** to the gateway (:8080). withCredentials sends the httpOnly refresh
// cookie on /api/auth calls.
const client = axios.create({
  baseURL: '/',
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true,
})

const AUTH_PREFIX = '/api/auth'

// Attach the Bearer access token to every request except the auth endpoints
// (login/refresh/logout authenticate via body/cookie, not a Bearer token).
client.interceptors.request.use((config) => {
  if (accessToken && !config.url?.startsWith(AUTH_PREFIX)) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})

// Single-flight refresh: concurrent 401s share one refresh call.
let refreshPromise: Promise<string> | null = null

async function refreshAccessToken(): Promise<string> {
  // Raw axios (not `client`) to avoid recursing through these interceptors.
  const { data } = await axios.post(`${AUTH_PREFIX}/refresh`, null, { withCredentials: true })
  const newToken = data.token as string
  setAccessToken(newToken)
  return newToken
}

client.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const original = error.config as (InternalAxiosRequestConfig & { _retry?: boolean }) | undefined
    const status = error.response?.status
    const url = original?.url ?? ''

    const shouldRefresh =
      status === 401 && original && !original._retry && !url.startsWith(AUTH_PREFIX)

    if (shouldRefresh) {
      original._retry = true
      try {
        if (!refreshPromise) {
          refreshPromise = refreshAccessToken().finally(() => {
            refreshPromise = null
          })
        }
        const newToken = await refreshPromise
        original.headers.Authorization = `Bearer ${newToken}`
        return client(original)
      } catch (refreshError) {
        setAccessToken(null)
        if (window.location.pathname !== '/login') {
          window.location.assign('/login')
        }
        return Promise.reject(refreshError)
      }
    }

    return Promise.reject(error)
  },
)

export default client
