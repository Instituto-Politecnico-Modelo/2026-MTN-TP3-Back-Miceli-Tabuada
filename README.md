# ⚽ TP3 - Backend (Spring Boot)

![Estado](https://img.shields.io/badge/Estado-En%20progreso-orange?style=for-the-badge)
![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Puerto](https://img.shields.io/badge/Puerto-8081-blue?style=for-the-badge)
![TP3](https://img.shields.io/badge/Proyecto-TP3-purple?style=for-the-badge)

Backend del proyecto **TP3**, desarrollado con **Spring Boot**.

> 🚧 **Este proyecto está en progreso** y puede cambiar con frecuencia.

## 📌 Descripción

Este repositorio contiene la API backend del trabajo práctico.
La aplicación expone servicios REST y está pensada para integrarse con el frontend en React + Vite.

## 🔗 Frontend relacionado

- Repositorio frontend (React + Vite):
  - https://github.com/Instituto-Politecnico-Modelo/2026-MTN-TP3-Front-Miceli-Tabuada

## 🚀 Puerto de ejecución

La aplicación corre en el puerto:

- `8081`

## 🐳 Docker

Este proyecto incluye dos opciones básicas:

- `Dockerfile` (multi-stage)
- `Dockerfile.single` (single-stage)

### Build con Dockerfile (multi-stage)

```bash
docker build -t tp3-backend:latest -f Dockerfile .
```

### Build con Dockerfile.single (single-stage)

```bash
docker build -t tp3-backend:single -f Dockerfile.single .
```

### Run (ejemplo)

```bash
docker run -p 8081:8081 tp3-backend:latest
```

## 👥 Equipo

### Estudiantes

- Francisco Miceli
- Lorenzo Tabuada

### Docentes

- Nicolás Pruscino
- Martín Barbieri
- Magali Cristobo

