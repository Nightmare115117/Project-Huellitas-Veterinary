# 🐾 Project Huellitas - Veterinary Management System

[🇪🇸 Versión en Español](readme.md) | [🇺🇸 English Version](README_EN.md)

[![Java](https://img.shields.io/badge/Java-21-orange.svg?logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18%2F19-61DAFB.svg?logo=react)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6.svg?logo=typescript)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-7-646CFF.svg?logo=vite)](https://vitejs.dev/)
[![Android](https://img.shields.io/badge/Android-Jetpack%20Compose-3DDC84.svg?logo=android)](https://developer.android.com/jetpack/compose)
[![MariaDB](https://img.shields.io/badge/MariaDB-10+-003545.svg?logo=mariadb)](https://mariadb.org/)
[![Security](https://img.shields.io/badge/Security-JWT%20%7C%20AES--256-red.svg)]()
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

> **Comprehensive Veterinary Clinic Platform**: Full-stack solution for managing patients, appointments, medical treatments, prescriptions, clinic branches, and real-time security auditing with Web and Mobile clients.

---

## 📑 Table of Contents

- [Key Features](#-key-features)
- [Architecture & Project Structure](#-architecture--project-structure)
- [Technologies Used](#-technologies-used)
- [Security & Cryptography](#-security--cryptography)
- [Administrative Audit Log (Bitácora)](#-administrative-audit-log-bitácora)
- [User Roles](#-user-roles)
- [Prerequisites](#-prerequisites)
- [Configuration & Environment Variables](#-configuration--environment-variables)
- [Installation & Execution](#-installation--execution)
  - [1. Database Setup (MariaDB)](#1-database-setup-mariadb)
  - [2. Backend (Spring Boot)](#2-backend-spring-boot)
  - [3. Web Frontend (React + Vite)](#3-web-frontend-react--vite)
  - [4. Mobile Application (Android)](#4-mobile-application-android)
  - [5. Automated Setup with Scripts](#5-automated-setup-with-scripts)

---

## ✨ Key Features

- 🐶 **Patient Management (Pets):** Medical records, species, breeds, pet owners, and personalized tracking.
- 📅 **Comprehensive Appointment Scheduling:** Booking workflow, status transitions (Pending, Scheduled, Completed, Cancelled), cancellation flow, and filtered lookups by customer or pet.
- 🩺 **Medical Consultations & Treatments:** Diagnosis tracking, prescription management, medication registry, and clinical history.
- 📜 **Administrative Audit Logging:** Automatic auditing of login attempts and state-modifying operations (`POST`, `PUT`, `DELETE`, `PATCH`) capturing client IP, timestamp, user, role, and full operation traceability.
- 📊 **Dynamic Administrator Dashboard:** Real-time metrics, user management, clinical staff directory, full multi-table CRUD explorer, and live search filters.
- 📱 **Native Mobile Application (Android):** Built with Jetpack Compose, featuring dedicated interfaces tailored to each user role.
- 🏢 **Multi-Branch Management & Geolocation:** Branch oversight, branch managers, addresses, neighborhoods, cities, and states.

---

## 🏛 Architecture & Project Structure

The repository is organized as a modular monorepo:

```text
Project-Huellitas-Veterinary/
├── devops-proyect/         # Backend: REST API with Spring Boot 3.5 (Java 21)
│   ├── src/main/java/      # Modules: bitacora, citas, consultaMedica, mascotas, users, etc.
│   ├── src/main/resources/ # application.properties and server resources
│   └── pom.xml             # Maven dependencies & build configuration
│
├── devops-frontend/        # Web Frontend: SPA built with React, TypeScript & Vite
│   ├── src/                # Role-based pages, UI components, API client & mappers
│   ├── package.json        # Node dependencies & npm scripts
│   └── vite.config.ts      # Build pipeline & dev server configuration
│
├── devops-app-mobil/       # Mobile App: Native Android built with Kotlin & Jetpack Compose
│   ├── app/src/main/       # Activities, role screens, data models & network layer
│   └── build.gradle.kts    # Gradle build configuration
│
├── scripts/                # Fish scripts for installation, backups & automation
│   ├── instalarelproyecto.fish
│   ├── backup.fish
│   └── desintalar.fish
│
├── readme.md               # Main documentation (Spanish)
└── README_EN.md            # English documentation
```

---

## 🚀 Technologies Used

| Layer | Technologies | Description |
| :--- | :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.5, Spring Data JPA, Hibernate | Modular REST API, generic services, and MVC interceptors. |
| **Database** | MariaDB / MySQL | Relational data persistence with MariaDB dialect. |
| **Web Frontend** | React 18/19, TypeScript, Vite, Lucide Icons | Responsive, accessible, and reactive single-page interface. |
| **Android Mobile** | Kotlin, Jetpack Compose, Material 3 | Native application with role-tailored navigation. |
| **Security** | Spring Security, OAuth2 Resource Server, Nimbus JWT, BCrypt, AES-GCM | Protected endpoints, stateless token authentication, and symmetric encryption. |
| **Testing** | JUnit Jupiter, Mockito, Spring Boot Test | Unit testing for interceptors, services, and controllers. |

---

## 🔐 Security & Cryptography

The application enforces robust security standards:

1. **Stateless JWT Authentication:** Access tokens are issued upon successful authentication (`POST /usuario/login`), cryptographically signed using HMAC-SHA256 (`HmacSHA256`) with a 15-minute expiration window.
2. **AES-256-GCM Symmetric Encryption:** End-to-end data encryption for sensitive fields using `AES/GCM/NoPadding` with a 12-byte initialization vector (IV) and 128-bit authentication tags.
3. **Role-Based Access Control (RBAC):** Configured in `SecurityConfig`:
   - Endpoint `/bitacora/**` is restricted strictly to `Admin` and `Administrador` authorities.
   - User administration endpoints are secured against unauthorized access.
4. **Password Security:** Salted password hashing powered by `BCryptPasswordEncoder(12)`.

---

## 📜 Administrative Audit Log (Bitácora)

The **Audit Log** system provides full traceability, compliance, and incident investigation:

* **Automatic Access Monitoring:** Logs `LOGIN_EXITOSO` (successful logins) and `LOGIN_FALLIDO` (failed logins due to invalid credentials or missing payload fields) directly from `UsuarioController`, recording the client's IP address.
* **Global Action Interceptor (`BitacoraInterceptor`):** Automatically intercepts and logs every state-modifying action (`POST`, `PUT`, `DELETE`, `PATCH`) across all controllers in the application.
* **Recorded Audit Fields per Event:**
  - `ID`: Unique auto-incremented event identifier.
  - `Date & Time`: Official server timestamp.
  - `User`: Authenticated account email (or *Anónimo* if unauthenticated).
  - `Role`: Verified role claim extracted from the JWT token.
  - `Action`: Standardized action code (e.g., `CREAR_CITA`, `CANCELAR_CITA`, `ELIMINAR_MASCOTA`).
  - `Module`: System component (`Citas`, `Mascotas`, `Usuarios`, `Tratamientos`, etc.).
  - `Detail`: HTTP method, target URI, and outcome description (HTTP status code and error messages if applicable).
  - `IP`: Remote client IP address (with `X-Forwarded-For` proxy detection).
* **Independent Transactions (`REQUIRES_NEW`):** Audit entries are committed in their own transaction scope, guaranteeing that failed requests or rolled-back transactions are still permanently recorded.

---

## 👥 User Roles

The platform dynamically adjusts navigation and permissions according to the user's role:

| Role | ID | Scope |
| :--- | :---: | :--- |
| **Administrator** | `1` | Full administrative control: user and staff management, pets, appointments, treatments, audit log viewer, and multi-table maintenance. |
| **Pet Owner / Client** | `2` | Customer portal: register and view personal pets, request and cancel appointments, and consult active treatments/prescriptions. |
| **Branch Manager / Staff** | `3` | Operational management: appointment schedule tracking, patient consultations, and medical treatment assignments. |

---

## 💻 Prerequisites

- **Java Development Kit (JDK) 21** or higher.
- **Node.js** (v20+ or v24 LTS recommended) and **npm** package manager.
- **MariaDB** or **MySQL** (v10.5+).
- **Android Studio** with Android SDK (if building the mobile application).
- **bash** or **fish** shell.

---

## 🔑 Configuration & Environment Variables

The backend requires 64-character hexadecimal cryptographic keys (256 bits).

Create a `.env` file in the root directory of `devops-proyect/` (or configure them as environment variables):

```dotenv
# Hex-encoded cryptographic keys (64 characters = 256 bits)
JWT_SECRET=your_64_character_hex_secret_key_for_signing_jwt_tokens_here
AES_SECRET=your_64_character_hex_secret_key_for_aes_gcm_encryption_here
HMAC_SECRET=your_64_character_hex_secret_key_for_hmac_validation_here
```

> **Tip:** You can generate secure 64-character hex keys using OpenSSL:
> ```bash
> openssl rand -hex 32
> ```

---

## 📦 Installation & Execution

### 1. Database Setup (MariaDB)

Create the database and database user configured in `application.properties`:

```sql
CREATE DATABASE IF NOT EXISTS huellitas_db;
CREATE USER IF NOT EXISTS 'Admin'@'localhost' IDENTIFIED BY 'a123b123';
GRANT ALL PRIVILEGES ON huellitas_db.* TO 'Admin'@'localhost';
FLUSH PRIVILEGES;
```

---

### 2. Backend (Spring Boot)

Navigate to the backend directory and launch the application:

```bash
cd devops-proyect

# Compile classes and run tests
./mvnw test-compile

# Start the Spring Boot server (port 8080 by default)
./mvnw spring-boot:run
```

---

### 3. Web Frontend (React + Vite)

Navigate to the frontend directory, install dependencies, and start the development server:

```bash
cd devops-frontend

# Install dependencies
npm install

# Start development server (port 5173 by default)
npm run dev
```

Open your browser at `http://localhost:5173`.

---

### 4. Mobile Application (Android)

To build the Android application via the command line:

```bash
cd devops-app-mobil

# Build debug APK
./gradlew :app:assembleDebug
```

The compiled APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`.

---

### 5. Automated Setup with Scripts

On Arch Linux-based environments, you can run the automation scripts located in `scripts/`:

```bash
# Grant execution permissions
chmod +x scripts/*.fish

# Run full automated installer
./scripts/instalarelproyecto.fish
```

To stop background background services:

```bash
pkill -f "spring-boot" 2>/dev/null
pkill -f "node" 2>/dev/null
pkill -f "npm" 2>/dev/null
```

---

## 📄 License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.
