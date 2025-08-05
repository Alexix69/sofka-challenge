# Backend Challenge - Spring Boot + PostgreSQL

## 📌 Descripción del Proyecto

Este es un servicio backend desarrollado en **Spring Boot** que gestiona clientes, cuentas y transacciones bancarias. La aplicación sigue principios de **arquitectura hexagonal**, usa **PostgreSQL** como base de datos y está completamente dockerizada para facilitar su despliegue.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Lombok**
- **MapStruct**
- **JUnit & Mockito (para pruebas)**

## 📂 Estructura del Proyecto

```
backend-challenge/
│── src/main/java/com/sofka/backend_challenge
│   ├── account_transaction/   # Módulo de cuentas y transacciones
│   ├── client_person/         # Módulo de clientes y personas
│   ├── common/                # Excepciones, DTOs y configuraciones comunes
│   ├── application/           # Casos de uso
│   ├── infrastructure/        # Persistencia y configuración
│   ├── web/controller/        # Controladores REST
│── src/main/resources/
│   ├── application.properties # Configuración de la base de datos
│── target/                    # JAR generado tras la compilación
│── Dockerfile                  # Configuración del contenedor
│── docker-compose.yml           # Configuración para ejecución con Docker
│── pom.xml                      # Dependencias de Maven
│── README.md                    # Documentación
```

## ⚡ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17** ([Descargar](https://adoptium.net/))
- **Maven** ([Descargar](https://maven.apache.org/download.cgi))
- **Docker y Docker Compose** ([Descargar](https://www.docker.com/get-started))

## 🔧 Instalación y Configuración

### 1️⃣ Clonar el repositorio

```sh
git clone https://github.com/tu_usuario/backend-challenge.git
cd backend-challenge
```

### 2️⃣ Compilar la aplicación

```sh
mvn clean package -DskipTests
```

### 3️⃣ Configurar la base de datos (Opcional, si no usas Docker)

Si deseas ejecutar la aplicación sin Docker, edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/backend_db
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## 🐳 Despliegue con Docker

Para ejecutar la aplicación junto con PostgreSQL en contenedores Docker, sigue estos pasos:

### 1️⃣ Construir y levantar los contenedores

```sh
docker-compose up --build
```

### 2️⃣ Verificar que los contenedores están corriendo

```sh
docker ps
```

Deberías ver algo similar a esto:

```
CONTAINER ID   IMAGE        STATUS           PORTS                  NAMES
abc123         backend_app  Up 10 minutes   0.0.0.0:8080->8080/tcp  backend_app
xyz456         postgres     Up 10 minutes   0.0.0.0:5432->5432/tcp  postgres_db
```

### 3️⃣ Acceder a la API

La aplicación estará disponible en `http://localhost:8080`

## 📌 Endpoints Principales

La API expone los siguientes endpoints:

### **🔹 Clientes**

- `POST /v1/clients` → Crear cliente
- `GET /v1/clients/{id}` → Obtener cliente por ID
- `DELETE /v1/clients/{id}` → Eliminar cliente (solo si no tiene cuentas/movimientos)

### **🔹 Cuentas**

- `POST /v1/accounts` → Crear cuenta
- `GET /v1/accounts/{id}` → Obtener cuenta por ID
- `DELETE /v1/accounts/{id}` → Eliminar cuenta (solo si no tiene transacciones)

### **🔹 Transacciones**

- `POST /v1/transactions` → Crear transacción (retiros/depositos)
- `GET /v1/transactions/account/{accountId}` → Obtener transacciones de una cuenta

### **🔹 Reportes**

- `GET /v1/reports?clientId={id}&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD` → Generar reporte de transacciones

## 🧪 Pruebas

Para ejecutar las pruebas unitarias:

```sh
mvn test
```

## ⛔ Detener los contenedores

```sh
docker-compose down
```

Esto eliminará los contenedores sin borrar los datos almacenados en el volumen de PostgreSQL.

## 🎯 Conclusión

✅ **Proyecto modular y escalable** con arquitectura hexagonal. ✅ **Dockerizado para fácil despliegue en cualquier entorno.** ✅ **Compatible con PostgreSQL y completamente probado.**

