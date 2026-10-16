/** Roles que devuelve GET /usuario/Rol */
export const Rol = {
  Administrador: 1,
  Dueno: 2,
  Gestor: 3,
} as const
export type Rol = (typeof Rol)[keyof typeof Rol]

export interface LoginResponse {
  login: boolean
}

export interface Perfil {
  nombre?: string
  name?: string
}

export interface Especie {
  nombre?: string
}

export interface Raza {
  nombre?: string
  especie?: Especie
}

export interface Mascota {
  idMascota?: number
  nombre?: string
  fechaNacimiento?: string
  raza?: Raza | string
  especie?: string
  breed?: string
}

/** 1 = Programada, 2 = Confirmada, 3 = Cancelada */
export type EstadoCita = 1 | 2 | 3

export interface Cita {
  idCita: number
  fecha?: string
  entradaAgendada?: string
  nombreVeterinario?: string
  estadoCita?: number
}

/**
 * El panel de administrador consume endpoints cuya forma varía
 * (nombres de campos distintos según el recurso), así que se
 * reciben como registros desconocidos y se normalizan en lib/normalize.ts
 */
export type UnknownRecord = Record<string, unknown>
