// Mirrors ProgramResponse / ProgramRequest in program-service.
export interface Program {
  id: number
  name: string
  description: string
  durationYears: number
}

export interface ProgramRequest {
  name: string
  description: string
  durationYears: number
}
