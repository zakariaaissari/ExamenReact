import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// The Spring Cloud Gateway runs on :8080 and exposes /programs, /students,
// /notes, and /api/students/** (the relevé endpoints). We proxy those paths in
// dev so the browser talks to Vite (same origin) and Vite forwards to the
// gateway — no CORS config needed on the backend.
// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
      '/programs': 'http://localhost:8080',
      '/students': 'http://localhost:8080',
      '/notes': 'http://localhost:8080',
    },
  },
})
