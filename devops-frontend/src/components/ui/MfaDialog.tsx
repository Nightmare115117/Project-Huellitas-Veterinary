import { useState } from 'react'
import { alerts } from '../../lib/alerts'
import { ApiError, apiPost } from '../../lib/api'

interface SetupResponse {
  qrUri: string
  secreto: string
}

type Paso = 'inicio' | 'confirmar' | 'desactivar'

const ENCABEZADO: Record<Paso, string> = {
  inicio: 'Verificación en dos pasos',
  confirmar: 'Vincula tu app de autenticación',
  desactivar: 'Desactivar verificación en dos pasos',
}

function mensajeError(e: unknown): string {
  if (e instanceof ApiError && (e.status === 401 || e.status === 403)) {
    return 'Código incorrecto o expirado.'
  }
  if (e instanceof ApiError && e.status === 400) {
    return 'Solicitud inválida: revisa el código o el estado actual de tu MFA.'
  }
  return 'No se pudo completar la operación. Intenta de nuevo.'
}

/** Activa (setup → confirm) o desactiva (disable) el MFA del usuario con sesión iniciada */
export default function MfaDialog({ onClose }: { onClose: () => void }) {
  const [paso, setPaso] = useState<Paso>('inicio')
  const [setup, setSetup] = useState<SetupResponse | null>(null)
  const [codigo, setCodigo] = useState('')
  const [error, setError] = useState('')
  const [enviando, setEnviando] = useState(false)

  const ejecutar = async (accion: () => Promise<void>) => {
    setError('')
    setEnviando(true)
    try {
      await accion()
    } catch (e) {
      setError(mensajeError(e))
    } finally {
      setEnviando(false)
    }
  }

  const iniciar = () =>
    ejecutar(async () => {
      setSetup(await apiPost<object, SetupResponse>('/usuario/mfa/setup', {}))
      setPaso('confirmar')
    })

  const enviarCodigo = (ruta: 'confirm' | 'disable', titulo: string) =>
    ejecutar(async () => {
      await apiPost<{ codigo: string }, unknown>(`/usuario/mfa/${ruta}`, { codigo })
      onClose()
      void alerts.done(titulo)
    })

  const cambiarPaso = (nuevo: Paso) => {
    setPaso(nuevo)
    setCodigo('')
    setError('')
  }

  const campoCodigo = (
    <div className="field">
      <label htmlFor="mfa-codigo">Código de 6 dígitos</label>
      <input
        id="mfa-codigo"
        className="control"
        inputMode="numeric"
        autoComplete="one-time-code"
        placeholder="123456"
        value={codigo}
        onChange={(e) => setCodigo(e.target.value.replace(/\D/g, ''))}
        required
        pattern="\d{6}"
        maxLength={6}
        autoFocus
      />
    </div>
  )

  return (
    <dialog
      className="dialog dialog--sm"
      aria-labelledby="mfa-titulo"
      ref={(el) => {
        if (el && !el.open) el.showModal()
      }}
      onClose={onClose}
      onClick={(e) => e.target === e.currentTarget && onClose()}
    >
      <form
        className="dialog__body"
        onSubmit={(e) => {
          e.preventDefault()
          if (paso === 'confirmar') void enviarCodigo('confirm', 'MFA habilitado')
          if (paso === 'desactivar') void enviarCodigo('disable', 'MFA deshabilitado')
        }}
      >
        <h2 id="mfa-titulo">{ENCABEZADO[paso]}</h2>

        {paso === 'inicio' && (
          <p className="muted">
            Protege tu cuenta con un código temporal de Google Authenticator u otra app TOTP.
          </p>
        )}

        {paso === 'confirmar' && setup && (
          <>
            <p className="muted">
              En tu app agrega una cuenta con esta clave (o abre el enlace desde tu celular) y escribe
              el código que genere.
            </p>
            <p className="mfa__secret" aria-label="Clave secreta">
              {setup.secreto}
            </p>
            <p>
              <a href={setup.qrUri}>Abrir en mi app de autenticación</a>
            </p>
            {campoCodigo}
          </>
        )}

        {paso === 'desactivar' && campoCodigo}

        {error && (
          <p role="alert" className="dialog__error">
            {error}
          </p>
        )}

        <div className="dialog__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cerrar
          </button>
          {paso === 'inicio' ? (
            <>
              <button type="button" className="btn btn--ghost" onClick={() => cambiarPaso('desactivar')}>
                Desactivar
              </button>
              <button type="button" className="btn btn--primary" disabled={enviando} onClick={() => void iniciar()}>
                {enviando ? 'Generando…' : 'Activar'}
              </button>
            </>
          ) : (
            <button type="submit" className="btn btn--primary" disabled={enviando}>
              {enviando ? 'Verificando…' : paso === 'confirmar' ? 'Confirmar' : 'Desactivar'}
            </button>
          )}
        </div>
      </form>
    </dialog>
  )
}
