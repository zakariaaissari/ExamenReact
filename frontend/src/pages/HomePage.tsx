import { Link } from 'react-router-dom'
import type { UseQueryResult } from '@tanstack/react-query'
import { usePrograms } from '../hooks/usePrograms'
import { useStudents } from '../hooks/useStudents'
import { useNotes } from '../hooks/useNotes'

// Turn any list query into a short count label for the dashboard cards.
function countLabel(query: UseQueryResult<unknown[]>): string {
  if (query.isLoading) return '…'
  if (query.isError) return '—'
  return String(query.data?.length ?? 0)
}

export default function HomePage() {
  const programs = usePrograms()
  const students = useStudents()
  const notes = useNotes()

  const cards = [
    {
      to: '/programs',
      title: 'Programs',
      desc: 'Browse the programs your school offers and keep their details up to date.',
      count: countLabel(programs),
    },
    {
      to: '/students',
      title: 'Students',
      desc: 'Enroll students, keep their information current, and download their transcripts.',
      count: countLabel(students),
    },
    {
      to: '/notes',
      title: 'Grades',
      desc: 'Enter and review student grades for each subject.',
      count: countLabel(notes),
    },
  ]

  return (
    <div>
      <h1 className="text-2xl font-bold text-slate-900">Welcome to Campus</h1>
      <p className="mt-1 text-slate-500">
        Manage your school in one place — programs, students, and grades.
      </p>

      <div className="mt-6 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {cards.map((card) => (
          <Link
            key={card.to}
            to={card.to}
            className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm transition hover:border-slate-300 hover:shadow"
          >
            <div className="flex items-start justify-between">
              <h2 className="text-lg font-semibold text-slate-900">{card.title}</h2>
              <span className="text-3xl font-bold tabular-nums text-slate-900">
                {card.count}
              </span>
            </div>
            <p className="mt-1 text-sm text-slate-500">{card.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  )
}
