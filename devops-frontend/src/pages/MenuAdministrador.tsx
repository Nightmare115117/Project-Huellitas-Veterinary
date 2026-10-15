import { useEffect, useState } from 'react'
import {
  CalendarDays,
  LayoutDashboard,
  PawPrint,
  Pill,
  Stethoscope,
  Users,
  type LucideIcon,
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
import {
  isStaff,
  toAppointment,
  toPet,
  toTreatment,
  toUser,
  type AdminAppointment,
  type AdminPet,
  type AdminTreatment,
  type AdminUser,
  type StaffMember,
} from '../lib/adminMappers'
import { apiGetOr } from '../lib/api'
import { formatDate, formatTime } from '../lib/format'
import { extractArray } from '../lib/normalize'
import type { Perfil } from '../types/api'

type Section = 'inicio' | 'usuarios' | 'mascotas' | 'citas' | 'tratamientos' | 'personal'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'inicio', label: 'Inicio', icon: LayoutDashboard },
  { id: 'usuarios', label: 'Usuarios', icon: Users },
  { id: 'mascotas', label: 'Mascotas', icon: PawPrint },
  { id: 'citas', label: 'Citas', icon: CalendarDays },
  { id: 'tratamientos', label: 'Tratamientos', icon: Pill },
  { id: 'personal', label: 'Personal', icon: Stethoscope },
]

interface RecentActivity {
  user: string
  action: string
  date: string
  status: string
}

interface SummaryCard {
  title: string
  value: number
  detail: string
  icon: LucideIcon
  accent: 'blue' | 'red'
}

interface Dashboard {
  users: AdminUser[]
  pets: AdminPet[]
  appointments: AdminAppointment[]
  treatments: AdminTreatment[]
  staff: StaffMember[]
  recentActivity: RecentActivity[]
  summaryCards: SummaryCard[]
}

const emptyDashboard: Dashboard = {
  users: [],
  pets: [],
  appointments: [],
  treatments: [],
  staff: [],
  recentActivity: [],
  summaryCards: [],
}

const statusCell = <T extends { status: string }>(row: T) => <StatusBadge label={row.status} />

const userColumns: Column<AdminUser>[] = [
  { header: 'Nombre', cell: (u) => u.name },
  { header: 'Correo', cell: (u) => u.email },
  { header: 'Rol', cell: (u) => u.role },
  { header: 'Estado', cell: statusCell },
]

const petColumns: Column<AdminPet>[] = [
  { header: 'Nombre', cell: (p) => p.name },
  { header: 'Especie', cell: (p) => p.species },
  { header: 'Raza', cell: (p) => p.breed },
  { header: 'Dueño', cell: (p) => p.owner },
]

const appointmentColumns: Column<AdminAppointment>[] = [
  { header: 'Fecha', cell: (a) => formatDate(a.date) },
  { header: 'Hora', cell: (a) => formatTime(a.time) },
  { header: 'Mascota', cell: (a) => a.pet },
  { header: 'Dueño', cell: (a) => a.owner },
  { header: 'Estado', cell: statusCell },
]

const treatmentColumns: Column<AdminTreatment>[] = [
  { header: 'Mascota', cell: (t) => t.pet },
  { header: 'Medicamento', cell: (t) => t.medicine },
  { header: 'Descripción', cell: (t) => t.description },
  { header: 'Estatus', cell: statusCell },
]

const staffColumns: Column<StaffMember>[] = [
  { header: 'Nombre', cell: (m) => m.name },
  { header: 'Rol', cell: (m) => m.role },
  { header: 'Turno', cell: (m) => m.shift || '-' },
  { header: 'Estado', cell: statusCell },
]

const activityColumns: Column<RecentActivity>[] = [
  { header: 'Usuario', cell: (e) => e.user },
  { header: 'Actividad', cell: (e) => e.action },
  { header: 'Fecha', cell: (e) => formatDate(e.date) },
  { header: 'Estado', cell: statusCell },
]

export default function MenuAdministrador() {
  const correo = useSession()
  const handleLogout = useLogout()

  const [active, setActive] = useState<Section>('inicio')
  const [adminName, setAdminName] = useState('Administrador')
  const [loading, setLoading] = useState(true)
  const [dashboard, setDashboard] = useState<Dashboard>(emptyDashboard)

  useEffect(() => {
    if (!correo) return
    let isMounted = true

    const load = async () => {
      try {
        const [perfil, usuariosRaw, mascotasRaw, citasRaw, tratamientosRaw] = await Promise.all([
          apiGetOr<Perfil | null>(`/usuario/Nombre/${correo}`, null),
          apiGetOr<unknown>('/usuario', []),
          apiGetOr<unknown>('/mascota', []),
          apiGetOr<unknown>('/cita', []),
          apiGetOr<unknown>('/tratamiento', []),
        ])
        if (!isMounted) return

        const users = extractArray(usuariosRaw, 'usuarios').map(toUser)
        const pets = extractArray(mascotasRaw, 'mascotas').map(toPet)
        const appointments = extractArray(citasRaw, 'citas').map(toAppointment)
        const treatments = extractArray(tratamientosRaw, 'tratamientos').map(toTreatment)

        const pendingAppointments = appointments.filter(
          (a) => a.status === 'Pendiente' || a.status === 'Programada',
        ).length
        const activeTreatments = treatments.filter((t) => t.status === 'Activo').length

        setAdminName(perfil?.nombre || perfil?.name || 'Administrador')
        setDashboard({
          users,
          pets,
          appointments,
          treatments,
          staff: users.filter(isStaff),
          recentActivity: appointments.slice(0, 3).map((a) => ({
            user: a.owner,
            action: `Solicitó una cita para ${a.pet}`,
            date: a.date,
            status: a.status,
          })),
          summaryCards: [
            { title: 'Usuarios', value: users.length, detail: 'Usuarios registrados', icon: Users, accent: 'blue' },
            { title: 'Mascotas', value: pets.length, detail: 'Mascotas registradas', icon: PawPrint, accent: 'red' },
            { title: 'Citas', value: pendingAppointments, detail: 'Citas pendientes', icon: CalendarDays, accent: 'blue' },
            { title: 'Tratamientos', value: activeTreatments, detail: 'Tratamientos activos', icon: Pill, accent: 'red' },
          ],
        })
      } catch (error) {
        console.error(error)
        if (isMounted) setDashboard(emptyDashboard)
      } finally {
        if (isMounted) setLoading(false)
      }
    }

    void load()
    return () => {
      isMounted = false
    }
  }, [correo])

  return (
    <AppShell
      roleLabel="Administrador"
      userName={adminName}
      items={navItems}
      activeId={active}
      onSelect={(id) => setActive(id as Section)}
      onLogout={handleLogout}
    >
      {loading ? (
        <LoadingState message="Cargando información del administrador…" />
      ) : (
        <Reveal key={active}>
          {active === 'inicio' && (
            <>
              <PageHeader title="Resumen del sistema" subtitle="Vista general de Huellitas" />
              <div className="stats">
                {dashboard.summaryCards.map((card) => (
                  <StatCard key={card.title} {...card} />
                ))}
              </div>
              <Panel title="Actividad reciente">
                <DataTable
                  caption="Actividad reciente"
                  columns={activityColumns}
                  rows={dashboard.recentActivity}
                  getRowKey={(e, i) => `${e.user}-${e.date}-${i}`}
                  emptyMessage="No hay actividad reciente para mostrar."
                />
              </Panel>
            </>
          )}

          {active === 'usuarios' && (
            <>
              <PageHeader title="Usuarios" subtitle="Administración de usuarios registrados" />
              <Panel>
                <DataTable
                  caption="Usuarios registrados"
                  columns={userColumns}
                  rows={dashboard.users}
                  getRowKey={(u) => `${u.email}-${u.name}`}
                  emptyMessage="No se encontraron usuarios."
                />
              </Panel>
            </>
          )}

          {active === 'mascotas' && (
            <>
              <PageHeader title="Mascotas" subtitle="Mascotas registradas en el sistema" />
              <Panel>
                <DataTable
                  caption="Mascotas registradas"
                  columns={petColumns}
                  rows={dashboard.pets}
                  getRowKey={(p, i) => `${p.name}-${p.owner}-${i}`}
                  emptyMessage="No se encontraron mascotas."
                />
              </Panel>
            </>
          )}

          {active === 'citas' && (
            <>
              <PageHeader title="Citas" subtitle="Gestión de citas veterinarias" />
              <Panel>
                <DataTable
                  caption="Citas veterinarias"
                  columns={appointmentColumns}
                  rows={dashboard.appointments}
                  getRowKey={(a, i) => `${a.pet}-${a.date}-${i}`}
                  emptyMessage="No hay citas registradas."
                />
              </Panel>
            </>
          )}

          {active === 'tratamientos' && (
            <>
              <PageHeader title="Tratamientos" subtitle="Administración de tratamientos" />
              <Panel>
                <DataTable
                  caption="Tratamientos"
                  columns={treatmentColumns}
                  rows={dashboard.treatments}
                  getRowKey={(t, i) => `${t.pet}-${t.medicine}-${i}`}
                  emptyMessage="No hay tratamientos registrados."
                />
              </Panel>
            </>
          )}

          {active === 'personal' && (
            <>
              <PageHeader title="Personal" subtitle="Equipo de atención veterinaria" />
              <Panel>
                <DataTable
                  caption="Personal de la clínica"
                  columns={staffColumns}
                  rows={dashboard.staff}
                  getRowKey={(m) => `${m.name}-${m.role}`}
                  emptyMessage="No hay personal disponible."
                />
              </Panel>
            </>
          )}
        </Reveal>
      )}
    </AppShell>
  )
}
