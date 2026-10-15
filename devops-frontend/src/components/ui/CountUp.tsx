import { useLayoutEffect, useRef } from 'react'
import { animate } from 'animejs'
import { prefersReducedMotion } from '../../lib/motion'

interface CountUpProps {
  value: number
  /** ms */
  duration?: number
}

/** Cuenta de 0 al valor. Lectores de pantalla reciben solo el valor final. */
export default function CountUp({ value, duration = 600 }: CountUpProps) {
  const ref = useRef<HTMLSpanElement>(null)

  useLayoutEffect(() => {
    const el = ref.current
    if (!el || value === 0 || prefersReducedMotion()) return

    const counter = { n: 0 }
    el.textContent = '0'

    const animation = animate(counter, {
      n: value,
      duration,
      ease: 'outExpo',
      onUpdate: () => {
        el.textContent = String(Math.round(counter.n))
      },
    })

    return () => {
      animation.cancel()
      el.textContent = String(value)
    }
  }, [value, duration])

  return (
    <>
      <span ref={ref} aria-hidden>
        {value}
      </span>
      <span className="sr-only">{value}</span>
    </>
  )
}
