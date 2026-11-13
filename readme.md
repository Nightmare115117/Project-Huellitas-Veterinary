# 🐾 Project Huellitas - Sistema de Gestión Veterinaria

[🇪🇸 Versión en Español](readme.md) | [🇺🇸 English Version](README_EN.md)

[![Java](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18%2F19-61DAFB.svg?logo=react)](https://es.react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6.svg?logo=typescript)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-7-646CFF.svg?logo=vite)](https://vitejs.dev/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![MariaDB](https://img.shields.io/badge/MariaDB-10+-003545.svg?logo=mariadb)](https://mariadb.org/)
[![Security](https://img.shields.io/badge/Security-JWT%20%7C%20AES--256-red.svg)]()
[![License](https://img.shields.io/badge/Licencia-MIT-green)](LICENSE)

> **Plataforma integral para clínicas veterinarias**: Solución fullstack para la gestión de pacientes, citas, tratamientos, recetas, control de sucursales y auditoría de seguridad en tiempo real con clientes Web y Móvil.

---

## 📑 Tabla de Contenidos

- [Características Principales](#-características-principales)
- [Arquitectura y Estructura del Proyecto](#-arquitectura-y-estructura-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Seguridad y Criptografía](#-seguridad-y-criptografía)
- [Bitácora Administrativa (Auditoría)](#-bitácora-administrativa-auditoría)
- [Roles de Usuario](#-roles-de-usuario)
- [Requisitos Previos](#-requisitos-previos)
- [Configuración y Variables de Entorno](#-configuración-y-variables-de-entorno)
- [Instalación y Ejecución](#-instalación-y-ejecución)
  - [1. Base de Datos (MariaDB)](#1-base-de-datos-mariadb)
  - [2. Backend (Spring Boot)](#2-backend-spring-boot)
  - [3. Frontend Web (React + Vite)](#3-frontend-web-react--vite)
  - [4. Aplicación Móvil (Android)](#4-aplicación-móvil-android)
  - [5. Instalación Automatizada con Scripts](#5-instalación-automatizada-con-scripts)
  - [6. Despliegue en Kubernetes con HTTPS](#6-despliegue-en-kubernetes-con-https)

---

## ✨ Características Principales

- 🐶 **Gestión de Pacientes (Mascotas):** Expedientes clínicos, especies, razas, control de dueños y seguimiento personalizado.
- 📅 **Control Integral de Citas:** Programación, cambio de estados (Pendiente, Programada, Completada, Cancelada) y consultas filtradas por cliente o mascota.
- 🩺 **Consultas y Tratamientos Médicos:** Registro de diagnósticos, control de recetas, medicamentos e historial médico.
- 📜 **Bitácora Administrativa y Auditoría:** Registro automático de inicios de sesión y operaciones de modificación (`POST`, `PUT`, `DELETE`, `PATCH`) con IP, usuario, rol y trazabilidad completa.
- 📊 **Panel Administrativo Dinámico:** Dashboard con métricas clave, gestión de usuarios, personal de salud, explorador multi-tabla CRUD y filtros en tiempo real.
- 📱 **Aplicación Móvil Nativa (Android):** Diseñada con Jetpack Compose y adaptada a la vista de cada rol.
- 🏢 **Gestión Multi-Sucursal y Geolocalización:** Control de sucursales, gestores, direcciones, colonias, ciudades y estados.

---

## 🏛 Arquitectura y Estructura del Proyecto

El repositorio está organizado como un monorepo modular:

```text
Project-Huellitas-Veterinary/
├── devops-proyect/         # Backend: API REST en Spring Boot 3.5 (Java 21)
│   ├── src/main/java/      # Módulos: bitacora, citas, consultaMedica, mascotas, users, etc.
│   ├── src/main/resources/ # application.properties y recursos del servidor
│   └── pom.xml             # Dependencias Maven
│
├── devops-frontend/        # Frontend Web: SPA en React, TypeScript y Vite
│   ├── src/                # Vistas por rol, componentes, servicios de API y mappers
│   ├── package.json        # Dependencias de Node y scripts npm
│   └── vite.config.ts      # Configuración de compilación y servidor de desarrollo
│
├── devops-app-mobil/       # Aplicación Móvil: Android nativo con Kotlin y Jetpack Compose
│   ├── app/src/main/       # Actividades, vistas por rol, modelos y llamadas de red
│   └── build.gradle.kts    # Configuración de Gradle
│
├── scripts/                # Scripts Fish para instalación, respaldo y automatización
│   ├── instalarelproyecto.fish
│   ├── backup.fish
│   └── desintalar.fish
│
└── readme.md               # Documentación general del repositorio
```

---

## 🚀 Tecnologías Utilizadas

| Capa | Tecnologías | Descripción |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5, Spring Data JPA, Hibernate | API REST modular, servicios genéricos e interceptores. |
| **Base de Datos** | MariaDB / MySQL | Almacenamiento relacional con dialecto MariaDB. |
| **Frontend Web** | React 18/19, TypeScript, Vite, Lucide Icons | Interfaz moderna, reactiva, accesible y responsive. |
| **Móvil Android** | Kotlin, Jetpack Compose, Material 3 | Aplicación nativa con navegación diferenciada por roles. |
| **Seguridad** | Spring Security, OAuth2 Resource Server, Nimbus JWT, Argon2id, AES-GCM, Cloudflare Turnstile | Protección de endpoints, autenticación sin estado, hashes de contraseñas, CAPTCHA y cifrado simétrico. |
| **Testing** | JUnit Jupiter, Mockito, Spring Boot Test | Pruebas unitarias de servicios, controladores e interceptores. |

---

## 🔐 Seguridad y Criptografía

El sistema implementa medidas de seguridad sólidas:

1. **Autenticación Stateless con JWT:** Los tokens de acceso se emiten tras el login (`POST /usuario/login`), firmados criptográficamente mediante algoritmo HMAC-SHA256 (`HmacSHA256`) con tiempo de expiración de 15 minutos.
2. **Cifrado Simétrico AES-256-GCM:** Soporte para cifrado y descifrado de datos sensibles mediante `AES/GCM/NoPadding` con vector de inicialización (IV) de 12 bytes y tags de autenticación de 128 bits.
3. **Control de Acceso Basado en Roles (RBAC):** Restricciones automáticas en `SecurityConfig`:
   - Endpoint `/bitacora/**` restringido exclusivamente a roles `Admin` y `Administrador`.
   - Endpoints de administración de `/usuario` protegidos.
   - Operaciones CRUD genéricas restringidas a administradores y personal autorizado; las consultas de mascotas, citas, tratamientos y nombres por correo solo permiten al propietario o al personal autorizado.
4. **Transporte HTTPS:** TLS y las cabeceras de seguridad se gestionan en el proxy inverso externo; el backend permanece como servicio interno del clúster.
5. **Protección de Contraseñas:** Hash Argon2id con salt aleatorio; las claves nuevas requieren 12 caracteres, mayúscula, minúscula, número y símbolo. Los hashes BCrypt existentes se migran progresivamente en el inicio de sesión.
6. **Protección anti-bots:** Cloudflare Turnstile se valida en el backend antes de consultar credenciales. La API comprueba el éxito, el hostname configurado y la acción `login`; en producción el CAPTCHA es obligatorio y falla de forma cerrada si Cloudflare no responde.

---

## 📜 Bitácora Administrativa (Auditoría)

El módulo de **Bitácora** garantiza la trazabilidad y rendición de cuentas en el sistema:

* **Registro Automático de Accesos:** Monitorea en `UsuarioController` los eventos `LOGIN_EXITOSO` y `LOGIN_FALLIDO` (tanto por credenciales erróneas como por campos incompletos), registrando la IP de origen.
* **Interceptor de Acciones (`BitacoraInterceptor`):** Registra automáticamente toda operación mutante (`POST`, `PUT`, `DELETE`, `PATCH`) en cualquier controlador o recurso del sistema.
* **Datos Auditados por Evento:**
  - `ID`: Identificador autoincremental único.
  - `Fecha y Hora`: Timestamp oficial del servidor.
  - `Usuario`: Correo de la cuenta autenticada (o *Anónimo*).
  - `Rol`: Rol verificado del usuario extraído del JWT.
  - `Acción`: Nombre estandarizado de la operación (ej. `CREAR_CITA`, `CANCELAR_CITA`, `ELIMINAR_MASCOTA`).
  - `Módulo`: Sección del sistema (`Citas`, `Mascotas`, `Usuarios`, `Tratamientos`, etc.).
  - `Detalle`: Método HTTP, URI y código de resultado (`exitoso` o error con código HTTP y mensaje).
  - `IP`: Dirección IP remota (con detección de proxies vía `X-Forwarded-For`).
* **Transacciones Independientes (`REQUIRES_NEW`):** Los logs se confirman en su propia transacción, asegurando que los eventos de error o excepciones queden guardados aunque el controlador principal haga rollback.

---

## 👥 Roles de Usuario

El sistema adapta sus vistas y permisos según el tipo de cuenta:

| Rol | ID | Alcance |
| :--- | :---: | :--- |
| **Administrador** | `1` | Control total del sistema: gestión de usuarios, personal, mascotas, citas, tratamientos, visor de bitácora y mantenimiento de tablas. |
| **Dueño de Mascota** | `2` | Portal de clientes: registro y consulta de sus propias mascotas, solicitud y cancelación de citas médicas y consulta de recetas/tratamientos activos. |
| **Gestor / Personal** | `3` | Administración operativa: control de agenda de citas, consulta de mascotas y asignación de tratamientos veterinarios. |

---

## 💻 Requisitos Previos

- **Java Development Kit (JDK) 21** o superior.
- **Node.js** (versión 20+ o 24 LTS) y gestor **npm**.
- **MariaDB** o **MySQL** (versión 10.5+).
- **Android Studio** con SDK instalado (si se desea compilar la app móvil).
- Shell **bash** o **fish**.

---

## 🔑 Configuración y Variables de Entorno

El backend requiere claves criptográficas seguras de 64 caracteres hexadecimales (256 bits).

Crea un archivo `.env` en la raíz de `devops-proyect/` (o exporta las variables de entorno en el sistema):

```dotenv
# Claves criptográficas en formato HEX (64 caracteres = 256 bits)
JWT_SECRET=tu_clave_secreta_hex_de_64_caracteres_para_firmar_tokens_jwt_aqui
AES_SECRET=tu_clave_secreta_hex_de_64_caracteres_para_cifrado_aes_gcm_aqui
HMAC_SECRET=tu_clave_secreta_hex_de_64_caracteres_para_validacion_hmac_aqui
SPRING_DATASOURCE_USERNAME=huellitas_app
SPRING_DATASOURCE_PASSWORD=usa_una_contrasena_larga_y_unica
# Solo necesario cuando se habilita Turnstile en el backend
TURNSTILE_ENABLED=true
TURNSTILE_SECRET=secreto_privado_de_cloudflare
TURNSTILE_EXPECTED_HOSTNAME=tu-dominio-publico
```

> **Tip:** Puedes generar claves válidas desde la terminal con OpenSSL:
> ```bash
> openssl rand -hex 32
> ```

---

## 📦 Instalación y Ejecución

### 1. Base de Datos (MariaDB)

Crea la base de datos y un usuario de aplicación con una contraseña fuerte. No uses credenciales predeterminadas ni publiques estas credenciales:

```sql
CREATE DATABASE IF NOT EXISTS huellitas_db;
CREATE USER IF NOT EXISTS 'huellitas_app'@'localhost' IDENTIFIED BY '<contraseña-fuerte-generada>';
GRANT SELECT, INSERT, UPDATE, DELETE ON huellitas_db.* TO 'huellitas_app'@'localhost';
FLUSH PRIVILEGES;
```

`SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD` son obligatorias; la aplicación no incluye credenciales por defecto. `APP_CORS_ALLOWED_ORIGINS` permite configurar los orígenes para desarrollo; en producción, el frontend y la API se sirven desde el mismo origen a través del proxy inverso.

Registra el dominio público en Cloudflare Turnstile. Configura la **clave del sitio** como `VITE_TURNSTILE_SITE_KEY` al construir el frontend (por ejemplo, en un archivo `.env` local, no versionado) y guarda la **clave secreta** únicamente en el entorno del backend. En Kubernetes, añade `TURNSTILE_SECRET` al Secret `huellitas-secrets`; el deployment espera que `TURNSTILE_EXPECTED_HOSTNAME` coincida con el hostname registrado.

---

### 2. Backend (Spring Boot)

Entra a la carpeta del backend y levanta la aplicación:

```bash
cd devops-proyect

# Compilar y ejecutar pruebas
./mvnw test-compile

# Iniciar servidor Spring Boot (puerto 8080 por defecto)
./mvnw spring-boot:run
```

---

### 3. Frontend Web (React + Vite)

Entra a la carpeta del frontend, instala dependencias e inicia el servidor de desarrollo:

```bash
cd devops-frontend

# Instalar dependencias
npm install

# Publica la clave del sitio de Turnstile al proceso de Vite
export VITE_TURNSTILE_SITE_KEY=clave-publica-de-cloudflare

# Iniciar servidor de desarrollo (puerto 5173 por defecto)
npm run dev
```

Abre tu navegador en `http://localhost:5173`.

---

### 4. Aplicación Móvil (Android)

Para compilar la aplicación móvil desde la línea de comandos:

```bash
cd devops-app-mobil

# Compilar APK de depuración
./gradlew :app:assembleDebug
```

El archivo APK generado estará disponible en:
`app/build/outputs/apk/debug/app-debug.apk`.

---

### 5. Instalación Automatizada con Scripts

En sistemas basados en Arch Linux, puedes usar los scripts incluidos en `scripts/`:

```bash
# Otorgar permisos de ejecución
chmod +x scripts/*.fish

# Ejecutar el instalador automático completo
./scripts/instalarelproyecto.fish
```

Para detener los servicios en segundo plano:

```bash
pkill -f "spring-boot" 2>/dev/null
pkill -f "node" 2>/dev/null
pkill -f "npm" 2>/dev/null
```

---

### 6. Despliegue en Kubernetes detrás de un proxy inverso

El proxy inverso externo termina TLS y agrega las cabeceras de seguridad. El frontend del clúster escucha HTTP en el NodePort `30081`; configura el proxy para enviarle allí las solicitudes del sitio. El backend permanece como servicio interno y recibe las llamadas API mediante el frontend. Antes de desplegar, configura la variable de repositorio de GitHub Actions `VITE_TURNSTILE_SITE_KEY` y agrega `TURNSTILE_SECRET` al Secret `huellitas-secrets`. El hostname configurado en `K8s/backend-deployment.yml` debe ser el mismo registrado en Turnstile.

---

## 📄 Licencia

Este proyecto se encuentra bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
