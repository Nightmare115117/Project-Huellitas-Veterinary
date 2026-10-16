interface BrandProps {
  /** Muestra el nombre junto al logo */
  showName?: boolean
  /** Tamaño del logo en px (el logo es de línea fina: conviene ≥ 48) */
  size?: number
}

export default function Brand({ showName = true, size = 48 }: BrandProps) {
  return (
    <div className="brand">
      <img
        className="brand__logo"
        src={`${import.meta.env.BASE_URL}Logo-invertido.svg`}
        alt={showName ? '' : 'Huellitas'}
        width={size}
        height={size}
      />
      {showName && (
        <span className="brand__name">
          Huellitas
          <small>Clínica veterinaria</small>
        </span>
      )}
    </div>
  )
}
