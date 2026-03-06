# 📚 Proyecto: Gestión de Libros y Autores

## 🌟 Descripción

Este proyecto permite interactuar con una **API de libros y autores**, almacenar los datos en una **base de datos local** utilizando **JPA/Hibernate**, y realizar diferentes consultas sobre los datos almacenados.

El sistema funciona mediante un **menú en consola** donde el usuario puede buscar libros en la API, guardarlos en la base de datos y realizar consultas como listar libros por idioma o autores vivos en un determinado año.

---

## ⚙️ Funcionalidades

### 🔹 Funciones con la API

* 📖 Listar todos los libros disponibles en la API ordenados por número de descargas.
* 🔍 Buscar libros por título en la API.
* 💾 Guardar libros y sus autores en la base de datos.
* ✍️ Listar autores disponibles en la API.
* 👥 Buscar libros por autor disponibles en la API.
* 🕰️ Mostrar autores vivos en un año determinado.

### 🔹 Funciones con la Base de Datos

* 📚 Listar libros guardados en la base de datos.
* 👥 Listar autores guardados en la base de datos.
* 🕰️ Mostrar autores vivos en un año determinado.
* 📊 Mostrar estadísticas de libros por idioma.

---

## 🛠️ Tecnologías utilizadas

* Java 17+
* Spring Boot
* Spring Data JPA
* Hibernate
* Maven
* Base de datos PostgreSQL
* Streams de Java

---


## ⚠️ Manejo de errores

El sistema contempla diferentes validaciones:

* Evita guardar libros duplicados en la base de datos.
* Maneja excepciones de integridad de datos.
* Valida entradas del usuario (por ejemplo, años inválidos).
* Controla errores de conexión con la API.

---

## 🎯 Objetivo del proyecto

El objetivo de este proyecto es **practicar el consumo de APIs, el uso de JPA/Hibernate y el manejo de Streams en Java**, además de reforzar conceptos de arquitectura por capas y manejo de bases de datos.

---

## 👩‍💻 Autor

Proyecto desarrollado como parte de la práctica de aprendizaje en **Java, Spring Boot y persistencia de datos**.
