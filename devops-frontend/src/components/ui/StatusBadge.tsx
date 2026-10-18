import { CheckCircle2, Clock, Info, XCircle, type LucideIcon } from 'lucide-react'
import { toneFor, type StatusTone } from '../../lib/status'

const icons: Record<StatusTone, LucideIcon> = {
  success: CheckCircle2,
  pending: Clock,
  danger: XCircle,
  info: Info,
}

interface StatusBadgeProps {
  label: string
  /** Si se omite, se deduce del texto del estado */
  tone?: StatusTone
}

export default function StatusBadge({ label, tone = toneFor(label) }: StatusBadgeProps) {
  const Icon = icons[tone]
  return (
    <span className={`badge badge--${tone}`}>
      <Icon size={14} aria-hidden />
      {label}
    </span>
  )
}
