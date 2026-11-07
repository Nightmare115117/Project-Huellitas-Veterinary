# API requerida por el frontend · Gestor y Veterinario

Endpoints **nuevos** que necesitan las vistas `MenuGestor` y `MenuVeterinario`. Los CRUD genéricos actuales (`/usuario`, `/cita`, …) siguen siendo solo del administrador; estas rutas son aparte para no abrirle esas tablas a otros roles.

## 0. Convenciones

- Auth: `Authorization: Bearer <jwt>` (igual que hoy).
- `{correo}` es el del usuario logueado. **El backend debe validar que coincida con el correo del JWT** (si no, `403`). Nunca confiar en el path.
- Roles: `/gestor/**` solo `GESTOR` (rol 3); `/veterinario/**` solo `VETERINARIO` (rol 4).
- Fechas `yyyy-MM-dd`, horas `HH:mm:ss`. JSON plano (DTOs sin entidades anidadas).
- Errores: `401` sin token, `403` rol/correo incorrecto, `404` no existe, `409` conflicto (correo repetido, cita ya con receta), `400` validación.
- Escrituras relevantes deben registrarse en `bitacora` (`CREAR_VETERINARIO`, `ACTUALIZAR_VETERINARIO`, `ELIMINAR_VETERINARIO`, `ACTUALIZAR_CITA`, `CREAR_RECETA`).

## 1. Cambios al modelo / rol existente

| # | Cambio | Por qué |
|---|--------|---------|
| 1 | Insertar en `roles` el rol **Veterinario** (el frontend asume `id_rol = 4`; si es otro, cambiar `Rol.Veterinario` en `src/types/api.ts`). | `GET /usuario/Rol?correo=` debe devolver `4` para que el login redirija a `/MainMenuVeterinario`. |
| 2 | **Agregar `id_sucursal` (nullable, FK a `sucursales`) a `usuarios`.** | El modelo no relaciona veterinarios con sucursal. Derivarlos de `citas` no sirve: un veterinario recién creado no tendría citas y nunca aparecería para asignarlo. |
| 3 | Confirmar que `consulta-tratamiento.id_cita` es en realidad el **id de la consulta** (`consulta-medica.id_consulta`). | En el diagrama dice `id_cita`, pero la tabla une consulta ↔ tratamiento. Si de verdad es la cita, la receta queda ligada a la cita (1 consulta por cita) y funciona igual. |

## 2. Gestor (`/gestor/{correo}/…`)

La **sucursal del gestor** se resuelve: `usuarios.correo → gestores.id_usuario → sucursales.id_gestor`.
Todo lo de abajo se filtra a esa sucursal.

### 2.1 `GET /gestor/{correo}/sucursal`
Dirección completa + RFC. `404` si el gestor no tiene sucursal (el front lo tolera).
```json
{
  "idSucursal": 1,
  "calle": "Av. Juárez",
  "colonia": "Centro",
  "ciudad": "Saltillo",
  "estado": "Coahuila",
  "pais": "México",
  "codigoPostal": 25000,
  "rfcGestor": "PEEF900101ABC"
}
```
Join: `sucursales → direcciones → calles → colonias → ciudades → estados → paises` y `gestores.rfc`.

### 2.2 `GET /gestor/{correo}/citas`
Citas cuyas `mascotas.id_sucursal` = sucursal del gestor. (`estadoCita`: 1 Programada, 2 Confirmada, 3 Cancelada.)
```json
[{
  "idCita": 10,
  "fecha": "2026-11-12",
  "entradaAgendada": "10:30:00",
  "estadoCita": 1,
  "mascotaNombre": "Firulais",
  "duenoNombre": "Ana López Ruiz",
  "idVeterinario": null,
  "veterinarioNombre": null
}]
```
`idVeterinario` = `citas.id_usuariov`, `duenoNombre` = `citas.id_usuariom` (o el dueño de la mascota).

### 2.2b `PUT /gestor/{correo}/citas/{idCita}`
Actualización parcial: solo se aplican los campos presentes.
```json
{ "idUsuarioV": 7 }        // asignar (null = desasignar)
{ "estadoCita": 2 }        // 1→2 confirmar
{ "estadoCita": 3 }        // cancelar
```
Validar: la cita es de la sucursal; el veterinario pertenece a la sucursal (`usuarios.id_sucursal`) y tiene rol 4; no se cancela una cita que ya tiene consulta/receta. `204`.

### 2.3 `GET /gestor/{correo}/veterinarios`
Usuarios rol 4 con `id_sucursal` = la del gestor.
```json
[{ "idUsuario": 7, "nombre": "Luis", "paterno": "Mora", "materno": null, "correo": "luis@huellitas.mx" }]
```

### 2.3b `POST /gestor/{correo}/veterinarios`
```json
{ "nombre": "Luis", "paterno": "Mora", "materno": null, "correo": "luis@huellitas.mx", "contrasena": "…" }
```
El backend fija `id_rol = 4` y `id_sucursal` = la del gestor (no vienen del cliente), y procesa contraseña y `correohmac` igual que el alta actual de `/usuario`. `201`; `409` si el correo ya existe.

### 2.3c `PUT /gestor/{correo}/veterinarios/{idUsuario}`
Mismo cuerpo; `contrasena` ausente = no cambiar. Solo si el usuario es rol 4 de esa sucursal. `204`.

### 2.3d `DELETE /gestor/{correo}/veterinarios/{idUsuario}`
Solo rol 4 de esa sucursal. `409` si tiene citas o consultas (el front muestra "puede tener citas o recetas relacionadas"). `204`.

### 2.4 `GET /gestor/{correo}/duenos`
Dueños con al menos una mascota en la sucursal (`mascotas.id_sucursal`). Mismo DTO que 2.3.

### 2.5 `GET /gestor/{correo}/mascotas`
```json
[{
  "idMascota": 3, "nombre": "Firulais", "especie": "Perro", "raza": "Labrador",
  "fechaNacimiento": "2021-05-03", "duenoNombre": "Ana López Ruiz"
}]
```

### 2.6 `GET /gestor/{correo}/reportes?desde=yyyy-MM-dd&hasta=yyyy-MM-dd`
Rango inclusivo sobre `citas.fecha`. Excluye citas canceladas de los ingresos.
```json
{
  "totalCitas": 42,
  "citasPorEstado": [{ "estado": 1, "total": 10 }, { "estado": 2, "total": 28 }, { "estado": 3, "total": 4 }],
  "citasPorVeterinario": [{ "veterinario": "Luis Mora", "total": 20 }, { "veterinario": "Sin asignar", "total": 3 }],
  "ingresosServicios": 12500.0,
  "ingresosTratamientos": 4300.0,
  "serviciosTop": [{ "nombre": "Vacunación", "total": 15, "ingreso": 6000.0 }]
}
```
- `ingresosServicios` = Σ `servicios.costo` vía `cita-servicio`.
- `ingresosTratamientos` = Σ `tratamientos.costo` vía `consulta-tratamiento → consulta-medica → citas`.
- `serviciosTop`: máx. 10, ordenado por `total` desc.
- `400` si `desde > hasta`.

## 3. Veterinario (`/veterinario/{correo}/…`)

El veterinario **solo** puede ver sus citas y generar/ver sus recetas. Sin acceso a nada más.

### 3.1 `GET /veterinario/{correo}/citas`
Citas con `citas.id_usuariov` = el veterinario.
```json
[{
  "idCita": 10, "fecha": "2026-11-12", "entradaAgendada": "10:30:00", "estadoCita": 2,
  "mascotaNombre": "Firulais", "especie": "Perro", "raza": "Labrador",
  "duenoNombre": "Ana López Ruiz",
  "tieneReceta": false
}]
```
`tieneReceta` = existe `consulta-medica` con `cita_id_cita` = la cita.

### 3.2 `GET /veterinario/{correo}/recetas`
Recetas de sus citas, más recientes primero.
```json
[{
  "idConsulta": 5, "idCita": 10, "fecha": "2026-11-12",
  "mascotaNombre": "Firulais", "duenoNombre": "Ana López Ruiz",
  "motivo": "Vómito", "diagnostico": "Gastritis leve", "peso": 18.5,
  "recomendaciones": "Dieta blanda 3 días",
  "tratamientos": [
    { "medicamento": "Omeprazol", "descripcion": "10 mg cada 24 h por 5 días", "costo": 120.0 }
  ]
}]
```
`fecha` = `citas.fecha`.

### 3.3 `POST /veterinario/{correo}/recetas`
Una sola transacción.
```json
{
  "idCita": 10,
  "motivo": "Vómito",
  "diagnostico": "Gastritis leve",
  "peso": 18.5,
  "recomendaciones": "Dieta blanda 3 días",
  "tratamientos": [
    { "medicamento": "Omeprazol", "descripcion": "10 mg cada 24 h por 5 días", "costo": 120.0 }
  ]
}
```
Pasos: (1) insertar `consulta-medica` con `id_mascota` tomado de la cita y `cita_id_cita = idCita`; (2) insertar cada fila en `tratamientos` con `estatus = 0` (en curso); (3) insertar `consulta-tratamiento` por cada una.

Validar: la cita pertenece al veterinario del JWT; no está cancelada (`estado_cita ≠ 3`); **no tiene ya** consulta (`409`); `peso > 0`; `motivo` y `diagnostico` no vacíos; al menos 1 tratamiento con `medicamento` y `descripcion`; `costo ≥ 0`. `201` (el front no lee el cuerpo, vuelve a pedir 3.1 y 3.2).

## 4. Resumen

| Método | Ruta | Rol |
|--------|------|-----|
| GET | `/gestor/{correo}/sucursal` | Gestor |
| GET | `/gestor/{correo}/citas` | Gestor |
| PUT | `/gestor/{correo}/citas/{idCita}` | Gestor |
| GET | `/gestor/{correo}/veterinarios` | Gestor |
| POST | `/gestor/{correo}/veterinarios` | Gestor |
| PUT | `/gestor/{correo}/veterinarios/{idUsuario}` | Gestor |
| DELETE | `/gestor/{correo}/veterinarios/{idUsuario}` | Gestor |
| GET | `/gestor/{correo}/duenos` | Gestor |
| GET | `/gestor/{correo}/mascotas` | Gestor |
| GET | `/gestor/{correo}/reportes?desde&hasta` | Gestor |
| GET | `/veterinario/{correo}/citas` | Veterinario |
| GET | `/veterinario/{correo}/recetas` | Veterinario |
| POST | `/veterinario/{correo}/recetas` | Veterinario |

Ya existentes y usados: `GET /usuario/Nombre/{correo}`, `GET /usuario/Rol?correo=`.
