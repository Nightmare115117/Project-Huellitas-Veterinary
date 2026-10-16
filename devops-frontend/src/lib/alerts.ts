import Swal from 'sweetalert2'
import { isRecord } from './normalize'
import type { UnknownRecord } from '../types/api'

/** Swal con el tema de la app (estilos en styles/components.css) */
const themed = Swal.mixin({
  buttonsStyling: false,
  customClass: {
    popup: 'swal-popup',
    title: 'swal-title',
    htmlContainer: 'swal-text',
    confirmButton: 'swal-btn swal-btn--primary',
    cancelButton: 'swal-btn swal-btn--ghost',
    actions: 'swal-actions',
  },
})

export const alerts = {
  loginOk: () =>
    themed.fire({
      title: 'Login correcto',
      text: 'Bienvenido, has iniciado sesión correctamente',
      icon: 'success',
      timer: 1800,
      showConfirmButton: false,
    }),

  loginError: () =>
    themed.fire({
      title: 'Credenciales incorrectas',
      text: 'Por favor, ingrese sus credenciales nuevamente',
      icon: 'error',
      confirmButtonText: 'Entendido',
    }),

  sessionExpired: () =>
    themed.fire({
      title: 'Sesión expirada',
      text: 'Por favor, inicia sesión nuevamente',
      icon: 'warning',
      timer: 3000,
      timerProgressBar: true,
      showConfirmButton: false,
    }),

  error: (text: string) => themed.fire({ title: 'Error', text, icon: 'error' }),

  async confirmLogout(): Promise<boolean> {
    const result = await themed.fire({
      title: 'Cerrar sesión',
      text: '¿Seguro de querer cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      reverseButtons: true,
    })
    return result.isConfirmed
  },

  done: (title: string) =>
    themed.fire({ title, icon: 'success', timer: 1500, showConfirmButton: false }),

  async confirmDelete(): Promise<boolean> {
    const result = await themed.fire({
      title: 'Eliminar registro',
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true,
    })
    return result.isConfirmed
  },

  /** Editor JSON de un registro. Devuelve el objeto, o undefined si se cancela. */
  async editJson(title: string, initial: string): Promise<UnknownRecord | undefined> {
    const result = await themed.fire({
      title,
      input: 'textarea',
      inputValue: initial,
      inputAttributes: { spellcheck: 'false', 'aria-label': 'Registro en formato JSON' },
      width: 'min(92vw, 640px)',
      showCancelButton: true,
      confirmButtonText: 'Guardar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true,
      preConfirm: (text: string) => {
        try {
          const parsed: unknown = JSON.parse(text)
          if (isRecord(parsed)) return parsed
          themed.showValidationMessage('Debe ser un objeto JSON { … }')
        } catch {
          themed.showValidationMessage('JSON no válido')
        }
        return false
      },
    })
    return result.isConfirmed ? (result.value as UnknownRecord) : undefined
  },

  loggedOut: () =>
    themed.fire({
      title: 'Sesión cerrada con éxito',
      text: 'Hasta pronto',
      icon: 'success',
      timer: 2000,
      showConfirmButton: false,
    }),
}
