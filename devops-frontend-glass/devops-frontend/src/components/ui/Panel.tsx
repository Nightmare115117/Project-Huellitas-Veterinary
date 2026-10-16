import type { ReactNode } from 'react'

interface PanelProps {
  title?: string
  children: ReactNode
}

export default function Panel({ title, children }: PanelProps) {
  return (
    <section className="panel">
      {title && <h2>{title}</h2>}
      {children}
    </section>
  )
}
