
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

/** Genera los headers comunes e incluye el JWT si existe */
function apiHeaders(json = false): HeadersInit {
  const token = sessionStorage.getItem('token')

  return {
    ...(json ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  }
}

/** GET que lanza ApiError si la respuesta no es 2xx */
export async function apiGet<T>(path: string): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: apiHeaders(),
  })

  if (!res.ok) {
    throw new ApiError(`GET ${path} falló`, res.status)
  }

  return (await res.json()) as T
}

/** GET tolerante: devuelve el valor de respaldo si hay error HTTP */
export async function apiGetOr<T>(
  path: string,
  fallback: T,
): Promise<T> {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: apiHeaders(),
  })

  return res.ok ? ((await res.json()) as T) : fallback
}

/** POST con JWT y respuesta tipada */
export async function apiPost<TBody, TResponse>(
  path: string,
  body: TBody,
): Promise<TResponse> {
  const res = await fetch(`${API_BASE}${path}`, {
    method: 'POST',
    headers: apiHeaders(true),
    body: JSON.stringify(body),
  })

  if (!res.ok) {
    throw new ApiError(`POST ${path} falló`, res.status)
  }

  return (await res.json()) as TResponse
}

/** POST/PUT/DELETE que lanza ApiError si la respuesta no es 2xx */
export async function apiSend(
  method: 'POST' | 'PUT' | 'DELETE',
  path: string,
  body?: unknown,
): Promise<void> {
  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers: apiHeaders(body !== undefined),
    body: body === undefined ? undefined : JSON.stringify(body),
  })

  if (!res.ok) {
    throw new ApiError(`${method} ${path} falló`, res.status)
  }
}
