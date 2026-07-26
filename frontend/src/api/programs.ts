import client from './client'
import type { Program, ProgramRequest } from '../types/program'

export async function getPrograms(): Promise<Program[]> {
  const { data } = await client.get<Program[]>('/programs')
  return data
}

export async function getProgram(id: number): Promise<Program> {
  const { data } = await client.get<Program>(`/programs/${id}`)
  return data
}

export async function createProgram(body: ProgramRequest): Promise<Program> {
  const { data } = await client.post<Program>('/programs', body)
  return data
}

export async function updateProgram(id: number, body: ProgramRequest): Promise<Program> {
  const { data } = await client.put<Program>(`/programs/${id}`, body)
  return data
}

export async function deleteProgram(id: number): Promise<void> {
  await client.delete(`/programs/${id}`)
}
