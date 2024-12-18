#FROM maven:3.6-openjdk-17-slim
FROM maven:3.6-openjdk-17-slim as builder

WORKDIR /app/msvc-users
# no hablitar -> WORKDIR /app

ARG MSVC_NAME=msvc-users

# Importante: como son proyectos anidados este docker file
# depende de un proyecto padre lo que significa que necesita
# el pom del padre y obviamente el pom del hijo para descargar las
# dependencias de mvsc-users y de ochestrator ya que en el pom de
# mvsc-users apunta al del ochestrator por esa razon son los siguientes
# COPY
# COPY ./pom.xml /app
# COPY ./msvc-users .

# Mejora para cuando hacemos un cambio en el proyecto no vuelve a descargar las dependecias de nuevo
# pasamos los poms en orden jerarquico
COPY ./pom.xml /app
COPY ./${MSVC_NAME}/.mvn ./.mvn
COPY ./${MSVC_NAME}/mvnw .
COPY ./${MSVC_NAME}/pom.xml .

# no hablitar -> COPY ./target/msvc-users-0.0.1-SNAPSHOT.jar .

# Commando para empaquetar nuestra app generar el .jar
# cuando se crea el WORKDIR y ya contiene el proyecto msvc-users
# entonces podemos ejecutar el comando mvn para generar el .jar
# el jar queda en la carpeta /app/msvc-users/target
# por esa razon el ENTRYPOIN de abajo tiene el commando
# con la ruta ./target/msvc-users-0.0.1-SNAPSHOT.jar
# RUN mvn clean package -DskipTests

# Nuevo comando descarga solo las dependencias del pom elimina el target
RUN mvn clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

# Esto es por si ya se descargaron las dependencias solo copie el cambio
# en el proyecto y compile
COPY ./${MSVC_NAME}/src ./src
RUN mvn clean package -DskipTests

# --------------------------------------------------------------------------------
# esto lo que significa que es que las imagenes se dividen con FROM
# lo que quiere decir que desde una imagen base creamos otra y copiamos lo que tenga el builder
# a la imagen de aqui abajo
FROM maven:3.6-openjdk-17-slim 

ARG MSVC_NAME=msvc-users
ARG TARGET_FOLDER=/app/${MSVC_NAME}/target

WORKDIR /app

RUN mkdir ./logs

COPY --from=builder ${TARGET_FOLDER}/msvc-users-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001

# ENViRONMENT VARIABLES
ENV PORT=${PORT_APP}

EXPOSE ${PORT}

#ENTRYPOINT [ "java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]
CMD [ "java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]