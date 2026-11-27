# Bestiario

Aplicación web **Jakarta EE 10** (Servlet API 6.0 / JSP / MVC) para el registro, catalogación, georreferenciación y validación colaborativa de criaturas ("bestias"), hábitats, categorías, evidencias multimedia y noticias.

---

## Requisitos de entorno

* **Java Development Kit (JDK):** 21
* **Apache Maven:** 3.8+
* **Servidor de aplicaciones:** Apache Tomcat 10.1+ (compatible con Jakarta EE 10 / `jakarta.servlet`)
* **Base de datos:** MySQL 8.x

---

## Instalación y puesta en marcha

### 1. Base de datos (MySQL)

1. Crear la base de datos:
   ```sql
   CREATE DATABASE bestiario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Importar el esquema desde `docs/bestiario.sql`:
   ```bash
   mysql -u <usuario> -p bestiario < docs/bestiario.sql
   ```
3. Cargar los datos iniciales desde `docs/seed_data.sql`:
   ```bash
   mysql -u <usuario> -p bestiario < docs/seed_data.sql
   ```

### 2. Variables de entorno (`.env`)

Copiar `.env.example` a `.env` en la raíz del proyecto y completar los valores correspondientes:

```env
## CLOUDINARY
CLOUD_NAME=tu_cloud_name
API_KEY=tu_api_key
API_SECRET=tu_api_secret
DEFAULT_PICTURE_ID=id_imagen_por_defecto

## JAKARTA MAIL (SMTP Gmail)
EMAIL_USER=tu_correo@gmail.com
EMAIL_PASS=tu_app_password_de_gmail

## DB CONNECTION
DB_DRIVER=com.mysql.cj.jdbc.Driver
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=tu_password_mysql
DB_NAME=bestiario

## API
BASE_URL=http://localhost:8080/Bestiario
```

* **Cloudinary:** almacenamiento de imágenes y archivos multimedia de evidencias.
* **Jakarta Mail:** envío de correos de notificaciones y recuperación de contraseña (`EMAIL_PASS` corresponde a una contraseña de aplicación si se usa Gmail).
* **DB Connection:** parámetros utilizados por `DbConnector` (`jdbc:mysql://<DB_HOST>:<DB_PORT>/<DB_NAME>`).
* **BASE_URL:** URL base de la aplicación desplegada (por defecto `http://localhost:8080/Bestiario`).

### 3. Compilación y empaquetado (Maven)

Para compilar y generar el archivo `.war`:

```bash
mvn clean package
```

El artefacto generado quedará en `target/Bestiario-0.0.1-SNAPSHOT.war` (puede renombrarse a `Bestiario.war` para desplegarlo en el contexto `/Bestiario` dentro de `webapps/` de Apache Tomcat 10.1+).

---

## Usuarios de prueba (`docs/seed_data.sql`)

El script `docs/seed_data.sql` precarga los siguientes usuarios con contraseñas almacenadas en formato BCrypt:

| Correo | Contraseña | Rol (`estado`) | Nombre |
| :--- | :--- | :--- | :--- |
| `juan@bestiario.com` | `123` | `investigador` | Juan Perez |
| `maria@bestiario.com` | `123` | `investigador` | Maria Gomez |
| `test@a.com` | `123` | `investigador` | Admin Test |
| `carlos@bestiario.com` | `123` | `lector` | Carlos Ruiz |

También es posible registrar nuevos usuarios (`lector`) desde `/auth/register` o restablecer contraseñas mediante `/forgot-password`.

---

## Seguridad y control de acceso

* El filtro `AuthFilter` (`@WebFilter("/*")`) protege las rutas de la aplicación separando:
  * **Zona pública:** recursos estáticos, autenticación (`/auth/*`, `/forgot-password`) y vistas/consultas públicas de bestias, registros, noticias y mapas.
  * **Zona autenticada:** creación de bestias, evidencias, comentarios, actualización de registros y solicitud para ser investigador.
  * **Zona investigador:** panel administrativo (`/admin/*`), gestión de lectores, hábitats, categorías, tipos de evidencia y aprobación/moderación de contenido.
* Las contraseñas se almacenan utilizando **BCrypt** (`org.mindrot.jbcrypt.BCrypt`) con migración automática para contraseñas legadas en Base64 al iniciar sesión.

---

## Documentación del proyecto

* **DER:** [https://drive.google.com/file/d/1ucrdbu4E7ac4S-Lf4EgTGoeFLRmaMsbf/view?usp=drive_link](https://drive.google.com/file/d/1ucrdbu4E7ac4S-Lf4EgTGoeFLRmaMsbf/view?usp=sharing)
* **MD:** [https://drive.google.com/file/d/1rW1u4po6B9GaY64AdkJxnFR-zs0PZ7Y1/view?usp=drive_link](https://drive.google.com/file/d/1rW1u4po6B9GaY64AdkJxnFR-zs0PZ7Y1/view?usp=drive_link)
* **PROPUESTA:** [https://docs.google.com/document/d/1MGnucBxPakG-8iqLuWFKOT3St3GTlDiL2GUZYuQiW0c/edit?usp=drive_link](https://docs.google.com/document/d/1MGnucBxPakG-8iqLuWFKOT3St3GTlDiL2GUZYuQiW0c/edit?usp=sharing)
* **DRIVE COMPLETO:** https://drive.google.com/drive/folders/1ccgwzvcRNsP5z6RZaLPbQoq2ogoyZucQ
