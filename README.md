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

# create docker image
./docker-compile-image.sh

# start docker container
./docker-start-container.sh 

![image](https://user-images.githubusercontent.com/1216181/118358501-6383fb00-b57f-11eb-9195-01378a6e5bd4.png)

![image](https://user-images.githubusercontent.com/1216181/118358526-86aeaa80-b57f-11eb-8e62-6ebcd06c7fc7.png)

![image](https://user-images.githubusercontent.com/1216181/118358553-9928e400-b57f-11eb-96d8-1438e1506dd4.png)