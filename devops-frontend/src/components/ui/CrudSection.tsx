import { useEffect, useState } from 'react'
import { Pencil, Plus, Trash2 } from 'lucide-react'
import DataTable, { type Column } from './DataTable'
import EmptyState from './EmptyState'
import LoadingState from './LoadingState'
import Panel from './Panel'
import RecordDialog from './RecordDialog'
import { alerts } from '../../lib/alerts'
import { ApiError, apiGet, apiSend } from '../../lib/api'
import {
  aCuerpo,
  aValores,
  camposDe,
  crudTables,
  idDe,
  rotulo,
  type Campo,
  type Opcion,
} from '../../lib/crud'
import { extractArray, firstText, isRecord } from '../../lib/normalize'
import type { UnknownRecord } from '../../types/api'

interface Loaded {
  ruta: string
  rows: UnknownRecord[]
  error: string
}

/** Formulario abierto: `row` solo existe al editar */
interface Editor {
  row?: UnknownRecord
  campos: Campo[]
  initial: Record<string, string>
  opciones: Record<string, Opcion[]>
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

export default function CrudSection() {
  const [ruta, setRuta] = useState<string>(crudTables[0].ruta)
  const [data, setData] = useState<Loaded | null>(null)
  const [version, setVersion] = useState(0)
  const [editor, setEditor] = useState<Editor | null>(null)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState('')

  useEffect(() => {
    let alive = true
    apiGet<unknown>(ruta)
      .then((raw) => alive && setData({ ruta, rows: extractArray(raw, ruta.slice(1)), error: '' }))
      .catch((e: unknown) => alive && setData({ ruta, rows: [], error: errorText(e) }))
    return () => {
      alive = false
    }
  }, [ruta, version])

  const tabla = crudTables.find((t) => t.ruta === ruta) ?? crudTables[0]
  const loading = data?.ruta !== ruta
  const rows = data?.rows ?? []
  const reload = () => setVersion((v) => v + 1)

  /** Abre el formulario; antes carga las tablas relacionadas para los selectores */
  const abrir = async (row?: UnknownRecord) => {
    if (row && idDe(row, ruta) === undefined) {
      return void alerts.error('No encontré la clave primaria de este registro.')
    }

    const campos = camposDe(tabla, rows)
    const refs = [...new Set(campos.flatMap((c) => (c.tipo === 'ref' && c.ref ? [c.ref] : [])))]

    try {
      const listas = await Promise.all(refs.map((r) => apiGet<unknown>(r)))
      const opciones = Object.fromEntries(
        refs.map((r, i) => [
          r,
          extractArray(listas[i], r.slice(1)).flatMap((x) => {
            const id = idDe(x, r)
            return id === undefined ? [] : [{ value: String(id), label: rotulo(x, r) }]
          }),
        ]),
      )
      setFormError('')
      setEditor({ row, campos, opciones, initial: aValores(campos, row) })
    } catch (e) {
      void alerts.error(errorText(e))
    }
  }

  const guardar = async (ed: Editor, values: Record<string, string>) => {
    const body = aCuerpo(ed.campos, values, ed.row)
    setGuardando(true)
    setFormError('')
    try {
      await (ed.row
        ? apiSend('PUT', `${ruta}/${idDe(ed.row, ruta)}`, body)
        : apiSend('POST', ruta, body))
      setEditor(null)
      reload()
      void alerts.done(ed.row ? 'Registro actualizado' : 'Registro creado')
    } catch (e) {
      setFormError(errorText(e))
    } finally {
      setGuardando(false)
    }
  }

  const remove = async (row: UnknownRecord) => {
    const id = idDe(row, ruta)
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
          <button type="button" className="icon-btn" aria-label="Editar registro" onClick={() => void abrir(r)}>
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
          <select id="crud-tabla" className="control" value={ruta} onChange={(e) => setRuta(e.target.value)}>
            {crudTables.map((t) => (
              <option key={t.ruta} value={t.ruta}>
                {t.label}
              </option>
            ))}
          </select>
        </div>
        <button type="button" className="btn btn--primary" onClick={() => void abrir()}>
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
          caption={`Registros de ${tabla.label}`}
          columns={columns}
          rows={rows}
          getRowKey={(r, i) => idDe(r, ruta) ?? i}
          emptyMessage="No hay registros en esta tabla."
        />
      )}

      {editor && (
        <RecordDialog
          titulo={`${editor.row ? 'Editar' : 'Nuevo'} · ${tabla.label}`}
          editando={!!editor.row}
          campos={editor.campos}
          initial={editor.initial}
          opciones={editor.opciones}
          guardando={guardando}
          error={formError}
          onSubmit={(values) => void guardar(editor, values)}
          onClose={() => setEditor(null)}
        />
      )}
    </Panel>
  )
}
