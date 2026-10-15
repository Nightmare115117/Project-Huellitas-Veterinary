import type { UnknownRecord } from '../types/api'

export function isRecord(value: unknown): value is UnknownRecord {
  return typeof value === 'object' && value !== null && !Array.isArray(value)
}

/** Lee una ruta anidada: pick(cita, 'mascota', 'nombre') */
export function pick(source: unknown, ...path: string[]): unknown {
  let current: unknown = source
  for (const key of path) {
    if (!isRecord(current)) return undefined
    current = current[key]
  }
  return current
}

/** Primer valor "útil" (texto o número, no vacío) de una lista de candidatos */
export function firstText(...candidates: unknown[]): string | undefined {
  for (const c of candidates) {
    if (typeof c === 'string' && c.trim() !== '') return c
    if (typeof c === 'number') return String(c)
  }
  return undefined
}

/** Primer valor truthy sin convertir (para estados que pueden ser boolean) */
export function firstTruthy(...candidates: unknown[]): unknown {
  return candidates.find(Boolean)
}

const ARRAY_KEYS = [
  'usuarios',
  'users',
  'data',
  'content',
  'items',
  'result',
  'results',
  'records',
  'list',
]

/** Acepta un arreglo directo o un objeto que lo envuelve bajo alguna clave conocida */
export function extractArray(response: unknown, preferredKey: string): UnknownRecord[] {
  if (Array.isArray(response)) return response.filter(isRecord)
  if (isRecord(response)) {
    for (const key of [preferredKey, ...ARRAY_KEYS]) {
      const value = response[key]
      if (Array.isArray(value)) return value.filter(isRecord)
    }
  }
  return []
}

export function normalizeStatus(value: unknown): string {
  if (!value && value !== 0) return 'Activo'
  return String(value).trim()
}
