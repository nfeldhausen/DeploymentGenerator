FROM openjdk:11-jre-slim

WORKDIR /backend
COPY ./backend.jar ./

ENV DOCKER_HOST unix:///var/run/docker.sock

EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar","./backend.jar"]
