import { useEffect, useRef, useState } from 'react'

interface TurnstileApi {
  render: (
    container: HTMLElement,
    options: {
      sitekey: string
      action: 'login'
      callback: (token: string) => void
      'error-callback': () => void
      'expired-callback': () => void
    },
  ) => string
  remove: (widgetId: string) => void
}

declare global {
  interface Window {
    turnstile?: TurnstileApi
  }
}

let scriptPromise: Promise<void> | undefined

function loadTurnstile(): Promise<void> {
  if (window.turnstile) return Promise.resolve()
  if (!scriptPromise) {
    scriptPromise = new Promise((resolve, reject) => {
      const script = document.createElement('script')
      script.src = 'https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit'
      script.async = true
      script.defer = true
      script.onload = () => resolve()
      script.onerror = () => {
        scriptPromise = undefined
        reject(new Error('No se pudo cargar Cloudflare Turnstile'))
      }
      document.head.appendChild(script)
    })
  }
  return scriptPromise
}

interface TurnstileWidgetProps {
  onTokenChange: (token: string) => void
}

export default function TurnstileWidget({ onTokenChange }: TurnstileWidgetProps) {
  const containerRef = useRef<HTMLDivElement>(null)
  const [widgetError, setWidgetError] = useState('')
  const siteKey = import.meta.env.VITE_TURNSTILE_SITE_KEY?.trim() ?? ''

  useEffect(() => {
    if (!siteKey) return

    let widgetId: string | undefined
    let disposed = false

    void loadTurnstile()
      .then(() => {
        if (disposed || !containerRef.current || !window.turnstile) return
        widgetId = window.turnstile.render(containerRef.current, {
          sitekey: siteKey,
          action: 'login',
          callback: (token) => {
            setWidgetError('')
            onTokenChange(token)
          },
          'error-callback': () => {
            onTokenChange('')
            setWidgetError('No se pudo completar el CAPTCHA. Intenta de nuevo.')
          },
          'expired-callback': () => onTokenChange(''),
        })
      })
      .catch(() => {
        if (!disposed) setWidgetError('No se pudo cargar el CAPTCHA. Recarga la página o intenta más tarde.')
      })

    return () => {
      disposed = true
      if (widgetId && window.turnstile) window.turnstile.remove(widgetId)
    }
  }, [onTokenChange, siteKey])

  return (
    <div className="turnstile">
      {siteKey ? (
        <div ref={containerRef} />
      ) : (
        <p role="alert" className="turnstile__error">
          CAPTCHA sin configurar. Contacta al administrador.
        </p>
      )}
      {widgetError && <p role="alert" className="turnstile__error">{widgetError}</p>}
    </div>
  )
}
