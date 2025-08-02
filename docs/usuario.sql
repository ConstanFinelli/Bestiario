CREATE TABLE `bestiario`.`usuario` (
  `idUsuario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `correo` VARCHAR(45) NOT NULL,
  `contraseña` VARCHAR(45) NOT NULL,
  `fechaNacimiento` DATE NULL,
  `nombre` VARCHAR(45) NULL,
  `apellido` VARCHAR(45) NULL,
  `dni` VARCHAR(10) NULL,
  `esInvestigador` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idUsuario`));
