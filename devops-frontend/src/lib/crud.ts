import type { UnknownRecord } from '../types/api'

/** Tablas del backend. Todas exponen GET/POST en `ruta` y PUT/DELETE en `ruta/{id}`. */
export const crudTables = [
  { ruta: '/usuario', label: 'Usuarios' },
  { ruta: '/rol', label: 'Roles' },
  { ruta: '/mascota', label: 'Mascotas' },
  { ruta: '/especie', label: 'Especies' },
  { ruta: '/raza', label: 'Razas' },
  { ruta: '/cita', label: 'Citas' },
  { ruta: '/consulta', label: 'Consultas médicas' },
  { ruta: '/tratamiento', label: 'Tratamientos' },
  { ruta: '/servicio', label: 'Servicios' },
  { ruta: '/sucursal', label: 'Sucursales' },
  { ruta: '/gestor', label: 'Gestores' },
  { ruta: '/direccion', label: 'Direcciones' },
  { ruta: '/pais', label: 'Países' },
  { ruta: '/estado', label: 'Estados' },
  { ruta: '/ciudad', label: 'Ciudades' },
  { ruta: '/colonia', label: 'Colonias' },
  { ruta: '/calle', label: 'Calles' },
] as const

/**
 * Clave primaria de una fila, por convención `id<Entidad>` (idMascota, idCita…).
 * shortcut: se deduce del nombre; si una tabla usa otro nombre, fijarlo aquí.
 */
export function idKeyOf(row: UnknownRecord, ruta: string): string | undefined {
  const keys = Object.keys(row)
  const prefijo = `id${ruta.slice(1)}`.toLowerCase()
  return (
    keys.find((k) => k.toLowerCase() === prefijo) ??
    keys.find((k) => k.toLowerCase().startsWith(prefijo)) ??
    keys.find((k) => k === 'id')
  )
}
