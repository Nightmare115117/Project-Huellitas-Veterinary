FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia el JAR ya compilado
COPY target/devops-proyect-*.jar app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mariadb://mariadb:3306/huellitas_db
ENV SPRING_DATASOURCE_USERNAME=Admin
ENV SPRING_DATASOURCE_PASSWORD=a123b123

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]