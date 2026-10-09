# Informe de Auditoría y Correcciones Pendientes del Sistema Bestiario

**Fecha de Actualización:** Octubre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE 10 / MVC / Apache Tomcat / MySQL 8)  
**Ubicación del Documento:** `docs/correcciones.md`  
**Dictamen de Entrega:** 🔴 **NO ESTÁ LISTO PARA SER ENTREGADO EN SU ESTADO ACTUAL**

---

## Índice

1. [Dictamen Ejecutivo](#1-dictamen-ejecutivo)
2. [Bloqueantes Críticos de Ejecución y Despliegue (Showstoppers)](#2-bloqueantes-críticos-de-ejecución-y-despliegue-showstoppers)
3. [Vulnerabilidades de Seguridad y Control de Acceso](#3-vulnerabilidades-de-seguridad-y-control-de-acceso)
4. [Manejo de Errores, Robustez y Experiencia de Usuario](#4-manejo-de-errores-robustez-y-experiencia-de-usuario)
5. [Arquitectura, Persistencia y Calidad de Código](#5-arquitectura-persistencia-y-calidad-de-código)
6. [Documentación y Requisitos de Entrega](#6-documentación-y-requisitos-de-entrega)
7. [Plan de Acción Priorizado](#7-plan-de-acción-priorizado)

---

## 1. Dictamen Ejecutivo

El proyecto compila exitosamente bajo Maven (`mvn compile` / `mvn package` -> `BUILD SUCCESS`) y cuenta con una arquitectura base MVC funcional, mapeo entidad-relacional e integraciones externas (Cloudinary, Jakarta Mail).

Sin embargo, **no está listo para ser entregado** debido a la presencia de:
- **Fallas críticas de seguridad:** Endpoints administrativos sin autenticación ni filtros de acceso, contraseñas reversibles en Base64 y riesgo de inyección XSS.
- **Pérdida de mensajes de error:** Anti-patrón de asignación a `request` previo a `response.sendRedirect(...)`.
- **Fragilidad en despliegue:** Dependencia estricta de `.env` que impide el arranque fuera de entornos locales específicos y driver MySQL fuera del `pom.xml`.
- **Cero tests automatizados:** Ausencia total de pruebas en `src/test/java`.
- **Documentación incompleta:** `README.md` sin instrucciones de instalación, variables ni restauración de BD.

---

## 2. Bloqueantes Críticos de Ejecución y Despliegue (Showstoppers)

### 2.1. Fragilidad en Carga de Variables de Entorno (`EnvHelper.java`)
- **Ubicación:** `src/main/java/helpers/EnvHelper.java` (línea 6)
- **Diagnóstico:** `public static Dotenv dotEnv = Dotenv.load();` exige la presencia física de un archivo `.env` en el directorio de trabajo del proceso JVM (`CATALINA_HOME/bin/` en Tomcat).
- **Efecto:** Si un evaluador despliega el WAR en un servidor Tomcat o contenedor sin copiar manualmente el `.env` en `bin/`, la aplicación arroja `DotenvException` y falla con `ExceptionInInitializerError` al cargar cualquier clase que dependa de la base de datos o Cloudinary.
- **Solución:** Configurar `Dotenv.configure().ignoreIfMissing().load()` y proveer un fallback a variables del sistema con `System.getenv(variableName)`.

### 2.2. Driver JDBC de MySQL Omitido en `pom.xml`
- **Ubicación:** `pom.xml` vs `src/main/webapp/WEB-INF/lib/`
- **Diagnóstico:** El archivo `mysql-connector-j-9.3.0.jar` se encuentra copiado manualmente en `WEB-INF/lib/` en vez de estar declarado como dependencia en el archivo de construcción Maven.
- **Efecto:** Entornos de compilación limpia, empaquetado CI/CD o entornos que utilicen el POM carecerán del driver si no se incluye explícitamente.
- **Solución:** Añadir la dependencia formal en `pom.xml`:
  ```xml
  <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <version>9.3.0</version>
  </dependency>
  ```

---

## 3. Vulnerabilidades de Seguridad y Control de Acceso

### 3.1. Inexistencia de Filtros (`Filter` / `@WebFilter`) y Endpoints Desprotegidos
- **Diagnóstico:** El proyecto no posee ningún filtro Jakarta EE (`Filter` / `@WebFilter`). De más de 40 servlets en el sistema, más de 30 no realizan validación de sesión ni de rol.
- **Efecto:** Cualquier usuario anónimo (mediante Postman, curl o formularios manipulados) puede ejecutar operaciones críticas sin autenticación:
  - `POST /investigadores/aprobarSolicitud?idUsuario=X` -> Asciende a cualquier usuario a investigador ([`AprobarSolicitud.java`]).
  - `POST /investigadores/rechazarSolicitud?idUsuario=X` -> Blanquea el perfil del usuario ([`RechazarSolicitud.java`]).
  - `GET /investigadores/listarSolicitantes` -> Expone listado con DNI, nombre y correos de postulantes ([`ListarSolicitantes.java`]).
  - `POST /bestias/eliminar?id=X` -> Borra una bestia de la BD ([`EliminarBestia.java`]).
  - Operaciones CRUD sobre categorías, hábitats, tipos de evidencia y noticias totalmente abiertas.
- **Solución:** Implementar un `AuthFilter` que intercepte `/admin/*`, `/investigadores/*` y endpoints de mutación, verificando sesión activa y rol (`investigador` / `lector`).

### 3.2. Contraseñas Codificadas en Base64 (Falsa Encriptación)
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

### 3.3. Riesgo de XSS e Inyección de Script en `components/error.jsp`
- **Ubicación:** `src/main/webapp/components/error.jsp` (línea 28)
- **Diagnóstico:**
  ```jsp
  html: '<%= request.getAttribute("errorGlobal")%><br>Por favor, intente mas tarde.',
  ```
- **Efecto:** La cadena Java se imprime sin escape dentro del script JS de SweetAlert2. Si un mensaje contiene comillas simples (`'`), saltos de línea o texto proveniente de parámetros de entrada, rompe el script o expone a vulnerabilidades de Cross-Site Scripting (XSS).
- **Solución:** Escapar caracteres especiales con JSTL `<c:out value="..." />` o sanitizar comillas y caracteres HTML previo a la renderización en JS.

---

## 4. Manejo de Errores, Robustez y Experiencia de Usuario

### 4.1. Pérdida de Feedback por `request.setAttribute` Previo a `sendRedirect`
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

## 5. Arquitectura, Persistencia y Calidad de Código

### 5.1. Ausencia de Transacciones Relacionales (`ACID`)
- **Diagnóstico:** Ningún método de la capa DAO (`data.*`) gestiona transacciones explícitas (`conn.setAutoCommit(false)`, `conn.commit()`, `conn.rollback()`).
- **Efecto:** En operaciones compuestas (e.g. alta o edición de una bestia y sus relaciones en `bestia_categoria` y `bestia_habitat`), si una sentencia SQL secundaria falla, la base de datos queda con registros huérfanos e inconsistentes.
- **Solución:** Implementar control transaccional explícito o encapsular operaciones atómicas multi-tabla dentro de transacciones JDBC.

### 5.2. Violación de Capas en Módulos de Autenticación
- **Ubicaciones:** `src/main/java/servlet/auth/SvForgotPassword.java` (línea 49) y `src/main/java/servlet/auth/SvResetPassword.java` (líneas 33, 70).
- **Diagnóstico:** Los servlets instancian directamente `DataUsuario` y `DataPasswordResetToken`, puenteando la capa `Logic*`.
- **Solución:** Enrutar todas las operaciones a través de `LogicUsuario` y crear `LogicPasswordResetToken`.

### 5.3. Inexistencia de Pruebas Automatizadas
- **Diagnóstico:** La carpeta `src/test/java/` se encuentra vacía (0 pruebas unitarias y 0 pruebas de integración).
- **Solución:** Incorporar suite mínima de pruebas unitarias con JUnit 5 para lógica de negocio crítica (`LogicUsuario`, `LogicBestia`, validaciones de hashing y tokens).

---

## 6. Documentación y Requisitos de Entrega

### 6.1. `README.md` Incompleto
- **Diagnóstico:** El archivo `README.md` actual contiene únicamente 4 enlaces a carpetas de Google Drive.
- **Solución:** Proveer una documentación completa que detalle:
  1. Requisitos de entorno (JDK 21, Apache Tomcat 10.1+, MySQL 8.x).
  2. Instrucciones para levantar la base de datos a partir de `docs/bestiario.sql` y `docs/seed_data.sql`.
  3. Guía de configuración del archivo `.env` basada en `.env.example` (Cloudinary, Gmail SMTP, credenciales BD).
  4. Credenciales de usuarios de prueba predefinidos (administrador/investigador y lector).
  5. Instrucciones de compilación y empaquetado con Maven (`mvn clean package`).

---

## 7. Plan de Acción Priorizado

### Fase 1: Correcciones Críticas Inmediatas (Imprescindibles para Aprobar)
- [ ] Declarar la dependencia `mysql-connector-j` en `pom.xml`.
- [ ] Implementar `AuthFilter` (`@WebFilter`) para proteger endpoints administrativos y de mutación (`/admin/*`, `/investigadores/*`, eliminación y altas).
- [ ] Robustecer `EnvHelper.java` con `.ignoreIfMissing()` y fallback a `System.getenv(...)`.

### Fase 2: Seguridad y Robustez de Errores
- [ ] Reemplazar la codificación Base64 en `LogicUsuario` por hashing criptográfico unidireccional (BCrypt o SHA-256 + salt).
- [ ] Corregir la pérdida de feedback de error (`request.setAttribute` + `sendRedirect`) en `AprobarSolicitud`, `RechazarSolicitud` y `EliminarBestia` migrando a atributos de sesión o forward.
- [ ] Sanitizar o escapar el atributo `errorGlobal` en `components/error.jsp` para evitar rotura de script y riesgo XSS.

### Fase 3: Arquitectura y Calidad
- [ ] Encapsular el acceso a datos de tokens en `LogicPasswordResetToken` para eliminar la instanciación directa de DAOs en `SvForgotPassword` y `SvResetPassword`.
- [ ] Implementar manejo transaccional explícito (`commit`/`rollback`) en operaciones compuestas multi-tabla.
- [ ] Agregar suite de pruebas unitarias mínimas en `src/test/java` con JUnit 5.

### Fase 4: Documentación y Entrega
- [ ] Completar `README.md` con instrucciones de instalación, prerequisitos, guía de variables `.env`, restauración de base de datos y usuarios de prueba.

---

_Documento de seguimiento técnico y control de calidad - Proyecto Bestiario._
