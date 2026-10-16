import Swal from 'sweetalert2'

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

  loggedOut: () =>
    themed.fire({
      title: 'Sesión cerrada con éxito',
      text: 'Hasta pronto',
      icon: 'success',
      timer: 2000,
      showConfirmButton: false,
    }),
}
