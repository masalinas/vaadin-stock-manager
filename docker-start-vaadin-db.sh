docker run -d \
--name vaadin-db \
-p 3306:3306 \
-v "$(pwd)"/dump:/docker-entrypoint-initdb.d \
-e MYSQL_ROOT_PASSWORD=underground -e MYSQL_DATABASE=training \
mysql:latest