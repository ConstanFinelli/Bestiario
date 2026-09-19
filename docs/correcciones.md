# Informe de Auditoría y Propuesta de Correcciones del Sistema Bestiario

**Fecha:** Septiembre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE / MVC / Tomcat)  
**Ubicación del Documento:** `docs/correcciones.md`  

---

## Índice
1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Rutas HTTP No Utilizadas y Referencias Rotas (`helpers.HttpRoutes`)](#1-rutas-http-no-utilizadas-y-referencias-rotas)
3. [Servlets Huérfanos o Nunca Invocados (`@WebServlet`)](#2-servlets-huérfanos-o-nunca-invocados)
4. [Métodos, Servlets y Lógica Repetida / Código Muerto](#3-métodos-servlets-y-lógica-repetida--código-muerto)
5. [Atributos No Utilizados en Entidades de Dominio (`entities.*`)](#4-atributos-no-utilizados-en-entidades-de-dominio)
6. [Auditoría Integral de Logging (`java.util.logging.Logger`)](#5-auditoría-integral-de-logging)
7. [Plan de Acción Recomendado y Priorización](#6-plan-de-acción-recomendado-y-priorización)

---

## Resumen Ejecutivo

Se realizó una auditoría estática y dinámica exhaustiva sobre todo el código fuente del proyecto (`src/main/java` y `src/main/webapp`), analizando:
- Enrutamiento y vistas declaradas en `HttpRoutes.java`.
- Mapeos de servlets `@WebServlet` y su invocabilidad real desde la interfaz de usuario (JSP) o llamadas internas.
- Código muerto, métodos duplicados y consultas innecesarias en capas `data`, `logic` y `servlet`.
- Atributos huérfanos o de sólo-escritura en las entidades (`entities.*`).
- Calidad, cobertura y consistencia en el manejo de logs y excepciones mediante `java.util.logging.Logger`.

### Métricas Principales de Hallazgos
- **11 métodos en `HttpRoutes.java` sin uso alguno** en la aplicación.
- **8 rutas JSP declaradas en `HttpRoutes.java` que NO existen físicamente en disco** (riesgo de HTTP 404 en runtime).
- **7 servlets huérfanos** que no son invocados por ningún componente de la aplicación y despachan a vistas inexistentes.
- **1 ruta HTTP crítica faltante en `HttpRoutes`** (hardcodeada en JSP).
- **3 métodos DAO muertos** y **2 métodos públicos que deberían ser privados**.
- **1 consulta N+1 innecesaria** en cada obtención de Bestia (`completarBestia`).
- **502 líneas de `System.out.println` / `e.printStackTrace()`** en 13 clases de producción (capas `data`, `logic` y `listeners`).
- **22 clases de backend sin Logger configurado**.
- **3 errores en definición de Logger** (apuntando a clases equivocadas o con modificadores incorrectos).
- **8 capturas de excepción donde se pierde el objeto `Throwable`** al invocar `logger.log(...)`.
- **4 mensajes de log con texto copiado y pegado erróneo** (hacen referencia a servlets o entidades equivocadas).

---

## 1. Rutas HTTP No Utilizadas y Referencias Rotas

### 1.1. Métodos de `HttpRoutes.java` con 0 referencias en todo el proyecto
Los siguientes 11 métodos declarados en `helpers.HttpRoutes` nunca son invocados desde ningún servlet, JSP ni clase Java:

| Método en `HttpRoutes` | URL / Recurso Retornado | Diagnóstico |
|---|---|---|
| `BESTIA_FORMS_JSP(base)` | `/bestias/bestiaForms.jsp` | Vista inexistente en disco. No se usa. |
| `CARAC_HABITAT_FORM_JSP(base)` | `/habitats/carHabitatForms.jsp` | Vista inexistente. La gestión de características se hace en `adminCarHabitats.jsp`. |
| `OBTENER_HABITAT(base)` | `/habitats/obtener` | Inconsistencia: el servlet `ObtenerHabitat` está anotado como `/habitat/obtener` (en singular). Ninguno de los dos se llama. |
| `NOTICIA_FORM_JSP(base)` | `/noticias/noticiaForms.jsp` | Vista inexistente. La redacción se realiza en `redactarNoticia.jsp`. |
| `OBTENER_CATEGORIA(base)` | `/categorias/obtener` | Servlet huérfano. La edición se realiza directamente en `adminCategorias.jsp`. |
| `OBTENER_EVIDENCIA(base)` | `/evidencias/obtener` | Servlet huérfano. |
| `LISTAR_EVIDENCIAS(base)` | `/evidencias/listar` | Servlet huérfano. |
| `LISTAR_EVIDENCIAS_TIPO(base)` | `/evidencias/listarPorTipo` | Servlet huérfano. |
| `ACTUALIZAR_EVIDENCIA(base)` | `/evidencias/actualizar` | Servlet huérfano sin enlace en interfaz. |
| `OBTENER_TIPO_EVIDENCIA(base)` | `/evidencias/obtenerTipoEvidencia` | Servlet huérfano. La gestión se hace en `adminTipoEvidencia.jsp`. |
| `HABITATS_CSS(base)` | `/css/habitats.css` | Archivo CSS residual; la vista administrativa de hábitats incluye y utiliza `adminDashboard.css`. |

---

### 1.2. Archivos JSP declarados en `HttpRoutes` que NO existen en `src/main/webapp/`
Si algún flujo de ejecución despacha o redirige a estas rutas, el servidor retornará un error **HTTP 404**:

1. `/bestias/bestiaForms.jsp` (referenciado en `HttpRoutes.BESTIA_FORMS_JSP`).
2. `/habitats/carHabitatForms.jsp` (referenciado en `HttpRoutes.CARAC_HABITAT_FORM_JSP`).
3. `/habitats/habitatForms.jsp` (referenciado en `HttpRoutes.HABITAT_FORM_JSP` y despachado por `ObtenerHabitat.java`).
4. `/habitats/habitats.jsp` (referenciado en `HttpRoutes.HABITATS_JSP` y despachado en `ListarHabitat.java` cuando `flag == null`).  
   > ⚠️ **Peligro Crítico:** En `ListarHabitat.java` (línea 53), si la petición no incluye el parámetro `flag`, el servlet ejecuta:
   > ```java
   > rd = request.getRequestDispatcher(HttpRoutes.HABITATS_JSP(""));
   > ```
   > provocando un error 404 inmediato.
5. `/noticias/noticiaForms.jsp` (referenciado en `HttpRoutes.NOTICIA_FORM_JSP`).
6. `/categorias/categoriaForms.jsp` (referenciado en `HttpRoutes.CATEGORIA_FORM_JSP` y despachado por `ObtenerCategoria.java`).
7. `/evidencias/evidenciaForms.jsp` (referenciado en `HttpRoutes.EVIDENCIA_FORM_JSP` y despachado por 4 servlets de evidencias).
8. `/evidencias/tipoEvidenciaForms.jsp` (referenciado en `HttpRoutes.TIPO_EVIDENCIA_FORM_JSP` y despachado por `ObtenerTipoEvidencia.java`).

---

### 1.3. Rutas Faltantes en `HttpRoutes.java` (Hardcodeadas)
- En `src/main/webapp/auth/resetPassword.jsp` (línea 21):
  ```jsp
  <form class="logForm" action="<%=request.getContextPath()%>/reset-password" method="POST">
  ```
  La URL `/reset-password` está hardcodeada directamente en el JSP porque no existe el método helper correspondiente en `HttpRoutes.java`.

---

### 1.4. Solución Propuesta para Enrutamiento
1. **Eliminar los 11 métodos huérfanos** de `HttpRoutes.java` para mantener una única fuente de verdad y evitar confusión sobre qué rutas existen.
2. **Corregir `ListarHabitat.java`**:
   Eliminar la bifurcación condicional a la vista inexistente `/habitats/habitats.jsp`. Todo redireccionamiento o forward de hábitats debe apuntar a:
   ```java
   rd = request.getRequestDispatcher(HttpRoutes.ADMIN_DASHBOARD_JSP("") + "?crud=habitats");
   ```
3. **Agregar la ruta faltante en `HttpRoutes.java`**:
   ```java
   public static String RESET_PASSWORD(String base) {
       return base + "/reset-password";
   }
   ```
   Y actualizar `resetPassword.jsp`:
   ```jsp
   <form class="logForm" action="<%= HttpRoutes.RESET_PASSWORD(request.getContextPath()) %>" method="POST">
   ```
4. **Eliminar las constantes/métodos de JSPs inexistentes** de `HttpRoutes.java`.

---

## 2. Servlets Huérfanos o Nunca Invocados

Se detectaron 7 servlets mapeados con `@WebServlet` que no tienen ningún llamado desde la interfaz web, ni desde JavaScript (`fetch`/`AJAX`), ni redirecciones de otros servlets. Además, todos ellos intentan despachar a archivos JSP que no existen en el proyecto:

### Detalle de Servlets Huérfanos

| Servlet | URL Mapeada | Despacha a (Inexistente) | Causa de la Condición Huérfana |
|---|---|---|---|
| `ObtenerCategoria.java` | `/categorias/obtener` | `CATEGORIA_FORM_JSP` | La gestión y edición de categorías se realiza de forma interactiva en la misma página `adminCategorias.jsp`. |
| `ObtenerHabitat.java` | `/habitat/obtener` *(singular)* | `HABITAT_FORM_JSP` | La consulta y edición de hábitats se realiza en `adminHabitats.jsp`. La URL además difiere del prefijo plural `/habitats/`. |
| `ListarEvidencias.java` | `/evidencias/listar` | `EVIDENCIA_FORM_JSP` | Las evidencias se visualizan contextualizadas dentro de cada registro en `registro.jsp` o en el panel de bestias. |
| `ListarEvidenciasTipo.java` | `/evidencias/listarPorTipo` | `EVIDENCIA_FORM_JSP` | No existe ningún filtro de evidencias por tipo expuesto en la UI. |
| `ObtenerEvidencia.java` | `/evidencias/obtener` | `EVIDENCIA_FORM_JSP` | Las evidencias se cargan asociadas a las bestias/registros mediante `LogicEvidencia` y `LogicBestia`. |
| `ObtenerTipoEvidencia.java` | `/evidencias/obtenerTipoEvidencia` | `TIPO_EVIDENCIA_FORM_JSP` | La administración de tipos de evidencia se realiza mediante modal/formulario en `adminTipoEvidencia.jsp`. |
| `ActualizarEvidencia.java` | `/evidencias/actualizar` | `EVIDENCIA_FORM_JSP` | No existe interfaz de edición de evidencias existentes (sólo creación, aprobación y eliminación). Además, ejecutaba `CloudinaryHelper.upload(archivo)` de manera incondicional. |

### Solución Propuesta para Servlets Huérfanos
- **Eliminar los 7 servlets y sus rutas correspondientes**: No forman parte del flujo de usuario ni del panel de administración. Mantenerlos en el proyecto agrega superficie de ataque (servlets públicos con parámetros parseados sin autenticación estricta), deuda técnica y riesgo de errores 404/500 si algún actor malicioso o scraper los invoca.
- Si en el futuro se requiriera consultar una categoría, evidencia o hábitat individual vía API (por ejemplo para modales asíncronos), se deberá diseñar un servlet que devuelva JSON (`application/json`) con `HttpServletResponse` y ObjectMapper, no despachando a JSPs inexistentes.

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

## 6. Plan de Acción Recomendado y Priorización

Para ejecutar estas correcciones de manera segura y sin generar regresiones en la aplicación, se recomienda dividir las tareas en 4 fases ordenadas por criticidad:

### Fase 1: Correcciones Críticas de Seguridad y Runtime (Inmediata)
1. **Corregir `ListarHabitat.java`**: Eliminar el forward a `/habitats/habitats.jsp` (evita HTTP 404).
2. **Agregar `HttpRoutes.RESET_PASSWORD(base)`** y actualizar `resetPassword.jsp` (elimina ruta hardcodeada).
3. **Corregir las clases referenciadas en Loggers**:
   - `LogicNoticia.java`: Usar `LogicNoticia.class`.
   - `servlet.habitat.EliminarCaracteristica.java`: Normalizar a `private static final Logger`.
4. **Pasar la excepción (`Throwable`) en las 8 llamadas truncadas de `logger.log`** y corregir los nombres de servlet copiados y pegados.

### Fase 2: Depuración de Código Huérfano y Rutas Inexistentes
1. **Remover servlets no utilizados** (`ObtenerCategoria`, `ObtenerHabitat`, `ListarEvidencias`, `ListarEvidenciasTipo`, `ObtenerEvidencia`, `ObtenerTipoEvidencia`, `ActualizarEvidencia`).
2. **Eliminar los 11 métodos no utilizados** de `HttpRoutes.java` y las referencias a JSPs inexistentes (`*Forms.jsp`).
3. **Eliminar archivo de estilos no utilizado** (`css/habitats.css`) y su método `HttpRoutes.HABITATS_CSS`.

### Fase 3: Estandarización del Sistema de Logging
1. **Incorporar `Logger` en todas las clases DAO (`Data*.java`)**, reemplazando las 495+ líneas de `System.out.println` por llamadas a `logger.log(Level.SEVERE, ..., ex)` incluyendo `SQLState` y código de proveedor.
2. **Incorporar `Logger` en `LogicEmail.java` y `BackgroundJobListener.java`**, reemplazando `System.out.println` y `e.printStackTrace()`.
3. **Estandarizar formato de logs** agregando un archivo de configuración base `logging.properties` en `src/main/resources` si se desea persistir logs en archivo en Tomcat.

### Fase 4: Optimización de Consultas y Limpieza de Entidades
1. **Optimizar `DataBestia.completarBestia()`**: Retirar `addRegistros(bestia)` para eliminar la query N+1 que carga registros que ninguna vista utiliza.
2. **Hacer privados los métodos auxiliares de DAOs**: `asignarNroEvidencia` y `asignarNroRegistro`.
3. **Limpiar atributos muertos de entidades**:
   - Eliminar `caracteristicas` y `bestias` de `Habitat.java`.
4. **Normalizar nombres y encoding**:
   - Renombrar `Usuario.contraseña` a `contrasena` para robustez multiplataforma.
   - Normalizar nombres de servlets de características de hábitat.
5. **Estandarizar mensajes de error en la UI**:
   - Asegurar que `errorGlobal` siempre sea un `String` limpio para SweetAlert2 en `error.jsp`.

---
*Fin del documento de auditoría. Generado para el proyecto Bestiario.*
