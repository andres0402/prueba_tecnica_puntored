## Prueba Técnica Puntored – Backend de recargas

## Características
- **Obtención de token**: consume el endpoint externo de autenticación y maneja errores.
- **Listado de proveedores**: expone `/suppliers` y delega en el API externo.
- **Recarga**: expone `/recharge` (POST), envía la transacción al API externo y persiste el resultado.
- **Historial de transacciones**: expone `/transactions` (GET) consultando la base de datos MySQL.
- **Manejo de errores**: devuelve mensajes claros y utiliza códigos HTTP apropiados.
- **CORS habilitado**: `@CrossOrigin(origins = "*")` en el controlador.

## Tecnologías utilizadas
- **Java 17** (JDK 17)
- **Spring Boot 3.5.4** (Web, Data JPA)
- **Hibernate/JPA**
- **RestTemplate** para consumo de API externo
- **Lombok** para generación de boilerplate
- **Base de datos**: MySQL (H2 incluido como dependencia de runtime opcional)
- **Maven** como gestor de dependencias y build

## Estructura del proyecto
```
prueba_tecnica/
  ├─ pom.xml
  └─ src/
     ├─ main/
     │  ├─ java/com/prueba_tecnica/prueba_tecnica/
     │  │  ├─ PruebaTecnicaApplication.java
     │  │  ├─ controller/
     │  │  │  └─ TransactionsController.java
     │  │  ├─ data/
     │  │  │  └─ TransactionsRepository.java
     │  │  ├─ model/
     │  │  │  ├─ AuthRequestBody.java
     │  │  │  ├─ Recharge.java
     │  │  │  ├─ Supplier.java
     │  │  │  ├─ Token.java
     │  │  │  └─ TransactionResult.java
     │  │  └─ service/
     │  │     ├─ RechargeService.java
     │  │     └─ RechargeServiceImpl.java
     │  └─ resources/
     │     └─ application.properties
     └─ test/
        └─ java/com/prueba_tecnica/prueba_tecnica/
           └─ PruebaTecnicaApplicationTests.java
```

## Prerrequisitos
- Java 17 instalado y configurado (`JAVA_HOME`).
- Maven 3.9+.
- MySQL accesible y credenciales válidas.

## Instalación y configuración
1. Clona el repositorio.
2. Configura `src/main/resources/application.properties` con tus valores:
   - Base de datos (ejemplo):
     ```properties
     spring.datasource.url=jdbc:mysql://<host>:<port>/<db>?createDatabaseIfNotExist=true
     spring.datasource.username=<usuario>
     spring.datasource.password=<password>
     spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

     spring.jpa.hibernate.ddl-auto=create-drop
     spring.jpa.database=mysql
     spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
     spring.jpa.show-sql=true
     ```
   - API externo Puntored (reemplaza con tus credenciales):
     ```properties
     puntored.api.url=https://us-central1-puntored-dev.cloudfunctions.net/technicalTest-developer/api/
     puntored.api.key=<API_KEY>
     puntored.api.username=<USUARIO_API>
     puntored.api.password=<PASSWORD_API>
     ```

## Instalación
- Compilar y empaquetar:
  ```bash
  mvn clean package -DskipTests
  ```
- Ejecutar en modo dev:
  ```bash
  mvn spring-boot:run
  ```
- O ejecutar el JAR:
  ```bash
  java -jar target/prueba_tecnica-0.0.1-SNAPSHOT.jar
  ```

## Dependencias principales (pom.xml)
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `jakarta.persistence-api` 3.1.0
- `lombok`
- `mysql-connector-j` (runtime)
- `h2` (runtime opcional)
- `spring-boot-starter-test` (test)

## API Backend
- **Base URL**: `http://localhost:8080`
- **Formato**: JSON.
- **CORS**: permitido para cualquier origen.

## Endpoints utilizados
- **GET** `/getAuthToken`
  - Retorna: `{ "token": "..." }` o mensaje de error.

- **GET** `/suppliers`
  - Retorna: lista de proveedores del API externo.
  - Ejemplo de respuesta:
    ```json
    [
      { "id": "1", "name": "Proveedor A" },
      { "id": "2", "name": "Proveedor B" }
    ]
    ```

- **POST** `/recharge`
  - Cuerpo: ver "Formato de datos para recarga".
  - Retorna: resultado de la transacción y persiste en BD cuando es exitosa.
  - Códigos: `200 OK` en éxito, `500` con mensaje cuando falla.

- **GET** `/transactions`
  - Retorna: historial persistido en MySQL.
  - Códigos: `200 OK` con lista, `204 No Content` si no hay registros.

## Formato de datos para recarga
Modelo `Recharge` (cuerpo del POST `/recharge`):
```json
{
  "cellPhone": "3001234567",
  "value": 10000.0,
  "supplierId": "1"
}
```
- **cellPhone**: string (número de teléfono)
- **value**: number (monto a recargar)
- **supplierId**: string (identificador del proveedor)

Respuesta exitosa (`TransactionResult`):
```json
{
  "transactionalID": "abc-123",
  "message": "Transacción exitosa",
  "cellPhone": "3001234567",
  "value": "10000"
}
```
En error, el backend devuelve `500` con un mensaje en el cuerpo o un `TransactionResult` con `transactionalID` vacío.

## Funcionalidades
- **Autenticación integrada** con el servicio externo (obtención de token con `x-api-key`).
- **Consumo de proveedores** desde el servicio externo.
- **Ejecución de recargas** y **persistencia** del resultado en la tabla `transactions`.
- **Consulta de historial** desde MySQL vía `TransactionsRepository`.
- **Manejo básico de excepciones** y códigos HTTP.

## Ejemplos (cURL)
- Obtener token:
  ```bash
  curl -X GET http://localhost:8080/getAuthToken
  ```
- Listar proveedores:
  ```bash
  curl -X GET http://localhost:8080/suppliers
  ```
- Realizar recarga:
  ```bash
  curl -X POST http://localhost:8080/recharge \
    -H "Content-Type: application/json" \
    -d '{
      "cellPhone": "3001234567",
      "value": 10000.0,
      "supplierId": "4689"
    }'
  ```
- Ver historial:
  ```bash
  curl -X GET http://localhost:8080/transactions
  ```

## Ejecutar tests unitarios
Para ejecutar los tests: ./mvnw.cmd -q -B test en Windows PowerShell, o mvn test


