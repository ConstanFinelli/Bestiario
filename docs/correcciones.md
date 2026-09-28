# Informe de Correcciones Pendientes del Sistema Bestiario

**Fecha:** Septiembre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE / MVC / Tomcat)  
**Ubicación del Documento:** `docs/correcciones.md`

---

## Índice

1. [Resumen de Tareas Pendientes](#resumen-de-tareas-pendientes)
2. [Métodos, Lógica Repetida y Código Muerto](#1-métodos-lógica-repetida-y-código-muerto)
3. [Refactorización Arquitectónica SOLID: Descomposición de Capa DAO](#2-refactorización-arquitectónica-solid-descomposición-de-capa-dao)
4. [Plan de Acción Priorizado](#3-plan-de-acción-priorizado)

---

## Resumen de Tareas Pendientes

El presente documento concentra exclusivamente los defectos, código redundante, inconsistencias y oportunidades de optimización que restan por solucionar en el proyecto:

- **Refactorización modular de la capa DAO (`data.*`)**: Modularizar clases DAO extensas (`DataBestia`, `DataRegistro`, `DataUsuario`, etc.) convirtiéndolas en ensambladores/fachadas que deleguen a clases individuales por operación (Single Responsibility Principle - SRP).
- **Disparidad en el manejo de `errorGlobal`** (formatos de lista vs string con corchetes en UI).

---

## 1. Métodos, Lógica Repetida y Código Muerto

### 1.1. Disparidad en Manejo de Errores Globales (`errorGlobal`)

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

---

## 2. Refactorización Arquitectónica SOLID: Descomposición de Capa DAO

### 3.1. Motivación y Diagnóstico

Actualmente, las clases de la capa DAO (`src/main/java/data/`) concentran la totalidad de las operaciones de persistencia de una entidad en archivos únicos de gran tamaño (e.g., `DataBestia.java` ~540 líneas, `DataRegistro.java` ~490 líneas, `DataUsuario.java` ~450 líneas).

Esto presenta desventajas frente a los principios **SOLID**, especialmente el **Principio de Responsabilidad Única (SRP - Single Responsibility Principle)**:

- **Sobrecarga de responsabilidades:** Una misma clase gestiona consultas individuales, listados filtrados, comandos de alta, actualización de estado, borrados en cascada manuales y gestión de relaciones N:M.
- **Dificultad de mantenimiento y testeo:** La modificación de una consulta SQL o el ajuste de una regla de mapeo (`ResultSet`) obliga a editar un archivo extenso y central, aumentando el riesgo de efectos colaterales.
- **Baja cohesión:** Cada método maneja sus propios parámetros, sentencias SQL y ciclos de vida de recursos, compartiendo únicamente la conexión a base de datos.

### 3.2. Propuesta: Clases DAO como Ensambladores de Clases de Operación Individual

Se propone descomponer cada clase `Data*` para que actúe como un **Ensamblador / Fachada (Facade)** que delega la ejecución en clases granulares independientes (una clase por método u operación de persistencia):

#### Arquitectura Propuesta:

1. **Clases de Operación Específicas (Action / Query / Command Objects):**
   - Agrupadas en subpaquetes modulares bajo `data` (por ejemplo, `data.bestia.*`, `data.registro.*`, `data.usuario.*`).
   - Cada clase encapsula:
     - La consulta SQL correspondiente como constante.
     - El manejo del ciclo de vida de `PreparedStatement`, `ResultSet` y liberación de recursos en bloque `finally`.
     - El mapeo específico de la entidad.
     - El control y lanzamiento de `DataNotFoundException` y registro en `Logger`.
   - _Ejemplo en `data.bestia`:_
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

### 3.3. Beneficios Técnicos

- **Cumplimiento estricto de SRP:** Cada clase de operación tiene una sola razón para cambiar (su sentencia SQL o mapeo específico).
- **Archivos compactos y legibles:** Clases individuales de 30 a 70 líneas en lugar de archivos monolíticos de 500+ líneas.
- **Facilidad para pruebas unitarias / de integración:** Posibilidad de testear o mockear operaciones individuales de forma aislada.
- **Sin impacto en capas superiores:** La capa `logic` continúa consumiendo `DataBestia`, `DataRegistro`, etc., sin requerir modificaciones.

---

## 3. Plan de Acción Priorizado

### Fase 1: Optimización de Consultas, Entidades y Código Muerto (Media/Baja Prioridad)

- [ ] Estandarizar la carga de `errorGlobal` como `String` limpio en todos los servlets.

### Fase 2: Refactorización Arquitectónica SOLID en Capa DAO (Mejora Estructural)

- [ ] Definir la estructura base de clases de operación (convención o interfaz `execute(...)`) y subpaquetes (`data.<entidad>.*`).
- [ ] Implementar la descomposición piloto en `DataBestia.java` (el DAO más extenso y acoplado).
- [ ] Reestructurar progresivamente `DataRegistro.java` y `DataUsuario.java`.
- [ ] Extender la arquitectura al resto de DAOs del sistema (`DataEvidencia`, `DataHabitat`, `DataCategoria`, etc.).

---

_Documento de seguimiento técnico - Proyecto Bestiario._
