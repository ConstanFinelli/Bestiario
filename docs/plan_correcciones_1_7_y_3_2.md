# Plan de Implementación: Correcciones 1.7 y 3.2 de correcciones.md

Este documento define el plan de trabajo detallado para resolver los puntos **1.7** y **3.2** identificados en [`docs/correcciones.md`](./correcciones.md):
1. **Punto 1.7:** Implementar de forma integral la eliminación de comentarios por usuarios con rol `"investigador"`.
2. **Punto 3.2:** Instanciar el estándar `Logger` en las 9 clases restantes de la capa `logic`.

*(Nota: El punto 1.3 se mantiene postergado para una etapa posterior).*

---

## 1. Consideraciones de Diseño y Autorización

> [!IMPORTANT]
> - **Control de Acceso / Autorización:** La eliminación de comentarios está reservada exclusivamente para usuarios autenticados con rol `"investigador"`. Usuarios no autenticados o con rol `"lector"` no deben ver los botones de eliminación ni tener acceso al endpoint del servlet (`HttpServletResponse.SC_FORBIDDEN`).
> - **Confirmación Previa:** Se solicitará confirmación en el navegador (`confirm(...)`) antes de enviar la petición de eliminación para evitar borrados accidentales.
> - **Redirección y Preservación de Estado:** Tras eliminar el comentario, se redirigirá con `sendRedirect` a la ficha de la bestia conservando los parámetros `id` y `nroRegistro` (si aplica), añadiendo el fragmento `#comentarios`.

---

## 2. Cambios Propuestos por Capa

### 2.1. Capa de Persistencia y Helpers

#### 1. `src/main/java/data/DataComentario.java`
- En el método `delete(Comentario c)`:
  - Validar las filas afectadas por `pstmt.executeUpdate()`. Si `affectedRows == 0`, asignar `c = null`.
  - Lanzar `DataNotFoundException("No se encontró el comentario para eliminar.")` si `c == null`, garantizando consistencia con `update(...)` y el resto de DAOs del sistema.

#### 2. `src/main/java/helpers/HttpRoutes.java`
- Agregar la ruta estática para la acción de eliminación:
  ```java
  public static String ELIMINAR_COMENTARIO(String base) {
      return base + "/comentarios/eliminar";
  }
  ```

---

### 2.2. Capa de Negocio (`logic`)

#### 1. `src/main/java/logic/LogicComentario.java`
- Instanciar logger estándar:
  ```java
  private static final Logger logger = Logger.getLogger(LogicComentario.class.getName());
  ```
- Declarar `throws DataNotFoundException` en el método `delete(Comentario c)`.

#### 2. Instanciación de Loggers en Clases de Negocio Restantes (Punto 3.2)
Agregar `private static final Logger logger = Logger.getLogger(Clase.class.getName());` e import `java.util.logging.Logger` en las 8 clases de `src/main/java/logic/`:
- `LogicBestia.java`
- `LogicCaracteristicaHabitat.java`
- `LogicCategoria.java`
- `LogicEvidencia.java`
- `LogicHabitat.java`
- `LogicRegistro.java`
- `LogicTipoEvidencia.java`
- `LogicUsuario.java`

---

### 2.3. Capa de Controladores Web (Servlets)

#### `src/main/java/servlet/comentario/EliminarComentario.java` [NUEVO]
- Anotado con `@WebServlet("/comentarios/eliminar")`.
- En `doPost(HttpServletRequest request, HttpServletResponse response)`:
  1. **Validación de sesión:** Obtener `Usuario` de la sesión. Si es nulo o su estado/rol no es `"investigador"`, responder con `SC_FORBIDDEN` (403) o redirigir.
  2. **Lectura de parámetros:** `idUsuario`, `idBestia`, `fechaPublicacion` y opcionalmente `nroRegistro`.
  3. **Validación defensiva:** Comprobar que los parámetros primarios no sean nulos ni vacíos.
  4. **Parseo de fecha:** Parsear `fechaPublicacion` de forma segura (`LocalDateTime.parse(...)`).
  5. **Invocación a la lógica:** Construir el objeto `Comentario` e invocar `controladorComentario.delete(comentario)`.
  6. **Manejo de excepciones:** Capturar `DataNotFoundException` con `logger.log(Level.WARNING, ...)` y excepciones generales con `logger.log(Level.SEVERE, ...)`.
  7. **Redirección:** `response.sendRedirect(HttpRoutes.OBTENER_REGISTRO_BESTIA(request.getContextPath()) + queryParams + "#comentarios")`.

---

### 2.4. Capa de Presentación (JSP y CSS)

#### 1. `src/main/webapp/registros/registro.jsp`
- En el bloque de iteración de `comentarios` dentro de `#comentarios`:
  - Envolver la cabecera del comentario en un contenedor `.comentarioHeader`.
  - Si `usuario != null && "investigador".equals(usuario.getEstado())`, renderizar un formulario POST que apunta a `HttpRoutes.ELIMINAR_COMENTARIO(request.getContextPath())` con:
    - Inputs ocultos: `idUsuario`, `idBestia`, `fechaPublicacion` y `nroRegistro` (si existe registro activo).
    - Botón de submit `.btnEliminarComentario` con confirmación: `onsubmit="return confirm('¿Desea eliminar este comentario?');"`.

#### 2. `src/main/webapp/css/registro.css`
- Agregar reglas CSS para `.comentarioHeader` (flexbox con alineación y distribución espacial).
- Agregar reglas CSS para `.eliminarComentarioForm` y `.btnEliminarComentario` (estilo discreto con tono de advertencia/peligro y efecto hover).

---

### 2.5. Documentación

#### `docs/correcciones.md`
- Marcar como `[RESUELTO]` los puntos:
  - `1.7. Funcionalidad Faltante: Eliminación de Comentarios por Investigadores`
  - `3.2. Clases sin Logger Instanciado`
- Actualizar el resumen general y las listas de verificación (checkboxes).

---

## 3. Plan de Verificación

### 3.1. Compilación y Análisis Estático
- Ejecutar compilación limpia con Maven:
  ```powershell
  mvn clean compile
  ```
  Debe compilar al 100% sin errores de tipos, firmas ni sintaxis.

### 3.2. Pruebas Manuales
1. **Control de Acceso en Eliminación de Comentarios:**
   - Iniciar sesión como lector o navegar como invitado: verificar que no aparece ningún botón de eliminar comentario en `registro.jsp`.
   - Iniciar sesión como investigador: verificar que cada comentario muestra el botón "Eliminar".
   - Al pulsar "Eliminar", cancelar en el cuadro de diálogo: confirmar que no se envía la petición.
   - Al pulsar "Eliminar" y confirmar: verificar que el comentario es eliminado en la base de datos y la página se recarga posicionada en `#comentarios`.
   - Petición directa GET o POST no autorizado a `/comentarios/eliminar`: verificar bloqueo con 403 Forbidden.
2. **Loggers en Capa Logic:**
   - Verificar que las 10 clases de la capa `logic` tienen un logger estático declarado de forma uniforme.
