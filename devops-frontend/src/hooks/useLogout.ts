import { useNavigate } from 'react-router-dom'
import { alerts } from '../lib/alerts'
import { clearSession } from '../lib/session'

export function useLogout(): () => Promise<void> {
  const navigate = useNavigate()

  return async () => {
    if (!(await alerts.confirmLogout())) return
    clearSession()
    navigate('/')
    void alerts.loggedOut()
  }
}
