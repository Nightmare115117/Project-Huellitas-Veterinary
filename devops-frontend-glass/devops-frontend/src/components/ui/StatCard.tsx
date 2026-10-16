import type { ReactNode } from 'react'
import type { LucideIcon } from 'lucide-react'
import CountUp from './CountUp'

interface StatCardProps {
  title: string
  value: ReactNode
  detail: string
  icon: LucideIcon
  accent?: 'blue' | 'red'
}

export default function StatCard({ title, value, detail, icon: Icon, accent = 'blue' }: StatCardProps) {
  return (
    <article className={`stat stat--${accent}`}>
      <span className="stat__icon" aria-hidden>
        <Icon size={22} />
      </span>
      <div className="stat__body">
        <h3>{title}</h3>
        <strong>{typeof value === 'number' ? <CountUp value={value} /> : value}</strong>
        <p>{detail}</p>
      </div>
    </article>
  )
}
