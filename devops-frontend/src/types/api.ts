/** Roles que devuelve GET /usuario/Rol */
export const Rol = {
  Administrador: 1,
  Dueno: 2,
  Gestor: 3,
} as const
export type Rol = (typeof Rol)[keyof typeof Rol]

/** Con MFA activo el login responde login:false, mfaneed:true y un temToken (5 min) */
export interface LoginResponse {
  login: boolean
  token?: string
  mfaneed?: boolean
  temToken?: string
}

/** POST /usuario/mfa/verify */
export interface MfaVerifyResponse {
  validacion: boolean
  login: boolean
  token?: string
}

export interface Perfil {
  nombre?: string
  name?: string
}

/** MascotaDTO de GET /mascota/{correo} */
export interface Mascota {
  idMascota: number
  nombre: string
  fechaNacimiento: string | null
  status: number
  raza: { idRaza: number; nombre: string; especie: { nombre: string } | null } | null
}

/** 1 = Programada, 2 = Confirmada, 3 = Cancelada */
export type EstadoCita = 1 | 2 | 3

/** CitaDTO de GET /cita/{correo} */
export interface Cita {
  idCita: number
  nombreVeterinario: string
  idMascota: number
  fecha: string | null
  entradaAgendada: string | null
  estadoCita: number
}

/** TratamientoDTO de GET /tratamiento/usuario/{correo}; estatus: 0 = en curso, 1 = completado */
export interface Tratamiento {
  idTratamiento: number
  medicamento: string
  descripcion: string
  costo: number
  estatus: number
  mascotaNombre: string
}

/**
 * El panel de administrador consume endpoints cuya forma varía
 * (nombres de campos distintos según el recurso), así que se
 * reciben como registros desconocidos y se normalizan en lib/normalize.ts
 */
export type UnknownRecord = Record<string, unknown>
