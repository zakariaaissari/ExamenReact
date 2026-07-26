import { useState, type FormEvent } from 'react'
import type { Note, NoteRequest } from '../../types/note'
import { useStudents } from '../../hooks/useStudents'

interface NoteFormProps {
  initial?: Note
  submitting: boolean
  error?: string
  onSubmit: (body: NoteRequest) => void
  onCancel: () => void
}

const inputClass =
  'mt-1 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-slate-500 focus:outline-none'

export default function NoteForm({
  initial,
  submitting,
  error,
  onSubmit,
  onCancel,
}: NoteFormProps) {
  const { data: students, isLoading: studentsLoading } = useStudents()

  const [studentId, setStudentId] = useState(initial?.studentId?.toString() ?? '')
  const [subject, setSubject] = useState(initial?.subject ?? '')
  const [value, setValue] = useState(initial?.value?.toString() ?? '')
  const [comment, setComment] = useState(initial?.comment ?? '')

  function handleSubmit(e: FormEvent) {
    e.preventDefault()
    onSubmit({
      studentId: Number(studentId),
      subject: subject.trim(),
      value: Number(value),
      comment: comment.trim() ? comment.trim() : null,
    })
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div>
        <label className="block text-sm font-medium text-slate-700">Student</label>
        <select
          className={inputClass}
          value={studentId}
          onChange={(e) => setStudentId(e.target.value)}
          disabled={studentsLoading}
          required
        >
          <option value="">— Select a student —</option>
          {students?.map((s) => (
            <option key={s.id} value={s.id}>
              {s.firstName} {s.lastName}
            </option>
          ))}
        </select>
      </div>
      <div className="grid grid-cols-2 gap-3">
        <div>
          <label className="block text-sm font-medium text-slate-700">Subject</label>
          <input
            className={inputClass}
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
            required
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-700">
            Grade (0–20)
          </label>
          <input
            type="number"
            min={0}
            max={20}
            step={0.25}
            className={inputClass}
            value={value}
            onChange={(e) => setValue(e.target.value)}
            required
          />
        </div>
      </div>
      <div>
        <label className="block text-sm font-medium text-slate-700">Comment</label>
        <textarea
          className={inputClass}
          rows={2}
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />
      </div>

      {error && <p className="text-sm text-red-600">{error}</p>}

      <div className="flex justify-end gap-2 pt-2">
        <button
          type="button"
          onClick={onCancel}
          className="rounded-md border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
        >
          Cancel
        </button>
        <button
          type="submit"
          disabled={submitting}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800 disabled:opacity-50"
        >
          {submitting ? 'Saving…' : 'Save'}
        </button>
      </div>
    </form>
  )
}
