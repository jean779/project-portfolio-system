# Etapa 1: build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia os arquivos do projeto
COPY pom.xml .
COPY src ./src

# Compila o WAR
RUN mvn clean package -DskipTests

# Etapa 2: runtime com Tomcat
FROM tomcat:10.1.42-jdk17-temurin

# Limpa a aplicação padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado para o webapps
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
