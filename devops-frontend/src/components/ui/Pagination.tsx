import { ChevronLeft, ChevronRight } from 'lucide-react'

interface PaginationProps {
  /** Página actual, desde 1 */
  page: number
  pageSize: number
  total: number
  onChange: (page: number) => void
}

export default function Pagination({ page, pageSize, total, onChange }: PaginationProps) {
  if (total <= pageSize) return null

  const pages = Math.ceil(total / pageSize)
  const from = (page - 1) * pageSize + 1
  const to = Math.min(page * pageSize, total)

  return (
    <nav className="pagination" aria-label="Paginación">
      <p aria-live="polite">
        Mostrando {from}–{to} de {total}
      </p>
      <div className="pagination__buttons">
        <button type="button" className="btn btn--ghost" disabled={page <= 1} onClick={() => onChange(page - 1)}>
          <ChevronLeft size={18} aria-hidden />
          Anterior
        </button>
        <span>
          Página {page} de {pages}
        </span>
        <button type="button" className="btn btn--ghost" disabled={page >= pages} onClick={() => onChange(page + 1)}>
          Siguiente
          <ChevronRight size={18} aria-hidden />
        </button>
      </div>
    </nav>
  )
}
