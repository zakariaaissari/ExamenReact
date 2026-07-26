import { useState } from 'react'
import {
  useStudents,
  useCreateStudent,
  useUpdateStudent,
  useDeleteStudent,
} from '../hooks/useStudents'
import type { Student, StudentRequest } from '../types/student'
import Modal from '../components/Modal'
import ConfirmDialog from '../components/ConfirmDialog'
import StudentForm from '../components/students/StudentForm'
import RelevePreview from '../components/releve/RelevePreview'
import { errorMessage } from '../lib/error'

export default function StudentsPage() {
  const { data: students, isLoading, isError, error } = useStudents()
  const createMut = useCreateStudent()
  const updateMut = useUpdateStudent()
  const deleteMut = useDeleteStudent()

  const [formOpen, setFormOpen] = useState(false)
  const [editing, setEditing] = useState<Student | null>(null)
  const [deleting, setDeleting] = useState<Student | null>(null)
  const [releveFor, setReleveFor] = useState<Student | null>(null)

  function openCreate() {
    setEditing(null)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function openEdit(student: Student) {
    setEditing(student)
    createMut.reset()
    updateMut.reset()
    setFormOpen(true)
  }

  function handleSubmit(body: StudentRequest) {
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
        <h1 className="text-2xl font-bold text-slate-900">Students</h1>
        <button
          onClick={openCreate}
          className="rounded-md bg-slate-900 px-4 py-2 text-sm font-medium text-white hover:bg-slate-800"
        >
          + Add Student
        </button>
      </div>

      <div className="mt-6 overflow-hidden rounded-lg border border-slate-200 bg-white">
        {isLoading && <p className="p-6 text-sm text-slate-500">Loading…</p>}

        {isError && (
          <p className="p-6 text-sm text-red-600">
            Failed to load students: {errorMessage(error)}
          </p>
        )}

        {!isLoading && !isError && students && students.length === 0 && (
          <p className="p-6 text-sm text-slate-500">
            No students yet. Click “Add Student” to create one.
          </p>
        )}

        {!isLoading && !isError && students && students.length > 0 && (
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-50 text-slate-500">
              <tr>
                <th className="px-4 py-3 font-medium">Name</th>
                <th className="px-4 py-3 font-medium">Email</th>
                <th className="px-4 py-3 font-medium">Program</th>
                <th className="px-4 py-3 text-right font-medium">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {students.map((s) => (
                <tr key={s.id} className="hover:bg-slate-50">
                  <td className="px-4 py-3 font-medium text-slate-900">
                    {s.firstName} {s.lastName}
                  </td>
                  <td className="px-4 py-3 text-slate-600">{s.email}</td>
                  <td className="px-4 py-3 text-slate-600">
                    {s.program ? (
                      s.program.name
                    ) : (
                      <span className="text-slate-400">—</span>
                    )}
                  </td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => setReleveFor(s)}
                      className="mr-3 font-medium text-indigo-600 hover:text-indigo-800"
                    >
                      Relevé
                    </button>
                    <button
                      onClick={() => openEdit(s)}
                      className="mr-3 text-slate-600 hover:text-slate-900"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => setDeleting(s)}
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
        title={editing ? 'Edit Student' : 'Add Student'}
        onClose={() => setFormOpen(false)}
      >
        <StudentForm
          initial={editing ?? undefined}
          submitting={activeMut.isPending}
          error={activeMut.isError ? errorMessage(activeMut.error) : undefined}
          onSubmit={handleSubmit}
          onCancel={() => setFormOpen(false)}
        />
      </Modal>

      <ConfirmDialog
        open={deleting !== null}
        title="Delete Student"
        message={`Delete “${deleting?.firstName} ${deleting?.lastName}”? This cannot be undone.`}
        loading={deleteMut.isPending}
        onConfirm={handleDelete}
        onCancel={() => setDeleting(null)}
      />

      <RelevePreview
        studentId={releveFor?.id ?? null}
        fallbackName={releveFor ? `${releveFor.firstName}_${releveFor.lastName}` : 'etudiant'}
        onClose={() => setReleveFor(null)}
      />
    </div>
  )
}
