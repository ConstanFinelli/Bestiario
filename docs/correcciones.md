# Informe de Correcciones Pendientes del Sistema Bestiario

**Fecha:** Septiembre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE / MVC / Tomcat)  
**Ubicación del Documento:** `docs/correcciones.md`  

---

## Índice
1. [Resumen de Tareas Pendientes](#resumen-de-tareas-pendientes)
2. [Métodos, Lógica Repetida y Código Muerto](#1-métodos-lógica-repetida-y-código-muerto)
3. [Atributos No Utilizados en Entidades de Dominio (`entities.*`)](#2-atributos-no-utilizados-en-entidades-de-dominio)
4. [Auditoría Integral de Logging (`java.util.logging.Logger`)](#3-auditoría-integral-de-logging)
5. [Refactorización Arquitectónica SOLID: Descomposición de Capa DAO](#4-refactorización-arquitectónica-solid-descomposición-de-capa-dao)
6. [Plan de Acción Priorizado](#5-plan-de-acción-priorizado)

---

## Resumen de Tareas Pendientes

El presente documento concentra exclusivamente los defectos, código redundante, inconsistencias y oportunidades de optimización que restan por solucionar en el proyecto:

- **Refactorización modular de la capa DAO (`data.*`)**: Modularizar clases DAO extensas (`DataBestia`, `DataRegistro`, `DataUsuario`, etc.) convirtiéndolas en ensambladores/fachadas que deleguen a clases individuales por operación (Single Responsibility Principle - SRP).
- **3 métodos DAO muertos** (`DataBestia.java`).
- **2 métodos auxiliares públicos** que deben restringirse a visibilidad `private` (`DataEvidencia.java`, `DataRegistro.java`).
- **1 consulta N+1 innecesaria** ejecutada en cada obtención de Bestia (`DataBestia.completarBestia`).
- **[RESUELTO] Inconsistencias en nombres de servlets** de hábitats (`ActualizarCaracteristicaHabitat.java`, `EliminarCaracteristicaHabitat.java`).
- **Disparidad en el manejo de `errorGlobal`** (formatos de lista vs string con corchetes en UI).
- **[RESUELTO] Error de forward y dispatching en `AgregarComentario.java`** (separada ruta de forward de URL de redirect con ancla).
- **Funcionalidad faltante: Eliminación de comentarios para Investigadores**: Permitir la moderación y eliminación de comentarios en la vista de la bestia (`registro.jsp`) exclusivamente para usuarios con rol de investigador, implementando la ruta en `HttpRoutes`, el servlet `EliminarComentario.java` y los controles correspondientes en la interfaz.
- **4 atributos no utilizados** en entidades de dominio (`Habitat.java`, `Bestia.java`) e identificador con caracter no-ASCII (`Usuario.contraseña`).
- **[RESUELTO] 502 líneas de `System.out.println` y `e.printStackTrace()`** en 13 clases del backend migradas a `java.util.logging.Logger`.
- **22 clases de backend sin Logger instanciado** (capas DAO, Logic y Listeners).
- **[RESUELTO] 2 declaraciones incorrectas de Logger** (`LogicNoticia.java` referenciando clase errónea, `EliminarCaracteristicaHabitat.java` con orden de modificadores no canónico).
- **[RESUELTO] 8 capturas de excepción donde se pierde el objeto `Throwable`** al llamar a `logger.log(...)`.
- **[RESUELTO] Mensajes de log con textos copiados y pegados erróneos**.

---

## 1. Métodos, Lógica Repetida y Código Muerto

### 1.1. Métodos Muertos en Capa DAO (`DataBestia.java`)
1. **`deleteCategorias(Bestia b)` y `deleteHabitats(Bestia b)`**:
   - En `DataBestia.java` (líneas 224-229), el método `delete(Bestia b)` invoca manualmente a `deleteCategorias(b)` y `deleteHabitats(b)`.
   - En el esquema de base de datos (`bestiario.sql`), las tablas intermedias `bestia_categoria` y `bestia_habitat` poseen restricciones de clave foránea con `ON DELETE CASCADE`, por lo que el motor relacional elimina automáticamente los vínculos.
   - Además, la desvinculación explícita ya se gestiona a través de `removeRelation(Bestia b, Categoria cat)` y `removeRelation(Bestia b, Habitat ht)`.
   - **Solución:** Remover las llamadas y los métodos redundantes `deleteCategorias` y `deleteHabitats` de `DataBestia.java`.
2. **`saveRegistros(Bestia b)`**:
   - En `DataBestia.save(b)` (línea 162), se invoca `saveRegistros(b)`, el cual itera sobre `b.getRegistros()` y llama a `regDAO.save(registro)`.
   - Cuando se da de alta una bestia en `CrearBestia.java`, la lista de registros siempre está vacía (`registros` se inicializa como `new LinkedList<>()`). Los registros poseen su propio ciclo de vida a través de `CrearRegistro.java` y `ActualizarRegistro.java`.
   - **Solución:** Eliminar el método `saveRegistros(Bestia b)` y su invocación en `DataBestia.save(b)`.

### 1.2. Métodos Auxiliares con Visibilidad Excesiva
En `DataEvidencia.java` (línea 191) y `DataRegistro.java` (línea 398):
- `public void asignarNroEvidencia(Evidencia e)`
- `public void asignarNroRegistro(Registro r)`

Ambos métodos solo son consumidos internamente dentro de su propia clase DAO durante la ejecución de `save(...)` para calcular el siguiente identificador de secuencia.
- **Solución:** Modificar la visibilidad de ambos métodos de `public` a `private`.

### 1.3. Sobrecarga Innecesaria en `DataBestia.completarBestia()`
Al consultar una bestia mediante `DataBestia.getOne(bestia)`, se ejecuta el método `completarBestia(bestia)`:
```java
public void completarBestia(Bestia bestia) {
    addRegistros(bestia);
    addHabitats(bestia);
    addCategorias(bestia);
    addComentarios(bestia);
    addEvidencias(bestia);
}
```
- `addRegistros(bestia)` ejecuta una consulta SQL a la base de datos (`regDAO.findAllByBestia`) para poblar la lista `registros`.
- Ninguna vista ni lógica de negocio lee `bestia.getRegistros()`. Todas las consultas de registros se efectúan de forma independiente mediante `LogicRegistro.getRegistrosBestia(bestia)` o `LogicRegistro.getRegistroActual(bestia)`.
- Esto genera una consulta SQL pesada innecesaria cada vez que se busca o visualiza una bestia.
- **Solución:** Retirar la llamada `addRegistros(bestia)` dentro de `completarBestia()`.

### 1.4. Inconsistencia de Nombres en Servlets de Hábitats [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
En el paquete `servlet.habitat`:
- `CrearCaracteristicaHabitat.java`
- `ActualizarCaracteristicaHabitat.java` *(renombrada desde `ActualizarCaracteristica.java`)*
- `EliminarCaracteristicaHabitat.java` *(renombrada desde `EliminarCaracteristica.java`)*

Se renombraron los archivos, clases y constructores a `ActualizarCaracteristicaHabitat` y `EliminarCaracteristicaHabitat`, asegurando consistencia con las anotaciones `@WebServlet` (`/habitats/actualizarCaracteristicaHabitat` y `/habitats/eliminarCaracteristicaHabitat`) y sus loggers.

### 1.5. Disparidad en Manejo de Errores Globales (`errorGlobal`)
Existe una discrepancia entre cómo los distintos servlets cargan los mensajes de error en el request:
- Algunos servlets guardan una lista:
  ```java
  List<String> errores = new ArrayList<>();
  ...
  if(!errores.isEmpty()) {
      errores.add("");
      request.setAttribute("errorGlobal", errores);
  }
  ```
- Otros servlets asignan un string simple:
  ```java
  request.setAttribute("errorGlobal", "Mensaje de error");
  ```
En `components/error.jsp` se renderiza:
```jsp
html: '<%= request.getAttribute("errorGlobal")%><br>Por favor, intente mas tarde.',
```
Cuando `errorGlobal` es una lista, SweetAlert2 imprime el resultado de `List.toString()`, mostrando corchetes y espacios extra (e.g. `[Error 1, Error 2, ]`).
- **Solución:** Unificar el mecanismo para que `errorGlobal` contenga siempre un `String` limpio. Si se acumulan múltiples errores, unirlos previamente:
  ```java
  request.setAttribute("errorGlobal", String.join("<br>", errores));
  ```
  y eliminar la adición de cadenas vacías (`errores.add("")`).

### 1.6. Error de Dispatching en `AgregarComentario.java` [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
En `src/main/java/servlet/comentario/AgregarComentario.java`:
Se separó la ruta interna para el forward (`HttpRoutes.OBTENER_REGISTRO_BESTIA("") + queryParams`) de la URL absoluta utilizada para la redirección del cliente (`HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + queryParams + "#comentarios"`). El dispatcher ya no recibe el `contextPath` ni el fragmento `#comentarios`, evitando fallos en el despacho interno de peticiones. Adicionalmente, se agregó validación para `idBestia` nulo o vacío.

### 1.7. Funcionalidad Faltante: Eliminación de Comentarios por Investigadores
Actualmente, el sistema permite registrar nuevos comentarios en la vista de la bestia (`registro.jsp`) a través de `AgregarComentario.java`, pero carece de un mecanismo en la capa web para eliminarlos o moderarlos:
- **Estado en persistencia y negocio:** Tanto `DataComentario.delete(Comentario c)` como `LogicComentario.delete(Comentario c)` ya se encuentran implementados y funcionales, eliminando registros mediante su clave primaria compuesta (`idUsuario`, `idBestia`, `fechaPublicacion`).
- **Carencias identificadas:**
  1. **Control de Acceso / Autorización:** Solo usuarios autenticados con rol `"investigador"` deben tener permisos para eliminar comentarios.
  2. **Ruta centralizada:** Falta la definición del helper `ELIMINAR_COMENTARIO` en `helpers/HttpRoutes.java` (apuntando a `/comentarios/eliminar`).
  3. **Servlet controlador:** Falta implementar el servlet `servlet.comentario.EliminarComentario.java` (`@WebServlet("/comentarios/eliminar")`), el cual debe:
     - Validar que el usuario en sesión esté autenticado y sea de tipo `"investigador"`.
     - Parsear los parámetros `idUsuario`, `idBestia`, `fechaPublicacion` y opcionalmente `nroRegistro` (para preservar la vista del registro específico).
     - Invocar `controladorComentario.delete(comentario)` capturando `DataNotFoundException` y excepciones generales con log adecuado.
     - Redirigir al usuario con `sendRedirect` a la ficha de la bestia (`HttpRoutes.OBTENER_REGISTRO_BESTIA(...) + queryParams + "#comentarios"`).
  4. **Interfaz de usuario (`registro.jsp`):** En la sección de renderizado de comentarios (`#comentarios`), agregar un botón/formulario de eliminación ("Eliminar comentario") visible únicamente si `usuario != null && "investigador".equals(usuario.getEstado())`.
- **Solución:** Crear la ruta en `HttpRoutes.java`, desarrollar el servlet `EliminarComentario.java` con validación estricta del rol de investigador e integrar el botón de acción en `registro.jsp`.

---

## 2. Atributos No Utilizados en Entidades de Dominio (`entities.*`)

| Entidad | Atributo | Tipo | Diagnóstico | Solución Recomendada |
|---|---|---|---|---|
| `Habitat.java` | `caracteristicas` | `LinkedList<String>` | **Nunca leído** (`gets=0`). Las características se gestionan mediante la entidad `CaracteristicaHabitat`. | Eliminar el atributo y sus métodos getters/setters en `Habitat.java`. |
| `Habitat.java` | `bestias` | `LinkedList<Bestia>` | **Nunca leído** (`gets=0`). La relación inversa es gestionada desde `Bestia` y `bestia_habitat`. | Eliminar el atributo y sus métodos getters/setters en `Habitat.java`. |
| `Bestia.java` | `registros` | `LinkedList<Registro>` | **Solo se asigna**, nunca se consume en ninguna vista (la interfaz utiliza `LogicRegistro`). | Remover de la entidad y retirar su carga en `DataBestia.completarBestia()`. |
| `Bestia.java` | `comentarios` | `LinkedList<Comentario>` | Solo se utiliza dentro del método `toString()`. Los comentarios se cargan en el servlet vía `LogicComentario`. | Evaluar su remoción para evitar retención innecesaria de objetos en memoria. |
| `Usuario.java` | `contraseña` | `String` | El atributo y sus métodos accesores contienen la letra **ñ** (`getContraseña()`, `setContraseña()`). | Renombrar a `contrasena` o `password` para evitar problemas de encoding entre plataformas y compiladores. |

---

## 3. Auditoría Integral de Logging (`java.util.logging.Logger`)

### 3.1. Uso Masivo de `System.out.println` y `e.printStackTrace()` [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
Se migraron las **502 líneas** de salida estándar no estructurada a `java.util.logging.Logger` en las 13 clases del backend, capturando el stack trace completo (`Throwable`), el `SQLState` y el código del proveedor en nivel `Level.SEVERE` para errores de base de datos, y en niveles `Level.INFO` y `Level.WARNING` para tareas en segundo plano y envíos de correo:

- `src/main/java/data/DataBestia.java`: 90 líneas (30 bloques catch)
- `src/main/java/data/DataRegistro.java`: 66 líneas (22 bloques catch)
- `src/main/java/data/DataUsuario.java`: 66 líneas (22 bloques catch)
- `src/main/java/data/DataEvidencia.java`: 54 líneas (18 bloques catch)
- `src/main/java/data/DataHabitat.java`: 39 líneas (13 bloques catch)
- `src/main/java/data/DataCategoria.java`: 36 líneas (12 bloques catch)
- `src/main/java/data/DataNoticia.java`: 36 líneas (12 bloques catch)
- `src/main/java/data/DataComentario.java`: 30 líneas (10 bloques catch)
- `src/main/java/data/DataTipoEvidencia.java`: 30 líneas (10 bloques catch)
- `src/main/java/data/DataCaracteristicaHabitat.java`: 24 líneas (8 bloques catch)
- `src/main/java/data/DataPasswordResetToken.java`: 24 líneas (8 bloques catch)
- `src/main/java/listeners/BackgroundJobListener.java`: 4 líneas
- `src/main/java/logic/LogicEmail.java`: 3 líneas

---

### 3.2. Clases sin Logger Instanciado (22 Clases)
- **Capa DAO (11 clases):** `DataBestia`, `DataCaracteristicaHabitat`, `DataCategoria`, `DataComentario`, `DataEvidencia`, `DataHabitat`, `DataNoticia`, `DataPasswordResetToken`, `DataRegistro`, `DataTipoEvidencia`, `DataUsuario`.
- **Capa Logic (10 clases):** `LogicBestia`, `LogicCaracteristicaHabitat`, `LogicCategoria`, `LogicComentario`, `LogicEmail`, `LogicEvidencia`, `LogicHabitat`, `LogicRegistro`, `LogicTipoEvidencia`, `LogicUsuario`.
- **Listeners (1 clase):** `BackgroundJobListener`.

---

### 3.3. Declaraciones Incorrectas de Logger [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
1. **`LogicNoticia.java`:**
   - Corregido a: `private static final Logger logger = Logger.getLogger(LogicNoticia.class.getName());`
   - Se removió además la importación y referencia errónea a `DbConnector`.

2. **`servlet.habitat.EliminarCaracteristicaHabitat.java`:**
   - Corregido a: `private static final Logger logger = Logger.getLogger(EliminarCaracteristicaHabitat.class.getName());`
   - Normalizado al orden canónico de modificadores Java (`private static final`).

---

### 3.4. Llamadas a `logger.log(...)` sin Pasar el `Throwable` (Pérdida de Stack Trace) [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
Se actualizaron las 8 llamadas a `logger.log(...)` pasando la instancia de la excepción atrapada (`Throwable`) como parámetro para registrar el stack trace completo:

1. **`LogicNoticia.java`:** `logger.log(Level.WARNING, "Falló envío a: " + u.getCorreo(), e);`
2. **`ActualizarBestia.java`:** `logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet ActualizarBestia", nfe);`
3. **`CambiarCategoria.java`:** `logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet CambiarCategoria", nfe);`
4. **`CrearBestia.java`:** `logger.log(Level.WARNING, "Error buscando los tipos de evidencia en la base de datos en el servlet CrearBestia", e);`
5. **`EditarBestia.java`:** `logger.log(Level.WARNING, "Error parseando la id de la bestia en el servlet EditarBestia", nfe);`
6. **`CrearEvidencia.java`:** `logger.log(Level.WARNING, "Error parseando la fecha de obtencion en el servlet CrearEvidencia", e);`
7. **`CrearEvidencia.java`:** `logger.log(Level.SEVERE, "Error al recibir el numero de tipo de evidencia en el servlet CrearEvidencia", e);`
8. **`CrearEvidencia.java`:** `logger.log(Level.SEVERE, "Error al parsear la fecha de obtencion de la evidencia en el servlet CrearEvidencia", ex);`

---

### 3.5. Mensajes de Log con Texto Copiado y Pegado Erróneo [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
- **`CrearEvidencia.java`:** Se corrigieron los mensajes que indicaban erróneamente `"...en el servlet ActualizarRegistro"` reemplazándolos por `CrearEvidencia`.
- **`EditarBestia.java`:** Se corrigió el mensaje `"Error parseando la id de la bestia en el servlet ActualizarBestia"` por `EditarBestia`.
- *(Nota: `ObtenerEvidencia.java` ya fue eliminado previamente en la limpieza de servlets huérfanos).*

---

### 3.6. Estándar de Implementación para Logging [RESUELTO]
**Estado:** ✔️ **RESUELTO**  
Se implementó este estándar en las 11 clases DAO (`Data*.java`), `BackgroundJobListener.java` y `LogicEmail.java`:

#### En Clases DAO (`Data*.java`):
Reemplazar:
```java
} catch (SQLException ex) {
    System.out.println("Mensaje: " + ex.getMessage());
    System.out.println("SQLState: " + ex.getSQLState());
    System.out.println("Error del proveedor (VendorError): " + ex.getErrorCode());
}
```
Por:
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

#### En `BackgroundJobListener.java` y `LogicEmail.java`:
- Sustituir `System.out.println` y `e.printStackTrace()` por:
  ```java
  logger.log(Level.INFO, "Iniciando ejecución programada del resumen diario de registros.");
  logger.log(Level.WARNING, "Falló el envío de correo de resumen a: " + investigador.getCorreo(), e);
  ```
- En `LogicEmail.java`:
  ```java
  logger.log(Level.INFO, "Email enviado satisfactoriamente a: {0}", destinatario);
  logger.log(Level.SEVERE, "Error al enviar email a: " + destinatario, e);
  ```

---

## 4. Refactorización Arquitectónica SOLID: Descomposición de Capa DAO

### 4.1. Motivación y Diagnóstico
Actualmente, las clases de la capa DAO (`src/main/java/data/`) concentran la totalidad de las operaciones de persistencia de una entidad en archivos únicos de gran tamaño (e.g., `DataBestia.java` ~540 líneas, `DataRegistro.java` ~490 líneas, `DataUsuario.java` ~450 líneas).

Esto presenta desventajas frente a los principios **SOLID**, especialmente el **Principio de Responsabilidad Única (SRP - Single Responsibility Principle)**:
- **Sobrecarga de responsabilidades:** Una misma clase gestiona consultas individuales, listados filtrados, comandos de alta, actualización de estado, borrados en cascada manuales y gestión de relaciones N:M.
- **Dificultad de mantenimiento y testeo:** La modificación de una consulta SQL o el ajuste de una regla de mapeo (`ResultSet`) obliga a editar un archivo extenso y central, aumentando el riesgo de efectos colaterales.
- **Baja cohesión:** Cada método maneja sus propios parámetros, sentencias SQL y ciclos de vida de recursos, compartiendo únicamente la conexión a base de datos.

### 4.2. Propuesta: Clases DAO como Ensambladores de Clases de Operación Individual
Se propone descomponer cada clase `Data*` para que actúe como un **Ensamblador / Fachada (Facade)** que delega la ejecución en clases granulares independientes (una clase por método u operación de persistencia):

#### Arquitectura Propuesta:
1. **Clases de Operación Específicas (Action / Query / Command Objects):**
   - Agrupadas en subpaquetes modulares bajo `data` (por ejemplo, `data.bestia.*`, `data.registro.*`, `data.usuario.*`).
   - Cada clase encapsula:
     - La consulta SQL correspondiente como constante.
     - El manejo del ciclo de vida de `PreparedStatement`, `ResultSet` y liberación de recursos en bloque `finally`.
     - El mapeo específico de la entidad.
     - El control y lanzamiento de `DataNotFoundException` y registro en `Logger`.
   - *Ejemplo en `data.bestia`:*
     - `GetOneBestia.java`: Búsqueda individual por ID.
     - `FindAllBestias.java`: Listado completo.
     - `FindByCategoriaBestia.java`: Filtrado por categoría.
     - `SaveBestia.java`: Inserción de nueva bestia.
     - `UpdateBestia.java`: Actualización de datos básicos.
     - `ApproveBestia.java`: Cambio de estado a aprobado.
     - `DeleteBestia.java`: Eliminación de bestia.
     - `VincularHabitatBestia.java` / `DesvincularHabitatBestia.java`: Gestión de relaciones intermedias.

2. **Clase `Data*` como Ensamblador / Fachada (`DataBestia.java`):**
   - Conserva exactamente los mismos métodos y firmas públicas consumidas por la capa `logic` (`LogicBestia`), garantizando **cero impacto y total compatibilidad hacia atrás**:
     ```java
     package data;
     
     import data.bestia.*;
     import entities.Bestia;
     import exceptions.DataNotFoundException;
     import java.util.LinkedList;
     
     public class DataBestia {
         private final GetOneBestia getOneOp = new GetOneBestia();
         private final FindAllBestias findAllOp = new FindAllBestias();
         private final SaveBestia saveOp = new SaveBestia();
         private final UpdateBestia updateOp = new UpdateBestia();
         private final ApproveBestia approveOp = new ApproveBestia();
         private final DeleteBestia deleteOp = new DeleteBestia();
         // ...
         
         public Bestia getOne(Bestia b) throws DataNotFoundException {
             return getOneOp.execute(b);
         }
         
         public LinkedList<Bestia> findAll() {
             return findAllOp.execute();
         }
         
         public Bestia update(Bestia b) throws DataNotFoundException {
             return updateOp.execute(b);
         }
         // ...
     }
     ```

### 4.3. Beneficios Técnicos
- **Cumplimiento estricto de SRP:** Cada clase de operación tiene una sola razón para cambiar (su sentencia SQL o mapeo específico).
- **Archivos compactos y legibles:** Clases individuales de 30 a 70 líneas en lugar de archivos monolíticos de 500+ líneas.
- **Facilidad para pruebas unitarias / de integración:** Posibilidad de testear o mockear operaciones individuales de forma aislada.
- **Sin impacto en capas superiores:** La capa `logic` continúa consumiendo `DataBestia`, `DataRegistro`, etc., sin requerir modificaciones.

---

## 5. Plan de Acción Priorizado

### Fase 1: Correcciones Críticas de Logger y Mensajería (Alta Prioridad)
- [x] Corregir la clase referenciada en `LogicNoticia.java` (`LogicNoticia.class`).
- [x] Normalizar la declaración en `EliminarCaracteristicaHabitat.java` a `private static final Logger`.
- [x] Pasar el parámetro `Throwable` en las 8 llamadas truncadas de `logger.log(...)`.
- [x] Corregir los textos con nombres de servlets erróneos en `CrearEvidencia.java` y `EditarBestia.java`.
- [x] Corregir la lógica de dispatch en `AgregarComentario.java` (separar URL de redirect del dispatcher).

### Fase 2: Estandarización de Logging en DAOs y Servicios (Media Prioridad)
- [x] Incorporar `Logger` en todas las clases DAO (`Data*.java`), reemplazando las llamadas a `System.out.println` con registro de `SQLState`, código de error y objeto `SQLException`.
- [x] Incorporar `Logger` en `BackgroundJobListener.java` y `LogicEmail.java`, retirando `System.out.println` y `e.printStackTrace()`.
- [ ] Incorporar `Logger` en las clases de la capa `logic` para trazabilidad de reglas de negocio.

### Fase 3: Optimización de Consultas, Entidades y Código Muerto (Media/Baja Prioridad)
- [ ] Retirar `addRegistros(bestia)` de `DataBestia.completarBestia()` para evitar la consulta N+1.
- [ ] Eliminar los métodos muertos `deleteCategorias`, `deleteHabitats` y `saveRegistros` de `DataBestia.java`.
- [ ] Cambiar a `private` los métodos auxiliares `asignarNroEvidencia` y `asignarNroRegistro`.
- [ ] Eliminar atributos y accesores de `caracteristicas` y `bestias` en `Habitat.java`.
- [ ] Renombrar `Usuario.contraseña` a `contrasena` o `password`.
- [x] Normalizar nombres de servlets de características de hábitat a `*CaracteristicaHabitat`.
- [ ] Estandarizar la carga de `errorGlobal` como `String` limpio en todos los servlets.
- [ ] Implementar la eliminación de comentarios para usuarios con rol investigador (ruta `HttpRoutes`, servlet `EliminarComentario` y UI en `registro.jsp`).

### Fase 4: Refactorización Arquitectónica SOLID en Capa DAO (Mejora Estructural)
- [ ] Definir la estructura base de clases de operación (convención o interfaz `execute(...)`) y subpaquetes (`data.<entidad>.*`).
- [ ] Implementar la descomposición piloto en `DataBestia.java` (el DAO más extenso y acoplado).
- [ ] Reestructurar progresivamente `DataRegistro.java` y `DataUsuario.java`.
- [ ] Extender la arquitectura al resto de DAOs del sistema (`DataEvidencia`, `DataHabitat`, `DataCategoria`, etc.).

---
*Documento de seguimiento técnico - Proyecto Bestiario.*
