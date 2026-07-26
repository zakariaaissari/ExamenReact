import client from './client'
import type { Releve } from '../types/releve'

export async function getReleve(studentId: number): Promise<Releve> {
  const { data } = await client.get<Releve>(`/api/students/${studentId}/releve`)
  return data
}

/**
 * Fetches the relevé PDF as a blob and triggers a browser download. The
 * filename comes from the server's Content-Disposition header when present.
 */
export async function downloadRelevePdf(studentId: number, fallbackName: string): Promise<void> {
  const response = await client.get(`/api/students/${studentId}/releve/pdf`, {
    responseType: 'blob',
  })

  const disposition = response.headers['content-disposition'] as string | undefined
  const match = disposition?.match(/filename="?([^"]+)"?/)
  const filename = match?.[1] ?? fallbackName

  const url = window.URL.createObjectURL(response.data)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  link.remove()
  window.URL.revokeObjectURL(url)
}
