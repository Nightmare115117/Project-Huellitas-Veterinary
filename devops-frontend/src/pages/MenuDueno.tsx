import { useEffect, useState } from 'react'
import {
  CalendarDays,
  ClipboardList,
  House,
  PawPrint,
  Pill,
  Plus,
  UserRound,
} from 'lucide-react'
import AppShell, { type NavItem } from '../components/layout/AppShell'
import DataTable, { type Column } from '../components/ui/DataTable'
import LoadingState from '../components/ui/LoadingState'
import PageHeader from '../components/ui/PageHeader'
import RecordDialog from '../components/ui/RecordDialog'
import Reveal from '../components/ui/Reveal'
import Panel from '../components/ui/Panel'
import StatCard from '../components/ui/StatCard'
import StatusBadge from '../components/ui/StatusBadge'
import { useLogout } from '../hooks/useLogout'
import { useSession } from '../hooks/useSession'
import { alerts } from '../lib/alerts'
import { apiGet, apiSend } from '../lib/api'
import type { Campo } from '../lib/crud'
import { calcularEdad, dateKey, formatDate, formatMoney, formatTime } from '../lib/format'
import { citaStatus, tratamientoStatus } from '../lib/status'
import type { Cita, Mascota, Perfil, Tratamiento } from '../types/api'

type Section = 'inicio' | 'mascotas' | 'citas' | 'tratamientos' | 'historial' | 'perfil'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'inicio', label: 'Inicio', icon: House },
  { id: 'mascotas', label: 'Mis mascotas', icon: PawPrint },
  { id: 'citas', label: 'Citas', icon: CalendarDays },
  { id: 'tratamientos', label: 'Tratamientos', icon: Pill },
  { id: 'historial', label: 'Historial', icon: ClipboardList },
  { id: 'perfil', label: 'Perfil', icon: UserRound },
]

const especieDe = (m: Mascota) => m.raza?.especie?.nombre ?? 'Sin especie'
const razaDe = (m: Mascota) => m.raza?.nombre ?? 'Sin raza'

const camposCita: Campo[] = [
  { key: 'mascota', label: 'Mascota', tipo: 'ref', ref: '/mascota' },
  { key: 'fecha', label: 'Fecha', tipo: 'date' },
  { key: 'hora', label: 'Hora', tipo: 'time' },
]

const tratamientoStatusCell = (t: Tratamiento) => {
  const { label, tone } = tratamientoStatus(t.estatus)
  return <StatusBadge label={label} tone={tone} />
}

const citaStatusCell = (c: Cita) => {
  const { label, tone } = citaStatus(c.estadoCita)
  return <StatusBadge label={label} tone={tone} />
}

export default function MenuDueno() {
  const correo = useSession()
  const handleLogout = useLogout()

  const [active, setActive] = useState<Section>('inicio')
  const [ownerName, setOwnerName] = useState('Dueño')
  const [mascotas, setMascotas] = useState<Mascota[]>([])
  const [citas, setCitas] = useState<Cita[]>([])
  const [tratamientos, setTratamientos] = useState<Tratamiento[]>([])
  const [loading, setLoading] = useState(true)
  const [nuevaCita, setNuevaCita] = useState(false)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState('')

  useEffect(() => {
    if (!correo) return
    let isMounted = true

    const load = async () => {
      try {
        const [perfil, mascotasData, citasData, tratamientosData] = await Promise.all([
          apiGet<Perfil>(`/usuario/Nombre/${correo}`),
          apiGet<Mascota[]>(`/mascota/${correo}`),
          apiGet<Cita[]>(`/cita/${correo}`),
          apiGet<Tratamiento[]>(`/tratamiento/usuario/${correo}`),
        ])
        if (!isMounted) return
        setOwnerName(perfil?.nombre || 'Dueño')
        setMascotas(Array.isArray(mascotasData) ? mascotasData : [])
        setCitas(Array.isArray(citasData) ? citasData : [])
        setTratamientos(Array.isArray(tratamientosData) ? tratamientosData : [])
      } catch (error) {
        console.error(error)
        if (isMounted) {
          setOwnerName('Dueño')
          setMascotas([])
          setCitas([])
          setTratamientos([])
        }
      } finally {
        if (isMounted) setLoading(false)
      }
    }

    void load()
    return () => {
      isMounted = false
    }
  }, [correo])

  const pet = mascotas[0] ?? null
  const petListSummary =
    mascotas.length > 0 ? mascotas.map((m) => m.nombre || 'Mascota').join(', ') : 'Sin mascotas'
  const mascotaDe = (idMascota: number) => mascotas.find((m) => m.idMascota === idMascota)?.nombre ?? '--'
  // La próxima cita es la más cercana desde hoy que no esté cancelada (antes salía la más antigua)
  const hoy = dateKey(new Date().toISOString())
  const nextCita =
    citas
      .filter((c) => c.estadoCita !== 3 && c.fecha != null && c.fecha >= hoy)
      .sort((a, b) => `${a.fecha}${a.entradaAgendada}`.localeCompare(`${b.fecha}${b.entradaAgendada}`))[0] ?? null
  const cerrarNuevaCita = () => {
    setNuevaCita(false)
    setFormError('')
  }

  // POST /cita/crear: el backend la guarda como Programada (1) y el veterinario se asigna después
  const crearCita = async (v: Record<string, string>) => {
    if (!v.mascota || !v.fecha || !v.hora) return void setFormError('Completa mascota, fecha y hora.')
    if (v.fecha < hoy) return void setFormError('La fecha no puede ser anterior a hoy.')

    setGuardando(true)
    setFormError('')
    try {
      await apiSend('POST', '/cita/crear', {
        correoCliente: correo,
        idMascota: Number(v.mascota),
        fecha: v.fecha,
        entradaAgendada: v.hora,
        estadoCita: 1,
      })
      setCitas(await apiGet<Cita[]>(`/cita/${correo}`))
      cerrarNuevaCita()
      void alerts.done('Cita creada')
    } catch {
      setFormError('No se pudo crear la cita. Intenta de nuevo.')
    } finally {
      setGuardando(false)
    }
  }

  const tratamientosActivos = tratamientos.filter((t) => t.estatus === 0).length

  const mascotaColumns: Column<Mascota>[] = [
    { header: 'Nombre', cell: (m) => m.nombre },
    { header: 'Especie', cell: especieDe },
    { header: 'Raza', cell: razaDe },
    { header: 'Edad', cell: (m) => calcularEdad(m.fechaNacimiento) },
    { header: 'Fecha de nacimiento', cell: (m) => formatDate(m.fechaNacimiento) },
  ]

  const resumenCitaColumns: Column<Cita>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Hora', cell: (c) => formatTime(c.entradaAgendada) },
    { header: 'Veterinario', cell: (c) => c.nombreVeterinario || 'Pendiente de asignación' },
    { header: 'Estado', cell: citaStatusCell },
  ]

  const citaColumns: Column<Cita>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Hora', cell: (c) => formatTime(c.entradaAgendada) },
    { header: 'Mascota', cell: (c) => mascotaDe(c.idMascota) },
    { header: 'Doctor', cell: (c) => c.nombreVeterinario || 'Pendiente' },
    { header: 'Estado', cell: citaStatusCell },
  ]

  const tratamientoColumns: Column<Tratamiento>[] = [
    { header: 'Medicamento', cell: (t) => t.medicamento },
    { header: 'Descripción', cell: (t) => t.descripcion },
    { header: 'Mascota', cell: (t) => t.mascotaNombre },
    { header: 'Costo', cell: (t) => formatMoney(t.costo) },
    { header: 'Estado', cell: tratamientoStatusCell },
  ]

  const historialColumns: Column<Cita>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Mascota', cell: (c) => mascotaDe(c.idMascota) },
    { header: 'Veterinario', cell: (c) => c.nombreVeterinario || 'Pendiente' },
    { header: 'Estado', cell: citaStatusCell },
  ]

  return (
    <AppShell
      roleLabel="Dueño de mascota"
      userName={ownerName}
      items={navItems}
      activeId={active}
      onSelect={(id) => setActive(id as Section)}
      onLogout={handleLogout}
    >
      {loading ? (
        <LoadingState message="Cargando tu información…" />
      ) : (
        <Reveal key={active}>
          {active === 'inicio' && (
            <>
              <PageHeader title="Panel del dueño" subtitle="Resumen de tus mascotas y sus cuidados" />

              <div className="stats">
                <StatCard
                  icon={PawPrint}
                  title="Mis mascotas"
                  value={mascotas.length}
                  detail={mascotas.length > 0 ? petListSummary : 'Sin mascotas registradas'}
                />
                <StatCard
                  icon={CalendarDays}
                  accent="red"
                  title="Próxima cita"
                  value={nextCita ? formatDate(nextCita.fecha) : '--'}
                  detail={
                    nextCita
                      ? `${formatTime(nextCita.entradaAgendada)} · ${nextCita.nombreVeterinario || 'Veterinario'}`
                      : 'Sin citas registradas'
                  }
                />
                <StatCard
                  icon={Pill}
                  title="Tratamientos activos"
                  value={tratamientosActivos}
                  detail={tratamientos.length > 0 ? `${tratamientos.length} registrados` : 'Sin tratamientos registrados'}
                />
                <StatCard
                  icon={ClipboardList}
                  accent="red"
                  title="Historial"
                  value={citas.length}
                  detail="Eventos registrados"
                />
              </div>

              <Panel title="Actividad reciente">
                <DataTable
                  caption="Últimas citas"
                  columns={resumenCitaColumns}
                  rows={citas.slice(0, 3)}
                  getRowKey={(c) => c.idCita}
                  emptyMessage="No hay citas registradas para este usuario."
                />
              </Panel>
            </>
          )}

          {active === 'mascotas' && (
            <>
              <PageHeader title="Mis mascotas" subtitle="Información principal de tus mascotas" />
              <Panel>
                <DataTable
                  caption="Mis mascotas"
                  columns={mascotaColumns}
                  rows={mascotas}
                  getRowKey={(m) => m.idMascota ?? `${m.nombre}-${m.fechaNacimiento}`}
                  emptyMessage="No se encontró ninguna mascota asociada a este usuario."
                />
              </Panel>
            </>
          )}

          {active === 'citas' && (
            <>
              <PageHeader
                title="Citas"
                subtitle="Tus próximas citas veterinarias"
                action={
                  <button
                    type="button"
                    className="btn btn--primary"
                    disabled={mascotas.length === 0}
                    title={mascotas.length === 0 ? 'Necesitas una mascota registrada' : undefined}
                    onClick={() => setNuevaCita(true)}
                  >
                    <Plus size={20} aria-hidden />
                    Nueva cita
                  </button>
                }
              />
              <Panel>
                <DataTable
                  caption="Mis citas"
                  columns={citaColumns}
                  rows={citas}
                  getRowKey={(c) => c.idCita}
                  emptyMessage="Todavía no tienes citas registradas."
                />
              </Panel>
            </>
          )}

          {active === 'tratamientos' && (
            <>
              <PageHeader title="Tratamientos" subtitle="Medicamentos y cuidados actuales" />
              <Panel>
                <DataTable
                  caption="Tratamientos"
                  columns={tratamientoColumns}
                  rows={tratamientos}
                  getRowKey={(t, i) => `${t.idTratamiento}-${i}`}
                  emptyMessage="Sin información de tratamientos."
                />
              </Panel>
            </>
          )}

          {active === 'historial' && (
            <>
              <PageHeader title="Historial" subtitle="Citas registradas de tus mascotas" />
              <Panel>
                <DataTable
                  caption="Historial médico"
                  columns={historialColumns}
                  rows={citas}
                  getRowKey={(c) => `historial-${c.idCita}`}
                  emptyMessage="Aún no hay historial médico disponible."
                />
              </Panel>
            </>
          )}

          {active === 'perfil' && (
            <>
              <PageHeader title="Perfil" subtitle="Datos del propietario" />
              <Panel>
                <dl className="detail-list">
                  <div>
                    <dt>Nombre</dt>
                    <dd>{ownerName}</dd>
                  </div>
                  <div>
                    <dt>Correo</dt>
                    <dd>{correo}</dd>
                  </div>
                  <div>
                    <dt>Mascotas</dt>
                    <dd>{petListSummary}</dd>
                  </div>
                  <div>
                    <dt>Especie</dt>
                    <dd>{pet ? especieDe(pet) : 'Sin especie'}</dd>
                  </div>
                </dl>
              </Panel>
            </>
          )}
        </Reveal>
      )}
      {nuevaCita && (
        <RecordDialog
          titulo="Nueva cita"
          editando={false}
          campos={camposCita}
          initial={{ mascota: mascotas.length === 1 ? String(mascotas[0].idMascota) : '', fecha: '', hora: '' }}
          opciones={{ '/mascota': mascotas.map((m) => ({ value: String(m.idMascota), label: m.nombre })) }}
          guardando={guardando}
          error={formError}
          onSubmit={(v) => void crearCita(v)}
          onClose={cerrarNuevaCita}
        />
      )}
    </AppShell>
  )
}
