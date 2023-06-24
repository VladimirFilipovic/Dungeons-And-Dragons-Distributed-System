: ${HOST=localhost}
: ${PORT=8080}

echo "Gradle build"
./gradlew build -x test

echo "Docker up"
docker-compose up -d --build

docker-compose logs -f


echo "Ready"