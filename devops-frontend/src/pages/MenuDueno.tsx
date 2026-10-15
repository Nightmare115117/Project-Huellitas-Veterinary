import { useEffect, useState } from 'react'
import {
  CalendarDays,
  ClipboardList,
  House,
  PawPrint,
  Pill,
  UserRound,
} from 'lucide-react'
import AppShell, { type NavItem } from '../components/layout/AppShell'
import DataTable, { type Column } from '../components/ui/DataTable'
import LoadingState from '../components/ui/LoadingState'
import PageHeader from '../components/ui/PageHeader'
import Reveal from '../components/ui/Reveal'
import Panel from '../components/ui/Panel'
import StatCard from '../components/ui/StatCard'
import StatusBadge from '../components/ui/StatusBadge'
import { useLogout } from '../hooks/useLogout'
import { useSession } from '../hooks/useSession'
import { apiGet } from '../lib/api'
import { calcularEdad, formatDate, formatTime } from '../lib/format'
import { citaStatus } from '../lib/status'
import type { Cita, Mascota, Perfil } from '../types/api'

type Section = 'inicio' | 'mascotas' | 'citas' | 'tratamientos' | 'historial' | 'perfil'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'inicio', label: 'Inicio', icon: House },
  { id: 'mascotas', label: 'Mis mascotas', icon: PawPrint },
  { id: 'citas', label: 'Citas', icon: CalendarDays },
  { id: 'tratamientos', label: 'Tratamientos', icon: Pill },
  { id: 'historial', label: 'Historial', icon: ClipboardList },
  { id: 'perfil', label: 'Perfil', icon: UserRound },
]

function especieDe(m: Mascota): string {
  if (typeof m.raza === 'object') return m.raza.especie?.nombre ?? m.raza.nombre ?? m.especie ?? 'Sin especie'
  return m.especie ?? 'Sin especie'
}

function razaDe(m: Mascota): string {
  if (typeof m.raza === 'object') return m.raza.nombre ?? 'Sin raza'
  return m.raza ?? m.breed ?? 'Sin raza'
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
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!correo) return
    let isMounted = true

    const load = async () => {
      try {
        const [perfil, mascotasData, citasData] = await Promise.all([
          apiGet<Perfil>(`/usuario/Nombre/${correo}`),
          apiGet<Mascota[]>(`/mascota/${correo}`),
          apiGet<Cita[]>(`/cita/${correo}`),
        ])
        if (!isMounted) return
        setOwnerName(perfil?.nombre || 'Dueño')
        setMascotas(Array.isArray(mascotasData) ? mascotasData : [])
        setCitas(Array.isArray(citasData) ? citasData : [])
      } catch (error) {
        console.error(error)
        if (isMounted) {
          setOwnerName('Dueño')
          setMascotas([])
          setCitas([])
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
  const petName = pet?.nombre || (mascotas.length > 0 ? 'Mis mascotas' : 'Mi mascota')
  const petListSummary =
    mascotas.length > 0 ? mascotas.map((m) => m.nombre || 'Mascota').join(', ') : 'Sin mascotas'
  const nextCita =
    [...citas].sort((a, b) => new Date(a.fecha ?? '').getTime() - new Date(b.fecha ?? '').getTime())[0] ?? null

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
    { header: 'Motivo', cell: () => 'Consulta veterinaria' },
    { header: 'Doctor', cell: (c) => c.nombreVeterinario || 'Pendiente' },
    { header: 'Estado', cell: citaStatusCell },
  ]

  const historialColumns: Column<Cita>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Tipo', cell: () => 'Consulta' },
    { header: 'Veterinario', cell: (c) => c.nombreVeterinario || 'Pendiente' },
    { header: 'Resultado', cell: citaStatusCell },
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
                {/* TODO: dato de ejemplo, falta un endpoint de tratamientos para el dueño */}
                <StatCard
                  icon={Pill}
                  title="Tratamiento"
                  value={pet ? 'Activo' : 'Sin dato'}
                  detail={pet ? 'Revisión y cuidado continuo' : 'No hay información disponible'}
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
              <PageHeader title="Citas" subtitle="Tus próximas citas veterinarias" />
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
                {/* TODO: dato de ejemplo (igual que en la versión JSX): reemplazar por datos reales */}
                <DataTable
                  caption="Tratamientos actuales"
                  columns={[
                    { header: 'Medicamento', cell: () => 'Antiinflamatorio' },
                    { header: 'Descripción', cell: () => 'Seguimiento veterinario' },
                    { header: 'Frecuencia', cell: () => 'Cada 12 horas' },
                    { header: 'Estado', cell: () => <StatusBadge label="Activo" tone="success" /> },
                  ]}
                  rows={pet ? [pet] : []}
                  getRowKey={(p) => p.idMascota ?? p.nombre ?? 'tratamiento'}
                  emptyMessage="Sin información de tratamientos."
                />
              </Panel>
            </>
          )}

          {active === 'historial' && (
            <>
              <PageHeader title="Historial" subtitle={`Registros médicos de ${petName}`} />
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
    </AppShell>
  )
}
