# Sigma-Bank

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

Sigma Bank es una aplicación robusta de servicios bancarios desarrollada con **Spring Boot 3**. Implementa una arquitectura de seguridad basada en **JWT (JSON Web Tokens)**, permitiendo a los usuarios gestionar cuentas en múltiples divisas, realizar transferencias y gestionar tarjetas virtuales de forma segura.

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

## Configuración del Proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Penya123/Sigma-Bank.git
   cd Sigma-Bank
   
2. **Configurar appliation.properties**

   ```code 
   spring.datasource.url=jdbc:postgresql://localhost:5432/sigma_bank_db
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
    curl -X POST http://localhost:8080/api/v1/user/register \
    -H "Content-Type: application/json" \
    -d '{
    "firstName": "Jorge",
    "lastName": "User",
    "username": "jorge@email.com",
    "password": "password123",
    "gender": "MALE"
    }'
  ### Login (Obtener Token JWT)
    curl -X POST http://localhost:8080/api/v1/user/login \
    -H "Content-Type: application/json" \
    -d '{
    "username": "jorge@email.com",
    "password": "password123"
    }'
  ### Crear cuenta bancaria
    curl -X POST http://localhost:8080/api/v1/accounts/create \
    -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
    -H "Content-Type: application/json" \
    -d '{
    "code": "USD",
    "symbol": "$",
    "label": "United States Dollar"
    }'
  
  ### Transferencia de fondo
    curl -X POST http://localhost:8080/api/v1/accounts/transfer \
    -H "Authorization: Bearer <TU_TOKEN_AQUI>" \
    -H "Content-Type: application/json" \
    -d '{
    "recipientAccountNumber": 1234567890,
    "amount": 100.0,
    "code": "USD"
    }'

