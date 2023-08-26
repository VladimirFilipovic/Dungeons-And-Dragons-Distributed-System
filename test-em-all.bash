#!/usr/bin/env bash
#
# ./grdelw clean build
# docker-compose build
# docker-compose up -d
#
# Sample usage:
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
: ${HOST=localhost}
: ${PORT=8443}
: ${CHAR_ID=0}

function assertCurl() {

    local expectedHttpCode=$1
    local curlCmd="$2 -w \"%{http_code}\""
    local result=$(eval $curlCmd)
    local httpCode="${result:(-3)}"
    RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

    if [ "$httpCode" = "$expectedHttpCode" ]
    then
        if [ "$httpCode" = "200" ]
        then
            echo "Test OK (HTTP Code: $httpCode)"
        else
            echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
        fi
        return 0
    else
        echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
        echo  "- Failing command: $curlCmd"
        echo  "- Response Body: $RESPONSE"
        return 1
    fi
}

function assertEqual() {

    local expected=$1
    local actual=$2

    if [ "$actual" = "$expected" ]
    then
        echo "Test OK (actual value: $actual)"
        return 0
    else
        echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
        return 1
    fi
}

function testUrl() {
    url=$@
    if $url -ks -f -o /dev/null
    then
          return 0
    else
          return 1
    fi;
}

function waitForService() {
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

function testCompositeCreated() {
    echo "Testing composite creation..."

    #create item
    itemBody='{
    "name": "Magic-Wands-lvl-12",
    "description": "A powerful wand lvl 12"
    }'
    item=$(curl $AUTH -X  POST -k https://$HOST:$PORT/items  -H "Content-Type: application/json"  -H "Authorization: Bearer $ACCESS_TOKEN" --data "$itemBody")

    itemId=$(echo "$item" | jq -r .id)
    itemName=$(echo "$item" | jq -r .name)

    echo "Item: $item"
    echo "Item ID: $itemId"

    body='{
    "name": "John Doe",
    "race": "Human",
    "religion": "None",
    "serviceAddress": "123 Main St",
    "items": [
        {
        "id": '$itemId',
        "amount": 1
        }
    ],
    "spells": [
        {
        "spellName": "acid-arrow",
        "spellLevel": 3
        }
    ],
    "stats": [
        {
        "name": "HP",
        "value": 10
        }
    ]
    }'

    #create character   
    character=$(curl  -X  POST -k https://$HOST:$PORT/characters/ -H "Content-Type: application/json" -H "Authorization: Bearer $ACCESS_TOKEN" --data "$body")

    echo "Character: $character"

    characterId=$(echo $character | jq -r '.character.id')

    if ! assertCurl 200 "curl -k  https://$HOST:$PORT/characters/$characterId $AUTH -s"
    then
        echo -n "FAIL"
        return 1
    fi

    echo response: $RESPONSE

    echo $RESPONSE | jq -r '.name'

    set +e
    assertEqual "John Doe" "$(echo $RESPONSE | jq -r '.name')"
    if [ "$?" -eq "1" ] ; then return 1; fi

    assertEqual 1  $(echo $RESPONSE | jq ".items | length")
    if [ "$?" -eq "1" ] ; then return 1; fi

    assertEqual 1 $(echo $RESPONSE | jq ".spells | length")
    if [ "$?" -eq "1" ] ; then return 1; fi

    
    assertEqual 1 $(echo $RESPONSE | jq ".stats | length")
    if [ "$?" -eq "1" ] ; then return 1; fi


    #delete character and item
    assertCurl 200 "curl $AUTH -X DELETE -k https://$HOST:$PORT/characters/$characterId -s"
    assertCurl 200 "curl $AUTH -X DELETE -k https://$HOST:$PORT/items/$itemName -s"

    set -e
}

function waitForMessageProcessing() {
    echo "Wait for messages to be processed... "

    # Give background processing some time to complete...
    sleep 1

    ACCESS_TOKEN=$(curl -k https://writer:secret@$HOST:$PORT/oauth/token -d grant_type=password -d username=vlada -d password=password -s | jq .access_token -r)
    AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\""

    echo "AUTH: $AUTH"
    

    sleep 1

    n=0
    until testCompositeCreated
    do
        n=$((n + 1))
        if [[ $n == 40 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
    echo "All messages are now processed!"
}

set -e

echo "Start Tests:" `date`

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down --remove-orphans"
    docker-compose down --remove-orphans
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

waitForService curl -k https://$HOST:$PORT/actuator/health

ACCESS_TOKEN=$(curl -k https://writer:secret@$HOST:$PORT/oauth/token -d grant_type=password -d username=vlada -d password=password -s | jq .access_token -r)
AUTH="-H \"Authorization: Bearer $ACCESS_TOKEN\"" 

echo "AUTH: $AUTH"

testCompositeCreated

echo "End, all tests OK:" `date`

if [[ $@ == *"stop"* ]]
then
    echo "Stopping the test environment..."
    echo "$ docker-compose down --remove-orphans"
    docker-compose down --remove-orphans
fi