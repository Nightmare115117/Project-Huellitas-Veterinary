const KEY = 'Usuario'

/** Correo guardado en sessionStorage (limpia comillas sobrantes). "" si no hay sesión. */
export function getCorreo(): string {
  return sessionStorage.getItem(KEY)?.replace(/^"|"$/g, '') ?? ''
}

export function setCorreo(correo: string): void {
  sessionStorage.setItem(KEY, correo)
}

export function clearSession(): void {
  sessionStorage.clear()
}
