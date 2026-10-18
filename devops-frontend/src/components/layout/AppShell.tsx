import { useEffect, useState, type ReactNode } from 'react'
import { LogOut, Menu, X, type LucideIcon } from 'lucide-react'
import { getCorreo } from '../../lib/session'
import Brand from './Brand'

export interface NavItem {
  id: string
  label: string
  icon: LucideIcon
}

interface AppShellProps {
  roleLabel: string
  userName: string
  items: NavItem[]
  activeId: string
  onSelect: (id: string) => void
  onLogout: () => void
  children: ReactNode
}

export default function AppShell({
  roleLabel,
  userName,
  items,
  activeId,
  onSelect,
  onLogout,
  children,
}: AppShellProps) {
  const [menuOpen, setMenuOpen] = useState(false)

  useEffect(() => {
    if (!menuOpen) return
    const onKey = (e: KeyboardEvent) => e.key === 'Escape' && setMenuOpen(false)
    document.addEventListener('keydown', onKey)
    return () => document.removeEventListener('keydown', onKey)
  }, [menuOpen])

  const correo = getCorreo()
  const initial = userName.trim().charAt(0).toUpperCase() || '?'

  return (
    <div className="shell">
      <a className="skip-link" href="#contenido">
        Saltar al contenido
      </a>

      <header className="topbar">
        <Brand />
        <button
          type="button"
          className="icon-btn"
          aria-label={menuOpen ? 'Cerrar menú' : 'Abrir menú'}
          aria-expanded={menuOpen}
          aria-controls="sidebar"
          onClick={() => setMenuOpen((open) => !open)}
        >
          {menuOpen ? <X size={22} aria-hidden /> : <Menu size={22} aria-hidden />}
        </button>
      </header>

      {menuOpen && (
        <button
          type="button"
          className="scrim"
          aria-label="Cerrar menú"
          onClick={() => setMenuOpen(false)}
        />
      )}

      <aside id="sidebar" className="sidebar" data-open={menuOpen}>
        <div className="sidebar__brand">
          <Brand />
        </div>

        <div className="user-card">
          <span className="user-card__avatar" aria-hidden>
            {initial}
          </span>
          <div className="user-card__text">
            <strong title={userName}>{userName}</strong>
            <span title={correo}>{correo}</span>
          </div>
        </div>

        <nav className="nav" aria-label={`Menú de ${roleLabel.toLowerCase()}`}>
          {items.map(({ id, label, icon: Icon }) => (
            <button
              key={id}
              type="button"
              className="nav__item"
              aria-current={activeId === id ? 'page' : undefined}
              onClick={() => {
                onSelect(id)
                setMenuOpen(false)
              }}
            >
              <Icon size={20} aria-hidden />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        <button type="button" className="logout" onClick={onLogout}>
          <LogOut size={20} aria-hidden />
          <span>Cerrar sesión</span>
        </button>
      </aside>

      <main id="contenido" className="content" tabIndex={-1}>
        {children}
      </main>
    </div>
  )
}
