import { useEffect, useState } from 'react'
import { Building2, ChartColumn, LayoutDashboard, Users } from 'lucide-react'
import AppShell, { type NavItem } from '../components/layout/AppShell'
import PageHeader from '../components/ui/PageHeader'
import Panel from '../components/ui/Panel'
import Reveal from '../components/ui/Reveal'
import { useLogout } from '../hooks/useLogout'
import { useSession } from '../hooks/useSession'
import { alerts } from '../lib/alerts'
import { apiGet } from '../lib/api'
import type { Perfil } from '../types/api'

type Section = 'inicio' | 'usuarios' | 'sucursal' | 'reportes'

const navItems: (NavItem & { id: Section })[] = [
  { id: 'inicio', label: 'Inicio', icon: LayoutDashboard },
  { id: 'usuarios', label: 'Usuarios', icon: Users },
  { id: 'sucursal', label: 'Sucursal', icon: Building2 },
  { id: 'reportes', label: 'Reportes', icon: ChartColumn },
]

const sectionCopy: Record<Section, { title: string; subtitle: string }> = {
  inicio: { title: 'Panel del gestor', subtitle: 'Selecciona una opción del menú para comenzar.' },
  usuarios: { title: 'Usuarios', subtitle: 'Administración de usuarios.' },
  sucursal: { title: 'Sucursal', subtitle: 'Información y administración de la sucursal.' },
  reportes: { title: 'Reportes', subtitle: 'Consulta los reportes de la sucursal.' },
}

export default function MenuGestor() {
  const correo = useSession()
  const handleLogout = useLogout()
  const [gestor, setGestor] = useState<Perfil | null>(null)
  const [active, setActive] = useState<Section>('inicio')

  useEffect(() => {
    if (!correo) return

    apiGet<Perfil>(`/usuario/Nombre/${correo}`)
      .then(setGestor)
      .catch((err: unknown) => {
        console.error(err)
        void alerts.error('No se pudo cargar la información del gestor')
      })
  }, [correo])

  const { title, subtitle } = sectionCopy[active]

  return (
    <AppShell
      roleLabel="Gestor de sucursal"
      userName={gestor?.nombre ?? 'Gestor de sucursal'}
      items={navItems}
      activeId={active}
      onSelect={(id) => setActive(id as Section)}
      onLogout={handleLogout}
    >
      <Reveal key={active}>
        <PageHeader title={title} subtitle={subtitle} />
        {active !== 'inicio' && (
          <Panel>
            <p className="muted">Esta sección estará disponible próximamente.</p>
          </Panel>
        )}
      </Reveal>
    </AppShell>
  )
}
