import type { UnknownRecord } from '../types/api'
import { firstText, isRecord } from './normalize'

export type Tipo = 'text' | 'number' | 'date' | 'time' | 'password' | 'ref'

export interface Campo {
  key: string
  label: string
  tipo: Tipo
  /** Ruta de la tabla relacionada (solo tipo 'ref') */
  ref?: string
}

export interface CrudTable {
  ruta: string
  label: string
  /** Respaldo para tablas vacías; con datos, los campos se deducen de los registros */
  campos: Campo[]
}

export interface Opcion {
  value: string
  label: string
}

/** 'fechaNacimiento' → 'Fecha nacimiento' */
export function etiqueta(key: string): string {
  const s = key.replace(/([a-z])([A-Z])/g, '$1 $2').toLowerCase()
  return s.charAt(0).toUpperCase() + s.slice(1)
}

const campo = (key: string, tipo: Tipo = 'text', ref?: string): Campo => ({
  key,
  label: etiqueta(key),
  tipo,
  ref,
})

/** Tablas del backend. Todas exponen GET/POST en `ruta` y PUT/DELETE en `ruta/{id}`. */
export const crudTables: CrudTable[] = [
  {
    ruta: '/usuario',
    label: 'Usuarios',
    campos: [
      campo('nombre'),
      campo('paterno'),
      campo('materno'),
      campo('correo'),
      campo('contrasena', 'password'),
      campo('rol', 'ref', '/rol'),
    ],
  },
  { ruta: '/rol', label: 'Roles', campos: [campo('nombre')] },
  {
    ruta: '/mascota',
    label: 'Mascotas',
    campos: [
      campo('nombre'),
      campo('fechaNacimiento', 'date'),
      campo('status', 'number'),
      campo('raza', 'ref', '/raza'),
      campo('sucursal', 'ref', '/sucursal'),
      campo('usuario', 'ref', '/usuario'),
    ],
  },
  { ruta: '/especie', label: 'Especies', campos: [campo('nombre')] },
  {
    ruta: '/raza',
    label: 'Razas',
    campos: [campo('nombre'), campo('especie', 'ref', '/especie')],
  },
  {
    ruta: '/cita',
    label: 'Citas',
    campos: [
      campo('fecha', 'date'),
      campo('entradaAgendada', 'time'),
      campo('horaSalida', 'time'),
      campo('estadoCita', 'number'),
      campo('mascota', 'ref', '/mascota'),
      campo('usuarioM', 'ref', '/usuario'),
      campo('usuarioV', 'ref', '/usuario'),
    ],
  },
  {
    ruta: '/consulta',
    label: 'Consultas médicas',
    campos: [
      campo('motivo'),
      campo('diagnostico'),
      campo('recomendaciones'),
      campo('peso', 'number'),
      campo('cita', 'ref', '/cita'),
      campo('mascota', 'ref', '/mascota'),
    ],
  },
  {
    ruta: '/tratamiento',
    label: 'Tratamientos',
    campos: [
      campo('medicamento'),
      campo('descripcion'),
      campo('costo', 'number'),
      campo('estatus', 'number'),
    ],
  },
  {
    ruta: '/servicio',
    label: 'Servicios',
    campos: [campo('nombre'), campo('costo', 'number')],
  },
  {
    ruta: '/sucursal',
    label: 'Sucursales',
    campos: [campo('direccion', 'ref', '/direccion'), campo('gestor', 'ref', '/gestor')],
  },
  {
    ruta: '/gestor',
    label: 'Gestores',
    campos: [campo('rfc'), campo('salario', 'number'), campo('usuario', 'ref', '/usuario')],
  },
  {
    ruta: '/direccion',
    label: 'Direcciones',
    campos: [campo('codigoPostal', 'number'), campo('calle', 'ref', '/calle')],
  },
  { ruta: '/pais', label: 'Países', campos: [campo('nombre')] },
  {
    ruta: '/estado',
    label: 'Estados',
    campos: [campo('nombre'), campo('pais', 'ref', '/pais')],
  },
  {
    ruta: '/ciudad',
    label: 'Ciudades',
    campos: [campo('nombre'), campo('estado', 'ref', '/estado')],
  },
  {
    ruta: '/colonia',
    label: 'Colonias',
    campos: [campo('nombre'), campo('ciudad', 'ref', '/ciudad')],
  },
  {
    ruta: '/calle',
    label: 'Calles',
    campos: [campo('nombre'), campo('colonia', 'ref', '/colonia')],
  },
  {
    ruta: '/bitacora',
    label: 'Bitácora',
    campos: [
      campo('fechaHora'),
      campo('usuario'),
      campo('rol'),
      campo('accion'),
      campo('modulo'),
      campo('detalle'),
      campo('ip'),
    ],
  },
]

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

export function idDe(row: UnknownRecord, ruta: string): string | number | undefined {
  const key = idKeyOf(row, ruta)
  const id = key ? row[key] : undefined
  return typeof id === 'string' || typeof id === 'number' ? id : undefined
}

/** Tabla a la que apunta una clave anidada: 'usuarioM' → '/usuario' */
function refRuta(key: string): string | undefined {
  const k = key.toLowerCase()
  return crudTables
    .filter((t) => k.startsWith(t.ruta.slice(1)))
    .sort((a, b) => b.ruta.length - a.ruta.length)[0]?.ruta
}

/** Nombre del id dentro de un objeto anidado: '/raza' → 'idRaza' */
function refIdKey(ref: string, anidado?: unknown): string {
  return (
    (isRecord(anidado) ? idKeyOf(anidado, ref) : undefined) ??
    `id${ref.charAt(1).toUpperCase()}${ref.slice(2)}`
  )
}

/**
 * Campos del formulario. Con registros, se deducen de su forma real
 * (los objetos anidados son relaciones); sin registros, se usa el respaldo.
 */
export function camposDe(tabla: CrudTable, rows: UnknownRecord[]): Campo[] {
  if (rows.length === 0) return tabla.campos

  const propio = idKeyOf(rows[0], tabla.ruta)
  const keys = [...new Set(rows.slice(0, 20).flatMap(Object.keys))].filter(
    (k) => k !== propio && !/hmac/i.test(k),
  )

  return keys.flatMap((key): Campo[] => {
    const v = rows.find((r) => r[key] != null)?.[key]
    const ref = refRuta(key)
    const base = { key, label: etiqueta(key) }

    if (isRecord(v) || (v == null && ref)) return ref ? [{ ...base, tipo: 'ref', ref }] : []
    if (typeof v === 'number') return [{ ...base, tipo: 'number' }]
    if (v == null || typeof v === 'string') {
      const s = typeof v === 'string' ? v : ''
      const tipo: Tipo = /contrase|password/i.test(key)
        ? 'password'
        : /^\d{4}-\d{2}-\d{2}$/.test(s)
          ? 'date'
          : /^\d{2}:\d{2}/.test(s)
            ? 'time'
            : 'text'
      return [{ ...base, tipo }]
    }
    return [] // booleanos, arreglos…: no se editan, se conservan tal cual
  })
}

/** Texto de una opción de relación: "#3 Firulais" */
export function rotulo(r: UnknownRecord, ruta: string): string {
  const nombre = [r.nombre, r.paterno, r.materno]
    .filter((x): x is string => typeof x === 'string' && x !== '')
    .join(' ')
  const texto = nombre || firstText(r.fecha, r.rfc, r.motivo, r.medicamento, r.codigoPostal) || ''
  return `#${idDe(r, ruta) ?? '?'} ${texto}`.trim()
}

/** Registro → valores de texto para los inputs (las relaciones quedan como su id) */
export function aValores(campos: Campo[], row?: UnknownRecord): Record<string, string> {
  return Object.fromEntries(
    campos.map((c) => {
      const v = row?.[c.key]
      if (c.tipo === 'password') return [c.key, '']
      if (c.tipo === 'ref') return [c.key, isRecord(v) ? String(idDe(v, c.ref ?? '') ?? '') : '']
      return [c.key, v == null ? '' : String(v)]
    }),
  )
}

/**
 * Valores del formulario → cuerpo para POST/PUT. Al editar parte del registro
 * original, así lo que el formulario no muestra (hmac, listas…) no se pierde.
 */
export function aCuerpo(
  campos: Campo[],
  values: Record<string, string>,
  row?: UnknownRecord,
): UnknownRecord {
  const body: UnknownRecord = { ...row }

  for (const c of campos) {
    const raw = values[c.key] ?? ''
    const v = raw.trim()

    if (c.tipo === 'password') {
      if (v) body[c.key] = raw // vacío: se conserva lo que ya hubiera
    } else if (c.tipo === 'ref') {
      const ref = c.ref ?? ''
      const actual = row?.[c.key]
      body[c.key] =
        v === ''
          ? null
          : isRecord(actual) && String(idDe(actual, ref)) === v
            ? actual
            : { [refIdKey(ref, actual)]: Number(v) }
    } else if (c.tipo === 'number') {
      body[c.key] = v === '' ? null : Number(v)
    } else if (c.tipo === 'text') {
      body[c.key] = raw
    } else {
      body[c.key] = v === '' ? null : v
    }
  }
  return body
}
