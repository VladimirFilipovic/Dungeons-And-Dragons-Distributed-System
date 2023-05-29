# Dungeons-And-Dragons-Distributed-System
A dungeons and dragons microservice-based api 

Hopefuly it will make our beloved DM's job easier


TODO: 
    -spell service fix persistance tests done boy
    - create item method done 
    - stats service stats same as the items done
        -meaning that we will create stats on our side without using dnd5e api

    -composite service adjustments: -currently working on it
        -add delete methods - check
        -update methods so that compiler doesnt scream at us
            -character service [done] 
            -items service
            -inventory service
            -spells service
            -character spells service
            -stats service
        -swagger documentation update
        -integration service updates with create and delete methods
            



Running database tests: 
    ./gradlew {service-name}-service:test --tests {test-name}

Example: 
    ./gradlew stats-service:test --tests PersistenceTests