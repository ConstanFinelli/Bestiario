# Informe de Auditoría y Correcciones Pendientes del Sistema Bestiario

**Fecha de Actualización:** Octubre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE 10 / MVC / Apache Tomcat / MySQL 8)  
**Ubicación del Documento:** `docs/correcciones.md`  
**Dictamen de Entrega:** 🔴 **NO ESTÁ LISTO PARA SER ENTREGADO EN SU ESTADO ACTUAL**

---

## Índice

1. [Dictamen Ejecutivo](#1-dictamen-ejecutivo)
2. [Vulnerabilidades de Seguridad y Control de Acceso](#2-vulnerabilidades-de-seguridad-y-control-de-acceso)
3. [Documentación y Requisitos de Entrega](#3-documentación-y-requisitos-de-entrega)
4. [Plan de Acción Priorizado](#4-plan-de-acción-priorizado)

---

## 1. Dictamen Ejecutivo

El proyecto compila exitosamente bajo Maven (`mvn compile` / `mvn package` -> `BUILD SUCCESS`) y cuenta con una arquitectura base MVC funcional, mapeo entidad-relacional e integraciones externas (Cloudinary, Jakarta Mail).

Sin embargo, **se encuentra en fase final de preparación para entrega**, restando únicamente:
- **Seguridad y Control de Acceso:** ✅ **Resuelto** (AuthFilter y Hashing BCrypt implementados).
- **Documentación:** `README.md` pendiente de completar con instrucciones de instalación, variables `.env` y restauración de BD.

---

## 2. Vulnerabilidades de Seguridad y Control de Acceso

### 2.1. Inexistencia de Filtros (`Filter` / `@WebFilter`) y Endpoints Desprotegidos
- **Diagnóstico:** El proyecto no poseía ningún filtro Jakarta EE (`Filter` / `@WebFilter`). De más de 40 servlets en el sistema, más de 30 no realizaban validación de sesión ni de rol.
- **Efecto:** Cualquier usuario anónimo (mediante Postman, curl o formularios manipulados) podía ejecutar operaciones críticas sin autenticación.
- **Solución Implementada:** Se implementó `AuthFilter` (`@WebFilter("/*")` en [`AuthFilter.java`](file:///Users/constantinofinelli/Desktop/Bestiario/src/main/java/filters/AuthFilter.java)), protegiendo mediante control de acceso por 3 zonas (Pública, Autenticada, Investigador) todos los endpoints administrativos (`/admin/*`), postulaciones y candidaturas (`/investigadores/*`), gestión de usuarios (`/lectores/*`), y mutaciones CRUD (bestias, hábitats, categorías, evidencias, comentarios y noticias), preservando la URL de origen tras el login.

---

## 3. Documentación y Requisitos de Entrega

### 3.1. `README.md` Incompleto
- **Diagnóstico:** El archivo `README.md` actual contiene únicamente 4 enlaces a carpetas de Google Drive.
- **Solución:** Proveer una documentación completa que detalle:
  1. Requisitos de entorno (JDK 21, Apache Tomcat 10.1+, MySQL 8.x).
  2. Instrucciones para levantar la base de datos a partir de `docs/bestiario.sql` y `docs/seed_data.sql`.
  3. Guía de configuración del archivo `.env` basada en `.env.example` (Cloudinary, Gmail SMTP, credenciales BD).
  4. Credenciales de usuarios de prueba predefinidos (administrador/investigador y lector).
  5. Instrucciones de compilación y empaquetado con Maven (`mvn clean package`).

---

## 4. Plan de Acción Priorizado

### Fase 1: Correcciones Críticas Inmediatas (Imprescindibles para Aprobar)
- [x] **Completado:** Implementar `AuthFilter` (`@WebFilter`) para proteger endpoints administrativos y de mutación (`/admin/*`, `/investigadores/*`, eliminación y altas). Documentado en [`plan_implementacion_auth_filter.md`](file:///Users/constantinofinelli/Desktop/Bestiario/docs/plan_implementacion_auth_filter.md).

### Fase 2: Seguridad y Robustez de Errores
- [x] **Completado:** Reemplazar la codificación Base64 en `LogicUsuario` por hashing criptográfico unidireccional (BCrypt con salt y migración transparente lazy).

### Fase 3: Documentación y Entrega
- [ ] Completar `README.md` con instrucciones de instalación, prerequisitos, guía de variables `.env`, restauración de base de datos y usuarios de prueba.

---

_Documento de seguimiento técnico y control de calidad - Proyecto Bestiario._
