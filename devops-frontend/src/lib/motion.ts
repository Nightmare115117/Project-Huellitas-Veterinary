/** true si el usuario pidió reducir el movimiento (accesibilidad) */
export function prefersReducedMotion(): boolean {
  return window.matchMedia('(prefers-reduced-motion: reduce)').matches
}

/** Quita los estilos inline que deja anime.js para que vuelva a mandar el CSS (p. ej. :hover) */
export function clearMotionStyles(elements: HTMLElement[]): void {
  for (const el of elements) {
    el.style.removeProperty('opacity')
    el.style.removeProperty('transform')
  }
}
