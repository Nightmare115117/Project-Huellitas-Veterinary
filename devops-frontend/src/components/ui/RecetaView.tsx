import { Printer } from 'lucide-react'
import { formatDate, formatMoney } from '../../lib/format'
import type { Receta } from '../../types/api'

interface Props {
  receta: Receta
  veterinario: string
  onClose: () => void
}

/** Receta lista para leer o imprimir (el CSS de impresión deja solo `.receta`) */
export default function RecetaView({ receta, veterinario, onClose }: Props) {
  return (
    <dialog
      className="dialog"
      aria-labelledby="receta-ver-titulo"
      ref={(el) => {
        if (el && !el.open) el.showModal()
      }}
      onClose={onClose}
      onClick={(e) => e.target === e.currentTarget && onClose()}
    >
      <div className="dialog__body">
        <article className="receta">
          <h2 id="receta-ver-titulo">Receta médica · Huellitas</h2>
          <dl className="detail-list">
            <div>
              <dt>Fecha</dt>
              <dd>{formatDate(receta.fecha)}</dd>
            </div>
            <div>
              <dt>Paciente</dt>
              <dd>{receta.mascotaNombre}</dd>
            </div>
            <div>
              <dt>Dueño</dt>
              <dd>{receta.duenoNombre}</dd>
            </div>
            <div>
              <dt>Peso</dt>
              <dd>{receta.peso} kg</dd>
            </div>
            <div>
              <dt>Motivo</dt>
              <dd>{receta.motivo}</dd>
            </div>
            <div>
              <dt>Diagnóstico</dt>
              <dd>{receta.diagnostico}</dd>
            </div>
          </dl>

          <h3>Medicamentos</h3>
          <ol className="receta__meds">
            {receta.tratamientos.map((t, i) => (
              <li key={i}>
                <strong>{t.medicamento}</strong>
                <span>{t.descripcion}</span>
                {t.costo > 0 && <span className="muted">{formatMoney(t.costo)}</span>}
              </li>
            ))}
          </ol>

          {receta.recomendaciones && (
            <>
              <h3>Recomendaciones</h3>
              <p>{receta.recomendaciones}</p>
            </>
          )}
          <p className="receta__firma">MVZ {veterinario}</p>
        </article>

        <div className="dialog__actions">
          <button type="button" className="btn btn--ghost" onClick={onClose}>
            Cerrar
          </button>
          <button type="button" className="btn btn--primary" onClick={() => window.print()}>
            <Printer size={18} aria-hidden />
            Imprimir
          </button>
        </div>
      </div>
    </dialog>
  )
}
