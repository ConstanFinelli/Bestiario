# Plan de Migración Arquitectónica: Descomposición SOLID de la Capa DAO

**Fecha de Creación:** Septiembre 2026  
**Proyecto:** Bestiario (Aplicación Web Jakarta EE / Tomcat / MVC)  
**Ubicación:** `docs/plan_migracion.md`  
**Estado:** Documentado y listo para ejecución futura  

---

## 1. Resumen Ejecutivo y Objetivos

### 1.1. Contexto Actual
La capa de acceso a datos (`src/main/java/data/`) está compuesta actualmente por 11 clases DAO que concentran de forma monolítica todas las operaciones SQL sobre cada entidad. Algunos archivos han superado las 500 líneas de código (`DataBestia` ~540 líneas, `DataRegistro` ~490 líneas, `DataUsuario` ~450 líneas).

### 1.2. Problemas Detectados
1. **Violación del Principio de Responsabilidad Única (SRP)**: Una sola clase asume responsabilidades disímiles: lecturas simples, búsquedas con filtros dinámicos, inserciones, actualizaciones de estado, cálculo de números de secuencia, borrados lógicos/físicos y gestión de tablas intermedias (N:M).
2. **Alto Riesgo de Regresión**: Modificar una consulta SQL o el mapeo de un `ResultSet` implica alterar un archivo crítico con docenas de otros métodos.
3. **Baja Testabilidad**: Dificultad para mockear o probar operaciones individuales sin instanciar la totalidad del DAO.

### 1.3. Objetivo Arquitectónico
Transformar la capa DAO en una **arquitectura basada en Ensambladores / Fachadas (Assembler / Facade Pattern)**:
- Cada consulta, comando o relación intermedia se encapsula en una **clase individual de operación única** (e.g., `GetOneBestia`, `SaveBestia`, `ApproveBestia`).
- Las clases `Data*` tradicionales (`DataBestia`, `DataRegistro`, etc.) se convierten en **ensambladores ligeros** que conservan exactamente sus firmas públicas, delegando la ejecución en las clases especializadas.
- **Cero impacto en capas superiores**: La capa `logic` y los `servlets` no sufren modificaciones de firmas ni llamadas.

---

## 2. Patrón de Diseño y Convenciones Técnicas

### 2.1. Estructura de Paquetes
Para evitar la dispersión caótica de clases, cada entidad tendrá su subpaquete dentro de `data`:

```
src/main/java/data/
├── DataBestia.java                 (Ensamblador / Fachada pública)
├── DataRegistro.java               (Ensamblador / Fachada pública)
├── DataUsuario.java                (Ensamblador / Fachada pública)
├── ...
├── bestia/                         (Subpaquete de operaciones de Bestia)
│   ├── GetOneBestia.java
│   ├── FindAllBestias.java
│   ├── FindByCategoriaBestia.java
│   ├── FindAllBestiasFromHabitat.java
│   ├── SaveBestia.java
│   ├── UpdateBestia.java
│   ├── DeleteBestia.java
│   ├── ApproveBestia.java
│   ├── SaveHabitatsBestia.java
│   ├── SaveCategoriasBestia.java
│   ├── SaveEvidenciasBestia.java
│   ├── RemoveRelationHabitat.java
│   ├── RemoveRelationCategoria.java
│   └── CompletarBestia.java
├── registro/                       (Subpaquete de operaciones de Registro)
├── usuario/                        (Subpaquete de operaciones de Usuario)
├── evidencia/                      (Subpaquete de operaciones de Evidencia)
├── habitat/                        (Subpaquete de operaciones de Hábitat)
├── categoria/                      (Subpaquete de operaciones de Categoría)
├── comentario/                     (Subpaquete de operaciones de Comentario)
├── noticia/                        (Subpaquete de operaciones de Noticia)
├── tipoevidencia/                  (Subpaquete de operaciones de TipoEvidencia)
├── caracteristicahabitat/          (Subpaquete de operaciones de CaracteristicaHabitat)
└── token/                          (Subpaquete de operaciones de ResetToken)
```

### 2.2. Anatomía de una Clase de Operación Individual
Cada clase de operación cumple con los siguientes estándares:
- Inmutabilidad y responsabilidad única.
- Constante SQL estática y privada (`private static final String SQL_...`).
- Instancia canónica de Logger (`private static final Logger logger = Logger.getLogger(...);`).
- Manejo estricto de recursos JDBC en bloque `finally` con `DbConnector.getInstancia().releaseConn()`.
- Captura de `SQLException` con log en `Level.SEVERE` que incluye `SQLState` y `ErrorCode`.
- Validación y lanzamiento de `DataNotFoundException` cuando la entidad no existe o las filas afectadas son `0`.

#### Plantilla Canónica de Operación de Consulta (`GetOneBestia.java`):
```java
package data.bestia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.DbConnector;
import entities.Bestia;
import exceptions.DataNotFoundException;

public class GetOneBestia {
    private static final String SQL_SELECT = "SELECT * FROM bestia WHERE idBestia = ?";
    private static final Logger logger = Logger.getLogger(GetOneBestia.class.getName());

    public Bestia execute(Bestia b) throws DataNotFoundException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Bestia bestiaEncontrada = null;

        try {
            pstmt = DbConnector.getInstancia().getConn().prepareStatement(SQL_SELECT);
            pstmt.setInt(1, b.getIdBestia());
            rs = pstmt.executeQuery();

            if (rs != null && rs.next()) {
                bestiaEncontrada = new Bestia(
                    rs.getInt("idBestia"),
                    rs.getString("nombre"),
                    rs.getString("peligrosidad"),
                    rs.getString("estado")
                );
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, String.format(
                "Error SQL al consultar bestia [SQLState: %s, ErrorCode: %d]: %s",
                ex.getSQLState(), ex.getErrorCode(), ex.getMessage()), ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DbConnector.getInstancia().releaseConn();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error SQL al cerrar recursos en GetOneBestia", ex);
            }
        }

        if (bestiaEncontrada == null) {
            throw new DataNotFoundException("No se encontró la bestia con id " + b.getIdBestia());
        }

        return bestiaEncontrada;
    }
}
```

### 2.3. Anatomía del Ensamblador (`DataBestia.java`)
La clase ensambladora actúa como fachada cohesiva:
```java
package data;

import java.util.LinkedList;
import data.bestia.*;
import entities.Bestia;
import entities.Categoria;
import entities.Habitat;
import exceptions.DataNotFoundException;

public class DataBestia {
    private final GetOneBestia getOneOp = new GetOneBestia();
    private final FindAllBestias findAllOp = new FindAllBestias();
    private final FindByCategoriaBestia findByCatOp = new FindByCategoriaBestia();
    private final FindAllBestiasFromHabitat findByHabOp = new FindAllBestiasFromHabitat();
    private final SaveBestia saveOp = new SaveBestia();
    private final UpdateBestia updateOp = new UpdateBestia();
    private final DeleteBestia deleteOp = new DeleteBestia();
    private final ApproveBestia approveOp = new ApproveBestia();
    private final SaveHabitatsBestia saveHabitatsOp = new SaveHabitatsBestia();
    private final SaveCategoriasBestia saveCategoriasOp = new SaveCategoriasBestia();
    private final SaveEvidenciasBestia saveEvidenciasOp = new SaveEvidenciasBestia();
    private final RemoveRelationHabitat removeHabOp = new RemoveRelationHabitat();
    private final RemoveRelationCategoria removeCatOp = new RemoveRelationCategoria();
    private final CompletarBestia completarOp = new CompletarBestia();

    public Bestia getOne(Bestia b) throws DataNotFoundException {
        Bestia found = getOneOp.execute(b);
        completarBestia(found);
        return found;
    }

    public LinkedList<Bestia> findAll() {
        return findAllOp.execute();
    }

    public LinkedList<Bestia> findByCategoria(String cat) {
        return findByCatOp.execute(cat);
    }

    public LinkedList<Bestia> findAllBestiasFromHabitat(Habitat ht) {
        return findByHabOp.execute(ht);
    }

    public Bestia save(Bestia b) {
        return saveOp.execute(b);
    }

    public Bestia update(Bestia b) throws DataNotFoundException {
        return updateOp.execute(b);
    }

    public Bestia delete(Bestia b) {
        return deleteOp.execute(b);
    }

    public void approve(Bestia b) throws DataNotFoundException {
        approveOp.execute(b);
    }

    public void saveHabitats(Bestia b) {
        saveHabitatsOp.execute(b);
    }

    public void saveCategorias(Bestia b) {
        saveCategoriasOp.execute(b);
    }

    public void saveEvidencias(Bestia b) {
        saveEvidenciasOp.execute(b);
    }

    public void removeRelation(Bestia b, Habitat ht) {
        removeHabOp.execute(b, ht);
    }

    public void removeRelation(Bestia b, Categoria cat) {
        removeCatOp.execute(b, cat);
    }

    public void completarBestia(Bestia b) {
        completarOp.execute(b);
    }
}
```

---

## 3. Matriz de Migración por Entidad y Operación

A continuación se detalla el desglose completo de clases a crear para los 11 DAOs del sistema:

| DAO Ensamblador | Subpaquete Destino | Clases de Operación a Crear (1 por método) | Oportunidades de Limpieza Asociadas |
|---|---|---|---|
| **`DataBestia`** | `data.bestia` | `GetOneBestia`<br>`FindAllBestias`<br>`FindByCategoriaBestia`<br>`FindAllBestiasFromHabitat`<br>`SaveBestia`<br>`UpdateBestia`<br>`DeleteBestia`<br>`ApproveBestia`<br>`SaveHabitatsBestia`<br>`SaveCategoriasBestia`<br>`SaveEvidenciasBestia`<br>`RemoveRelationHabitat`<br>`RemoveRelationCategoria`<br>`CompletarBestia` | - Omitir `deleteCategorias` y `deleteHabitats` (redundantes con `ON DELETE CASCADE`).<br>- Omitir `saveRegistros` (código muerto).<br>- Retirar `addRegistros` de `CompletarBestia` (evita consulta N+1). |
| **`DataRegistro`** | `data.registro` | `GetOneRegistro`<br>`GetRegistroToShow`<br>`FindAllByBestia`<br>`FindRegistrosPendientes`<br>`FindRegistrosAprobadosHoy`<br>`SaveRegistro`<br>`UpdateRegistro`<br>`UpdateEstadoRegistro`<br>`DeleteRegistro`<br>`SetLastRegistroFechaBaja`<br>`AsignarNroRegistro` | - `AsignarNroRegistro` y `SetLastRegistroFechaBaja` quedan con visibilidad de paquete (no públicas). |
| **`DataUsuario`** | `data.usuario` | `GetOneUsuario`<br>`GetByEmailUsuario`<br>`FindAllUsuarios`<br>`FindAllSolicitantes`<br>`FindByRecibirNotificaciones`<br>`SaveUsuario`<br>`UpdateUsuario`<br>`DeleteUsuario`<br>`CambiarContrasenaUsuario` | - Centralizar el polimorfismo de `Lector` / `Investigador` en el mapeo de `ResultSet`. |
| **`DataEvidencia`** | `data.evidencia` | `GetOneEvidencia`<br>`FindAllEvidencias`<br>`FindAllTypeEvidencias`<br>`FindAllByBestiaEvidencias`<br>`SaveEvidencia`<br>`UpdateEvidencia`<br>`UpdateEstadoEvidencia`<br>`DeleteEvidencia`<br>`AsignarNroEvidencia` | - `AsignarNroEvidencia` queda con visibilidad de paquete. |
| **`DataHabitat`** | `data.habitat` | `GetOneHabitat`<br>`FindAllHabitats`<br>`SaveHabitat`<br>`UpdateHabitat`<br>`DeleteHabitat`<br>`FindHabitatsByBestia` | - Desacoplar atributos no utilizados (`caracteristicas`, `bestias`). |
| **`DataCaracteristicaHabitat`** | `data.caracteristicahabitat` | `FindAllByHabitat`<br>`SaveCaracteristicaHabitat`<br>`UpdateCaracteristicaHabitat`<br>`DeleteCaracteristicaHabitat` | - Mantener consistencia de nombres normalizados. |
| **`DataCategoria`** | `data.categoria` | `GetOneCategoria`<br>`FindAllCategorias`<br>`SaveCategoria`<br>`UpdateCategoria`<br>`DeleteCategoria`<br>`FindCategoriasByBestia` | - Archivos altamente compactos (~35 líneas cada uno). |
| **`DataComentario`** | `data.comentario` | `GetOneComentario`<br>`FindAllByBestiaComentarios`<br>`SaveComentario`<br>`UpdateComentario`<br>`DeleteComentario` | - Aislamiento del mapeo fecha/hora. |
| **`DataNoticia`** | `data.noticia` | `GetOneNoticia`<br>`FindAllNoticias`<br>`GetUltimasNoticias`<br>`SaveNoticia`<br>`UpdateNoticia`<br>`DeleteNoticia` | - Asegurar propagación de `DataNotFoundException`. |
| **`DataTipoEvidencia`** | `data.tipoevidencia` | `GetOneTipoEvidencia`<br>`FindAllTiposEvidencia`<br>`SaveTipoEvidencia`<br>`UpdateTipoEvidencia`<br>`DeleteTipoEvidencia` | - Estructura directa y estándar. |
| **`DataPasswordResetToken`** | `data.token` | `GetOneToken`<br>`SaveToken`<br>`DeleteTokenByUser`<br>`MarkTokenAsUsed` | - Manejo seguro de expiración y consumo. |

---

## 4. Fases de Ejecución Progresiva

Para garantizar la estabilidad del proyecto y permitir compilaciones limpias en cada hito, la migración debe ejecutarse en **5 fases ordenadas**:

```
Fase 0: Definición de Patrón y Utilidades Compartidas
  └── Fase 1: Proyecto Piloto con DataBestia (El más complejo)
        └── Fase 2: DAOs Nucleares (DataRegistro y DataUsuario)
              └── Fase 3: DAOs Relacionales (Evidencia, Habitat, Categoria)
                    └── Fase 4: DAOs Secundarios (Comentario, Noticia, Tipos, Tokens)
                          └── Fase 5: Verificación Integral y Limpieza Final
```

### Fase 0: Estándar y Preparación
- Validar que el proyecto compila con `mvn clean compile` antes de iniciar.
- Crear los paquetes correspondientes bajo `src/main/java/data/`.

### Fase 1: Implementación Piloto (`DataBestia`)
1. Crear el paquete `data.bestia`.
2. Implementar individualmente cada clase de operación extrayendo la lógica correspondiente de `DataBestia.java`.
3. Aplicar las optimizaciones de `docs/correcciones.md`:
   - No implementar `deleteCategorias`, `deleteHabitats` ni `saveRegistros`.
   - Retirar `addRegistros` en `CompletarBestia`.
4. Reescribir `DataBestia.java` como ensamblador delegador.
5. Ejecutar `mvn clean compile` para comprobar que `LogicBestia` y los servlets compilan sin advertencias.
6. Commit atómico: `refactor(data): modularizar DataBestia en clases de operacion individuales`.

### Fase 2: DAOs Nucleares (`DataRegistro` y `DataUsuario`)
1. Crear `data.registro` y `data.usuario`.
2. Extraer las operaciones individuales.
3. Reducir visibilidad a nivel paquete para generadores de secuencia (`AsignarNroRegistro`).
4. Convertir `DataRegistro.java` y `DataUsuario.java` en ensambladores.
5. Compilar y verificar con `mvn clean compile`.
6. Commit atómico: `refactor(data): modularizar DataRegistro y DataUsuario`.

### Fase 3: DAOs Relacionales (`DataEvidencia`, `DataHabitat`, `DataCategoria`)
1. Crear `data.evidencia`, `data.habitat` y `data.categoria`.
2. Modularizar métodos individuales y relaciones intermedias.
3. Actualizar los 3 ensambladores.
4. Compilar y verificar con `mvn clean compile`.
5. Commit atómico: `refactor(data): modularizar DataEvidencia, DataHabitat y DataCategoria`.

### Fase 4: DAOs Secundarios y de Soporte
1. Modularizar `DataComentario`, `DataNoticia`, `DataTipoEvidencia`, `DataCaracteristicaHabitat` y `DataPasswordResetToken`.
2. Actualizar sus ensambladores correspondientes.
3. Compilar y verificar con `mvn clean compile`.
4. Commit atómico: `refactor(data): modularizar DAOs restantes de soporte`.

### Fase 5: Verificación Integral de Sistema
1. `mvn clean compile`: Asegurar cero errores de compilación.
2. Despliegue en contenedor Tomcat local o suite de pruebas.
3. Verificación de flujos principales:
   - Login, registro y recuperación de contraseña.
   - Listado de bestias, búsqueda por filtro y mapa.
   - Creación de propuesta de bestia y aprobación.
   - Carga y aprobación de registros y evidencias multimedia.
   - Publicación de comentarios y noticias.
4. Actualizar `docs/correcciones.md` marcando la Fase 4 como completada.

---

## 5. Criterios de Aceptación

1. **Cumplimiento de SRP**: Ninguna clase de operación individual debe exceder las 80 líneas de código ni gestionar más de una consulta/comando SQL.
2. **Compatibilidad Estricta (Backward Compatibility)**: Los métodos y firmas de las clases `Data*` existentes se mantienen intactos. No se modifica ningún archivo en `logic/` ni en `servlet/`.
3. **Consistencia de Excepciones**: Todas las clases de operación de lectura individual o actualización mantienen el lanzamiento de `DataNotFoundException`.
4. **Cero Leaks de Conexiones**: Toda clase individual garantiza el bloque `finally` con `DbConnector.getInstancia().releaseConn()`.
5. **Compilación Limpia**: `mvn clean compile` finaliza exitosamente con `BUILD SUCCESS`.

---
*Plan de migración arquitectónica - Proyecto Bestiario.*

