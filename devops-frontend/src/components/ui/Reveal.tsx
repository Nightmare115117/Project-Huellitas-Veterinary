import type { ReactNode } from 'react'
import { useStaggerIn } from '../../hooks/useStaggerIn'

/** Encabezado → tarjetas de resumen (una a una) → panel */
const SELECTOR = ':scope > *:not(.stats), :scope > .stats > *'

/**
 * Anima su contenido al montarse. Úsalo con `key` para repetir la
 * animación al cambiar de sección: <Reveal key={active}>…</Reveal>
 */
export default function Reveal({ children }: { children: ReactNode }) {
  const ref = useStaggerIn<HTMLDivElement>(SELECTOR)
  return <div ref={ref}>{children}</div>
}
