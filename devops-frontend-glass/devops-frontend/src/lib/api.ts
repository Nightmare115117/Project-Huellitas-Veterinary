export const API_BASE: string = import.meta.env.DEV
  ? '/api'
  : `${import.meta.env.BASE_URL}api`

export class ApiError extends Error {
  status: number
  constructor(message: string, status: number) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

/** GET que lanza ApiError si la respuesta no es 2xx */
export async function apiGet<T>(path: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`)
  if (!res.ok) throw new ApiError(`GET ${path} falló`, res.status)
  return (await res.json()) as T
}

/** GET tolerante: si el servidor responde error, devuelve el valor de respaldo */
export async function apiGetOr<T>(path: string, fallback: T): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`)
  return res.ok ? ((await res.json()) as T) : fallback
}

export async function apiPost<TBody, TResponse>(
  path: string,
  body: TBody,
): Promise<TResponse> {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
  return (await res.json()) as TResponse
}
