#!/bin/fish
cd ..
cd devops-proyect
./mvnw clean package -DskipTests
docker build -t maven-app:1.0 .
minikube image rm docker.io/library/maven-app:1.0
minikube image load maven-app:1.0