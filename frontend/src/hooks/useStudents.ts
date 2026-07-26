import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import {
  getStudents,
  createStudent,
  updateStudent,
  deleteStudent,
} from '../api/students'
import type { StudentRequest } from '../types/student'

const KEY = ['students']

export function useStudents() {
  return useQuery({ queryKey: KEY, queryFn: getStudents })
}

export function useCreateStudent() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (body: StudentRequest) => createStudent(body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useUpdateStudent() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, body }: { id: number; body: StudentRequest }) =>
      updateStudent(id, body),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}

export function useDeleteStudent() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteStudent(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: KEY }),
  })
}
