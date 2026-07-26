import { useQuery } from '@tanstack/react-query'
import { getReleve } from '../api/releve'

// Fetches the transcript for the preview modal. Only runs when a studentId is
// provided (the modal passes null while closed).
export function useReleve(studentId: number | null) {
  return useQuery({
    queryKey: ['releve', studentId],
    queryFn: () => getReleve(studentId as number),
    enabled: studentId !== null,
  })
}
