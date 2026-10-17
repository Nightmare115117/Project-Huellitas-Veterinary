import type { EstadoCita } from '../types/api'

export type StatusTone = 'success' | 'pending' | 'danger' | 'info'

const toneByLabel: Record<string, StatusTone> = {
  Completado: 'success',
  Confirmada: 'success',
  Activo: 'success',
  Disponible: 'success',
  Programada: 'pending',
  Pendiente: 'pending',
  Ocupado: 'pending',
  Cancelada: 'danger',
  LOGIN_EXITOSO: 'success',
  LOGIN_FALLIDO: 'danger',
  CREAR_USUARIO: 'info',
  ELIMINAR_USUARIO: 'danger',
}

export function toneFor(label: string): StatusTone {
  return toneByLabel[label] ?? 'info'
}

export function citaStatus(estado?: number): { label: string; tone: StatusTone } {
  switch (estado as EstadoCita | undefined) {
    case 1:
      return { label: 'Programada', tone: 'pending' }
    case 2:
      return { label: 'Confirmada', tone: 'success' }
    case 3:
      return { label: 'Cancelada', tone: 'danger' }
    default:
      return { label: 'Desconocida', tone: 'info' }
  }
}
