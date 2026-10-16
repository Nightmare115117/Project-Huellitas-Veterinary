import { useEffect } from 'react'

const base = import.meta.env.BASE_URL

/** Índice de mes (0 = enero) → archivo en /public/Music */
const tracksByMonth: Partial<Record<number, string>> = {
  1: `${base}Music/TANK QUINCEAÑERA (PARODIA).mp3`,
  2: `${base}Music/La mejor música de elevador o ascensor.mp3`,
  7: `${base}Music/Marcha militar alemana _Erika_ - Subtitulado en Español(MP3_160K).mp3`,
  11: `${base}Music/Sleigh Ride (Indian Christmas Remix) - Vindaloo Singh.mp3`,
}

/** Reproduce música de temporada mientras el componente está montado (tras el primer clic/tecla). */
export function useSeasonalMusic(): void {
  useEffect(() => {
    const src = tracksByMonth[new Date().getMonth()]
    if (!src) return

    const audio = new Audio(src)
    audio.loop = true
    audio.volume = 0.5

    function removeListeners() {
      document.removeEventListener('click', start)
      document.removeEventListener('keydown', start)
    }

    function start() {
      audio.play().catch((err: unknown) => console.log('Error al reproducir audio:', err))
      removeListeners()
    }

    document.addEventListener('click', start)
    document.addEventListener('keydown', start)

    return () => {
      audio.pause()
      audio.currentTime = 0
      removeListeners()
    }
  }, [])
}
