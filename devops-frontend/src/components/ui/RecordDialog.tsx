import { useState } from 'react'
import type { Campo, Opcion } from '../../lib/crud'

interface RecordDialogProps {
  titulo: string
  editando: boolean
  campos: Campo[]
  initial: Record<string, string>
  opciones: Record<string, Opcion[]>
  guardando: boolean
  error: string
  onSubmit: (values: Record<string, string>) => void
  onClose: () => void
}

/** Formulario modal (<dialog> nativo: foco atrapado y Esc para cerrar) */
export default function RecordDialog({
  titulo,
  editando,
  campos,
  initial,
  opciones,
  guardando,
  error,
  onSubmit,
  onClose,
}: RecordDialogProps) {
  const [values, setValues] = useState(initial)
  const set = (key: string, value: string) => setValues((v) => ({ ...v, [key]: value }))

  return (
    <dialog
      className="dialog"
      aria-labelledby="dialog-titulo"
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
          onSubmit(values)
        }}
      >
        <h2 id="dialog-titulo">{titulo}</h2>

        <div className="dialog__grid">
          {campos.map((c) => {
            const id = `campo-${c.key}`
            return (
              <div className="field" key={c.key}>
                <label htmlFor={id}>{c.label}</label>
                {c.tipo === 'ref' ? (
                  <select
                    id={id}
                    className="control"
                    value={values[c.key] ?? ''}
                    onChange={(e) => set(c.key, e.target.value)}
                  >
                    <option value="">— Sin asignar —</option>
                    {(opciones[c.ref ?? ''] ?? []).map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    id={id}
                    className="control"
                    type={c.tipo}
                    step={c.tipo === 'number' ? 'any' : undefined}
                    autoComplete={c.tipo === 'password' ? 'new-password' : 'off'}
                    placeholder={c.tipo === 'password' && editando ? 'Vacío = no cambiar' : undefined}
                    value={values[c.key] ?? ''}
                    onChange={(e) => set(c.key, e.target.value)}
                  />
                )}
              </div>
            )
          })}
        </div>

        {error && (
          <p role="alert" className="dialog__error">
            {error}
          </p>
        )}

        <div className="dialog__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primary" disabled={guardando}>
            {guardando ? 'Guardando…' : 'Guardar'}
          </button>
        </div>
      </form>
    </dialog>
  )
}
