use bestiario;

CREATE TABLE `habitat` (
  `idHabitat` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `localizacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idHabitat`),
  UNIQUE KEY `idhabitat_UNIQUE` (`idHabitat`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `caracteristica` (
  `descripcion` varchar(255) NOT NULL,
  `idHabitat` int unsigned NOT NULL,
  PRIMARY KEY (`descripcion`,`idHabitat`),
  KEY `fk_habitat` (`idHabitat`),
  CONSTRAINT `fk_habitat` FOREIGN KEY (`idHabitat`) REFERENCES `habitat` (`idHabitat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
