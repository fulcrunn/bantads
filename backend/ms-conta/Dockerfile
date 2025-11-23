# --------------------------------------------------
# ESTÁGIO 1: Build (Compilação do JAR)
# Usa uma imagem robusta com Maven e JDK para compilar
# --------------------------------------------------
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia arquivos de build cruciais
# Isso permite que o Docker utilize o cache do Maven (dependency:go-offline)
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# LINHA A SER ADICIONADA: Dá permissão de execução ao Maven Wrapper
RUN chmod +x mvnw 

# Baixa dependências e instala...
RUN ./mvnw dependency:go-offline

# Copia o código fonte da aplicação
COPY src src

# Constrói o pacote JAR (Versão simples, sem BuildKit)
RUN ./mvnw package -DskipTests

# --------------------------------------------------
# ESTÁGIO 2: Runtime (Execução)
# Usa uma imagem leve contendo apenas o JRE
# --------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# A porta que o serviço expõe (ver application.properties)
# Exemplo: ms-cliente é 8080. Adapte para 8081, 8082, etc.
# PORTA DO SERVIÇO:
EXPOSE 8084

# Copia o JAR compilado da fase de build
# Altere o nome do JAR (ms-cliente-0.0.1-SNAPSHOT.jar) para o nome do seu microsserviço
COPY --from=build /app/target/ms-conta-0.0.1-SNAPSHOT.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","/app/app.jar"]