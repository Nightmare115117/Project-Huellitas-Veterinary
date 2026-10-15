export function formatDate(value?: string | null): string {
  if (!value) return '--'
  const date = new Date(`${value}T00:00:00`)
  if (Number.isNaN(date.getTime())) return value
  return date.toLocaleDateString('es-MX', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

export function formatTime(value?: string | null): string {
  if (!value) return '--'
  const time = new Date(`1970-01-01T${value}`)
  if (Number.isNaN(time.getTime())) return value
  return time.toLocaleTimeString('es-MX', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: true,
  })
}

export function calcularEdad(fechaNacimiento?: string | null): string {
  if (!fechaNacimiento) return 'Sin edad'
  const birth = new Date(`${fechaNacimiento}T00:00:00`)
  if (Number.isNaN(birth.getTime())) return 'Sin edad'

  const today = new Date()
  let years = today.getFullYear() - birth.getFullYear()
  const monthDiff = today.getMonth() - birth.getMonth()
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    years -= 1
  }
  return `${years} año${years === 1 ? '' : 's'}`
}
