USE `bestiario`;

CREATE TABLE `investigador` (
  `idUsuario` int unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into investigador values (1),(2);

CREATE TABLE `noticia` (
  `idNoticia` int NOT NULL AUTO_INCREMENT,
  `fechaPublicacion` date NOT NULL,
  `titulo` varchar(45) CHARACTER SET utf8mb4 NOT NULL,
  `contenido` varchar(180) CHARACTER SET utf8mb4 NOT NULL,
  `estado` varchar(20) CHARACTER SET utf8mb4 NOT NULL,
  `idUsuario` int unsigned NOT NULL,
  PRIMARY KEY (`idNoticia`),
  KEY `id_usuario` (`idUsuario`),
  CONSTRAINT `noticia_investigador_fk` FOREIGN KEY (`idUsuario`) REFERENCES `investigador` (`idUsuario`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

insert into noticia (idNoticia, fechaPublicacion, titulo, contenido, estado, idUsuario) values
	(1,'2023-04-04', 'Titulo-1','Contenido-1','Vigente','1'),
    (2,'2024-06-06', 'Titulo-2','Contenido-2','No Vigente','2');
    
