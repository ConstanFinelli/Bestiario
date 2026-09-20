# Informe de Auditoría y Propuesta de Correcciones del Sistema Bestiario

**Fecha de Creación:** Septiembre 2026  
**Última Actualización:** Septiembre 2026 (Seguimiento de correcciones aplicadas)  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE / MVC / Tomcat)  
**Ubicación del Documento:** `docs/correcciones.md`  

---

## Índice
1. [Resumen Ejecutivo y Estado de Avance](#resumen-ejecutivo-y-estado-de-avance)
2. [Rutas HTTP No Utilizadas y Referencias Rotas (`helpers.HttpRoutes`)](#1-rutas-http-no-utilizadas-y-referencias-rotas)
3. [Servlets Huérfanos o Nunca Invocados (`@WebServlet`)](#2-servlets-huérfanos-o-nunca-invocados)
4. [Métodos, Servlets y Lógica Repetida / Código Muerto](#3-métodos-servlets-y-lógica-repetida--código-muerto)
5. [Atributos No Utilizados en Entidades de Dominio (`entities.*`)](#4-atributos-no-utilizados-en-entidades-de-dominio)
6. [Auditoría Integral de Logging (`java.util.logging.Logger`)](#5-auditoría-integral-de-logging)
7. [Plan de Acción Recomendado y Registro de Progreso](#6-plan-de-acción-recomendado-y-registro-de-progreso)

---

## Resumen Ejecutivo y Estado de Avance

Se realizó una auditoría estática y dinámica exhaustiva sobre todo el código fuente del proyecto (`src/main/java` y `src/main/webapp`), analizando:
- Enrutamiento y vistas declaradas en `HttpRoutes.java`.
- Mapeos de servlets `@WebServlet` y su invocabilidad real desde la interfaz de usuario (JSP) o llamadas internas.
- Código muerto, métodos duplicados y consultas innecesarias en capas `data`, `logic` y `servlet`.
- Atributos huérfanos o de sólo-escritura en las entidades (`entities.*`).
- Calidad, cobertura y consistencia en el manejo de logs y excepciones mediante `java.util.logging.Logger`.

### Registro de Últimos Cambios Aplicados
> [!NOTE]
> **Últimas correcciones realizadas en el repositorio:**
> 1. **`ListarHabitat.java`:** Se eliminó la bifurcación condicional al JSP inexistente `/habitats/habitats.jsp`, despachando uniformemente a `HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats"` para prevenir error 404 en runtime.
> 2. **Ruta de Recuperación de Contraseña (`/auth/resetPassword`):**
>    - Se agregó el helper `RESET_PASSWORD(base)` en `HttpRoutes.java`.
>    - Se actualizó `src/main/webapp/auth/resetPassword.jsp` utilizando `HttpRoutes.RESET_PASSWORD(request.getContextPath())`.
>    - Se actualizó la anotación en `SvResetPassword.java` a `@WebServlet("/auth/resetPassword")`.
>    - Se unificó el enlace de correo en `SvForgotPassword.java` usando `HttpRoutes.RESET_PASSWORD(...)`.
> 3. **`HttpRoutes.java`:** Se eliminaron 16 métodos no utilizados o rotos en total:
>    - 4 resueltos previamente (`BESTIA_FORMS_JSP`, `CARAC_HABITAT_FORM_JSP`, `NOTICIA_FORM_JSP`, `HABITATS_CSS`).
>    - 5 métodos a JSPs inexistentes (`HABITAT_FORM_JSP`, `HABITATS_JSP`, `CATEGORIA_FORM_JSP`, `EVIDENCIA_FORM_JSP`, `TIPO_EVIDENCIA_FORM_JSP`).
>    - 7 métodos a servlets huérfanos (`OBTENER_HABITAT`, `OBTENER_CATEGORIA`, `OBTENER_EVIDENCIA`, `LISTAR_EVIDENCIAS`, `LISTAR_EVIDENCIAS_TIPO`, `ACTUALIZAR_EVIDENCIA`, `OBTENER_TIPO_EVIDENCIA`).
> 4. **Eliminación de Servlets Huérfanos:** Se eliminaron físicamente los 7 servlets muertos:
>    - `servlet.categoria.ObtenerCategoria`
>    - `servlet.habitat.ObtenerHabitat`
>    - `servlet.evidencia.ListarEvidencias`
>    - `servlet.evidencia.ListarEvidenciasTipo`
>    - `servlet.evidencia.ObtenerEvidencia`
>    - `servlet.evidencia.ObtenerTipoEvidencia`
>    - `servlet.evidencia.ActualizarEvidencia`
> 5. **Archivos Estáticos:** Se eliminó físicamente el archivo huérfano `src/main/webapp/css/habitats.css`.

### Métricas Actualizadas de Hallazgos
- **Rutas no utilizadas en `HttpRoutes.java`:** 11 identificadas inicialmente ➔ **11 resueltas (0 pendientes)**.
- **Rutas JSP a archivos inexistentes:** 8 identificadas inicialmente ➔ **8 resueltas (0 pendientes)**.
- **Archivo CSS huérfano:** `habitats.css` ➔ **Resuelto** (eliminado de disco y de `HttpRoutes`).
- **7 servlets huérfanos:** ➔ **Resuelto** (eliminados físicamente del proyecto).
- **1 ruta HTTP crítica faltante en `HttpRoutes`:** ➔ **Resuelto** (`RESET_PASSWORD` agregada y centralizada).
- **3 métodos DAO muertos** y **2 métodos públicos que deberían ser privados** (pendientes).
- **1 consulta N+1 innecesaria** en cada obtención de Bestia (`completarBestia`) (pendiente).
- **502 líneas de `System.out.println` / `e.printStackTrace()`** en 13 clases de producción (capas `data`, `logic` y `listeners`) (pendientes).
- **22 clases de backend sin Logger configurado** (pendientes).
- **3 errores en definición de Logger** (apuntando a clases equivocadas o con modificadores incorrectos) (pendientes).
- **8 capturas de excepción donde se pierde el objeto `Throwable`** al invocar `logger.log(...)` (pendientes).
- **4 mensajes de log con texto copiado y pegado erróneo** (hacen referencia a servlets o entidades equivocadas) (pendientes).

---

## 1. Rutas HTTP No Utilizadas y Referencias Rotas

### 1.1. Estado de los Métodos de `HttpRoutes.java` sin referencias

| Método en `HttpRoutes` | URL / Recurso Retornado | Diagnóstico | Estado |
|---|---|---|:---:|
| `BESTIA_FORMS_JSP(base)` | `/bestias/bestiaForms.jsp` | Vista inexistente en disco. No se usa. | **RESUELTO** (Eliminado) |
| `CARAC_HABITAT_FORM_JSP(base)` | `/habitats/carHabitatForms.jsp` | Vista inexistente. La gestión se hace en `adminCarHabitats.jsp`. | **RESUELTO** (Eliminado) |
| `NOTICIA_FORM_JSP(base)` | `/noticias/noticiaForms.jsp` | Vista inexistente. La redacción se realiza en `redactarNoticia.jsp`. | **RESUELTO** (Eliminado) |
| `HABITATS_CSS(base)` | `/css/habitats.css` | Archivo CSS residual eliminado; la vista usa `adminDashboard.css`. | **RESUELTO** (Eliminado) |
| `OBTENER_HABITAT(base)` | `/habitats/obtener` | Inconsistencia de nombre y servlet huérfano. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `OBTENER_CATEGORIA(base)` | `/categorias/obtener` | Servlet huérfano. La edición se realiza directamente en `adminCategorias.jsp`. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `OBTENER_EVIDENCIA(base)` | `/evidencias/obtener` | Servlet huérfano. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `LISTAR_EVIDENCIAS(base)` | `/evidencias/listar` | Servlet huérfano. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `LISTAR_EVIDENCIAS_TIPO(base)` | `/evidencias/listarPorTipo` | Servlet huérfano. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `ACTUALIZAR_EVIDENCIA(base)` | `/evidencias/actualizar` | Servlet huérfano sin enlace en interfaz. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |
| `OBTENER_TIPO_EVIDENCIA(base)` | `/evidencias/obtenerTipoEvidencia` | Servlet huérfano. La gestión se hace en `adminTipoEvidencia.jsp`. | **RESUELTO** (Eliminado de `HttpRoutes` y servlet borrado) |

---

### 1.2. Archivos JSP declarados que NO existen en `src/main/webapp/`
Si algún flujo de ejecución despacha o redirige a estas rutas, el servidor retornará un error **HTTP 404**:

1. `/bestias/bestiaForms.jsp` ➔ **RESUELTO** (Método `BESTIA_FORMS_JSP` eliminado de `HttpRoutes`).
2. `/habitats/carHabitatForms.jsp` ➔ **RESUELTO** (Método `CARAC_HABITAT_FORM_JSP` eliminado de `HttpRoutes`).
3. `/noticias/noticiaForms.jsp` ➔ **RESUELTO** (Método `NOTICIA_FORM_JSP` eliminado de `HttpRoutes`).
4. `/habitats/habitatForms.jsp` ➔ **RESUELTO** (Método `HABITAT_FORM_JSP` eliminado de `HttpRoutes` y servlet `ObtenerHabitat.java` eliminado).
5. `/habitats/habitats.jsp` ➔ **RESUELTO** (`ListarHabitat.java` corregido para despachar a `adminDashboard.jsp?crud=habitats` y método `HABITATS_JSP` eliminado de `HttpRoutes`).
6. `/categorias/categoriaForms.jsp` ➔ **RESUELTO** (Método `CATEGORIA_FORM_JSP` eliminado de `HttpRoutes` y servlet `ObtenerCategoria.java` eliminado).
7. `/evidencias/evidenciaForms.jsp` ➔ **RESUELTO** (Método `EVIDENCIA_FORM_JSP` eliminado de `HttpRoutes` y 4 servlets de evidencias eliminados).
8. `/evidencias/tipoEvidenciaForms.jsp` ➔ **RESUELTO** (Método `TIPO_EVIDENCIA_FORM_JSP` eliminado de `HttpRoutes` y servlet `ObtenerTipoEvidencia.java` eliminado).

---

### 1.3. Rutas Faltantes en `HttpRoutes.java` (Hardcodeadas)
- En `src/main/webapp/auth/resetPassword.jsp` (línea 21): ➔ **RESUELTO**
  Se incorporó `HttpRoutes.RESET_PASSWORD(base)` retornando `base + "/auth/resetPassword"`. Se actualizaron:
  - Formulario de `resetPassword.jsp` utilizando `HttpRoutes.RESET_PASSWORD(request.getContextPath())`.
  - Servlet `SvResetPassword.java` con anotación `@WebServlet("/auth/resetPassword")`.
  - Servlet `SvForgotPassword.java` construyendo el enlace con `HttpRoutes.RESET_PASSWORD(...)`.

---

### 1.4. Solución Aplicada para Enrutamiento
1. **Eliminados los 7 métodos huérfanos** de `HttpRoutes.java` vinculados a los servlets que no se usan (`OBTENER_HABITAT`, `OBTENER_CATEGORIA`, etc.).
2. **Corregido `ListarHabitat.java`**:
   Se eliminó la bifurcación condicional a la vista inexistente `/habitats/habitats.jsp`. Todo reenvío despacha uniformemente a:
   ```java
   rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
   ```
3. **Agregada la ruta centralizada en `HttpRoutes.java`**:
   ```java
   public static String RESET_PASSWORD(String base) {
       return base + "/auth/resetPassword";
   }
   ```
4. **Eliminados los 5 métodos de JSPs inexistentes restantes** en `HttpRoutes.java` (`HABITAT_FORM_JSP`, `HABITATS_JSP`, `CATEGORIA_FORM_JSP`, `EVIDENCIA_FORM_JSP`, `TIPO_EVIDENCIA_FORM_JSP`).

---

## 2. Servlets Huérfanos o Nunca Invocados (RESUELTO - Eliminados)

Se detectaron 7 servlets mapeados con `@WebServlet` que no tenían ningún llamado desde la interfaz web, ni desde JavaScript (`fetch`/`AJAX`), ni redirecciones de otros servlets. Además, todos ellos intentaban despachar a archivos JSP que no existen en el proyecto. **Los 7 servlets fueron removidos físicamente del repositorio**:

### Detalle de Servlets Huérfanos Eliminados

| Servlet | URL Mapeada | Despachaba a (Inexistente) | Causa de la Condición Huérfana / Estado |
|---|---|---|---|
| `ObtenerCategoria.java` | `/categorias/obtener` | `CATEGORIA_FORM_JSP` | La gestión y edición de categorías se realiza de forma interactiva en la misma página `adminCategorias.jsp`. ➔ **ELIMINADO** |
| `ObtenerHabitat.java` | `/habitat/obtener` *(singular)* | `HABITAT_FORM_JSP` | La consulta y edición de hábitats se realiza en `adminHabitats.jsp`. ➔ **ELIMINADO** |
| `ListarEvidencias.java` | `/evidencias/listar` | `EVIDENCIA_FORM_JSP` | Las evidencias se visualizan contextualizadas dentro de cada registro en `registro.jsp` o en el panel de bestias. ➔ **ELIMINADO** |
| `ListarEvidenciasTipo.java` | `/evidencias/listarPorTipo` | `EVIDENCIA_FORM_JSP` | No existe ningún filtro de evidencias por tipo expuesto en la UI. ➔ **ELIMINADO** |
| `ObtenerEvidencia.java` | `/evidencias/obtener` | `EVIDENCIA_FORM_JSP` | Las evidencias se cargan asociadas a las bestias/registros mediante `LogicEvidencia` y `LogicBestia`. ➔ **ELIMINADO** |
| `ObtenerTipoEvidencia.java` | `/evidencias/obtenerTipoEvidencia` | `TIPO_EVIDENCIA_FORM_JSP` | La administración de tipos de evidencia se realiza mediante modal/formulario en `adminTipoEvidencia.jsp`. ➔ **ELIMINADO** |
| `ActualizarEvidencia.java` | `/evidencias/actualizar` | `EVIDENCIA_FORM_JSP` | No existe interfaz de edición de evidencias existentes (sólo creación, aprobación y eliminación). ➔ **ELIMINADO** |

---

## 3. Métodos, Servlets y Lógica Repetida / Código Muerto

### 3.1. Métodos Muertos en Capa DAO (`DataBestia.java`)
1. **`deleteCategorias(Bestia b)` y `deleteHabitats(Bestia b)`**:
   - En `DataBestia.java` (líneas 224-229), el método `delete(Bestia b)` invoca manualmente a `deleteCategorias(b)` y `deleteHabitats(b)`.
   - Sin embargo, en el esquema de base de datos (`bestiario.sql`), las tablas intermedias `bestia_categoria` y `bestia_habitat` tienen claves foráneas con restricción `ON DELETE CASCADE`.
   - Además, la desvinculación explícita se hace a través de `removeRelation(Bestia b, Categoria cat)` y `removeRelation(Bestia b, Habitat ht)`.
   - Si se requiere eliminación manual, estos métodos tienen consultas manuales duplicadas que pueden ser encapsuladas en `DataCategoria` y `DataHabitat`.
2. **`saveRegistros(Bestia b)`**:
   - En `DataBestia.save(b)` (línea 162), se invoca `saveRegistros(b)`, el cual itera sobre `b.getRegistros()` y llama a `regDAO.save(registro)`.
   - Cuando se da de alta una bestia en `CrearBestia.java`, la lista de registros siempre está vacía (`registros` se inicializa como `new LinkedList<>()`).
   - Los registros tienen su propio ciclo de vida independiente mediante `CrearRegistro.java` y `ActualizarRegistro.java` asociados a un usuario investigador. `saveRegistros(b)` es código muerto que induce a error pensando que una bestia crea sus propios registros automáticamente.

### 3.2. Métodos Auxiliares con Visibilidad Excesiva
En `DataEvidencia.java` (línea 191) y `DataRegistro.java` (línea 398):
- `public void asignarNroEvidencia(Evidencia e)`
- `public void asignarNroRegistro(Registro r)`
Ambos métodos sólo se llaman internamente desde sus propios DAOs (durante el método `save` para autocalcular el siguiente número de secuencia en la tabla).
**Solución:** Cambiar el modificador de acceso de `public` a `private`.

### 3.3. Sobrecarga Innecesaria en `DataBestia.completarBestia()`
Al recuperar cualquier bestia con `DataBestia.getOne(bestia)`, se ejecuta `completarBestia(bestia)`:
```java
public void completarBestia(Bestia bestia) {
    addRegistros(bestia);
    addHabitats(bestia);
    addCategorias(bestia);
    addComentarios(bestia);
    addEvidencias(bestia);
}
```
- `addRegistros(bestia)` ejecuta una consulta SQL a la base de datos `findAllByBestia` para poblar la lista `registros`.
- **Ninguna vista ni lógica de negocio lee `bestia.getRegistros()`**. Toda la gestión de registros se realiza directamente consultando `LogicRegistro.getRegistrosBestia(bestia)` o `LogicRegistro.getRegistroActual(bestia)`.
- Esto provoca una consulta costosa adicional a la base de datos cada vez que se busca o lista una bestia.
- **Solución:** Retirar `addRegistros(bestia)` de `completarBestia()`.

### 3.4. Inconsistencias de Nombres en Servlets de Hábitats
En el paquete `servlet.habitat`:
- `CrearCaracteristicaHabitat.java`
- `ActualizarCaracteristica.java`  *(falta "Habitat" en el nombre de la clase)*
- `EliminarCaracteristica.java`    *(falta "Habitat" en el nombre de la clase)*
A pesar de tener nombres dispares, las tres anotaciones `@WebServlet` usan `/habitats/*CaracteristicaHabitat`.
**Solución:** Normalizar los nombres de las clases a `ActualizarCaracteristicaHabitat.java` y `EliminarCaracteristicaHabitat.java`.

### 3.5. Disparidad en Manejo de Errores Globales (`errorGlobal`)
En varios servlets (`CrearEvidencia`, `ListarEvidencias`, `ActualizarEvidencia`, `ObtenerEvidencia`, etc.) se repite el siguiente patrón:
```java
List<String> errores = new ArrayList<>();
...
if(!errores.isEmpty()) {
    errores.add("");
    request.setAttribute("errorGlobal", errores);
}
```
Mientras que en otros (`ActualizarBestia`, `EliminarBestia`, `ObtenerHabitat`, etc.) se utiliza:
```java
request.setAttribute("errorGlobal", "Mensaje de error simple");
```
Y en `components/error.jsp`:
```jsp
html: '<%= request.getAttribute("errorGlobal")%><br>Por favor, intente mas tarde.',
```
- Cuando `errorGlobal` es una `List<String>`, su `toString()` se renderiza como `[Error 1, Error 2, ]` en la ventana modal de SweetAlert2, incluyendo los corchetes y un espacio final por culpa del `errores.add("")`.
- **Solución:** Unificar el manejo de errores:
  - Si se utiliza una lista, concatenar los errores con delimitador `<br>` antes de asignarlos al atributo:
    ```java
    request.setAttribute("errorGlobal", String.join("<br>", errores));
    ```
  - Eliminar el `errores.add("")`.

---

## 4. Atributos No Utilizados en Entidades de Dominio (`entities.*`)

| Entidad | Atributo | Tipo | Diagnóstico | Solución Recomendada |
|---|---|---|---|---|
| `Habitat.java` | `caracteristicas` | `LinkedList<String>` | **Nunca leído** (`gets=0`). Las características se manejan mediante la entidad `CaracteristicaHabitat` (asociada a la tabla `caracteristica_habitat`). | Eliminar el atributo y sus getters/setters en `Habitat.java`. |
| `Habitat.java` | `bestias` | `LinkedList<Bestia>` | **Nunca leído** (`gets=0`). La relación inversa es gestionada desde `Bestia` y `bestia_habitat`. | Eliminar el atributo y sus getters/setters en `Habitat.java`. |
| `Bestia.java` | `registros` | `LinkedList<Registro>` | **Solo se asigna**, nunca se consume en ninguna vista (la vista usa `LogicRegistro`). | Mantener solo si se prevé como modelo de agregación; de lo contrario, removerlo y no poblarlo en `DataBestia.completarBestia()`. |
| `Bestia.java` | `comentarios` | `LinkedList<Comentario>` | Solo se usa en el método `toString()`. Los comentarios se consultan en el servlet mediante `LogicComentario`. | Evaluar eliminación para evitar acoplamiento y consumo innecesario de memoria. |
| `Usuario.java` | `contraseña` | `String` | El nombre del atributo, getter (`getContraseña()`) y setter (`setContraseña()`) utilizan la letra **ñ**. | Aunque Java admite identificadores Unicode, las herramientas de build, plugins de compilación en distintas plataformas (Windows con encoding `windows-1252` vs Linux/CI con `UTF-8`) pueden fallar o corromper caracteres. Renombrar a `contrasena` o `password`. |

---

## 5. Auditoría Integral de Logging (`java.util.logging.Logger`)

### 5.1. Uso Masivo de `System.out.println` y `e.printStackTrace()`
Se encontraron **502 líneas** con `System.out.println` o `e.printStackTrace()` en lugar de utilizar un framework o API de logging estándar (`java.util.logging.Logger`).

**Distribución de líneas por clase:**
- `src/main/java/data/DataBestia.java`: 90 líneas
- `src/main/java/data/DataRegistro.java`: 66 líneas
- `src/main/java/data/DataUsuario.java`: 66 líneas
- `src/main/java/data/DataEvidencia.java`: 54 líneas
- `src/main/java/data/DataHabitat.java`: 39 líneas
- `src/main/java/data/DataCategoria.java`: 36 líneas
- `src/main/java/data/DataNoticia.java`: 36 líneas
- `src/main/java/data/DataComentario.java`: 30 líneas
- `src/main/java/data/DataTipoEvidencia.java`: 30 líneas
- `src/main/java/data/DataCaracteristicaHabitat.java`: 24 líneas
- `src/main/java/data/DataPasswordResetToken.java`: 24 líneas
- `src/main/java/listeners/BackgroundJobListener.java`: 4 líneas
- `src/main/java/logic/LogicEmail.java`: 3 líneas

#### Impacto en Producción de `System.out.println`:
1. **Rendimiento bloqueante:** `System.out.println` sincroniza sobre el hilo principal de la consola de Tomcat.
2. **Sin contexto:** No incluye timestamp, nivel de severidad (`INFO`, `WARNING`, `SEVERE`), identificador de hilo (`Thread-ID`), ni nombre de la clase/método origen.
3. **Pérdida de trazabilidad:** Imprimir solo `ex.getMessage()` y `ex.getSQLState()` descarta por completo el **Stack Trace**, haciendo imposible diagnosticar la línea exacta donde falló la consulta o transacción.
4. **Sin control de configuración:** No es posible desactivar los logs en producción ni redirigirlos a archivos rotativos de auditoría sin modificar el código.

---

### 5.2. Clases del Backend sin Logger Instanciado (22 Clases)
Las siguientes clases manejan excepciones críticas o lógica de negocio y **carecen totalmente de instancia de Logger**:
- **Capa DAO (11 clases):** `DataBestia`, `DataCaracteristicaHabitat`, `DataCategoria`, `DataComentario`, `DataEvidencia`, `DataHabitat`, `DataNoticia`, `DataPasswordResetToken`, `DataRegistro`, `DataTipoEvidencia`, `DataUsuario`.
- **Capa Logic (10 clases):** `LogicBestia`, `LogicCaracteristicaHabitat`, `LogicCategoria`, `LogicComentario`, `LogicEmail`, `LogicEvidencia`, `LogicHabitat`, `LogicRegistro`, `LogicTipoEvidencia`, `LogicUsuario`.
- **Listeners (1 clase):** `BackgroundJobListener`.

---

### 5.3. Declaraciones Incorrectas de Logger
1. **`LogicNoticia.java` (línea 17):**
   ```java
   private static final Logger logger = Logger.getLogger(DbConnector.class.getName());
   ```
   ❌ **Error:** Referencia a `DbConnector.class` en lugar de `LogicNoticia.class`. Los logs emitidos por el servicio de noticias saldrán categorizados erróneamente como eventos de conexión de base de datos.
   ✔️ **Corrección:**
   ```java
   private static final Logger logger = Logger.getLogger(LogicNoticia.class.getName());
   ```

2. **`servlet.categoria.ObtenerCategoria.java` (línea 24):**
   ```java
   private static final Logger logger = Logger.getLogger(ActualizarBestia.class.getName());
   ```
   ❌ **Error:** Referencia a `ActualizarBestia.class` en lugar de `ObtenerCategoria.class`.
   ✔️ **Corrección:**
   ```java
   private static final Logger logger = Logger.getLogger(ObtenerCategoria.class.getName());
   ```

3. **`servlet.habitat.EliminarCaracteristica.java` (línea 27):**
   ```java
   private final static Logger logger = Logger.getLogger(EliminarCaracteristica.class.getName());
   ```
   ❌ **Inconsistencia:** Modificador `final static` en lugar del orden canónico Java `private static final Logger`.
   ✔️ **Corrección:**
   ```java
   private static final Logger logger = Logger.getLogger(EliminarCaracteristica.class.getName());
   ```

---

### 5.4. Llamadas a `logger.log(...)` sin Pasar el `Throwable` (Stack Trace Perdido)
En las siguientes 8 ubicaciones se captura una excepción pero se invoca `logger.log(Level, String)` sin pasar la variable de la excepción, **perdiendo el stack trace**:

1. **`LogicNoticia.java` (línea 44):**
   ```java
   } catch (Exception e) {
       logger.log(Level.WARNING,"Falló envío a: " + u.getCorreo()); // Falta , e
   }
   ```
2. **`ActualizarBestia.java` (línea 50):**
   ```java
   } catch (NumberFormatException nfe) {
       logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet ActualizarBestia"); // Falta , nfe
   ```
3. **`CambiarCategoria.java` (línea 46):**
   ```java
   } catch (NumberFormatException nfe) {
       logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet CambiarCategoria"); // Falta , nfe
   ```
4. **`CrearBestia.java` (línea 51):**
   ```java
   } catch (Exception e) {
       logger.log(Level.WARNING, "Error buscando los tipos de evidencia en la base de datos en el servlet CrearBestia"); // Falta , e
   ```
5. **`EditarBestia.java` (línea 50):**
   ```java
   } catch (NumberFormatException nfe) {
       logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet ActualizarBestia"); // Falta , nfe
   ```
6. **`CrearEvidencia.java` (línea 90):**
   ```java
   } catch (Exception e) {
       logger.log(Level.WARNING, "Error parseando la fecha de obtencion en el servlet CrearEvidencia"); // Falta , e
   ```
7. **`CrearEvidencia.java` (línea 113):**
   ```java
   } catch (NumberFormatException e) {
       logger.log(Level.SEVERE, "Error al recibir el numero de tipo de evidencia en el servlet ActualizarRegistro"); // Falta , e
   ```
8. **`CrearEvidencia.java` (línea 118):**
   ```java
   } catch (DateTimeParseException ex) {
       logger.log(Level.SEVERE, "Error al parsear la fecha de obtencion de la evidencia en el servlet ActualizarRegistro"); // Falta , ex
   ```

---

### 5.5. Mensajes de Log con Texto Copiado y Pegado Erróneo
- **`CrearEvidencia.java` (líneas 113 y 118):**
  Indican `"...en el servlet ActualizarRegistro"` en lugar de `"...en el servlet CrearEvidencia"`.
- **`EditarBestia.java` (línea 50):**
  Indica `"Error parseando la id de la bestia en el servlet ActualizarBestia"` en lugar de `EditarBestia`.
- **`ObtenerEvidencia.java` (línea 66):**
  En el bloque `catch` al buscar la evidencia (`controladorEvidencia.getOne`), el log dice:
  `"Error obteniendo el Tipo de evidencia en el servlet ObtenerEvidencia"` en lugar de `"Error obteniendo la evidencia..."`.

---

### 5.6. Solución e Implementación Estándar para Capa Data y Logic

#### Patrón Recomendado para Clases DAO (`Data*.java`)
Reemplazar todos los bloques:
```java
// Código Actual Obsoleto:
} catch (SQLException ex) {
    System.out.println("Mensaje: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
}
```
Por el estándar robusto:
```java
public class DataBestia {
    private static final Logger logger = Logger.getLogger(DataBestia.class.getName());

    // ...
    } catch (SQLException ex) {
        logger.log(Level.SEVERE, String.format(
            "Error SQL al consultar bestia [SQLState: %s, ErrorCode: %d]: %s",
            ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
    }
```

#### Patrón Recomendado para `BackgroundJobListener.java` y `LogicEmail.java`
- En `BackgroundJobListener.java`:
  ```java
  private static final Logger logger = Logger.getLogger(BackgroundJobListener.class.getName());

  // Reemplazar System.out.println("⏳ [PRUEBA] Enviando resumen...");
  logger.log(Level.INFO, "Iniciando ejecución programada del resumen diario de registros.");

  // Reemplazar e.printStackTrace(); y System.out.println("❌ Falló envío a...");
  logger.log(Level.WARNING, "Falló el envío de correo de resumen a: " + investigador.getCorreo(), e);
  ```
- En `LogicEmail.java`:
  ```java
  private static final Logger logger = Logger.getLogger(LogicEmail.class.getName());

  // En lugar de System.out.println("✅ Email enviado..."):
  logger.log(Level.INFO, "Email enviado satisfactoriamente a: {0}", destinatario);

  // En lugar de catch(MessagingException e) { e.printStackTrace(); System.out...}:
  logger.log(Level.SEVERE, "Error al enviar email a: " + destinatario, e);
  ```

---

## 6. Plan de Acción Recomendado y Registro de Progreso

Para ejecutar estas correcciones de manera segura y sin generar regresiones en la aplicación, se dividen las tareas en 4 fases ordenadas por criticidad:

### Fase 1: Correcciones Críticas de Seguridad y Runtime (Inmediata)
- [x] **Corregir `ListarHabitat.java`**: Eliminar el forward a `/habitats/habitats.jsp` (evita HTTP 404).
- [x] **Agregar `HttpRoutes.RESET_PASSWORD(base)`** y actualizar `resetPassword.jsp` (elimina ruta hardcodeada).
- [ ] **Corregir las clases referenciadas en Loggers**:
  - `LogicNoticia.java`: Usar `LogicNoticia.class`.
  - `servlet.habitat.EliminarCaracteristica.java`: Normalizar a `private static final Logger`.
- [ ] **Pasar la excepción (`Throwable`) en las 8 llamadas truncadas de `logger.log`** y corregir los nombres de servlet copiados y pegados.

### Fase 2: Depuración de Código Huérfano y Rutas Inexistentes
- [x] **Eliminar rutas de formularios JSP inexistentes ya identificadas**: `BESTIA_FORMS_JSP`, `CARAC_HABITAT_FORM_JSP`, `NOTICIA_FORM_JSP` de `HttpRoutes.java`.
- [x] **Eliminar archivo de estilos residual y su helper**: Borrado `src/main/webapp/css/habitats.css` y eliminado `HABITATS_CSS(base)` de `HttpRoutes.java`.
- [x] **Remover servlets huérfanos**: `ObtenerCategoria`, `ObtenerHabitat`, `ListarEvidencias`, `ListarEvidenciasTipo`, `ObtenerEvidencia`, `ObtenerTipoEvidencia`, `ActualizarEvidencia`.
- [x] **Eliminar los 7 métodos restantes no utilizados** en `HttpRoutes.java` y referencias a JSPs inexistentes restantes (`HABITAT_FORM_JSP`, `HABITATS_JSP`, `CATEGORIA_FORM_JSP`, `EVIDENCIA_FORM_JSP`, `TIPO_EVIDENCIA_FORM_JSP`).

### Fase 3: Estandarización del Sistema de Logging
- [ ] **Incorporar `Logger` en todas las clases DAO (`Data*.java`)**, reemplazando las 495+ líneas de `System.out.println` por llamadas a `logger.log(Level.SEVERE, ..., ex)` incluyendo `SQLState` y código de proveedor.
- [ ] **Incorporar `Logger` en `LogicEmail.java` y `BackgroundJobListener.java`**, reemplazando `System.out.println` y `e.printStackTrace()`.
- [ ] **Estandarizar formato de logs** agregando un archivo de configuración base `logging.properties` en `src/main/resources` si se desea persistir logs en archivo en Tomcat.

### Fase 4: Optimización de Consultas y Limpieza de Entidades
- [ ] **Optimizar `DataBestia.completarBestia()`**: Retirar `addRegistros(bestia)` para eliminar la query N+1 que carga registros que ninguna vista utiliza.
- [ ] **Hacer privados los métodos auxiliares de DAOs**: `asignarNroEvidencia` y `asignarNroRegistro`.
- [ ] **Limpiar atributos muertos de entidades**:
  - Eliminar `caracteristicas` y `bestias` de `Habitat.java`.
- [ ] **Normalizar nombres y encoding**:
  - Renombrar `Usuario.contraseña` a `contrasena` para robustez multiplataforma.
  - Normalizar nombres de servlets de características de hábitat.
- [ ] **Estandarizar mensajes de error en la UI**:
  - Asegurar que `errorGlobal` siempre sea un `String` limpio para SweetAlert2 en `error.jsp`.

---
*Fin del documento de auditoría. Generado para el proyecto Bestiario.*
