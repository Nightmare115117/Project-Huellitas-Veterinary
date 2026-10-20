import { qrMatrix } from '../../lib/qr'

/** QR en SVG. Siempre fondo blanco con margen de 4 módulos: sin él los lectores fallan en tema oscuro. */
export default function QrCode({ valor }: { valor: string }) {
  const m = qrMatrix(valor)
  const margen = 4
  const lado = m.length + margen * 2
  const d = m
    .flatMap((fila, y) => fila.map((oscuro, x) => (oscuro ? `M${x + margen} ${y + margen}h1v1h-1z` : '')))
    .join('')

  return (
    <svg
      className="mfa__qr"
      viewBox={`0 0 ${lado} ${lado}`}
      role="img"
      aria-label="Código QR para vincular tu app de autenticación"
      shapeRendering="crispEdges"
    >
      <rect width={lado} height={lado} fill="#fff" />
      <path d={d} fill="#000" />
    </svg>
  )
}
