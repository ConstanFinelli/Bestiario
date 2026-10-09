# Informe de Auditoría y Correcciones Pendientes del Sistema Bestiario

**Fecha de Actualización:** Octubre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE 10 / MVC / Apache Tomcat / MySQL 8)  
**Ubicación del Documento:** `docs/correcciones.md`  
**Dictamen de Entrega:** 🔴 **NO ESTÁ LISTO PARA SER ENTREGADO EN SU ESTADO ACTUAL**

---

## Índice

1. [Dictamen Ejecutivo](#1-dictamen-ejecutivo)
2. [Vulnerabilidades de Seguridad y Control de Acceso](#2-vulnerabilidades-de-seguridad-y-control-de-acceso)
3. [Manejo de Errores, Robustez y Experiencia de Usuario](#3-manejo-de-errores-robustez-y-experiencia-de-usuario)
4. [Documentación y Requisitos de Entrega](#4-documentación-y-requisitos-de-entrega)
5. [Plan de Acción Priorizado](#5-plan-de-acción-priorizado)

---

## 1. Dictamen Ejecutivo

El proyecto compila exitosamente bajo Maven (`mvn compile` / `mvn package` -> `BUILD SUCCESS`) y cuenta con una arquitectura base MVC funcional, mapeo entidad-relacional e integraciones externas (Cloudinary, Jakarta Mail).

Sin embargo, **no está listo para ser entregado** debido a la presencia de:
- **Fallas críticas de seguridad:** Endpoints administrativos sin autenticación ni filtros de acceso y contraseñas reversibles en Base64.
- **Pérdida de mensajes de error:** Anti-patrón de asignación a `request` previo a `response.sendRedirect(...)`.
- **Documentación incompleta:** `README.md` sin instrucciones de instalación, variables ni restauración de BD.

---

## 2. Vulnerabilidades de Seguridad y Control de Acceso

### 2.1. Inexistencia de Filtros (`Filter` / `@WebFilter`) y Endpoints Desprotegidos
- **Diagnóstico:** El proyecto no posee ningún filtro Jakarta EE (`Filter` / `@WebFilter`). De más de 40 servlets en el sistema, más de 30 no realizan validación de sesión ni de rol.
- **Efecto:** Cualquier usuario anónimo (mediante Postman, curl o formularios manipulados) puede ejecutar operaciones críticas sin autenticación:
  - `POST /investigadores/aprobarSolicitud?idUsuario=X` -> Asciende a cualquier usuario a investigador ([`AprobarSolicitud.java`]).
  - `POST /investigadores/rechazarSolicitud?idUsuario=X` -> Blanquea el perfil del usuario ([`RechazarSolicitud.java`]).
  - `GET /investigadores/listarSolicitantes` -> Expone listado con DNI, nombre y correos de postulantes ([`ListarSolicitantes.java`]).
  - `POST /bestias/eliminar?id=X` -> Borra una bestia de la BD ([`EliminarBestia.java`]).
  - Operaciones CRUD sobre categorías, hábitats, tipos de evidencia y noticias totalmente abiertas.
- **Solución:** Implementar un `AuthFilter` que intercepte `/admin/*`, `/investigadores/*` y endpoints de mutación, verificando sesión activa y rol (`investigador` / `lector`).

### 2.2. Contraseñas Codificadas en Base64 (Falsa Encriptación)
- **Ubicación:** `src/main/java/logic/LogicUsuario.java` (líneas 53-61)
- **Diagnóstico:**
  ```java
  public static String hashPassword(String pass) {
      String encoded = Base64.getEncoder().encodeToString(pass.getBytes(StandardCharsets.UTF_8));
      return encoded.toString();
  }
  public static String dehashPassword(String pass) {
      String decoded = new String(Base64.getDecoder().decode(pass.getBytes(StandardCharsets.UTF_8)));
      return decoded.toString();
  }
  ```
- **Efecto:** Base64 **no es un algoritmo criptográfico ni de hashing**, sino una simple codificación de texto plano totalmente reversible. Cualquier persona con acceso de lectura a la base de datos puede descifrar todas las contraseñas al instante. En una evaluación formal de desarrollo de software esto constituye un motivo habitual de reprobación.
- **Solución:** Implementar hashing criptográfico unidireccional con salt mediante BCrypt (o PBKDF2 / SHA-256 con salt aleatorio).

---

## 3. Manejo de Errores, Robustez y Experiencia de Usuario

### 3.1. Pérdida de Feedback por `request.setAttribute` Previo a `sendRedirect`
- **Ubicaciones:**
  - `src/main/java/servlet/Investigador/AprobarSolicitud.java` (líneas 38-40, 43-45)
  - `src/main/java/servlet/Investigador/RechazarSolicitud.java` (líneas 40-42, 45-47)
  - `src/main/java/servlet/bestia/EliminarBestia.java` (líneas 38-41)
- **Diagnóstico:** Se guarda el mensaje en el `request` y luego se ejecuta una redirección:
  ```java
  request.setAttribute("errorGlobal", "La id del usuario es inválida. ");
  response.sendRedirect(HttpRoutes.SOLICITUDES_INVESTIGADOR_JSP(request.getContextPath()));
  ```
- **Efecto:** Al redirigir, se inicia un nuevo ciclo de petición HTTP y los atributos del `request` original se descartan. El usuario no recibe ninguna notificación de error.
- **Solución:** Utilizar `session.setAttribute("errorMsg", ...)` (y consumirlo/removerlo en la vista receptora) o cambiar a `RequestDispatcher.forward(...)` cuando corresponda mantener los atributos del request.

---

## 4. Documentación y Requisitos de Entrega

### 4.1. `README.md` Incompleto
- **Diagnóstico:** El archivo `README.md` actual contiene únicamente 4 enlaces a carpetas de Google Drive.
- **Solución:** Proveer una documentación completa que detalle:
  1. Requisitos de entorno (JDK 21, Apache Tomcat 10.1+, MySQL 8.x).
  2. Instrucciones para levantar la base de datos a partir de `docs/bestiario.sql` y `docs/seed_data.sql`.
  3. Guía de configuración del archivo `.env` basada en `.env.example` (Cloudinary, Gmail SMTP, credenciales BD).
  4. Credenciales de usuarios de prueba predefinidos (administrador/investigador y lector).
  5. Instrucciones de compilación y empaquetado con Maven (`mvn clean package`).

---

## 5. Plan de Acción Priorizado

### Fase 1: Correcciones Críticas Inmediatas (Imprescindibles para Aprobar)
- [ ] Implementar `AuthFilter` (`@WebFilter`) para proteger endpoints administrativos y de mutación (`/admin/*`, `/investigadores/*`, eliminación y altas).

### Fase 2: Seguridad y Robustez de Errores
- [ ] Reemplazar la codificación Base64 en `LogicUsuario` por hashing criptográfico unidireccional (BCrypt o SHA-256 + salt).
- [ ] Corregir la pérdida de feedback de error (`request.setAttribute` + `sendRedirect`) en `AprobarSolicitud`, `RechazarSolicitud` y `EliminarBestia` migrando a atributos de sesión o forward.

### Fase 3: Documentación y Entrega
- [ ] Completar `README.md` con instrucciones de instalación, prerequisitos, guía de variables `.env`, restauración de base de datos y usuarios de prueba.

---

_Documento de seguimiento técnico y control de calidad - Proyecto Bestiario._
