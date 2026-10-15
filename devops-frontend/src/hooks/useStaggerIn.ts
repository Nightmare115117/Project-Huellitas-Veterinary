import { useLayoutEffect, useRef, type RefObject } from 'react'
import { animate, stagger } from 'animejs'
import { clearMotionStyles, prefersReducedMotion } from '../lib/motion'

/**
 * Entrada escalonada (fade + subida) de los elementos que coincidan con `selector`
 * dentro del contenedor devuelto por la ref. Solo anima transform/opacity.
 */
export function useStaggerIn<T extends HTMLElement>(selector: string): RefObject<T | null> {
  const ref = useRef<T>(null)

  useLayoutEffect(() => {
    const root = ref.current
    if (!root || prefersReducedMotion()) return

    // offsetParent === null → elemento oculto con display:none (no se anima)
    const targets = Array.from(root.querySelectorAll<HTMLElement>(selector)).filter(
      (el) => el.offsetParent !== null,
    )
    if (targets.length === 0) return

    // Se ocultan antes del primer pintado para evitar el parpadeo
    for (const el of targets) el.style.opacity = '0'

    const animation = animate(targets, {
      opacity: [0, 1],
      y: [12, 0],
      duration: 320,
      ease: 'outCubic',
      delay: stagger(40),
      onComplete: () => clearMotionStyles(targets),
    })

    return () => {
      animation.cancel()
      clearMotionStyles(targets)
    }
  }, [selector])

  return ref
}
