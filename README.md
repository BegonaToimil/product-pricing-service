# Product pricing service

A Spring Boot REST API that returns the applicable price for a product and brand at a specified date and time. It uses an in-memory H2 database, initialized with the sample prices, and a hexagonal architecture with separate domain, application, web, and persistence layers.

## Requirements

- Java 21
- Maven, or the included Maven Wrapper

## Run the application

From the project root, start the service with:

```shell
# Windows PowerShell
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

The service starts at `http://localhost:8080`. H2 is created and populated from `src/main/resources/schema.sql` and `src/main/resources/data.sql` on startup.

### H2 console

The H2 web console is available at `http://localhost:8080/h2-console`.

| Setting | Value |
|---|---|
| JDBC URL | `jdbc:h2:mem:pricesdb` |
| User name | `pricing_user` |
| Password | `pricing_password` |

These are exercise-only credentials. The console is enabled to let the reviewer inspect the in-memory data; disable it and replace the credentials before deploying the service to a shared or production environment.

## API

### Get the applicable price

```http
GET /api/v1/prices?applicationDate={date-time}&productId={id}&brandId={id}
```

All parameters are required. `applicationDate` uses an ISO-8601 local date-time, for example `2020-06-14T16:00:00`. Product and brand identifiers must be positive integers.

If multiple price records match the product, brand, and application date, the record with the highest priority is selected. The start and end dates are inclusive.

Example request:

```shell
curl "http://localhost:8080/api/v1/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"
```

Example response (`200 OK`):

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

The API returns `404 Not Found` when no price applies, `400 Bad Request` when a required parameter is missing or invalid, and `500 Internal Server Error` for unexpected internal failures. Unexpected failures are logged server-side without exposing internal details in the response.

## Logging

The application logs each price lookup and its selected price list and amount at `INFO` level. Missing prices and invalid requests are logged at `WARN`; unexpected application or persistence failures are logged at `ERROR` with their stack trace. Logs are written to the console by default.

## Tests

Run the unit and integration test suite:

```shell
# Windows PowerShell
.\mvnw.cmd clean install

# macOS/Linux
./mvnw clean install
```

The integration tests exercise the five requested scenarios:

| Test | Application date and time | Price list | Price |
|---|---|---:|---:|
| 1 | `2020-06-14T10:00:00` | 1 | 35.50 EUR |
| 2 | `2020-06-14T16:00:00` | 2 | 25.45 EUR |
| 3 | `2020-06-14T21:00:00` | 1 | 35.50 EUR |
| 4 | `2020-06-15T10:00:00` | 3 | 30.50 EUR |
| 5 | `2020-06-16T21:00:00` | 4 | 38.95 EUR |

Additional tests cover domain invariants, no matching price, missing and invalid parameters, inclusive date boundaries, and unsupported HTTP methods.

## Postman

Import `postman/Product Pricing Service.postman_collection.json` into Postman. With the application running at `http://localhost:8080`, run the **Product Pricing Service** collection. Its seven requests include the five required pricing scenarios and checks for the `404` and `400` responses. The collection's `baseUrl` variable can be changed when running the service on a different host or port.

## Architecture

- `domain`: the `Price` model and domain exceptions; it has no Spring or JPA dependencies.
- `application.port.in`: the price lookup use case and its input.
- `application.port.out`: the persistence capability required by the use case.
- `application.service`: the use case implementation, depending on the output port.
- `adapter.web`: REST request/response mapping and centralized HTTP error handling.
- `adapter.persistence`: the JPA entity, Spring Data repository, and output-port implementation.
- `adapter.config`: Spring wiring for the application use case.
