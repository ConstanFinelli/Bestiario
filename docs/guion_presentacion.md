# Guión de Presentación en Video: Proyecto Bestiario
**Duración sugerida**: 8 a 12 minutos  
**Formato**: 3 Presentadores con grabación de pantalla y cámara (opcional)

---

## 👥 Resumen de División de Tareas

| Presentador | Rol / Bloque Temático | Temas Clave y Demo en Pantalla |
| :--- | :--- | :--- |
| **Persona 1** | **Introducción, Arquitectura y Usuarios/Seguridad** | • Presentación del proyecto y problemática.<br>• Stack técnico y arquitectura MVC (3 capas).<br>• Registro, Login, Recuperación de contraseña por Token.<br>• Jerarquía de Roles (*Lector*, *Solicitante*, *Investigador*). |
| **Persona 2** | **El Corazón del Sistema: Bestias, Versionado y Cloudinary** | • Catálogo de Bestias y filtrado por categoría.<br>• **Sistema de versionado de Registros** (historial, vigencia, auditoría).<br>• Evidencias multimedia y optimización en Cloudinary (`ImageHelper` / 1080p).<br>• Flujo de aprobación y moderación. |
| **Persona 3** | **Georreferenciación, Panel Admin, Emails y Tareas Background** | • Mapas interactivos con Leaflet/ArcGIS y selector en `mapaCargaHabitat.jsp`.<br>• Panel de Control de Administrador (CRUDs).<br>• Módulo de Noticias y difusión por email.<br>• Scheduler en segundo plano (`BackgroundJobListener`).<br>• Conclusiones y cierre. |

---

## 🎬 Guión Detallado Paso a Paso

---

### ⏱️ BLOQUE 1 (00:00 - 03:00) | Persona 1: Introducción, Arquitectura y Seguridad

#### 1. Introducción y Propósito (00:00 - 00:45)
* **Qué decir**:
  > *"¡Hola a todos! Bienvenidos a la presentación de **Bestiario**, una plataforma web desarrollada en Java diseñada para recopilar, clasificar y documentar avistamientos de criaturas mitológicas y críptidos en todo el mundo. El objetivo principal es construir una enciclopedia colaborativa y confiable donde la comunidad pueda aportar relatos, fotos y videos, respaldados por investigadores que verifican la información."*
* **Qué mostrar en pantalla**:
  * Página de Inicio (`home.jsp`) navegando por la sección *"¿Quiénes somos?"* y las últimas noticias.

#### 2. Arquitectura y Stack Tecnológico (00:45 - 01:45)
* **Qué decir**:
  > *"Técnicamente, la aplicación está desarrollada sobre **Java 21** utilizando **Jakarta EE (Servlets 6.0)** y desplegada en **Apache Tomcat 10+**.  
  > Implementamos una **arquitectura MVC en 3 capas desacopladas**:
  > 1. **Capa de Presentación**: Vistas JSP dinámicas, formularios multipart y componentes reutilizables.
  > 2. **Capa de Negocio (`logic`)**: Servicios que contienen las reglas de negocio, validaciones y orquestación de correos y multimedia.
  > 3. **Capa de Datos (`data`)**: DAOs JDBC con `DbConnector` administrando conexiones hacia **MySQL**.
  > Además, nos integramos con **Cloudinary** para assets en la nube, **Jakarta Mail** para notificaciones SMTP y **Dotenv** para configuración segura."*
* **Qué mostrar en pantalla**:
  * Diagrama de capas o árbol del proyecto en el IDE mostrando los paquetes `data`, `entities`, `logic`, `servlet` y `helpers`.

#### 3. Autenticación, Roles y Recuperación de Contraseña (01:45 - 03:00)
* **Qué decir**:
  > *"El sistema maneja tres roles principales: **Lector** (usuario base que lee y propone), **Solicitante** (lector postulado a investigador) e **Investigador** (rol administrativo que aprueba y gestiona).  
  > Contamos con login seguro, hash de contraseñas y un flujo de **recuperación de contraseña por correo**: el sistema genera un token criptográfico único con expiración de 30 minutos almacenado en base de datos (`PasswordResetToken`) y envía un email con un enlace seguro para restablecerla.  
  > Asimismo, un lector puede postularse completando su DNI y datos para que un investigador revise su candidatura."*
* **Qué mostrar en pantalla**:
  * Formulario de Login, Register, "¿Olvidaste tu contraseña?" y la pantalla de postulación a investigador.

---

### ⏱️ BLOQUE 2 (03:00 - 06:30) | Persona 2: Bestias, Versionado de Registros y Multimedia

#### 1. Catálogo de Bestias y Clasificación (03:00 - 04:00)
* **Qué decir**:
  > *"Pasando al núcleo de la aplicación, encontramos el catálogo de Bestias. Cada criatura cuenta con su nombre, nivel de peligrosidad (Baja, Media, Alta), categorías asociadas (como Voladora, Acuática, Mitológica) y hábitats.  
  > Los lectores pueden explorar y filtrar criaturas por categoría en tiempo real, mientras que los investigadores pueden editar sus propiedades o aprobar nuevas bestias propuestas por la comunidad."*
* **Qué mostrar en pantalla**:
  * Vista de Bestias (`bestias.jsp`), usando el buscador/filtro de categorías y abriendo la ficha de una criatura.

#### 2. Sistema de Versionado de Registros (04:00 - 05:15)
* **Qué decir**:
  > *"Una de las características más innovadoras de Bestiario es su **sistema de versionado histórico de información (`Registro`)**.  
  > En lugar de sobrescribir el texto cuando se actualiza una criatura, el sistema crea un nuevo registro con fecha de aprobación y marca el registro anterior con una `fechaBaja`. Esto permite auditar qué investigador aprobó cada cambio y permite al usuario consultar cómo evolucionó la descripción o historia de la bestia a lo largo del tiempo según la fecha seleccionada."*
* **Qué mostrar en pantalla**:
  * Ficha de detalle (`registro.jsp`), mostrando la fecha de aprobación, quién lo publicó y la consulta por fechas históricas.
  * Formulario de propuesta de nuevo registro (`nuevoRegistro.jsp`).

#### 3. Evidencias Multimedia y Optimización con Cloudinary (05:15 - 06:30)
* **Qué decir**:
  > *"Cada bestia puede tener múltiples evidencias adjuntas: fotografías, videos o archivos de avistamiento.  
  > Toda la multimedia se almacena en la nube mediante **Cloudinary**. Para optimizar el rendimiento y sortear los límites de tamaño, creamos `ImageHelper.java`, que procesa en memoria las imágenes de gran resolución (4K/teléfonos móviles) usando `ImageIO` y `Graphics2D` para escalarlas a un máximo de **1920x1080** con compresión de alta calidad antes de subirlas.  
  > Además, el servlet cuenta con mecanismos de rollback para eliminar archivos huérfanos si la base de datos rechaza la transacción."*
* **Qué mostrar en pantalla**:
  * Modal interactivo abriendo fotos y videos de evidencia.
  * Subida de una imagen en `nuevoRegistro.jsp` mostrando la previsualización inmediata.

---

### ⏱️ BLOQUE 3 (06:30 - 10:00) | Persona 3: Mapas, Panel Admin, Emails y Tareas Background

#### 1. Georreferenciación con Mapas Interactivos (06:30 - 07:45)
* **Qué decir**:
  > *"Para situar los avistamientos en el mundo real, implementamos mapas interactivos utilizando **Leaflet** y capas cartográficas de **ArcGIS**.  
  > En el mapa global (`mapa.jsp`) podemos ver marcadores con fotos de cada bestia según las coordenadas de sus hábitats.  
  > Además, para facilitar la carga administrativa, diseñamos `mapaCargaHabitat.jsp`: un selector visual embebido en un iframe donde el administrador simplemente hace clic o arrastra un marcador en el mapa, y las coordenadas exactas de latitud y longitud se transfieren automáticamente al formulario."*
* **Qué mostrar en pantalla**:
  * Navegación por el Mapa Global interactivo haciendo zoom y click en los marcadores.
  * Apertura del modal selector de coordenadas en la creación de hábitats.

#### 2. Panel de Administración y Candidaturas (07:45 - 08:45)
* **Qué decir**:
  > *"Los usuarios con rol de Investigador tienen acceso al **Dashboard de Administración**. Desde allí pueden realizar la gestión completa (CRUD) de Categorías, Hábitats, Características ambientales, Tipos de Evidencia y Usuarios Lectores.  
  > También cuentan con la sección de **Candidaturas**, donde pueden revisar las solicitudes pendientes de los lectores que desean ser investigadores, aprobándolas o rechazándolas con un solo clic."*
* **Qué mostrar en pantalla**:
  * Panel de Administración (`adminDashboard.jsp`) alternando pestañas (Categorías, Hábitats, Usuarios) y la pantalla de Candidaturas (`solicitudesInvestigador.jsp`).

#### 3. Noticias, Jakarta Mail y Background Job (08:45 - 09:45)
* **Qué decir**:
  > *"Bestiario mantiene comunicada a su comunidad mediante dos vías:
  > 1. **Redacción de Noticias**: Los investigadores publican artículos que se difunden automáticamente a los usuarios suscritos vía email con plantillas HTML responsivas (`EmailTemplates.java`).
  > 2. **Procesos en Segundo Plano**: Implementamos un `BackgroundJobListener` que ejecuta un `ScheduledExecutorService` programado. Todas las noches recopila los nuevos registros aprobados en el día y despacha un resumen automático a todos los investigadores."*
* **Qué mostrar en pantalla**:
  * Vista de redactar noticia, bandeja de correo de prueba con un email HTML recibido o fragmento de código del scheduler en `BackgroundJobListener.java`.

#### 4. Conclusiones y Cierre (09:45 - 10:15)
* **Qué decir** (Hablan los 3 o Persona 3):
  > *"En conclusión, Bestiario combina una sólida arquitectura empresarial en Java, control de versiones de contenido, integración multimedia en la nube, georreferenciación y automatización en background. ¡Muchas gracias por su atención!"*

---

## 💡 Consejos para la Grabación

1. **Resolución y Pantalla**: Grabar en 1080p (1920x1080).
2. **Navegación Fluida**: Tener pestañas previamente abiertas con datos de prueba cargados (`seed_data.sql`).
3. **Roles para la demo**: Tener un navegador en modo normal logueado como **Investigador** y una ventana de incógnito como **Lector** para mostrar las diferencias de permisos sin tener que cerrar e iniciar sesión constantemente.
