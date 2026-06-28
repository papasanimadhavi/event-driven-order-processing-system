# Event Driven Order Processing System

## Overview

This repository demonstrates an Event-Driven Order Processing System built using Java, Spring Boot, Apache Kafka, and Microservices.

The application simulates a real-world order processing workflow using asynchronous event-driven communication between distributed microservices. It showcases how Apache Kafka can be used to coordinate business events, improve scalability, and decouple services in a microservices architecture.

As part of my backend engineering learning, I am exploring, customizing, and enhancing this project to gain practical experience with event-driven architecture, distributed transactions, and modern microservices development.

## Technologies Used

-   Java
-   Spring Boot
-   Apache Kafka
-   Spring Kafka
-   Microservices
-   REST APIs
-   Maven
-   Docker
-   Git
-   Kafka Streams
-   Hibernate
-   JPA
-   PostgreSQL/MySQL
-   JUnit
-   Mockito
-   Distributed Systems

## Project Modules

-   Order Service
-   Payment Service
-   Stock Service
-   Base Domain Module

## Architecture Overview

The system follows an event-driven microservices architecture.

Order Service:
- Creates orders
- Publishes order events to Kafka
- Maintains order status

Payment Service:
- Validates customer payment availability
- Reserves payment amount
- Sends payment status events

Stock Service:
- Checks product availability
- Reserves inventory
- Sends stock status events


## Event Flow

1. Client creates an order through Order Service REST API

2. Order Service publishes Order event to Kafka topic

3. Payment and Stock services consume the event

4. Each service performs its own transaction

5. Services publish response events

6. Order Service aggregates responses and updates final order status


## Design Patterns Used

- Event Driven Architecture
- Saga Pattern
- Microservices Architecture
- Producer Consumer Pattern
- Repository Pattern

## Project Highlights

* Event-Driven Microservices Architecture
* Apache Kafka Producer and Consumer Communication
* Saga Pattern for Distributed Transactions
* RESTful APIs using Spring Boot
* Asynchronous Order Processing
* Docker-based Local Deployment
* Modular Maven Project Structure


## Description
The application consists of the following microservices: \
`order-service` - it sends `Order` events to the Kafka topic and orchestrates the process of a distributed transaction \
`payment-service` - it performs local transaction on the customer account basing on the `Order` price \
`stock-service` - it performs local transaction on the store basing on number of products in the `Order`

Here's the diagram with our architecture:

![image](https://raw.githubusercontent.com/piomin/sample-spring-kafka-microservices/master/arch.png)

(1) `order-service` send a new `Order` -> `status == NEW` \
(2) `payment-service` and `stock-service` receive `Order` and handle it by performing a local transaction on the data \
(3) `payment-service` and `stock-service` send a reponse `Order` -> `status == ACCEPT` or `status == REJECT` \
(4) `order-service` process incoming stream of orders from `payment-service` and `stock-service`, join them by `Order` id and sends Order with a new status -> `status == CONFIRMATION` or `status == ROLLBACK` or `status == REJECTED` \
(5) `payment-service` and `stock-service` receive `Order` with a final status and "commit" or "rollback" a local transaction make before

## Prerequisites
- **Java**: JDK 21 (set as Project SDK in your IDE)
- **Maven**: Latest version installed
- **Git**: For cloning the repository

## Running Locally (without Docker)
This is the best option for development and testing. You'll run Kafka locally and then start each microservice.

### Step 1: Download and Install Kafka

1. Download Kafka 3.8.0 from [Apache Kafka Downloads](https://kafka.apache.org/downloads)
2. Extract the archive to a folder, e.g., `C:\kafka`
3. Verify the installation by checking for `bin\windows\` and `config\` directories

### Step 2: Start Zookeeper
Zookeeper is required for Kafka to coordinate brokers and manage metadata.

1. Open **PowerShell** and navigate to your Kafka directory:
   ```powershell
   cd C:\kafka
   ```
2. Start Zookeeper:
   ```powershell
   .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
   ```
3. Keep this terminal open. You should see logs indicating Zookeeper is running on port 2181.

### Step 3: Start Kafka Server
In a **new PowerShell terminal**, navigate to your Kafka directory:
```powershell
cd C:\kafka
```

Start the Kafka broker:
```powershell
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

Keep this terminal open. You should see logs indicating Kafka is running on `localhost:9092`.

### Step 4: Clone and Build the Project

1. Clone the repository:
   ```powershell
   git clone https://github.com/piomin/sample-spring-kafka-microservices.git
   cd sample-spring-kafka-microservices
   ```

2. Build all modules (from the project root directory):
   ```powershell
   mvn clean install
   ```

### Step 5: Start Each Microservice
Open **three separate PowerShell terminals** from the project root directory:

**Terminal 1 - Start Order Service:**
```powershell
cd order-service
mvn spring-boot:run
```
The service will start on `http://localhost:8080`

**Terminal 2 - Start Payment Service:**
```powershell
cd payment-service
mvn spring-boot:run
```

**Terminal 3 - Start Stock Service:**
```powershell
cd stock-service
mvn spring-boot:run
```

All three services should connect to Kafka on `localhost:9092` and show startup logs without errors.

### Step 6: Test the Microservices

Once all services are running, you can test the APIs using **Postman**.

#### Using Postman (GUI)

1. Download [Postman](https://www.postman.com/downloads/)
2. Create a **POST** request to `http://localhost:8080/orders` with JSON body:
   ```json
   {
     "customerId": 1,
     "productId": 1,
     "productCount": 2,
     "price": 100
   }
   ```
3. Create a **POST** request to `http://localhost:8080/orders/generate` (no body)
4. Create a **GET** request to `http://localhost:8080/orders`

#### Using Browser (for GET only)

Simply open your browser and navigate to:
```
http://localhost:8080/orders
```

### Expected Behavior

- When you create/generate an order, the `order-service` publishes an event to Kafka
- The `payment-service` and `stock-service` consume the order events and process local transactions
- The services send response events back to Kafka
- The `order-service` joins and aggregates responses to determine final order status (CONFIRMED, REJECTED, etc.)
- You can see logs in each terminal showing the event flow and processing

### Troubleshooting

- **Kafka connection error**: Ensure Zookeeper and Kafka are running on ports 2181 and 9092 respectively
- **Port already in use**: Check if another app is using port 8080 or 9092. Stop it or modify `server.port` in `application.yml`
- **ClassNotFoundException in stock-service**: Ensure `jackson-databind` dependency is uncommented in `stock-service/pom.xml`
- **Service startup issues**: Check logs in each terminal for detailed error messages

## Running on Docker locally
You can easily run all the apps on Docker with Spring Boot support for:
(a) Testcontainers
(b) Docker Compose

### (a) For Testcontainers
Go to the `order-service` directory and execute:
```shell
$ mvn clean spring-boot:test-run
```

Then go to the `payment-service` directory and execute:
```shell
$ mvn clean spring-boot:test-run
```

Finally go to the `stock-service` directory and execute:
```shell
$ mvn clean spring-boot:test-run
```

You will have three apps running with a single shared Kafka running on Testcontainers.

### (b) For Docker Compose
First build the whole project and images with the following command:
```shell
$ mvn clean package -DskipTests -Pbuild-image
```

Then, from the project root directory, start all services:
```shell
$ docker-compose up
```

You will have Kafka, order-service, payment-service, and stock-service running in Docker containers.


## Future Enhancements

* Add Notification Service for Email/SMS events
* Integrate Swagger/OpenAPI documentation
* Add Global Exception Handling
* Improve Logging and Monitoring
* Add Unit and Integration Tests
* Deploy using Docker Compose and Kubernetes
