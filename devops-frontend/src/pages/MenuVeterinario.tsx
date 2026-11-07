import { useCallback, useEffect, useState } from 'react'
import { CalendarDays, Eye, FilePlus2, FileText } from 'lucide-react'
import AppShell, { type NavItem } from '../components/layout/AppShell'
import DataTable, { type Column } from '../components/ui/DataTable'
import LoadingState from '../components/ui/LoadingState'
import PageHeader from '../components/ui/PageHeader'
import Panel from '../components/ui/Panel'
import RecetaDialog from '../components/ui/RecetaDialog'
import RecetaView from '../components/ui/RecetaView'
import Reveal from '../components/ui/Reveal'
import StatusBadge from '../components/ui/StatusBadge'
import { useLogout } from '../hooks/useLogout'
import { useSession } from '../hooks/useSession'
import { alerts } from '../lib/alerts'
import { apiGet, apiSend } from '../lib/api'
import { formatDate, formatTime } from '../lib/format'
import { citaStatus } from '../lib/status'
import type { CitaVeterinario, Perfil, Receta, RecetaNueva } from '../types/api'

type Section = 'citas' | 'recetas'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'citas', label: 'Mis citas', icon: CalendarDays },
  { id: 'recetas', label: 'Recetas', icon: FileText },
]

/** Vista del veterinario: solo ve sus citas y genera recetas. No edita nada más. */
export default function MenuVeterinario() {
  const correo = useSession()
  const handleLogout = useLogout()
  const base = `/veterinario/${encodeURIComponent(correo)}`

  const [active, setActive] = useState<Section>('citas')
  const [nombre, setNombre] = useState('Veterinario')
  const [loading, setLoading] = useState(true)
  const [citas, setCitas] = useState<CitaVeterinario[]>([])
  const [recetas, setRecetas] = useState<Receta[]>([])
  const [nueva, setNueva] = useState<CitaVeterinario | null>(null)
  const [viendo, setViendo] = useState<Receta | null>(null)
  const [guardando, setGuardando] = useState(false)
  const [formError, setFormError] = useState('')

  const recargar = useCallback(async () => {
    const [cit, rec] = await Promise.all([
      apiGet<CitaVeterinario[]>(`${base}/citas`),
      apiGet<Receta[]>(`${base}/recetas`),
    ])
    setCitas(Array.isArray(cit) ? cit : [])
    setRecetas(Array.isArray(rec) ? rec : [])
  }, [base])

  useEffect(() => {
    if (!correo) return
    let alive = true

    Promise.all([apiGet<Perfil>(`/usuario/Nombre/${correo}`), recargar()])
      .then(([perfil]) => alive && setNombre(perfil?.nombre || 'Veterinario'))
      .catch((err: unknown) => {
        console.error(err)
        void alerts.error('No se pudo cargar tu información')
      })
      .finally(() => alive && setLoading(false))

    return () => {
      alive = false
    }
  }, [correo, recargar])

  const cerrarNueva = () => {
    setNueva(null)
    setFormError('')
  }

  // POST /veterinario/{correo}/recetas: el backend crea consulta + tratamientos en una transacción
  const generar = async (receta: RecetaNueva) => {
    setGuardando(true)
    setFormError('')
    try {
      await apiSend('POST', `${base}/recetas`, receta)
      await recargar()
      cerrarNueva()
      setActive('recetas')
      void alerts.done('Receta generada')
    } catch {
      setFormError('No se pudo guardar la receta. Verifica que la cita no tenga ya una receta.')
    } finally {
      setGuardando(false)
    }
  }

  const citaColumns: Column<CitaVeterinario>[] = [
    { header: 'Fecha', cell: (c) => formatDate(c.fecha) },
    { header: 'Hora', cell: (c) => formatTime(c.entradaAgendada) },
    { header: 'Paciente', cell: (c) => `${c.mascotaNombre} (${c.especie})` },
    { header: 'Dueño', cell: (c) => c.duenoNombre },
    {
      header: 'Estado',
      cell: (c) => {
        const { label, tone } = citaStatus(c.estadoCita)
        return <StatusBadge label={label} tone={tone} />
      },
    },
    {
      header: 'Receta',
      cell: (c) =>
        c.tieneReceta ? (
          <StatusBadge label="Completado" tone="success" />
        ) : (
          <button
            type="button"
            className="btn btn--primary"
            disabled={c.estadoCita === 3}
            aria-label={`Generar receta para ${c.mascotaNombre}`}
            onClick={() => setNueva(c)}
          >
            <FilePlus2 size={18} aria-hidden />
            Generar receta
          </button>
        ),
    },
  ]

  const recetaColumns: Column<Receta>[] = [
    { header: 'Fecha', cell: (r) => formatDate(r.fecha) },
    { header: 'Paciente', cell: (r) => r.mascotaNombre },
    { header: 'Diagnóstico', cell: (r) => <span className="crud-cell" title={r.diagnostico}>{r.diagnostico}</span> },
    { header: 'Medicamentos', cell: (r) => r.tratamientos.map((t) => t.medicamento).join(', ') },
    {
      header: 'Ver',
      cell: (r) => (
        <button type="button" className="icon-btn" aria-label={`Ver receta de ${r.mascotaNombre}`} onClick={() => setViendo(r)}>
          <Eye size={18} aria-hidden />
        </button>
      ),
    },
  ]

  return (
    <AppShell
      roleLabel="Veterinario"
      userName={nombre}
      items={navItems}
      activeId={active}
      onSelect={(id) => setActive(id as Section)}
      onLogout={handleLogout}
    >
      {loading ? (
        <LoadingState message="Cargando tus citas…" />
      ) : (
        <Reveal key={active}>
          {active === 'citas' && (
            <>
              <PageHeader title="Mis citas" subtitle="Genera la receta médica de cada consulta" />
              <Panel>
                <DataTable
                  caption="Citas asignadas"
                  columns={citaColumns}
                  rows={citas}
                  getRowKey={(c) => c.idCita}
                  emptyMessage="No tienes citas asignadas."
                />
              </Panel>
            </>
          )}

          {active === 'recetas' && (
            <>
              <PageHeader title="Recetas" subtitle="Recetas que has generado" />
              <Panel>
                <DataTable
                  caption="Recetas generadas"
                  columns={recetaColumns}
                  rows={recetas}
                  getRowKey={(r) => r.idConsulta}
                  emptyMessage="Todavía no has generado recetas."
                />
              </Panel>
            </>
          )}
        </Reveal>
      )}

      {nueva && <RecetaDialog cita={nueva} guardando={guardando} error={formError} onSubmit={(r) => void generar(r)} onClose={cerrarNueva} />}
      {viendo && <RecetaView receta={viendo} veterinario={nombre} onClose={() => setViendo(null)} />}
    </AppShell>
  )
}
