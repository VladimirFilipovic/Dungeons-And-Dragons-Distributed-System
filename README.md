# Dungeons-And-Dragons-Distributed-System
A dungeons and dragons microservice-based api 

Hopefuly it will make our beloved DM's job easier


TODO: 
    -spell service fix persistance tests done boy
    - create item method done 
    - stats service stats same as the items: 
        -meaning that we will create stats on our side without using dnd5e api

    -composite service adjustments
    



Running database tests: 
    ./gradlew {service-name}-service:test --tests {test-name}

Example: 
    ./gradlew stats-service:test --tests PersistenceTests