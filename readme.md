# 🐾 Project Huellitas - Sistema de Gestión Veterinaria

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.java.com/)
[![Spring-Boot](https://img.shields.io/badge/Spring_boot-orange)](https://spring.io/projects/spring-boot)
[![JavaScript](https://img.shields.io/badge/JavaScript-ES6-yellow)](https://developer.mozilla.org/)
[![CSS](https://img.shields.io/badge/CSS-3-blue)](https://www.w3.org/Style/CSS/)
[![React](https://img.shields.io/badge/React-blue)](https://es.react.dev/)
[![License](https://img.shields.io/badge/Licencia-MIT-green)](LICENSE)
[![Status](https://img.shields.io/badge/Estado-En%20Desarrollo-brightgreen)]()

> **Sistema completo para clínicas veterinarias** - Gestión de pacientes, citas, dueños y recetas con interfaz moderna y segura.

---

## ✨ Características principales

- 🐶 **Gestión de pacientes** (mascotas): historial médico, vacunas, tratamientos
- 📅 **Sistema de citas**  con una tabla donde se indica el estatus de la cita
- 👨‍👩‍👧‍👦 **Módulo de dueños** con control de múltiples mascotas
- 🔐 **Seguridad avanzada**: encriptación de datos y generación criptográfica
- 💊 **Recetas electrónicas** e historial de medicamentos
- 🎨 **Interfaz intuitiva** con menús laterales y diseño responsive
- 📊 **Dashboard** con métricas clave de la clínica

---

## 🚀 Tecnologías utilizadas

| Capa | Tecnologías |
|------|-------------|
| **Backend** | Java 21 (Spring Boot) |
| **Frontend** | JavaScript (ES6), HTML5, CSS3, React |
| **Seguridad** | Encriptación personalizada, generación de códigos seguros |
| **Estilos** | CSS moderno con menús laterales dinámicos |

---

## 📦 Instalación y ejecución

### Requisitos previos
- Java 21
- Navegador web moderno (Chrome, Firefox, Edge)
- (Opcional) Git

### Pasos rápidos

```fish
# 1. En la carpeta de scripts se descargan las dependencias desde el archivo backup.fish y darle permisos de ejecucion

chmod +x scripts/backup.fish

# 2. Ejecutarlo

./backup.fish

# NOTA: el proyecto se va a ejecutar apenas se instale, para detenerlo usar los siguientes comandos:

pkill -f "spring-boot" 2>/dev/null
pkill -f "node" 2>/dev/null
pkill -f "npm" 2>/dev/null
