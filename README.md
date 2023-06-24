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
            -items service [done]
            -inventory service [done]
            -spells service [done]
            -character spells service [done]
            -stats service [done]
        -swagger documentation update
        -integration service updates with create and delete methods
                


Starting containers can be done with following script: start-em-all.bash

In order to run e2e tests you need to have docker installed and running on your machine
First step is to run all container with the following script: ./start-em-all.bash
Then you can run the tests with the following script: e2e-tests.bash