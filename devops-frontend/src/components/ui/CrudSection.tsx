import { useEffect, useState } from 'react'
import { Pencil, Plus, Trash2 } from 'lucide-react'
import DataTable, { type Column } from './DataTable'
import EmptyState from './EmptyState'
import LoadingState from './LoadingState'
import Panel from './Panel'
import { alerts } from '../../lib/alerts'
import { ApiError, apiGet, apiSend } from '../../lib/api'
import { crudTables, idKeyOf } from '../../lib/crud'
import { extractArray, firstText, isRecord } from '../../lib/normalize'
import type { UnknownRecord } from '../../types/api'

interface Loaded {
  ruta: string
  rows: UnknownRecord[]
  error: string
}

function errorText(e: unknown): string {
  if (!(e instanceof ApiError)) return 'No se pudo conectar con el servidor.'
  if (e.status === 401 || e.status === 403) return `Sin permiso (${e.status}). Revisa la sesión y el rol.`
  return `El servidor respondió ${e.status}.`
}

function show(v: unknown): string {
  if (v === null || v === undefined || v === '') return '-'
  if (typeof v === 'boolean') return v ? 'Sí' : 'No'
  if (isRecord(v)) return firstText(v.nombre, v.name) ?? JSON.stringify(v)
  return typeof v === 'object' ? JSON.stringify(v) : String(v)
}

/** Para crear: las claves de la primera fila sin su id; objetos anidados como ejemplo, el resto en null. */
function template(sample: UnknownRecord | undefined, ruta: string): UnknownRecord {
  if (!sample) return {}
  const idKey = idKeyOf(sample, ruta)
  return Object.fromEntries(
    Object.entries(sample)
      .filter(([k]) => k !== idKey)
      .map(([k, v]) => [k, isRecord(v) ? v : null]),
  )
}

export default function CrudSection() {
  const [ruta, setRuta] = useState<string>(crudTables[0].ruta)
  const [data, setData] = useState<Loaded | null>(null)
  const [version, setVersion] = useState(0)

  useEffect(() => {
    let alive = true
    apiGet<unknown>(ruta)
      .then((raw) => alive && setData({ ruta, rows: extractArray(raw, ruta.slice(1)), error: '' }))
      .catch((e: unknown) => alive && setData({ ruta, rows: [], error: errorText(e) }))
    return () => {
      alive = false
    }
  }, [ruta, version])

  const loading = data?.ruta !== ruta
  const rows = data?.rows ?? []
  const reload = () => setVersion((v) => v + 1)

  const idOf = (row: UnknownRecord): string | number | undefined => {
    const key = idKeyOf(row, ruta)
    const id = key ? row[key] : undefined
    return typeof id === 'string' || typeof id === 'number' ? id : undefined
  }

  const save = async (row?: UnknownRecord) => {
    const id = row ? idOf(row) : undefined
    if (row && id === undefined) return void alerts.error('No encontré la clave primaria de este registro.')

    const initial = row ?? template(rows[0], ruta)
    const body = await alerts.editJson(row ? 'Editar registro' : 'Nuevo registro', JSON.stringify(initial, null, 2))
    if (!body) return

    try {
      await (row ? apiSend('PUT', `${ruta}/${id}`, body) : apiSend('POST', ruta, body))
      reload()
      void alerts.done(row ? 'Registro actualizado' : 'Registro creado')
    } catch (e) {
      void alerts.error(errorText(e))
    }
  }

  const remove = async (row: UnknownRecord) => {
    const id = idOf(row)
    if (id === undefined) return void alerts.error('No encontré la clave primaria de este registro.')
    if (!(await alerts.confirmDelete())) return

    try {
      await apiSend('DELETE', `${ruta}/${id}`)
      reload()
      void alerts.done('Registro eliminado')
    } catch (e) {
      void alerts.error(`${errorText(e)} Puede tener registros relacionados.`)
    }
  }

  // Nunca se muestran contraseñas, aunque el backend las devuelva
  const keys = [...new Set(rows.slice(0, 20).flatMap(Object.keys))].filter(
    (k) => !/contrase|password/i.test(k),
  )

  const columns: Column<UnknownRecord>[] = [
    ...keys.map((k) => ({
      header: k,
      cell: (r: UnknownRecord) => (
        <span className="crud-cell" title={show(r[k])}>
          {show(r[k])}
        </span>
      ),
    })),
    {
      header: 'Acciones',
      cell: (r) => (
        <div className="row-actions">
          <button type="button" className="icon-btn" aria-label="Editar registro" onClick={() => void save(r)}>
            <Pencil size={18} aria-hidden />
          </button>
          <button
            type="button"
            className="icon-btn icon-btn--danger"
            aria-label="Eliminar registro"
            onClick={() => void remove(r)}
          >
            <Trash2 size={18} aria-hidden />
          </button>
        </div>
      ),
    },
  ]

  return (
    <Panel>
      <div className="crud-bar">
        <div className="field">
          <label htmlFor="crud-tabla">Tabla</label>
          <select id="crud-tabla" className="select" value={ruta} onChange={(e) => setRuta(e.target.value)}>
            {crudTables.map((t) => (
              <option key={t.ruta} value={t.ruta}>
                {t.label}
              </option>
            ))}
          </select>
        </div>
        <button type="button" className="btn btn--primary" onClick={() => void save()}>
          <Plus size={20} aria-hidden />
          Nuevo registro
        </button>
      </div>

      {loading ? (
        <LoadingState message="Cargando registros…" />
      ) : data?.error ? (
        <div role="alert">
          <EmptyState message={data.error} />
        </div>
      ) : (
        <DataTable
          caption={`Registros de ${crudTables.find((t) => t.ruta === ruta)?.label ?? ruta}`}
          columns={columns}
          rows={rows}
          getRowKey={(r, i) => idOf(r) ?? i}
          emptyMessage="No hay registros en esta tabla."
        />
      )}
    </Panel>
  )
}
