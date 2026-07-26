import { Routes, Route } from 'react-router-dom'
import Layout from './components/Layout'
import ProtectedRoute from './auth/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import HomePage from './pages/HomePage'
import ProgramsPage from './pages/ProgramsPage'
import StudentsPage from './pages/StudentsPage'
import NotesPage from './pages/NotesPage'

export default function App() {
  return (
    <Routes>
      {/* Public */}
      <Route path="/login" element={<LoginPage />} />

      {/* Protected — everything under the app layout requires authentication */}
      <Route
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route index element={<HomePage />} />
        <Route path="programs" element={<ProgramsPage />} />
        <Route path="students" element={<StudentsPage />} />
        <Route path="notes" element={<NotesPage />} />
      </Route>
    </Routes>
  )
}
