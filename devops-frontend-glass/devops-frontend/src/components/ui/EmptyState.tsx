import { PawPrint } from 'lucide-react'

export default function EmptyState({ message }: { message: string }) {
  return (
    <div className="empty">
      <PawPrint size={28} aria-hidden />
      <p>{message}</p>
    </div>
  )
}
