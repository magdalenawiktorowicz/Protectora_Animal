-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: protectora
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `ayuntamientos`
--

DROP TABLE IF EXISTS `ayuntamientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ayuntamientos` (
  `idAyuntamiento` int NOT NULL AUTO_INCREMENT,
  `nombreAyuntamiento` varchar(50) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `telefonoAyuntamiento` int DEFAULT NULL,
  `responsableAyuntamiento` varchar(60) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `direccionAyuntamiento` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `cpAyuntamiento` int DEFAULT NULL,
  PRIMARY KEY (`idAyuntamiento`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ayuntamientos`
--

LOCK TABLES `ayuntamientos` WRITE;
/*!40000 ALTER TABLE `ayuntamientos` DISABLE KEYS */;
/*!40000 ALTER TABLE `ayuntamientos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colonias`
--

DROP TABLE IF EXISTS `colonias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colonias` (
  `idColonia` int NOT NULL AUTO_INCREMENT,
  `nombreColonia` varchar(60) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `cpColonia` int DEFAULT NULL,
  `latitudColonia` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `longitudColonia` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `direccionColonia` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `idAyuntamientoFK1` int DEFAULT NULL,
  `idProtectoraFK2` int DEFAULT NULL,
  PRIMARY KEY (`idColonia`),
  KEY `idAyuntamientoFK1` (`idAyuntamientoFK1`),
  KEY `idProtectoraFK2` (`idProtectoraFK2`),
  CONSTRAINT `colonias_ibfk_1` FOREIGN KEY (`idAyuntamientoFK1`) REFERENCES `ayuntamientos` (`idAyuntamiento`),
  CONSTRAINT `colonias_ibfk_2` FOREIGN KEY (`idProtectoraFK2`) REFERENCES `protectoras` (`idProtectora`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colonias`
--

LOCK TABLES `colonias` WRITE;
/*!40000 ALTER TABLE `colonias` DISABLE KEYS */;
/*!40000 ALTER TABLE `colonias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuidados`
--

DROP TABLE IF EXISTS `cuidados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuidados` (
  `idCuida` int NOT NULL AUTO_INCREMENT,
  `fechaInicioCuidado` date DEFAULT NULL,
  `fechaFinCuidado` date DEFAULT NULL,
  `descripcionCuidado` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `posologiaCuidado` varchar(150) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `idGatoFK5` int DEFAULT NULL,
  `idVeterinarioFK6` int DEFAULT NULL,
  PRIMARY KEY (`idCuida`),
  KEY `idGatoFK4` (`idGatoFK5`),
  KEY `idVeterinarioFK5` (`idVeterinarioFK6`),
  CONSTRAINT `cuidados_ibfk_1` FOREIGN KEY (`idGatoFK5`) REFERENCES `gatos` (`idGato`),
  CONSTRAINT `cuidados_ibfk_2` FOREIGN KEY (`idVeterinarioFK6`) REFERENCES `veterinarios` (`idVeterinario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuidados`
--

LOCK TABLES `cuidados` WRITE;
/*!40000 ALTER TABLE `cuidados` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuidados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gatos`
--

DROP TABLE IF EXISTS `gatos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gatos` (
  `idGato` int NOT NULL AUTO_INCREMENT,
  `nombreGato` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `sexoGato` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `descripcionGato` varchar(150) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `esEsterilizado` tinyint DEFAULT NULL,
  `fotoGato` varchar(255) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `fechaNacimientoGato` date DEFAULT NULL,
  `chipGato` varchar(25) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `idColoniaFK4` int DEFAULT NULL,
  PRIMARY KEY (`idGato`),
  KEY `idColoniaFK4` (`idColoniaFK4`),
  CONSTRAINT `gatos_ibfk_2` FOREIGN KEY (`idColoniaFK4`) REFERENCES `colonias` (`idColonia`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gatos`
--

LOCK TABLES `gatos` WRITE;
/*!40000 ALTER TABLE `gatos` DISABLE KEYS */;
/*!40000 ALTER TABLE `gatos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `protectoras`
--

DROP TABLE IF EXISTS `protectoras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `protectoras` (
  `idProtectora` int NOT NULL AUTO_INCREMENT,
  `nombreProtectora` varchar(60) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `direccionProtectora` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `localidadProtectora` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `telefonoProtectora` int DEFAULT NULL,
  `correoProtectora` varchar(100) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`idProtectora`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `protectoras`
--

LOCK TABLES `protectoras` WRITE;
/*!40000 ALTER TABLE `protectoras` DISABLE KEYS */;
/*!40000 ALTER TABLE `protectoras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `idUsuario` int NOT NULL AUTO_INCREMENT,
  `nombreUsuario` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `contrasenaUsuario` varchar(256) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `tipoUsuario` int DEFAULT NULL,
  PRIMARY KEY (`idUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Magda','31e1df59b390aea997c354a3c06b126dc6317849a92505341d081b2eecf569fd',0),(2,'user','5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `veterinarios`
--

DROP TABLE IF EXISTS `veterinarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `veterinarios` (
  `idVeterinario` int NOT NULL AUTO_INCREMENT,
  `nombreVeterinario` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `apellidosVeterinario` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  `telefonoVeterinario` int DEFAULT NULL,
  `especialidadVeterinario` varchar(45) COLLATE utf8mb4_spanish2_ci DEFAULT NULL,
  PRIMARY KEY (`idVeterinario`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish2_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `veterinarios`
--

LOCK TABLES `veterinarios` WRITE;
/*!40000 ALTER TABLE `veterinarios` DISABLE KEYS */;
/*!40000 ALTER TABLE `veterinarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-13 20:27:15
