FROM openjdk:25-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
ENV DB_URL=jdbc:postgresql://metro.proxy.rlwy.net:53026/railway
ENV DB_USER=postgres
ENV DB_PASS=dDfDerCdczhiHiiORYjWtgHCuLVfrkjL
