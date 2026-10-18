import type { UnknownRecord } from '../types/api'
import { firstText, firstTruthy, normalizeStatus, pick } from './normalize'
import { citaStatus } from './status'

export interface AdminUser {
  name: string
  email: string
  role: string
  status: string
}

export interface StaffMember extends AdminUser {
  shift?: string
}

export interface AdminPet {
  name: string
  species: string
  breed: string
  owner: string
}

export interface AdminAppointment {
  date: string
  time: string
  pet: string
  owner: string
  status: string
}

export interface AdminTreatment {
  pet: string
  medicine: string
  description: string
  status: string
}

export function toUser(r: UnknownRecord): AdminUser {
  return {
    name: firstText(r.nombreCompleto, r.nombre, r.name, r.correo, r.email) ?? 'Sin nombre',
    email: firstText(r.correo, r.email, r.username) ?? '-',
    role: firstText(pick(r, 'rol', 'nombre'), r.rol, r.role, r.rolUsuario) ?? 'Usuario',
    status: normalizeStatus(firstTruthy(r.estado, r.status, r.estatus, r.activo)),
  }
}

export function toPet(r: UnknownRecord): AdminPet {
  return {
    name: firstText(r.nombre, r.name) ?? 'Sin nombre',
    species: firstText(pick(r, 'raza', 'especie', 'nombre'), r.especie, r.species, r.tipo) ?? 'Sin especie',
    breed: firstText(pick(r, 'raza', 'nombre'), r.raza, r.breed, r.tipoRaza) ?? 'Sin raza',
    owner: firstText(pick(r, 'dueno', 'nombre'), r.dueno, r.owner, r.nombreDueno) ?? 'Sin dueño',
  }
}

export function toAppointment(r: UnknownRecord): AdminAppointment {
  // El backend manda estadoCita como número (1/2/3): se traduce a su etiqueta
  const status =
    typeof r.estadoCita === 'number'
      ? citaStatus(r.estadoCita).label
      : normalizeStatus(firstTruthy(r.estadoCita, r.status, r.estatus, r.estado) ?? 'Pendiente')

  return {
    date: firstText(r.fecha, r.date, r.agenda) ?? '-',
    time: firstText(r.entradaAgendada, r.hora, r.time, r.horaCita) ?? '-',
    pet: firstText(pick(r, 'mascota', 'nombre'), r.pet, r.nombreMascota) ?? 'Sin mascota',
    owner: firstText(pick(r, 'dueno', 'nombre'), r.dueno, r.owner, r.nombreDueno) ?? 'Sin dueño',
    status,
  }
}

export function toTreatment(r: UnknownRecord): AdminTreatment {
  return {
    pet: firstText(pick(r, 'mascota', 'nombre'), r.pet, r.nombreMascota) ?? 'Sin mascota',
    medicine:
      firstText(pick(r, 'medicamento', 'nombre'), r.medicamento, r.medicine, r.nombreMedicamento) ??
      'Sin medicamento',
    description: firstText(r.descripcion, r.description, r.observaciones) ?? 'Sin descripción',
    status: normalizeStatus(firstTruthy(r.estado, r.status, r.estatus, r.activo) ?? 'Activo'),
  }
}

export function isStaff(user: AdminUser): boolean {
  const role = user.role.toLowerCase()
  return ['veter', 'asist', 'admin', 'gestor'].some((fragment) => role.includes(fragment))
}

export interface AdminBitacora {
  idBitacora: number
  fechaHora: string
  usuario: string
  rol?: string
  accion: string
  modulo?: string
  detalle?: string
  ip?: string
}

export function toBitacora(r: UnknownRecord): AdminBitacora {
  return {
    idBitacora: typeof r.idBitacora === 'number' ? r.idBitacora : Number(r.idBitacora ?? 0),
    fechaHora: firstText(r.fechaHora, r.fecha, r.createdAt) ?? '',
    usuario: firstText(r.usuario, r.correo, r.user, r.username) ?? 'Anónimo',
    rol: firstText(r.rol, r.role) ?? '-',
    accion: firstText(r.accion, r.action, r.evento) ?? 'ACCIÓN',
    modulo: firstText(r.modulo, r.module, r.seccion) ?? '-',
    detalle: firstText(r.detalle, r.descripcion, r.detail) ?? '-',
    ip: firstText(r.ip, r.direccionIp) ?? '-',
  }
}

