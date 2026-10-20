/**
 * Generador de QR mínimo (modo byte, corrección M, versiones 1–10 ≈ hasta 213 bytes),
 * suficiente para una URI otpauth://. Sin dependencias: el Dockerfile usa `npm ci`.
 * shortcut: máscara fija 0 (válida, sin evaluar penalizaciones), si hace falta más texto usar una librería.
 */

/** [bytes EC por bloque, nº bloques corto, datos corto, nº bloques largo, datos largo] por versión */
const BLOQUES_M = [
  [10, 1, 16, 0, 0],
  [16, 1, 28, 0, 0],
  [26, 1, 44, 0, 0],
  [18, 2, 32, 0, 0],
  [24, 2, 43, 0, 0],
  [16, 4, 27, 0, 0],
  [18, 4, 31, 0, 0],
  [22, 2, 38, 2, 39],
  [22, 3, 36, 2, 37],
  [26, 4, 43, 1, 44],
]

const ALINEACION = [[], [6, 18], [6, 22], [6, 26], [6, 30], [6, 34], [6, 22, 38], [6, 24, 42], [6, 26, 46], [6, 28, 50]]

// Campo de Galois GF(256) para Reed-Solomon
const EXP = new Uint8Array(512)
const LOG = new Uint8Array(256)
for (let i = 0, x = 1; i < 255; i++) {
  EXP[i] = x
  LOG[x] = i
  x <<= 1
  if (x & 256) x ^= 0x11d
}
for (let i = 255; i < 512; i++) EXP[i] = EXP[i - 255]
const mul = (a: number, b: number) => (a && b ? EXP[LOG[a] + LOG[b]] : 0)

function reedSolomon(datos: number[], n: number): number[] {
  let gen = [1]
  for (let i = 0; i < n; i++) {
    const sig = new Array<number>(gen.length + 1).fill(0)
    gen.forEach((g, j) => {
      sig[j] ^= g
      sig[j + 1] ^= mul(g, EXP[i])
    })
    gen = sig
  }
  const resto = new Array<number>(n).fill(0)
  for (const d of datos) {
    const f = d ^ resto.shift()!
    resto.push(0)
    for (let i = 0; i < n; i++) resto[i] ^= mul(gen[i + 1], f)
  }
  return resto
}

/** Devuelve la matriz de módulos (true = oscuro) para el texto dado */
export function qrMatrix(texto: string): boolean[][] {
  const bytes = Array.from(new TextEncoder().encode(texto))

  // 1. Versión más pequeña que alcance
  const version = BLOQUES_M.findIndex(([, n1, d1, n2, d2], i) => {
    const bitsContador = i + 1 < 10 ? 8 : 16
    return (n1 * d1 + n2 * d2) * 8 >= 4 + bitsContador + bytes.length * 8
  }) + 1
  if (version === 0) throw new Error('Texto demasiado largo para el QR')
  const [ecN, n1, d1, n2, d2] = BLOQUES_M[version - 1]
  const totalDatos = n1 * d1 + n2 * d2

  // 2. Bits de datos: modo byte + longitud + bytes + terminador + relleno
  const bits: number[] = []
  const push = (v: number, n: number) => {
    for (let i = n - 1; i >= 0; i--) bits.push((v >>> i) & 1)
  }
  push(0b0100, 4)
  push(bytes.length, version < 10 ? 8 : 16)
  bytes.forEach((b) => push(b, 8))
  push(0, Math.min(4, totalDatos * 8 - bits.length))
  while (bits.length % 8) bits.push(0)
  const cw: number[] = []
  for (let i = 0; i < bits.length; i += 8) cw.push(parseInt(bits.slice(i, i + 8).join(''), 2))
  for (let pad = 0xec; cw.length < totalDatos; pad ^= 0xec ^ 0x11) cw.push(pad)

  // 3. Bloques + Reed-Solomon, intercalados
  const bloques: number[][] = []
  let pos = 0
  for (let i = 0; i < n1 + n2; i++) {
    const len = i < n1 ? d1 : d2
    bloques.push(cw.slice(pos, pos + len))
    pos += len
  }
  const ecs = bloques.map((b) => reedSolomon(b, ecN))
  const final: number[] = []
  for (let i = 0; i < Math.max(d1, d2); i++) bloques.forEach((b) => i < b.length && final.push(b[i]))
  for (let i = 0; i < ecN; i++) ecs.forEach((e) => final.push(e[i]))

  // 4. Matriz y patrones de función
  const size = version * 4 + 17
  const m = Array.from({ length: size }, () => new Array<boolean>(size).fill(false))
  const fn = Array.from({ length: size }, () => new Array<boolean>(size).fill(false))
  const set = (x: number, y: number, oscuro: boolean) => {
    if (x < 0 || y < 0 || x >= size || y >= size) return
    m[y][x] = oscuro
    fn[y][x] = true
  }
  const bit = (v: number, i: number) => ((v >>> i) & 1) === 1

  for (let i = 0; i < size; i++) {
    set(6, i, i % 2 === 0)
    set(i, 6, i % 2 === 0)
  }
  for (const [cx, cy] of [[3, 3], [size - 4, 3], [3, size - 4]]) {
    for (let dy = -4; dy <= 4; dy++) {
      for (let dx = -4; dx <= 4; dx++) {
        const d = Math.max(Math.abs(dx), Math.abs(dy))
        set(cx + dx, cy + dy, d !== 2 && d !== 4)
      }
    }
  }
  const al = ALINEACION[version - 1]
  al.forEach((ax, i) =>
    al.forEach((ay, j) => {
      const esquina = (i === 0 && j === 0) || (i === 0 && j === al.length - 1) || (i === al.length - 1 && j === 0)
      if (esquina) return
      for (let dy = -2; dy <= 2; dy++) {
        for (let dx = -2; dx <= 2; dx++) set(ax + dx, ay + dy, Math.max(Math.abs(dx), Math.abs(dy)) !== 1)
      }
    }),
  )

  // Formato: nivel M (00) + máscara 0, con BCH(15,5)
  let rem = 0
  for (let i = 0; i < 10; i++) rem = (rem << 1) ^ ((rem >>> 9) * 0x537)
  const formato = rem ^ 0x5412
  for (let i = 0; i <= 5; i++) set(8, i, bit(formato, i))
  set(8, 7, bit(formato, 6))
  set(8, 8, bit(formato, 7))
  set(7, 8, bit(formato, 8))
  for (let i = 9; i < 15; i++) set(14 - i, 8, bit(formato, i))
  for (let i = 0; i < 8; i++) set(size - 1 - i, 8, bit(formato, i))
  for (let i = 8; i < 15; i++) set(8, size - 15 + i, bit(formato, i))
  set(8, size - 8, true)

  if (version >= 7) {
    let r = version
    for (let i = 0; i < 12; i++) r = (r << 1) ^ ((r >>> 11) * 0x1f25)
    const v = (version << 12) | r
    for (let i = 0; i < 18; i++) {
      const a = size - 11 + (i % 3)
      const b = Math.floor(i / 3)
      set(a, b, bit(v, i))
      set(b, a, bit(v, i))
    }
  }

  // 5. Datos en zigzag + máscara 0
  let k = 0
  for (let right = size - 1; right >= 1; right -= 2) {
    if (right === 6) right = 5
    for (let vert = 0; vert < size; vert++) {
      for (let j = 0; j < 2; j++) {
        const x = right - j
        const y = ((right + 1) & 2) === 0 ? size - 1 - vert : vert
        if (fn[y][x]) continue
        const oscuro = k < final.length * 8 && bit(final[k >>> 3], 7 - (k & 7))
        k++
        m[y][x] = oscuro !== ((x + y) % 2 === 0)
      }
    }
  }
  return m
}
