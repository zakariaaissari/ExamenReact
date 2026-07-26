import type { Program } from './program'

// Mirrors ReleveLineDTO in student-service.
export interface ReleveLine {
  subject: string
  value: number
  coefficient: number
  mention: string
  passed: boolean
}

// Mirrors ReleveDTO in student-service.
export interface Releve {
  studentId: number
  firstName: string
  lastName: string
  fullName: string
  email: string
  program: Program | null
  institution: string
  academicYear: string
  lieu: string
  date: string
  notes: ReleveLine[]
  moyenne: number
  mention: string
  decision: string
  rang: number
  effectif: number
  rangLabel: string
  admis: boolean
}
