docker run -d \
--name vaadin-stock-manager \
--link vaadin-db \
-p 8080:8080 \
-e SPRING_DATASOURCE_URL="jdbc:mysql://vaadin-db:3306/training?autoReconnect=true&allowPublicKeyRetrieval=true&useSSL=false" \
vaadin-stock-manager:latest