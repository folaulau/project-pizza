########### Run #########
FROM java:8

ADD target/pizzaria-api-springboot.jar pizzaria-api-springboot.jar
EXPOSE 8898

ENTRYPOINT ["java","-jar", "pizzaria-api-springboot.jar"]


########## Build and Run(eat more resources) ###########

# Build
#FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
#COPY pom.xml /tmp/
#COPY src /tmp/src/
#WORKDIR /tmp/
#RUN mvn package -Dmaven.test.skip=true
#COPY --from=MAVEN_TOOL_CHAIN /tmp/target/springboot-with-hasura.jar springboot-with-hasura.jar

# Run
#FROM java:8
#EXPOSE 8898
#ENTRYPOINT ["java","-jar", "springboot-with-hasura.jar"]

