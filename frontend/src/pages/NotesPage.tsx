import { useState } from 'react'
import {
  useNotes,
  useCreateNote,
  useUpdateNote,
  useDeleteNote,
} from '../hooks/useNotes'
import type { Note, NoteRequest } from '../types/note'
import Modal from '../components/Modal'
import ConfirmDialog from '../components/ConfirmDialog'
import NoteForm from '../components/notes/NoteForm'
import { errorMessage } from '../lib/error'

function studentName(note: Note): string {
  if (note.student) return `${note.student.firstName} ${note.student.lastName}`
  return `Student #${note.studentId}`
}

export default function NotesPage() {
  const { data: notes, isLoading, isError, error } = useNotes()
  const createMut = useCreateNote()
  const updateMut = useUpdateNote()
  const deleteMut = useDeleteNote()

  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Note | null>(null)
  const [deleting, setDeleting] = useState<Note | null>(null)

  function openCreate() {
    setEditing(null)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function openEdit(note: Note) {
    setEditing(note)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function handleSubmit(body: NoteRequest) {
    if (editing) {
      updateMut.mutate(
        { id: editing.id, body },
        { onSuccess: () => setFormOpen(false) },
      )
    } else {
      createMut.mutate(body, { onSuccess: () => setFormOpen(false) })
    }
  }

  function handleDelete() {
    if (!deleting) return
    deleteMut.mutate(deleting.id, { onSuccess: () => setDeleting(null) })
  }

  const activeMut = editing ? updateMut : createMut

  return (
    <div>
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-900">Notes</h1>
        <button
          onClick={openCreate}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
        >
          + Add Note
        </button>
      </div>

      <div className="mt-6 overflow-hidden rounded-lg border border-slate-200 bg-white">
        {isLoading && <p className="p-6 text-sm text-slate-500">Loading…</p>}

        {isError && (
          <p className="p-6 text-sm text-red-600">
            Failed to load notes: {errorMessage(error)}
          </p>
        )}

        {!isLoading && !isError && notes && notes.length === 0 && (
          <p className="p-6 text-sm text-slate-500">
            No notes yet. Click “Add Note” to record a grade.
          </p>
        )}

        {!isLoading && !isError && notes && notes.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="px-4 py-3 font-medium">Student</th>
                <th className="px-4 py-3 font-medium">Subject</th>
                <th className="px-4 py-3 font-medium">Grade</th>
                <th className="px-4 py-3 font-medium">Comment</th>
                <th className="px-4 py-3 text-right font-medium">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {notes.map((n) => (
                <tr key={n.id} className="hover:bg-slate-50">
                  <td className="px-4 py-3 font-medium text-slate-900">
                    {studentName(n)}
                  </td>
                  <td className="px-4 py-3 text-slate-600">{n.subject}</td>
                  <td className="px-4 py-3">
                    <span
                      className={`font-medium ${
                        n.value >= 10 ? 'text-green-700' : 'text-red-700'
                      }`}
                    >
                      {n.value}
                    </span>
                    <span className="text-slate-400"> / 20</span>
                  </td>
                  <td className="px-4 py-3 text-slate-600">
                    {n.comment || <span className="text-slate-400">—</span>}
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => openEdit(n)}
                      className="mr-3 text-slate-600 hover:text-slate-900"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => setDeleting(n)}
                      className="text-red-600 hover:text-red-800"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <Modal
        open={formOpen}
        title={editing ? 'Edit Note' : 'Add Note'}
        onClose={() => setFormOpen(false)}
      >
        <NoteForm
          initial={editing ?? undefined}
          submitting={activeMut.isPending}
          error={activeMut.isError ? errorMessage(activeMut.error) : undefined}
          onSubmit={handleSubmit}
          onCancel={() => setFormOpen(false)}
        />
      </Modal>

      <ConfirmDialog
        open={deleting !== null}
        title="Delete Note"
        message={`Delete the ${deleting?.subject} grade for ${
          deleting ? studentName(deleting) : ''
        }? This cannot be undone.`}
        loading={deleteMut.isPending}
        onConfirm={handleDelete}
        onCancel={() => setDeleting(null)}
      />
    </div>
  )
}
