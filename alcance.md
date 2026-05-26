# 🚀 Proyecto Futbol5Ya - Documento de Alcance

Este documento detalla los límites, requerimientos y tecnologías del sistema **Futbol5Ya** para la Etapa 1 del TP3, tomando como base el enunciado "Email de Cliente".

---

## 📋 Objetivo General

Desarrollar una plataforma integral para la gestión de complejos de fútbol 5, permitiendo la administración de turnos, venta de artículos deportivos y visualización de contenido multimedia, orientada tanto a clientes como a administradores del complejo.

---

## 🛠️ Stack Tecnológico
De acuerdo a las consignas del TP, el sistema se desarrollará con las siguientes tecnologías:

| Capa | Tecnología |
|------|-----------|
| **Backend** | Java + Spring Boot + Hibernate |
| **Frontend** | React.js con TypeScript |
| **Base de Datos** | MySQL |
| **Seguridad** | JWT (JSON Web Tokens) |

---

## ⚙️ Requerimientos Funcionales

El sistema incluirá los siguientes módulos:

### 🔐 Seguridad y Usuarios
* Registro e inicio de sesión de usuarios.
* **Autenticación obligatoria** en todos los módulos mediante JWT.
* Roles diferenciados: cliente y administrador.

### 🏟️ Gestión de Turnos (Reserva de Canchas)
* Visualización de disponibilidad de canchas en tiempo real.
* Alta, modificación y cancelación de reservas.
* Integración con servicios de clima para informar condiciones al momento de la reserva.
* Notificaciones de confirmación al usuario.
* Gestión de pagos reales con API (Ej: Mercado Pago).

---

## 🚫 Exclusiones del Alcance

Los siguientes aspectos quedan **fuera del alcance** de esta etapa:

* No incluye la grabación física de videos, solo su administración y reproducción en la plataforma.
* El sistema no dará soporte para la organización de torneos o ligas en esta etapa.
* No se implementará una app móvil nativa; el sistema será web responsivo.
* No se contempla la subida de multimedia a la página.
* No se tendrá en cuenta la implementación de la tienda online de camisetas.

