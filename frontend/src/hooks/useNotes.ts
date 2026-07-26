import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { getNotes, createNote, updateNote, deleteNote } from '../api/notes'
import type { NoteRequest } from '../types/note'

const KEY = ['notes']

export function useNotes() {
  return useQuery({ queryKey: KEY, queryFn: getNotes })
}

export function useCreateNote() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (body: NoteRequest) => createNote(body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useUpdateNote() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }: { id: number; body: NoteRequest }) =>
      updateNote(id, body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useDeleteNote() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteNote(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}
