import type { ReactNode } from 'react'
import EmptyState from './EmptyState'

export interface Column<T> {
  header: string
  cell: (row: T) => ReactNode
}

interface DataTableProps<T> {
  /** Descripción accesible de la tabla */
  caption: string
  columns: Column<T>[]
  rows: T[]
  getRowKey: (row: T, index: number) => string | number
  emptyMessage: string
  /** Filas más bajas, para tablas largas */
  compact?: boolean
}

/** Tabla genérica: T es el tipo de cada fila, así `cell` recibe la fila ya tipada. */
export default function DataTable<T>({
  caption,
  columns,
  rows,
  getRowKey,
  emptyMessage,
  compact = false,
}: DataTableProps<T>) {
  if (rows.length === 0) return <EmptyState message={emptyMessage} />

  return (
    <div className="table-scroll" role="region" aria-label={caption} tabIndex={0}>
      <table className={compact ? 'table table--compact' : 'table'}>
        <caption className="sr-only">{caption}</caption>
        <thead>
          <tr>
            {columns.map((col) => (
              <th key={col.header} scope="col">
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, index) => (
            <tr key={getRowKey(row, index)}>
              {columns.map((col) => (
                <td key={col.header} data-label={col.header}>
                  {col.cell(row)}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
