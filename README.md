# Sigma-Bank

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)

Sigma Bank es una aplicación robusta de servicios bancarios desarrollada con **Spring Boot 4**. Implementa una arquitectura de seguridad basada en **JWT (JSON Web Tokens)**, permitiendo a los usuarios gestionar cuentas en múltiples divisas, realizar transferencias y gestionar tarjetas virtuales de forma segura.

## Características Principales

- **Seguridad Avanzada:** Autenticación y autorización mediante JWT y Spring Security 6.
- **Gestión Multi-Divisa:** Soporte para USD, EUR, NGN, GBP, INR y MXN con tasas de cambio dinámicas.
- **Operaciones Bancarias:**
    - Registro e inicio de sesión de usuarios.
    - Creación de cuentas bancarias únicas por divisa.
    - Transferencias entre usuarios de Sigma Bank.
    - Conversión de divisas entre cuentas propias.
    - Gestión de tarjetas virtuales (crédito/débito).
- **Persistencia de Datos:** Integración con PostgreSQL y Spring Data JPA.

---

Tecnologías Utilizadas

- Java 21
- Spring Boot 4
- Spring Security 6
- JWT (JSON Web Tokens)
- PostgreSQL
- Spring Data JPA
- Docker
- Supabase

---

## Configurarion del Proyecto con docker

1. **Docker:**
   ```docker
   docker pull jorgecoder67/sigmabank:latest
   docker run -p 8070:8070 jorgecoder67/sigmabank:latest
## Configuración del Proyecto clonando

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Penya123/Sigma-Bank.git
   cd Sigma-Bank
   
2. **Configurar appliation.properties**

   ```code
   # Url en formato jdbc session pooler
   spring.datasource.url=jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres?sslmode=require
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_password
   jwt.secret=clave_jwt
   currency.api.key=clave_currency
   
3. **Ejecutar la aplicación**

   ```bash
   ./mvnw spring-boot:run
   
---

## Guia de pruebas

   ### Registrar usuario
    curl -X POST http://localhost:8070/user/register \
    -H "Content-Type: application/json" \
    -d '{
    "firstName": "Andrea",
    "lastName": "García",
    "username": "Andygar@email.com",
    "password": "123",
    "gender": "FEMALE"
    }'
  ### Login (Obtener Token JWT)
    curl -X POST http://localhost:8070/user/login \
    -H "Content-Type: application/json" \
    -d '{
    "username": "Andygar@email.com",
    "password": "123"
    }'
  ### Crear cuenta bancaria
    curl -X POST http://localhost:8070/accounts/create \
    -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
    -H "Content-Type: application/json" \
    -d '{
    "code": "USD",
    "symbol": "$",
    "label": "United States Dollar"
    }'
  
  ### Transferencia de fondo
    curl -X POST http://localhost:8070/accounts/transfer \
    -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
    -H "Content-Type: application/json" \
    -d '{
    "recipientAccountNumber": 1234567890,
    "amount": 100.0,
    "code": "USD"
    }'

