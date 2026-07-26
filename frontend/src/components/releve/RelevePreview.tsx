import { useState } from 'react'
import Modal from '../Modal'
import { useReleve } from '../../hooks/useReleve'
import { downloadRelevePdf } from '../../api/releve'
import { errorMessage } from '../../lib/error'

interface RelevePreviewProps {
  studentId: number | null
  fallbackName: string
  onClose: () => void
}

export default function RelevePreview({ studentId, fallbackName, onClose }: RelevePreviewProps) {
  const { data: releve, isLoading, isError, error } = useReleve(studentId)
  const [downloading, setDownloading] = useState(false)
  const [downloadError, setDownloadError] = useState<string | null>(null)

  async function handleDownload() {
    if (studentId === null) return
    setDownloading(true)
    setDownloadError(null)
    try {
      await downloadRelevePdf(studentId, `releve_${fallbackName}.pdf`)
    } catch (err) {
      setDownloadError(errorMessage(err))
    } finally {
      setDownloading(false)
    }
  }

  return (
    <Modal open={studentId !== null} title="Relevé de notes" size="lg" onClose={onClose}>
      {isLoading && <p className="text-sm text-slate-500">Chargement du relevé…</p>}

      {isError && (
        <p className="text-sm text-red-600">
          {/* 404 when the student has no notes */}
          Relevé indisponible : {errorMessage(error)}
        </p>
      )}

      {releve && (
        <div>
          {/* Header */}
          <div className="border-b border-slate-200 pb-3 text-center">
            <p className="font-semibold text-slate-900">{releve.institution}</p>
            <p className="text-xs text-slate-500">
              Année universitaire : {releve.academicYear}
            </p>
          </div>

          {/* Student identity */}
          <dl className="mt-4 grid grid-cols-2 gap-x-4 gap-y-2 text-sm">
            <div>
              <dt className="text-slate-500">Nom et prénom</dt>
              <dd className="font-medium text-slate-900">{releve.fullName}</dd>
            </div>
            <div>
              <dt className="text-slate-500">Identifiant</dt>
              <dd className="font-medium text-slate-900">{releve.studentId}</dd>
            </div>
            <div>
              <dt className="text-slate-500">Filière</dt>
              <dd className="font-medium text-slate-900">
                {releve.program?.name ?? '—'}
              </dd>
            </div>
            <div>
              <dt className="text-slate-500">Email</dt>
              <dd className="font-medium text-slate-900">{releve.email}</dd>
            </div>
          </dl>

          {/* Notes table */}
          <table className="mt-5 w-full text-left text-sm">
            <thead className="bg-slate-100 text-slate-500">
              <tr>
                <th className="px-3 py-2 font-medium">Matière / Élément</th>
                <th className="px-3 py-2 text-center font-medium">Note /20</th>
                <th className="px-3 py-2 text-center font-medium">Coefficient</th>
                <th className="px-3 py-2 text-center font-medium">Mention</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {releve.notes.map((line, i) => (
                <tr key={i}>
                  <td className="px-3 py-2 text-slate-700">{line.subject}</td>
                  <td
                    className={`px-3 py-2 text-center font-semibold ${
                      line.passed ? 'text-green-700' : 'text-red-700'
                    }`}
                  >
                    {line.value}
                  </td>
                  <td className="px-3 py-2 text-center text-slate-600">
                    {line.coefficient}
                  </td>
                  <td className="px-3 py-2 text-center text-slate-600">{line.mention}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Summary */}
          <div className="mt-5 grid grid-cols-2 gap-3 rounded-md bg-slate-50 p-4 text-sm">
            <div className="flex justify-between">
              <span className="text-slate-500">Moyenne générale</span>
              <span className="font-semibold text-slate-900">
                {releve.moyenne.toFixed(2)} / 20
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-slate-500">Mention</span>
              <span className="font-semibold text-slate-900">{releve.mention}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-slate-500">Décision</span>
              <span
                className={`rounded px-2 py-0.5 text-xs font-semibold text-white ${
                  releve.admis ? 'bg-green-600' : 'bg-red-600'
                }`}
              >
                {releve.decision}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-slate-500">Rang</span>
              <span className="font-semibold text-slate-900">{releve.rangLabel}</span>
            </div>
          </div>

          {downloadError && (
            <p className="mt-3 text-sm text-red-600">Échec du téléchargement : {downloadError}</p>
          )}

          {/* Actions */}
          <div className="mt-5 flex justify-end gap-2">
            <button
              onClick={onClose}
              className="rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Fermer
            </button>
            <button
              onClick={handleDownload}
              disabled={downloading}
              className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
            >
              {downloading ? 'Téléchargement…' : '⬇ Télécharger le relevé'}
            </button>
          </div>
        </div>
      )}
    </Modal>
  )
}
