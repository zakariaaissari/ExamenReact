import { useState } from 'react'
import {
  usePrograms,
  useCreateProgram,
  useUpdateProgram,
  useDeleteProgram,
} from '../hooks/usePrograms'
import type { Program, ProgramRequest } from '../types/program'
import Modal from '../components/Modal'
import ConfirmDialog from '../components/ConfirmDialog'
import ProgramForm from '../components/programs/ProgramForm'
import { errorMessage } from '../lib/error'

export default function ProgramsPage() {
  const { data: programs, isLoading, isError, error } = usePrograms()
  const createMut = useCreateProgram()
  const updateMut = useUpdateProgram()
  const deleteMut = useDeleteProgram()

  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Program | null>(null)
  const [deleting, setDeleting] = useState<Program | null>(null)

  function openCreate() {
    setEditing(null)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function openEdit(program: Program) {
    setEditing(program)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function handleSubmit(body: ProgramRequest) {
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
        <h1 className="text-2xl font-bold text-slate-900">Programs</h1>
        <button
          onClick={openCreate}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
        >
          + Add Program
        </button>
      </div>

      <div className="mt-6 overflow-hidden rounded-lg border border-slate-200 bg-white">
        {isLoading && <p className="p-6 text-sm text-slate-500">Loading…</p>}

        {isError && (
          <p className="p-6 text-sm text-red-600">
            Failed to load programs: {errorMessage(error)}
          </p>
        )}

        {!isLoading && !isError && programs && programs.length === 0 && (
          <p className="p-6 text-sm text-slate-500">
            No programs yet. Click “Add Program” to create one.
          </p>
        )}

        {!isLoading && !isError && programs && programs.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="px-4 py-3 font-medium">Name</th>
                <th className="px-4 py-3 font-medium">Description</th>
                <th className="px-4 py-3 font-medium">Duration</th>
                <th className="px-4 py-3 text-right font-medium">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {programs.map((p) => (
                <tr key={p.id} className="hover:bg-slate-50">
                  <td className="px-4 py-3 font-medium text-slate-900">{p.name}</td>
                  <td className="px-4 py-3 text-slate-600">{p.description}</td>
                  <td className="px-4 py-3 text-slate-600">
                    {p.durationYears} {p.durationYears > 1 ? 'years' : 'year'}
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => openEdit(p)}
                      className="mr-3 text-slate-600 hover:text-slate-900"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => setDeleting(p)}
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
        title={editing ? 'Edit Program' : 'Add Program'}
        onClose={() => setFormOpen(false)}
      >
        <ProgramForm
          initial={editing ?? undefined}
          submitting={activeMut.isPending}
          error={activeMut.isError ? errorMessage(activeMut.error) : undefined}
          onSubmit={handleSubmit}
          onCancel={() => setFormOpen(false)}
        />
      </Modal>

      <ConfirmDialog
        open={deleting !== null}
        title="Delete Program"
        message={`Delete “${deleting?.name}”? This cannot be undone.`}
        loading={deleteMut.isPending}
        onConfirm={handleDelete}
        onCancel={() => setDeleting(null)}
      />
    </div>
  )
}
