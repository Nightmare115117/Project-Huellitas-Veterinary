import { ChevronLeft, ChevronRight } from 'lucide-react'

interface PaginationProps {
  /** Página actual, desde 1 */
  page: number
  pageSize: number
  total: number
  onChange: (page: number) => void
  /** Nombre del landmark; debe ser distinto si hay dos paginadores en la página */
  label?: string
  /** Anunciar el rango a lectores de pantalla (solo en uno de los dos) */
  live?: boolean
}

export default function Pagination({
  page,
  pageSize,
  total,
  onChange,
  label = 'Paginación',
  live = true,
}: PaginationProps) {
  if (total <= pageSize) return null

  const pages = Math.ceil(total / pageSize)
  const from = (page - 1) * pageSize + 1
  const to = Math.min(page * pageSize, total)

  return (
    <nav className="pagination" aria-label={label}>
      <p aria-live={live ? 'polite' : undefined}>
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
