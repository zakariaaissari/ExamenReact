import type { Student } from './student'

// Mirrors NoteResponse in note-service: `student` is enriched via Feign.
// `value` is a grade between 0 and 20.
export interface Note {
  id: number
  subject: string
  value: number
  comment: string | null
  studentId: number
  student?: Student | null
}

export interface NoteRequest {
  studentId: number
  subject: string
  value: number
  comment: string | null
}
