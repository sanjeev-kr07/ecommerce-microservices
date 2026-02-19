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
curl "http://localhost:8081/inventory/v1/1001"
```
Sample response
```{
    "productId": 1001,
    "productName": "Laptop",
    "batches": [
        {
            "batchId": 1,
            "quantity": 54,
            "expiryDate": "2026-06-25"
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
    "message": "Inventory updated successfully"
}
```

3) Create an order (POST to order service)

Single-line form:

```bash
curl -X POST http://localhost:8082/order -H "Content-Type: application/json" -d '{"productId":1001,"quantity":2}'
```
```
{
    "orderId": 11,
    "productId": 1001,
    "productName": "Laptop",
    "quantity": 14,
    "status": "PLACED",
    "message": "Order placed. Inventory reserved."
}
```
