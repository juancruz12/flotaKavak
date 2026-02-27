FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Usar imagen base más pequeña para la ejecución
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR compilado desde el builder
COPY --from=builder /app/target/flota-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8087

# Configurar zona horaria
ENV TZ=America/Argentina/Buenos_Aires


# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

