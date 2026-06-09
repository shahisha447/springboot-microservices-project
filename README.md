# Spring Boot Microservices Project

A complete microservices architecture built using Spring Boot and Spring Cloud.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Cloud Netflix Eureka
- Zuul API Gateway
- Apache Kafka
- MySQL
- Docker
- Gradle

## Microservices

### Eureka Server
Service discovery and registration.

### Demo Service
User authentication and JWT-based authorization.

### Demo1 Service
Customer management service.

### Zuul Gateway
Single entry point for routing requests to microservices.

## Architecture

Client → Zuul Gateway → Microservices  
                      ↓  
                  Eureka Server

## Features

- JWT Authentication
- Role Based Access Control
- Kafka Messaging
- Service Discovery
- API Gateway Routing
- Dockerized Deployment

## Author

Isha Shah
