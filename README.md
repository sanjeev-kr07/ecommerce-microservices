Ecommerce Microservices — Docker Compose

This README explains how to build and run the two services in this repo (inventory-service and order-service) using Docker Compose, and provides sample API calls to test them.

Quick checklist

- Ensure Docker is installed on your machine.
- From the repository root run the build and up commands below.

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

Sample API calls

1) Get inventory for a product (example productId: 1001, productType: DEFAULT)

```bash
curl "http://localhost:8081/inventory/v1/1002"
```
Sample response
```{{
    "productId": 1002,
    "productName": "Smartphone",
    "batches": [
        {
            "batchId": 9,
            "quantity": 22,
            "expiryDate": "2026-05-31"
        },
        {
            "batchId": 10,
            "quantity": 83,
            "expiryDate": "2026-11-15"
        }
    ]
}
```

2) Updates inventory after an order is placed

```bash
curl -X POST http://localhost:8081/inventory/v1/update -H "Content-Type: application/json" -d '{
  "productId": 1002,
  "quantity": 7,
  "productType": "DEFAULT"
}'
```
Sample response
```

{
"reservedFromBatchIds": [9]
}
```

3) Create an order (POST to order service)

Single-line form:

```bash
curl -X POST http://localhost:8082/order -H "Content-Type: application/json" -d '{"productId":1002,"quantity":24}'
```
```
{
    "orderId": 13,
    "productId": 1002,
    "productName": "Smartphone",
    "quantity": 24,
    "status": "PLACED",
    "message": "Order placed. Inventory reserved.",
    "reservedFromBatchIds": [9,10]
}
```
