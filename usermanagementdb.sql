-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 09-10-2026 a las 21:40:35
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `usermanagementdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `Id` int(11) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Apellido` varchar(100) DEFAULT NULL,
  `Correo` varchar(100) NOT NULL,
  `PasswordHash` varchar(255) NOT NULL,
  `FechaNacimiento` datetime DEFAULT NULL,
  `Universidad` varchar(150) DEFAULT NULL,
  `Semestre` int(11) DEFAULT NULL,
  `FechaCreacion` datetime NOT NULL DEFAULT current_timestamp(),
  `Activo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`Id`, `Nombre`, `Apellido`, `Correo`, `PasswordHash`, `FechaNacimiento`, `Universidad`, `Semestre`, `FechaCreacion`, `Activo`) VALUES
(1, 'Andrés', 'Quintero', 'andres@test.com', '$2a$11$8dIlYU91pedR4nkBcaEJoOGRxNHOlzWIkhiRQJLUCXluXUHAwiVwW', '1988-08-15 00:00:00', 'UCompensar', 6, '2026-10-07 15:00:00', 1),
(2, 'Andres', 'Quintero', 'andres@ucompensar.edu.co', '$2a$11$yss2iujiOs13r7ULB5wQJux6uWJkdXNuDbe6T5g8sqPr9sZftUT5O', '1990-11-19 00:00:00', 'Ucompensar', 6, '2026-10-08 09:29:15', 1),
(3, 'Diego', 'Monrroy', 'diego@ucompensar.edu.co', '$2a$11$bHWgLXCs68XopF8Q5dBuOOeTxStsne/XPN/2RW7GhvaStW/tvlot6', '1990-11-19 00:00:00', 'Ucompensar', 6, '2026-10-08 20:12:48', 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
