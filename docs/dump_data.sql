USE `bestiario`;

INSERT INTO usuario (correo, contraseña, estado, fechaNacimiento, nombre, apellido, dni) VALUES
('lector@a.com', '123', 'lector', '1990-05-10', 'lector', 'Principal', '12345678'),
('test@a.com', '123', 'investigador', '1995-03-15', 'Juan', 'Perez', '23456789'),
('santifnob@gmail.com', '123', 'investigador', '1998-07-22', 'Maria', 'Gomez', '34567890');

INSERT INTO categoria (descripcion, nombre) VALUES
('Criaturas mitológicas clásicas', 'Mitológica'),
('Bestias voladoras', 'Voladora'),
('Criaturas acuáticas', 'Acuática'),
('Bestias terrestres', 'Terrestre');

INSERT INTO habitat (nombre, localizacion) VALUES
('Bosque Oscuro', 'Europa'),
('Montañas Heladas', 'Asia'),
('Océano Profundo', 'Atlántico'),
('Desierto Ardiente', 'África');

INSERT INTO caracteristica (descripcion, idHabitat) VALUES
('Vegetación densa', 1),
('Temperaturas bajo cero', 2),
('Alta presión', 3),
('Clima extremo', 4);

INSERT INTO tipo_evidencia (descripcion) VALUES
('Fotografía'),
('Video'),
('Huella'),
('Testimonio');

INSERT INTO bestia (nombre, peligrosidad) VALUES
('Dragón Carmesí', 'Alta'),
('Kraken', 'Alta'),
('Fénix', 'Media'),
('Hombre Lobo', 'Media'),
('Duende del Bosque', 'Baja');

INSERT INTO bestia_categoria VALUES
(1,1),(1,2),
(2,3),
(3,1),(3,2),
(4,4),
(5,4);

INSERT INTO bestia_habitat VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(1,5);

INSERT INTO contenido_registro (introduccion, historia, descripcion, resumen) VALUES
('Dragón legendario', 'Origen antiguo', 'Gran criatura voladora', 'Dragón poderoso'),
('Bestia marina', 'Leyendas de marineros', 'Gigante de tentáculos', 'Kraken temido'),
('Ave mítica', 'Renace de sus cenizas', 'Fuego eterno', 'Fénix inmortal');

INSERT INTO registro (nroRegistro, idContenido, fechaAprobacion, idUsuario, estado, idBestia) VALUES
(1,1,'2025-01-01',1,'aprobado',1),
(2,2,'2025-01-02',2,'aprobado',2),
(3,3,NULL,3,'pendiente',3);

INSERT INTO evidencia (nroEvidencia, fechaObtencion, estado, link, idTipoEvidencia) VALUES
(1,'2025-01-10','aprobado','http://img1.com',1),
(2,'2025-01-11','pendiente','http://img2.com',2),
(3,'2025-01-12','aprobado','http://img3.com',3);

INSERT INTO bestia_evidencia VALUES
(1,1,1,'Foto del dragón'),
(2,2,2,'Video del kraken'),
(3,3,3,'Huella encontrada');

INSERT INTO comentario (idUsuario, idBestia, fechaPublicacion, contenido) VALUES
(2,1,'2025-02-01','Increíble criatura'),
(3,2,'2025-02-02','Muy peligrosa'),
(1,3,'2025-02-03','Interesante caso');

INSERT INTO noticia (titulo, contenido, fechaPublicacion, idUsuario) VALUES
('Avistamiento de Dragón', 'Se vio un dragón en las montañas', '2025-03-01',1),
('Nueva evidencia del Kraken', 'Se encontró un video', '2025-03-02',2);

