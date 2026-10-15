import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { alerts } from '../lib/alerts'
import { getCorreo } from '../lib/session'

/** Devuelve el correo de la sesión; si no existe avisa y redirige al login. */
export function useSession(): string {
  const navigate = useNavigate()
  const correo = getCorreo()

  useEffect(() => {
    if (correo) return
    void alerts.sessionExpired().then(() => navigate('/'))
  }, [correo, navigate])

  return correo
}
