import client from './client'
import type { Student, StudentRequest } from '../types/student'

export async function getStudents(): Promise<Student[]> {
  const { data } = await client.get<Student[]>('/students')
  return data
}

export async function getStudent(id: number): Promise<Student> {
  const { data } = await client.get<Student>(`/students/${id}`)
  return data
}

export async function createStudent(body: StudentRequest): Promise<Student> {
  const { data } = await client.post<Student>('/students', body)
  return data
}

export async function updateStudent(id: number, body: StudentRequest): Promise<Student> {
  const { data } = await client.put<Student>(`/students/${id}`, body)
  return data
}

export async function deleteStudent(id: number): Promise<void> {
  await client.delete(`/students/${id}`)
}
