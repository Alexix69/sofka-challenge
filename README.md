# Backend Challenge - Spring Boot + PostgreSQL

## 📌 Descripción del Proyecto

Este es un servicio backend desarrollado en **Spring Boot** que gestiona clientes, cuentas y transacciones bancarias. La aplicación sigue principios de **arquitectura hexagonal** y usa **PostgreSQL** como base de datos.

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **MapStruct**

## 📂 Estructura del Proyecto

```
challenge/
│── src/main/java/com/sofka/challenge
│   ├── account_transaction/   # Módulo de cuentas y transacciones
│   ├── client_person/         # Módulo de clientes y personas
│   ├── common/                # Excepciones, DTOs y configuraciones comunes
│   ├── application/           # Casos de uso
│   ├── infrastructure/        # Persistencia y configuración
│   ├── web/controller/        # Controladores REST
│── src/main/resources/
│   ├── application.properties # Configuración de la base de datos
│── target/                    # JAR generado tras la compilación
│── pom.xml                      # Dependencias de Maven
│── README.md                    # Documentación
```

## ⚡ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17** ([Descargar](https://adoptium.net/))
- **Maven** ([Descargar](https://maven.apache.org/download.cgi))

## 🔧 Instalación y Configuración

### 1️⃣ Clonar el repositorio

```sh
git clone https://github.com/Alexix69/sofka-challenge
cd challenge
```

### 2️⃣ Compilar la aplicación

```sh
mvn clean package -DskipTests
```

### 3️⃣ Configurar la base de datos


```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/backend-challenge
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=none
```

### 4️⃣ Acceder a la API

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

## 📌 Recursos adicionales

- **Script de base de datos:** `/src/database`
- **Colección de Postman:** `/src/postman`
