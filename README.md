# Spring Boot Microservices(React JS)

Complete Hotel-Web application where user can add and booked Room. 


##
## Services Overview
  - Booking-Service
  - CLOUD-GATEWAY
  - Config-Server
  - Hotel-Service
  - Inventory-Service
  - Notification-Service
  - Payment-Service
  - Service-Registry
  - User-Service

##

## Tech Stack
The technologies used in this project are:
  - Spring Boot(Microservices)
  - React Js
  - Tanstack query
  - Apache Kafka(Schema-Registry)
  - JWT 
  - MongoDB
  - MySQL
  - Redis
  - API Gateway using Spring Cloud Gateway MVC
  - Unit and Integration Testing using Mockito, WireMock and Test Containers in Booking-Service.
  - Docker & Kubernetes

##

## Application Glimpse

add picture here



##  How to run the services
Make sure you have the following installed on your machine:
  - Docker Desktop 
  - kubectl

##

##  Start Minikube cluster

```bash
minikube start --driver=docker --cpus=3 --memory=5120
```
Run Kubernetes locally inside Docker, and give it 3 CPUs and 5 GB of RAM so it has enough power for this project.

## Deploy the infrastructure

```bash
kubectl apply -f k8s/infra/
```

## Deploy the services

```bash
kubectl apply -f k8s/applications/
```

##  MAKE SURE YOU ARE INSIDE BACK-END FOLDER

##

##  Check the Deployment

```bash
kubectl get all
```

If all the pods, deployment and services are fine then you are good to go, else wait for  
pods to be UP.

## Access the API Gateway

To access the API Gateway, you need to port-forward the gateway service to your local machine

```bash
kubectl port-forward svc/cloud-gateway-svc 9090:9090
```
## Access Hotel-Service

To store and get the image 

```bash
kubectl port-forward svc/hotel-service-svc 8080:8080
```
## To run Frontend

```bash
kubectl port-forward svc/frontend-svc 3000:3000
```
After port-forwarding go to this URL, your all services and frontend is ready now....
```bash
http://localhost:3000/
```

