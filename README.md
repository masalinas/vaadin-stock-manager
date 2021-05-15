# Description

A Vaadin Flow 14 PoC to manage stock in a organization

# Frameworks
- Vaadin Spring Boot 2.4.5
- Vaadin 14.5.4 Flow
- Vaadin Testbech 14.5.4
- Vaadin addon leaflet4vaadin 0.5.0
- Spring Boot Security 2.4.5
- Spring Boot Data JPA 2.4.5
- Spring Boot Validation 2.4.5
- Spring Boot Devtools 2.4.5
- Spring Boot Test 2.4.5
- MySQL Connector 8.0.23

# Configure database connection
```java
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
```

# Compile Vaadin Stock Manager in production mode
./build-production.sh

# Start Vaadin Stock Manager services from step by step
- build Vaadin Stock Manager docker image

```shell
./docker-compile-image.sh
```

- start Vaadin Mysql Database docker container

```shell
./docker-start-vaadin-db.sh 
```

- start Vaadin Stock Manager WebApp docker container

```shell
./docker-start-vaadin-stock-manager.sh 
```

# Clean Vaadin Stock Manager resources from step by step

```shell
docker stop vaadin-stock-manager
docker stop vaadin-db
docker rm vaadin-stock-manager
docker rm vaadin-db
docker rmi vaadin-stock-manager
```

# Start Vaadin Stock Manager services from docker compose

```shell
docker-compose up
```

# Clean Vaadin Stock Manager resources from docker compose

- stop and remove Vaadin Stock Manager services

```shell
docker-compose down
```

- remove Vaadin Stock Manager image

```shell
docker rmi vaadin-stock-manager
```

# Some Vaadin Stock Manager views

![image](https://user-images.githubusercontent.com/1216181/118358501-6383fb00-b57f-11eb-9195-01378a6e5bd4.png)

![image](https://user-images.githubusercontent.com/1216181/118358526-86aeaa80-b57f-11eb-8e62-6ebcd06c7fc7.png)

![image](https://user-images.githubusercontent.com/1216181/118358553-9928e400-b57f-11eb-96d8-1438e1506dd4.png)