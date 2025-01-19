# Auth service
This Layered Architecture is responsible for:
* Onboarding users
* Roles and permissions
* Authentication

## Tech stack
* Build tool: maven >= 3.9.9
* Java: 21
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites
* Java SDK 21
* A MySQL server

## Start application
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Docker guideline
### Build docker image
`docker build -t <account>/auth-service:0.1.2 .`
### Push docker image to Docker Hub
`docker image push <account>/auth-service:0.1.2`
### Create network:
`docker network create vdtry06-network`
### Start MySQL in vdtry06-network
`docker run --network vdtry06-network --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:9.0.1-oraclelinux9`
### Run your application in vdtry06-network
`docker run --name auth-service --network vdtry06-network -p 8080:8080 -e DBMS_CONNECTION=jdbc:mysql://mysql:3306/auth_service auth-service:0.1.2`
