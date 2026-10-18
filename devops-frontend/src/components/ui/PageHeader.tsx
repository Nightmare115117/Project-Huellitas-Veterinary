import type { ReactNode } from 'react'

interface PageHeaderProps {
  title: string
  subtitle: string
  /** Botón de acción a la derecha del título */
  action?: ReactNode
}

export default function PageHeader({ title, subtitle, action }: PageHeaderProps) {
  return (
    <header className="page-header">
      <div>
        <h1>{title}</h1>
        <p>{subtitle}</p>
      </div>
      {action}
    </header>
  )
}
