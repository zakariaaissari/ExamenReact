import client from './client'
import type { Note, NoteRequest } from '../types/note'

export async function getNotes(): Promise<Note[]> {
  const { data } = await client.get<Note[]>('/notes')
  return data
}

export async function getNote(id: number): Promise<Note> {
  const { data } = await client.get<Note>(`/notes/${id}`)
  return data
}

export async function getNotesByStudent(studentId: number): Promise<Note[]> {
  const { data } = await client.get<Note[]>(`/notes/student/${studentId}`)
  return data
}

export async function createNote(body: NoteRequest): Promise<Note> {
  const { data } = await client.post<Note>('/notes', body)
  return data
}

export async function updateNote(id: number, body: NoteRequest): Promise<Note> {
  const { data } = await client.put<Note>(`/notes/${id}`, body)
  return data
}

export async function deleteNote(id: number): Promise<void> {
  await client.delete(`/notes/${id}`)
}
