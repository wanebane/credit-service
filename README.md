# Credit Service

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7-green)

## Features
- ✅ CLI and REST API modes
- ✅ Dynamic interest rate calculation
- ✅ Input validation (DP %, vehicle year, etc.)

## Quick Start
```bash
# Clone and run with Docker
git clone https://github.com/wanebane/credit-service.git
cd credit-service
docker-compose up --build
```

### 1. CLI Mode
**Interactive:**
```bash
docker exec -it credit-simulator_app java -jar credit-simulator.jar
```

**File Input:**
```bash
docker exec -it credit-simulator_app java -jar credit-simulator.jar /app/input_samples/example.txt
```

### 2. API Mode
```bash
curl -X POST http://localhost:8080/credit-service/api/v1/loan/calculate \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleType": "Mobil",
    "vehicleCondition": "Baru",
    "vehicleYear": 2025,
    "totalLoan": 100000000,
    "tenure": 3,
    "downPayment": 35000000
  }'
```

## File Input Format
Place files in `./input_samples`:
```text
# Key-value format
vehicleType=Mobil
vehicleCondition=Baru
vehicleYear=2025
totalLoan=100000000
tenure=3
downPayment=35000000

# Or JSON
{
  "vehicleType": "Mobil",
  "vehicleCondition": "Baru",
  "vehicleYear": 2025,
  "totalLoan": 100000000,
  "tenure": 3,
  "downPayment": 35000000
}
```

## Development
```bash
# Run locally without Docker
mvn spring-boot:run
```