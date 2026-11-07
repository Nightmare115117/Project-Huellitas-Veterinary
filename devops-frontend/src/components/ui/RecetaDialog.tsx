import { useState } from 'react'
import { Plus, Trash2 } from 'lucide-react'
import { formatDate } from '../../lib/format'
import type { CitaVeterinario, RecetaNueva } from '../../types/api'

interface Props {
  cita: CitaVeterinario
  guardando: boolean
  /** Error del servidor al guardar */
  error: string
  onSubmit: (receta: RecetaNueva) => void
  onClose: () => void
}

interface Fila {
  medicamento: string
  descripcion: string
  costo: string
}

const filaVacia = (): Fila => ({ medicamento: '', descripcion: '', costo: '' })

/** Formulario de receta: datos de la consulta + uno o más medicamentos (<dialog> nativo) */
export default function RecetaDialog({ cita, guardando, error, onSubmit, onClose }: Props) {
  const [motivo, setMotivo] = useState('')
  const [diagnostico, setDiagnostico] = useState('')
  const [peso, setPeso] = useState('')
  const [recomendaciones, setRecomendaciones] = useState('')
  const [filas, setFilas] = useState<Fila[]>([filaVacia()])
  const [local, setLocal] = useState('')

  const setFila = (i: number, cambio: Partial<Fila>) =>
    setFilas((fs) => fs.map((f, j) => (j === i ? { ...f, ...cambio } : f)))

  const enviar = () => {
    const meds = filas.filter((f) => f.medicamento.trim() || f.descripcion.trim() || f.costo)
    if (!motivo.trim() || !diagnostico.trim()) return setLocal('Motivo y diagnóstico son obligatorios.')
    if (!(Number(peso) > 0)) return setLocal('Indica el peso de la mascota (mayor a 0).')
    if (meds.length === 0) return setLocal('Agrega al menos un medicamento.')
    if (meds.some((m) => !m.medicamento.trim() || !m.descripcion.trim())) {
      return setLocal('Cada medicamento necesita nombre e indicaciones.')
    }
    if (meds.some((m) => m.costo !== '' && Number(m.costo) < 0)) return setLocal('El costo no puede ser negativo.')

    setLocal('')
    onSubmit({
      idCita: cita.idCita,
      motivo: motivo.trim(),
      diagnostico: diagnostico.trim(),
      peso: Number(peso),
      recomendaciones: recomendaciones.trim(),
      tratamientos: meds.map((m) => ({
        medicamento: m.medicamento.trim(),
        descripcion: m.descripcion.trim(),
        costo: Number(m.costo) || 0,
      })),
    })
  }

  return (
    <dialog
      className="dialog"
      aria-labelledby="receta-titulo"
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
          enviar()
        }}
      >
        <h2 id="receta-titulo">Nueva receta · {cita.mascotaNombre}</h2>
        <p className="muted">
          {cita.especie} {cita.raza && `· ${cita.raza}`} · Dueño: {cita.duenoNombre} · Cita del {formatDate(cita.fecha)}
        </p>

        <div className="dialog__grid">
          <div className="field">
            <label htmlFor="rc-motivo">Motivo de la consulta</label>
            <input id="rc-motivo" className="control" value={motivo} onChange={(e) => setMotivo(e.target.value)} autoComplete="off" />
          </div>
          <div className="field">
            <label htmlFor="rc-peso">Peso (kg)</label>
            <input id="rc-peso" className="control" type="number" step="any" min="0" value={peso} onChange={(e) => setPeso(e.target.value)} />
          </div>
          <div className="field field--full">
            <label htmlFor="rc-diag">Diagnóstico</label>
            <textarea id="rc-diag" className="control control--area" value={diagnostico} onChange={(e) => setDiagnostico(e.target.value)} />
          </div>
        </div>

        {filas.map((f, i) => (
          <fieldset className="med-row" key={i}>
            <legend>Medicamento {i + 1}</legend>
            <div className="dialog__grid">
              <div className="field">
                <label htmlFor={`rc-med-${i}`}>Medicamento</label>
                <input id={`rc-med-${i}`} className="control" value={f.medicamento} onChange={(e) => setFila(i, { medicamento: e.target.value })} autoComplete="off" />
              </div>
              <div className="field">
                <label htmlFor={`rc-costo-${i}`}>Costo (opcional)</label>
                <input id={`rc-costo-${i}`} className="control" type="number" step="any" min="0" value={f.costo} onChange={(e) => setFila(i, { costo: e.target.value })} />
              </div>
              <div className="field field--full">
                <label htmlFor={`rc-ind-${i}`}>Indicaciones (dosis, frecuencia, duración)</label>
                <textarea id={`rc-ind-${i}`} className="control control--area" value={f.descripcion} onChange={(e) => setFila(i, { descripcion: e.target.value })} />
              </div>
            </div>
            {filas.length > 1 && (
              <button type="button" className="btn btn--ghost" onClick={() => setFilas((fs) => fs.filter((_, j) => j !== i))}>
                <Trash2 size={18} aria-hidden />
                Quitar medicamento {i + 1}
              </button>
            )}
          </fieldset>
        ))}

        <button type="button" className="btn btn--ghost" onClick={() => setFilas((fs) => [...fs, filaVacia()])}>
          <Plus size={18} aria-hidden />
          Agregar medicamento
        </button>

        <div className="field">
          <label htmlFor="rc-recom">Recomendaciones para el dueño</label>
          <textarea id="rc-recom" className="control control--area" value={recomendaciones} onChange={(e) => setRecomendaciones(e.target.value)} />
        </div>

        {(local || error) && (
          <p role="alert" className="dialog__error">
            {local || error}
          </p>
        )}

        <div className="dialog__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cancelar
          </button>
          <button type="submit" className="btn btn--primary" disabled={guardando}>
            {guardando ? 'Guardando…' : 'Generar receta'}
          </button>
        </div>
      </form>
    </dialog>
  )
}
