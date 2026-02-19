Ecommerce Microservices — Docker Compose

This README explains how to build and run the two services in this repo (inventory-service and order-service) using Docker Compose, and provides sample API calls to test them.

Quick checklist

- Ensure Docker is installed on your machine.
- From the repository root run the build and up commands below.
- Use the sample curl commands to test the services.

Notes and troubleshooting before you start

- The included `docker-compose.yml` must be valid YAML. If you see `//` style comments at the top of the file (e.g. `// filepath: ...`) replace them with `#` comments or remove them — `//` is not valid YAML and will cause Docker Compose to fail.
- The compose file expects `inventory-service/Dockerfile` and `order-service/Dockerfile` to exist and build successfully.
- The `order-service` depends on `inventory-service` and the compose file sets an env var `INVENTORY_BASE_URL=http://inventory-service:8081` so services can talk to each other inside the docker network.

Build and run (recommended)

From the repository root (where this `docker-compose.yml` is located):

```bash
# Build the images (Docker Compose v2)
docker compose build

# Start services in foreground (remove -d if you want to follow logs in the same terminal)
docker compose up -d

# Follow logs
docker compose logs -f

# Stop and remove containers when done
docker compose down
```

If your Docker installation still uses the legacy command, replace `docker compose` with `docker-compose`.

Service endpoints

- Inventory service (mapped to host port 8081): http://localhost:8081
- Order service (mapped to host port 8082): http://localhost:8082

Sample API calls

1) Get inventory for a product (example productId: 1001, productType: DEFAULT)

```bash
curl "http://localhost:8081/inventory/v1/1001"
```

2) Updates inventory after an order is placed

```bash
curl -X POST http://localhost:8081/inventory/v1/update -H "Content-Type: application/json" -d '{
  "productId": 1002,
  "quantity": 7,
  "productType": "DEFAULT"
}'
```

3) Create an order (POST to order service)

Single-line form:

```bash
curl -X POST http://localhost:8082/order -H "Content-Type: application/json" -d '{"productId":1001,"quantity":2}'
```

Multi-line form (more readable in shell):

```bash
curl -X POST http://localhost:8082/order \
  -H "Content-Type: application/json" \
  -d '{"productId":1001,"quantity":2}'
```
