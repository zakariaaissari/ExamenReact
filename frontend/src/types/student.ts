import type { Program } from './program'

// Mirrors StudentResponse in student-service: `program` is enriched via Feign.
export interface Student {
  id: number
  firstName: string
  lastName: string
  email: string
  programId: number | null
  program?: Program | null
}

export interface StudentRequest {
  firstName: string
  lastName: string
  email: string
  programId: number | null
}
