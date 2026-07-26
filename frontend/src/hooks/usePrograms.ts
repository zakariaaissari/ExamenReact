import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
  getPrograms,
  createProgram,
  updateProgram,
  deleteProgram,
} from '../api/programs'
import type { ProgramRequest } from '../types/program'

const KEY = ['programs']

export function usePrograms() {
  return useQuery({ queryKey: KEY, queryFn: getPrograms })
}

export function useCreateProgram() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (body: ProgramRequest) => createProgram(body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useUpdateProgram() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }: { id: number; body: ProgramRequest }) =>
      updateProgram(id, body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useDeleteProgram() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteProgram(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}
