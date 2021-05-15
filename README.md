# Description

A Vaadin Flow PoC to manage stock in a organization

# frameworks
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
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.datasource.url=jdbc:mysql://localhost:3306/training?createDatabaseIfNotExist=true
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
```

# compile in production mode
./build-production.sh

# Start Vaadin from step by step
- build vaadin docker image

```shell
./docker-compile-image.sh
```

- start vaadin mysql database docker container

```shell
./docker-start-vaadin-db.sh 
```

- start vaadin stock manager docker container

```shell
./docker-start-vaadin-stock-manager.sh 
```

- stop and remove all container and images

```shell
docker stop vaadin-stock-manager
docker stop vaadin-db
docker rm vaadin-stock-manager
docker rm vaadin-db
docker rmi vaadin-stock-manager
```

# Start Vaadin from docker compose


```shell
docker-compose up
```

# Stop Vaadin from docker compose

```shell
docker-compose down
docker rmi vaadin-stock-manager
```

# Some Vaadin Stock Manager views

![image](https://user-images.githubusercontent.com/1216181/118358501-6383fb00-b57f-11eb-9195-01378a6e5bd4.png)

![image](https://user-images.githubusercontent.com/1216181/118358526-86aeaa80-b57f-11eb-8e62-6ebcd06c7fc7.png)

![image](https://user-images.githubusercontent.com/1216181/118358553-9928e400-b57f-11eb-96d8-1438e1506dd4.png)