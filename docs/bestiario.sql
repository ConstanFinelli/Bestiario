CREATE DATABASE  IF NOT EXISTS `bestiario` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bestiario`;
-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (x86_64)
--
-- Host: localhost    Database: bestiario
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bestia`
--

DROP TABLE IF EXISTS `bestia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bestia` (
  `idBestia` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `peligrosidad` varchar(45) NOT NULL,
  PRIMARY KEY (`idBestia`),
  UNIQUE KEY `idbestia_UNIQUE` (`idBestia`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bestia_categoria`
--

DROP TABLE IF EXISTS `bestia_categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bestia_categoria` (
  `idBestia` int NOT NULL,
  `idCategoria` int NOT NULL,
  PRIMARY KEY (`idBestia`,`idCategoria`),
  KEY `fk_categoria_ bestia_categoria_idx` (`idCategoria`),
  CONSTRAINT `fk_bestia_bestia_categoria` FOREIGN KEY (`idBestia`) REFERENCES `bestia` (`idBestia`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_categoria_ bestia_categoria` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bestia_evidencia`
--

DROP TABLE IF EXISTS `bestia_evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bestia_evidencia` (
  `nroEvidencia` int NOT NULL,
  `idBestia` int NOT NULL,
  `detalle` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`nroEvidencia`,`idBestia`),
  KEY `idBestia-Evidencia_idx` (`idBestia`),
  CONSTRAINT `idBestia-Evidencia` FOREIGN KEY (`idBestia`) REFERENCES `bestia` (`idBestia`),
  CONSTRAINT `idEvidencia-Bestia` FOREIGN KEY (`nroEvidencia`) REFERENCES `evidencia` (`nroEvidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bestia_habitat`
--

DROP TABLE IF EXISTS `bestia_habitat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bestia_habitat` (
  `idHabitat` int NOT NULL,
  `idBestia` int NOT NULL,
  PRIMARY KEY (`idHabitat`,`idBestia`),
  KEY `fk_bestia_habitat_bestia_idx` (`idBestia`),
  CONSTRAINT `fk_bestia_habitat_bestia` FOREIGN KEY (`idBestia`) REFERENCES `bestia` (`idBestia`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bestia_habitat_habitat` FOREIGN KEY (`idHabitat`) REFERENCES `habitat` (`idHabitat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `caracteristica`
--

DROP TABLE IF EXISTS `caracteristica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `caracteristica` (
  `descripcion` varchar(255) NOT NULL,
  `idHabitat` int NOT NULL,
  PRIMARY KEY (`descripcion`,`idHabitat`),
  KEY `fk_habitat` (`idHabitat`),
  CONSTRAINT `fk_caracteristicas_habitat` FOREIGN KEY (`idHabitat`) REFERENCES `habitat` (`idHabitat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `idCategoria` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idCategoria`),
  UNIQUE KEY `idCategoria_UNIQUE` (`idCategoria`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comentario`
--

DROP TABLE IF EXISTS `comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentario` (
  `nroComentario` int unsigned NOT NULL AUTO_INCREMENT,
  `idUsuario` int NOT NULL,
  `idBestia` int NOT NULL,
  `fechaPublicacion` date NOT NULL,
  `contenido` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`nroComentario`,`idUsuario`,`idBestia`,`fechaPublicacion`),
  UNIQUE KEY `nroComentario_UNIQUE` (`nroComentario`),
  KEY `fk_comentario_idBestia_idx` (`idBestia`),
  KEY `fk_comentario_idUsuario_idx` (`idUsuario`),
  CONSTRAINT `fk_comentario_idBestia` FOREIGN KEY (`idBestia`) REFERENCES `bestia` (`idBestia`),
  CONSTRAINT `fk_comentario_idUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contenido_registro`
--

DROP TABLE IF EXISTS `contenido_registro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenido_registro` (
  `idContenido` int NOT NULL AUTO_INCREMENT,
  `introduccion` text NOT NULL,
  `historia` text NOT NULL,
  `descripcion` text NOT NULL,
  `resumen` text NOT NULL,
  PRIMARY KEY (`idContenido`),
  UNIQUE KEY `nro_registro_UNIQUE` (`idContenido`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `evidencia`
--

DROP TABLE IF EXISTS `evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evidencia` (
  `nroEvidencia` int NOT NULL,
  `fechaObtencion` date NOT NULL,
  `estado` varchar(45) NOT NULL DEFAULT '"pendiente"',
  `link` varchar(255) DEFAULT NULL,
  `idTipoEvidencia` int NOT NULL,
  PRIMARY KEY (`nroEvidencia`,`idTipoEvidencia`),
  KEY `idTipoEvidencia_idx` (`idTipoEvidencia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `habitat`
--

DROP TABLE IF EXISTS `habitat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `habitat` (
  `idHabitat` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `localizacion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idHabitat`),
  UNIQUE KEY `idhabitat_UNIQUE` (`idHabitat`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `noticia`
--

DROP TABLE IF EXISTS `noticia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `noticia` (
  `idNoticia` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(45) NOT NULL,
  `estado` varchar(45) NOT NULL DEFAULT 'pendiente',
  `fechaPublicacion` date DEFAULT NULL,
  `idUsuario` int DEFAULT NULL,
  PRIMARY KEY (`idNoticia`),
  KEY `fk_noticia_usuario` (`idUsuario`),
  CONSTRAINT `fk_noticia_usuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registro`
--

DROP TABLE IF EXISTS `registro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registro` (
  `nroRegistro` int NOT NULL,
  `idContenido` int NOT NULL,
  `fechaAprobacion` date DEFAULT NULL,
  `fechaBaja` date DEFAULT NULL,
  `idUsuario` int DEFAULT NULL,
  `estado` varchar(45) NOT NULL DEFAULT '"pendiente"',
  `idBestia` int NOT NULL,
  `main_picture` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`nroRegistro`,`idBestia`),
  UNIQUE KEY `idContenido_UNIQUE` (`idContenido`),
  KEY `idUsuario_idx` (`idUsuario`),
  KEY `idBestia_idx` (`idBestia`),
  CONSTRAINT `idBestia` FOREIGN KEY (`idBestia`) REFERENCES `bestia` (`idBestia`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `idUsuario` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tipo_evidencia`
--

DROP TABLE IF EXISTS `tipo_evidencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_evidencia` (
  `idTipoEvidencia` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) NOT NULL,
  PRIMARY KEY (`idTipoEvidencia`),
  UNIQUE KEY `idTipoEvidencia_UNIQUE` (`idTipoEvidencia`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(255) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `esInvestigador` tinyint NOT NULL DEFAULT '0',
  `fechaNacimiento` date DEFAULT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellido` varchar(45) DEFAULT NULL,
  `dni` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE KEY `correo_UNIQUE` (`correo`),
  UNIQUE KEY `idUsuario_UNIQUE` (`idUsuario`),
  UNIQUE KEY `dni_UNIQUE` (`dni`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-05 15:16:07
