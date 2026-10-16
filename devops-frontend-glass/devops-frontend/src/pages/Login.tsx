import { useRef, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { animate } from 'animejs'
import { Eye, EyeOff, Lock, Mail } from 'lucide-react'
import Brand from '../components/layout/Brand'
import { useSeasonalMusic } from '../hooks/useSeasonalMusic'
import { useStaggerIn } from '../hooks/useStaggerIn'
import { alerts } from '../lib/alerts'
import { apiGet, apiPost } from '../lib/api'
import { prefersReducedMotion } from '../lib/motion'
import { setCorreo } from '../lib/session'
import { Rol, type LoginResponse } from '../types/api'

interface LoginBody {
  correo: string
  contrasena: string // sin ñ: así lo espera el backend
}

const rutaPorRol: Record<Rol, string> = {
  [Rol.Administrador]: '/MainMenuAdministrador',
  [Rol.Dueno]: '/MainMenu/',
  [Rol.Gestor]: '/MainMenuGestor',
}

const ENTRADA = '.login__brand .brand, .login__hero > *, .login-card'

function esRol(value: unknown): value is Rol {
  return typeof value === 'number' && value in rutaPorRol
}

export default function Login() {
  const [user, setUser] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [mostrar, setMostrar] = useState(false)
  const [enviando, setEnviando] = useState(false)
  const navigate = useNavigate()

  useSeasonalMusic()
  const rootRef = useStaggerIn<HTMLDivElement>(ENTRADA)
  const formRef = useRef<HTMLFormElement>(null)

  /** Sacudida horizontal: comunica que las credenciales fallaron */
  const sacudir = () => {
    const form = formRef.current
    if (!form || prefersReducedMotion()) return
    animate(form, {
      x: [0, -8, 8, -5, 5, 0],
      duration: 380,
      ease: 'outQuad',
      onComplete: () => form.style.removeProperty('transform'),
    })
  }

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    setEnviando(true)

    try {
      const data = await apiPost<LoginBody, LoginResponse>('/usuario/login', {
        correo: user,
        contrasena,
      })

      if (data.login !== true) {
        sacudir()
        void alerts.loginError()
        return
      }

      setCorreo(user)
      void alerts.loginOk()

      const rol = await apiGet<unknown>(`/usuario/Rol?correo=${encodeURIComponent(user)}`)
      if (esRol(rol)) navigate(rutaPorRol[rol])
    } catch (error) {
      console.error('Error:', error)
      sacudir()
      void alerts.loginError()
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="login" ref={rootRef}>
      <section className="login__brand">
        <Brand size={64} />
        <div className="login__hero">
          <h1>Cada huella cuenta una historia de cuidado.</h1>
          <p>Gestiona mascotas, citas y tratamientos desde un solo lugar.</p>
        </div>
      </section>

      <main className="login__panel">
        <form className="login-card" ref={formRef} onSubmit={handleSubmit}>
          <div className="login-card__logo">
            <Brand showName={false} size={72} />
          </div>
          <h2>Iniciar sesión</h2>
          <p className="login-card__lead">Accede con tu correo y contraseña.</p>

          <div className="field">
            <label htmlFor="correo">Correo</label>
            <div className="field__control">
              <Mail size={18} aria-hidden />
              <input
                id="correo"
                type="email"
                autoComplete="email"
                placeholder="nombre@correo.com"
                value={user}
                onChange={(e) => setUser(e.target.value)}
                required
                maxLength={254}
              />
            </div>
          </div>

          <div className="field">
            <label htmlFor="contrasena">Contraseña</label>
            <div className="field__control">
              <Lock size={18} aria-hidden />
              <input
                id="contrasena"
                type={mostrar ? 'text' : 'password'}
                autoComplete="current-password"
                placeholder="8 a 16 caracteres"
                value={contrasena}
                onChange={(e) => setContrasena(e.target.value)}
                required
                minLength={8}
                maxLength={16}
              />
              <button
                type="button"
                className="field__toggle"
                aria-label={mostrar ? 'Ocultar contraseña' : 'Mostrar contraseña'}
                aria-pressed={mostrar}
                onClick={() => setMostrar((v) => !v)}
              >
                {mostrar ? <EyeOff size={18} aria-hidden /> : <Eye size={18} aria-hidden />}
              </button>
            </div>
          </div>

          <button type="submit" className="btn btn--primary btn--block" disabled={enviando}>
            {enviando ? (
              <>
                <span className="spinner spinner--sm" aria-hidden /> Entrando…
              </>
            ) : (
              'Entrar'
            )}
          </button>
        </form>
      </main>
    </div>
  )
}
