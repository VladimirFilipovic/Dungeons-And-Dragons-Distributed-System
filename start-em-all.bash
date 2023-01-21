: ${HOST=localhost}
: ${PORT=8080}

echo "Gradle build"
./gradlew build

echo "Docker compose"
docker-compose build 

echo "Docker up"
docker-compose up -d

docker-compose logs -f


echo "Ready"