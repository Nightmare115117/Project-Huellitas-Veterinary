import { useCallback, useEffect, useState } from 'react'
import {
  Building2,
  CalendarDays,
  ChartColumn,
  Check,
  LayoutDashboard,
  PawPrint,
  Pencil,
  Plus,
  Stethoscope,
  Trash2,
  Users,
  X,
} from 'lucide-react'
import AppShell, { type NavItem } from '../components/layout/AppShell'
import DataTable, { type Column } from '../components/ui/DataTable'
import EmptyState from '../components/ui/EmptyState'
import LoadingState from '../components/ui/LoadingState'
import PageHeader from '../components/ui/PageHeader'
import Pagination from '../components/ui/Pagination'
import Panel from '../components/ui/Panel'
import RecordDialog from '../components/ui/RecordDialog'
import Reveal from '../components/ui/Reveal'
import StatCard from '../components/ui/StatCard'
import StatusBadge from '../components/ui/StatusBadge'
import { useLogout } from '../hooks/useLogout'
import { useSession } from '../hooks/useSession'
import { alerts } from '../lib/alerts'
import { apiGet, apiSend } from '../lib/api'
import type { Campo } from '../lib/crud'
import { calcularEdad, dateKey, formatDate, formatMoney, formatTime } from '../lib/format'
import { citaStatus } from '../lib/status'
import type {
  CitaSucursal,
  MascotaSucursal,
  PersonaSucursal,
  Perfil,
  Reportes,
  SucursalInfo,
} from '../types/api'

type Section = 'inicio' | 'citas' | 'usuarios' | 'sucursal' | 'reportes'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'inicio', label: 'Inicio', icon: LayoutDashboard },
  { id: 'citas', label: 'Citas', icon: CalendarDays },
  { id: 'usuarios', label: 'Usuarios', icon: Users },
  { id: 'sucursal', label: 'Sucursal', icon: Building2 },
  { id: 'reportes', label: 'Reportes', icon: ChartColumn },
]

const PAGE_SIZE = 25

const nombreDe = (p: PersonaSucursal) => [p.nombre, p.paterno, p.materno].filter(Boolean).join(' ')

const camposVeterinario: Campo[] = [
  { key: 'nombre', label: 'Nombre', tipo: 'text' },
  { key: 'paterno', label: 'Apellido paterno', tipo: 'text' },
  { key: 'materno', label: 'Apellido materno', tipo: 'text' },
  { key: 'correo', label: 'Correo', tipo: 'text' },
  { key: 'contrasena', label: 'Contraseña', tipo: 'password' },
]

const estadoCell = (c: CitaSucursal) => {
  const { label, tone } = citaStatus(c.estadoCita)
  return <StatusBadge label={label} tone={tone} />
}

const mascotaColumns: Column<MascotaSucursal>[] = [
  { header: 'Nombre', cell: (m) => m.nombre },
  { header: 'Especie', cell: (m) => m.especie },
  { header: 'Raza', cell: (m) => m.raza },
  { header: 'Edad', cell: (m) => calcularEdad(m.fechaNacimiento) },
  { header: 'Dueño', cell: (m) => m.duenoNombre },
]

const duenoColumns: Column<PersonaSucursal>[] = [
  { header: 'Nombre', cell: nombreDe },
  { header: 'Correo', cell: (p) => p.correo },
]

/** Barras horizontales: el valor siempre va en texto, el color no lleva el significado solo */
function Bars({ rows, empty }: { rows: { label: string; value: number }[]; empty: string }) {
  const max = Math.max(1, ...rows.map((r) => r.value))
  if (rows.length === 0) return <EmptyState message={empty} />
  return (
    <ul className="bars">
      {rows.map((r) => (
        <li key={r.label}>
          <span className="bars__label">{r.label}</span>
          <span className="bars__track" aria-hidden>
            <span className="bars__fill" style={{ width: `${(r.value / max) * 100}%` }} />
          </span>
          <strong className="bars__value">{r.value}</strong>
        </li>
      ))}
    </ul>
  )
}

export default function MenuGestor() {
  const correo = useSession()
  const handleLogout = useLogout()
  const base = `/gestor/${encodeURIComponent(correo)}`

  const [active, setActive] = useState<Section>('inicio')
  const [gestorName, setGestorName] = useState('Gestor de sucursal')
  const [loading, setLoading] = useState(true)
  const [sucursal, setSucursal] = useState<SucursalInfo | null>(null)
  const [citas, setCitas] = useState<CitaSucursal[]>([])
  const [veterinarios, setVeterinarios] = useState<PersonaSucursal[]>([])
  const [duenos, setDuenos] = useState<PersonaSucursal[]>([])
  const [mascotas, setMascotas] = useState<MascotaSucursal[]>([])

  // Citas
  const [filtroEstado, setFiltroEstado] = useState('')
  const [filtroFecha, setFiltroFecha] = useState('')
  const [pagina, setPagina] = useState(1)

  // Usuarios
  const [verDuenos, setVerDuenos] = useState(false)
  const [editor, setEditor] = useState<{ row?: PersonaSucursal } | null>(null)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState('')

  // Reportes
  const hoy = dateKey(new Date().toISOString())
  const [desde, setDesde] = useState(`${hoy.slice(0, 8)}01`)
  const [hasta, setHasta] = useState(hoy)
  const [reportes, setReportes] = useState<{ clave: string; data: Reportes | null } | null>(null)

  const recargarCitas = useCallback(
    async () => setCitas(await apiGet<CitaSucursal[]>(`${base}/citas`)),
    [base],
  )
  const recargarVeterinarios = useCallback(
    async () => setVeterinarios(await apiGet<PersonaSucursal[]>(`${base}/veterinarios`)),
    [base],
  )

  useEffect(() => {
    if (!correo) return
    let alive = true

    Promise.all([
      apiGet<Perfil>(`/usuario/Nombre/${correo}`),
      apiGet<SucursalInfo>(`${base}/sucursal`).catch(() => null), // sin sucursal asignada no debe tumbar el resto
      apiGet<CitaSucursal[]>(`${base}/citas`),
      apiGet<PersonaSucursal[]>(`${base}/veterinarios`),
      apiGet<PersonaSucursal[]>(`${base}/duenos`),
      apiGet<MascotaSucursal[]>(`${base}/mascotas`),
    ])
      .then(([perfil, suc, cit, vets, dues, masc]) => {
        if (!alive) return
        setGestorName(perfil?.nombre || 'Gestor de sucursal')
        setSucursal(suc)
        setCitas(Array.isArray(cit) ? cit : [])
        setVeterinarios(Array.isArray(vets) ? vets : [])
        setDuenos(Array.isArray(dues) ? dues : [])
        setMascotas(Array.isArray(masc) ? masc : [])
      })
      .catch((err: unknown) => {
        console.error(err)
        void alerts.error('No se pudo cargar la información de la sucursal')
      })
      .finally(() => alive && setLoading(false))

    return () => {
      alive = false
    }
  }, [correo, base])

  // Los reportes se piden al abrir la sección o al cambiar el rango de fechas
  const clave = `${desde}|${hasta}`
  useEffect(() => {
    if (active !== 'reportes' || !correo || !desde || !hasta) return
    let alive = true
    apiGet<Reportes>(`${base}/reportes?desde=${desde}&hasta=${hasta}`)
      .then((data) => alive && setReportes({ clave, data }))
      .catch(() => alive && setReportes({ clave, data: null }))
    return () => {
      alive = false
    }
  }, [active, correo, base, clave, desde, hasta])

  /* ── Citas ── */
  const cambiarCita = async (c: CitaSucursal, cambio: { idUsuarioV?: number | null; estadoCita?: number }) => {
    try {
      await apiSend('PUT', `${base}/citas/${c.idCita}`, cambio)
      await recargarCitas()
    } catch {
      void alerts.error('No se pudo actualizar la cita.')
    }
  }

  const citasFiltradas = citas
    .filter((c) => (filtroEstado ? String(c.estadoCita) === filtroEstado : true))
    .filter((c) => (filtroFecha ? c.fecha === filtroFecha : true))
    .sort((a, b) => `${b.fecha}${b.entradaAgendada}`.localeCompare(`${a.fecha}${a.entradaAgendada}`))
  const paginaActual = Math.min(pagina, Math.max(1, Math.ceil(citasFiltradas.length / PAGE_SIZE)))
  const citasVisibles = citasFiltradas.slice((paginaActual - 1) * PAGE_SIZE, paginaActual * PAGE_SIZE)

  const citaColumns: Column<CitaSucursal>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Hora', cell: (c) => formatTime(c.entradaAgendada) },
    { header: 'Mascota', cell: (c) => c.mascotaNombre },
    { header: 'Dueño', cell: (c) => c.duenoNombre },
    {
      header: 'Veterinario',
      cell: (c) => (
        <select
          className="control control--inline"
          aria-label={`Veterinario de la cita de ${c.mascotaNombre}`}
          value={c.idVeterinario ?? ''}
          disabled={c.estadoCita === 3}
          onChange={(e) =>
            void cambiarCita(c, { idUsuarioV: e.target.value === '' ? null : Number(e.target.value) })
          }
        >
          <option value="">— Sin asignar —</option>
          {veterinarios.map((v) => (
            <option key={v.idUsuario} value={v.idUsuario}>
              {nombreDe(v)}
            </option>
          ))}
        </select>
      ),
    },
    { header: 'Estado', cell: estadoCell },
    {
      header: 'Acciones',
      cell: (c) => (
        <div className="row-actions">
          <button
            type="button"
            className="icon-btn"
            aria-label={`Confirmar cita de ${c.mascotaNombre}`}
            disabled={c.estadoCita !== 1}
            onClick={() => void cambiarCita(c, { estadoCita: 2 })}
          >
            <Check size={18} aria-hidden />
          </button>
          <button
            type="button"
            className="icon-btn icon-btn--danger"
            aria-label={`Cancelar cita de ${c.mascotaNombre}`}
            disabled={c.estadoCita === 3}
            onClick={() => void cambiarCita(c, { estadoCita: 3 })}
          >
            <X size={18} aria-hidden />
          </button>
        </div>
      ),
    },
  ]

  /* ── Veterinarios ── */
  const cerrarEditor = () => {
    setEditor(null)
    setFormError('')
  }

  const guardarVeterinario = async (v: Record<string, string>, row?: PersonaSucursal) => {
    if (!v.nombre.trim() || !v.paterno.trim() || !v.correo.trim()) {
      return void setFormError('Nombre, apellido paterno y correo son obligatorios.')
    }
    if (!/^\S+@\S+\.\S+$/.test(v.correo.trim())) return void setFormError('El correo no es válido.')
    if (!row && !v.contrasena) return void setFormError('La contraseña es obligatoria al crear.')

    const body = {
      nombre: v.nombre.trim(),
      paterno: v.paterno.trim(),
      materno: v.materno.trim() || null,
      correo: v.correo.trim(),
      ...(v.contrasena ? { contrasena: v.contrasena } : {}), // vacío al editar = no cambiar
    }

    setGuardando(true)
    setFormError('')
    try {
      await (row
        ? apiSend('PUT', `${base}/veterinarios/${row.idUsuario}`, body)
        : apiSend('POST', `${base}/veterinarios`, body))
      await recargarVeterinarios()
      cerrarEditor()
      void alerts.done(row ? 'Veterinario actualizado' : 'Veterinario registrado')
    } catch {
      setFormError('No se pudo guardar. Revisa que el correo no esté registrado.')
    } finally {
      setGuardando(false)
    }
  }

  const eliminarVeterinario = async (p: PersonaSucursal) => {
    if (!(await alerts.confirmDelete())) return
    try {
      await apiSend('DELETE', `${base}/veterinarios/${p.idUsuario}`)
      await Promise.all([recargarVeterinarios(), recargarCitas()])
      void alerts.done('Veterinario eliminado')
    } catch {
      void alerts.error('No se pudo eliminar. Puede tener citas o recetas relacionadas.')
    }
  }

  const veterinarioColumns: Column<PersonaSucursal>[] = [
    ...duenoColumns,
    {
      header: 'Acciones',
      cell: (p) => (
        <div className="row-actions">
          <button
            type="button"
            className="icon-btn"
            aria-label={`Editar a ${nombreDe(p)}`}
            onClick={() => setEditor({ row: p })}
          >
            <Pencil size={18} aria-hidden />
          </button>
          <button
            type="button"
            className="icon-btn icon-btn--danger"
            aria-label={`Eliminar a ${nombreDe(p)}`}
            onClick={() => void eliminarVeterinario(p)}
          >
            <Trash2 size={18} aria-hidden />
          </button>
        </div>
      ),
    },
  ]

  /* ── Inicio ── */
  const activas = citas.filter((c) => c.estadoCita !== 3)
  const proximas = activas
    .filter((c) => c.fecha != null && c.fecha >= hoy)
    .sort((a, b) => `${a.fecha}${a.entradaAgendada}`.localeCompare(`${b.fecha}${b.entradaAgendada}`))
    .slice(0, 5)

  const proximaColumns: Column<CitaSucursal>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Hora', cell: (c) => formatTime(c.entradaAgendada) },
    { header: 'Mascota', cell: (c) => c.mascotaNombre },
    { header: 'Veterinario', cell: (c) => c.veterinarioNombre ?? 'Sin asignar' },
    { header: 'Estado', cell: estadoCell },
  ]

  const rep = reportes?.clave === clave ? reportes.data : undefined // undefined = cargando

  return (
    <AppShell
      roleLabel="Gestor de sucursal"
      userName={gestorName}
      items={navItems}
      activeId={active}
      onSelect={(id) => setActive(id as Section)}
      onLogout={handleLogout}
    >
      {loading ? (
        <LoadingState message="Cargando información de la sucursal…" />
      ) : (
        <Reveal key={active}>
          {active === 'inicio' && (
            <>
              <PageHeader title="Panel del gestor" subtitle="Resumen de tu sucursal" />
              <div className="stats">
                <StatCard title="Mascotas" value={mascotas.length} detail="Registradas en la sucursal" icon={PawPrint} accent="blue" />
                <StatCard
                  title="Citas hoy"
                  value={activas.filter((c) => c.fecha === hoy).length}
                  detail="Sin contar canceladas"
                  icon={CalendarDays}
                  accent="red"
                />
                <StatCard
                  title="Sin veterinario"
                  value={activas.filter((c) => c.idVeterinario == null).length}
                  detail="Citas por asignar"
                  icon={Stethoscope}
                  accent="blue"
                />
                <StatCard title="Veterinarios" value={veterinarios.length} detail="Equipo de la sucursal" icon={Users} accent="red" />
              </div>
              <Panel title="Próximas citas">
                <DataTable
                  caption="Próximas citas"
                  columns={proximaColumns}
                  rows={proximas}
                  getRowKey={(c) => c.idCita}
                  emptyMessage="No hay citas próximas."
                />
              </Panel>
            </>
          )}

          {active === 'citas' && (
            <>
              <PageHeader title="Citas" subtitle="Asigna veterinario y confirma o cancela las citas de la sucursal" />
              <Panel>
                <div className="crud-bar">
                  <div className="field">
                    <label htmlFor="citas-estado">Estado</label>
                    <select
                      id="citas-estado"
                      className="control"
                      value={filtroEstado}
                      onChange={(e) => {
                        setFiltroEstado(e.target.value)
                        setPagina(1)
                      }}
                    >
                      <option value="">Todos</option>
                      <option value="1">Programada</option>
                      <option value="2">Confirmada</option>
                      <option value="3">Cancelada</option>
                    </select>
                  </div>
                  <div className="field">
                    <label htmlFor="citas-fecha">Fecha</label>
                    <input
                      id="citas-fecha"
                      type="date"
                      className="control"
                      value={filtroFecha}
                      onChange={(e) => {
                        setFiltroFecha(e.target.value)
                        setPagina(1)
                      }}
                    />
                  </div>
                </div>
                <DataTable
                  compact
                  caption="Citas de la sucursal"
                  columns={citaColumns}
                  rows={citasVisibles}
                  getRowKey={(c) => c.idCita}
                  emptyMessage="No hay citas con esos filtros."
                />
                <Pagination page={paginaActual} pageSize={PAGE_SIZE} total={citasFiltradas.length} onChange={setPagina} />
              </Panel>
            </>
          )}

          {active === 'usuarios' && (
            <>
              <PageHeader
                title="Usuarios"
                subtitle="Veterinarios y dueños de tu sucursal"
                action={
                  !verDuenos && (
                    <button type="button" className="btn btn--primary" onClick={() => setEditor({})}>
                      <Plus size={20} aria-hidden />
                      Nuevo veterinario
                    </button>
                  )
                }
              />
              <Panel>
                <div className="crud-bar">
                  <div className="field">
                    <label htmlFor="usuarios-tipo">Mostrar</label>
                    <select
                      id="usuarios-tipo"
                      className="control"
                      value={verDuenos ? 'duenos' : 'veterinarios'}
                      onChange={(e) => setVerDuenos(e.target.value === 'duenos')}
                    >
                      <option value="veterinarios">Veterinarios</option>
                      <option value="duenos">Dueños de mascotas</option>
                    </select>
                  </div>
                </div>
                {verDuenos ? (
                  <DataTable
                    caption="Dueños de mascotas"
                    columns={duenoColumns}
                    rows={duenos}
                    getRowKey={(p) => p.idUsuario}
                    emptyMessage="Aún no hay dueños registrados en la sucursal."
                  />
                ) : (
                  <DataTable
                    caption="Veterinarios"
                    columns={veterinarioColumns}
                    rows={veterinarios}
                    getRowKey={(p) => p.idUsuario}
                    emptyMessage="Aún no has registrado veterinarios."
                  />
                )}
              </Panel>
            </>
          )}

          {active === 'sucursal' && (
            <>
              <PageHeader title="Sucursal" subtitle="Información de la sucursal y sus mascotas" />
              <Panel title="Datos de la sucursal">
                {sucursal ? (
                  <dl className="detail-list">
                    <div>
                      <dt>Calle</dt>
                      <dd>{sucursal.calle}</dd>
                    </div>
                    <div>
                      <dt>Colonia</dt>
                      <dd>{sucursal.colonia}</dd>
                    </div>
                    <div>
                      <dt>Ciudad</dt>
                      <dd>{`${sucursal.ciudad}, ${sucursal.estado}, ${sucursal.pais}`}</dd>
                    </div>
                    <div>
                      <dt>Código postal</dt>
                      <dd>{sucursal.codigoPostal}</dd>
                    </div>
                    <div>
                      <dt>RFC del gestor</dt>
                      <dd>{sucursal.rfcGestor}</dd>
                    </div>
                  </dl>
                ) : (
                  <EmptyState message="No hay una sucursal asignada a tu usuario." />
                )}
              </Panel>
              <Panel title="Mascotas de la sucursal">
                <DataTable
                  compact
                  caption="Mascotas de la sucursal"
                  columns={mascotaColumns}
                  rows={mascotas}
                  getRowKey={(m) => m.idMascota}
                  emptyMessage="No hay mascotas registradas en la sucursal."
                />
              </Panel>
            </>
          )}

          {active === 'reportes' && (
            <>
              <PageHeader title="Reportes" subtitle="Actividad e ingresos de la sucursal por periodo" />
              <Panel>
                <div className="crud-bar">
                  <div className="field">
                    <label htmlFor="rep-desde">Desde</label>
                    <input
                      id="rep-desde"
                      type="date"
                      className="control"
                      value={desde}
                      max={hasta}
                      onChange={(e) => setDesde(e.target.value)}
                    />
                  </div>
                  <div className="field">
                    <label htmlFor="rep-hasta">Hasta</label>
                    <input
                      id="rep-hasta"
                      type="date"
                      className="control"
                      value={hasta}
                      min={desde}
                      onChange={(e) => setHasta(e.target.value)}
                    />
                  </div>
                </div>
              </Panel>

              {rep === undefined ? (
                <LoadingState message="Generando reporte…" />
              ) : rep === null ? (
                <Panel>
                  <div role="alert">
                    <EmptyState message="No se pudo generar el reporte para ese periodo." />
                  </div>
                </Panel>
              ) : (
                <>
                  <div className="stats">
                    <StatCard title="Citas" value={rep.totalCitas} detail="En el periodo" icon={CalendarDays} accent="blue" />
                    <StatCard title="Servicios" value={formatMoney(rep.ingresosServicios)} detail="Ingresos por servicios" icon={Stethoscope} accent="red" />
                    <StatCard title="Tratamientos" value={formatMoney(rep.ingresosTratamientos)} detail="Ingresos por recetas" icon={ChartColumn} accent="blue" />
                    <StatCard
                      title="Total"
                      value={formatMoney(rep.ingresosServicios + rep.ingresosTratamientos)}
                      detail="Servicios + tratamientos"
                      icon={Building2}
                      accent="red"
                    />
                  </div>
                  <Panel title="Citas por estado">
                    <Bars
                      empty="Sin citas en el periodo."
                      rows={rep.citasPorEstado.map((e) => ({ label: citaStatus(e.estado).label, value: e.total }))}
                    />
                  </Panel>
                  <Panel title="Citas por veterinario">
                    <Bars
                      empty="Sin citas asignadas en el periodo."
                      rows={rep.citasPorVeterinario.map((v) => ({ label: v.veterinario, value: v.total }))}
                    />
                  </Panel>
                  <Panel title="Servicios más solicitados">
                    <DataTable
                      caption="Servicios más solicitados"
                      columns={[
                        { header: 'Servicio', cell: (s) => s.nombre },
                        { header: 'Veces', cell: (s) => s.total },
                        { header: 'Ingreso', cell: (s) => formatMoney(s.ingreso) },
                      ]}
                      rows={rep.serviciosTop}
                      getRowKey={(s) => s.nombre}
                      emptyMessage="Sin servicios en el periodo."
                    />
                  </Panel>
                </>
              )}
            </>
          )}
        </Reveal>
      )}

      {editor && (
        <RecordDialog
          titulo={`${editor.row ? 'Editar' : 'Nuevo'} veterinario`}
          editando={!!editor.row}
          campos={camposVeterinario}
          initial={{
            nombre: editor.row?.nombre ?? '',
            paterno: editor.row?.paterno ?? '',
            materno: editor.row?.materno ?? '',
            correo: editor.row?.correo ?? '',
            contrasena: '',
          }}
          opciones={{}}
          guardando={guardando}
          error={formError}
          onSubmit={(v) => void guardarVeterinario(v, editor.row)}
          onClose={cerrarEditor}
        />
      )}
    </AppShell>
  )
}
