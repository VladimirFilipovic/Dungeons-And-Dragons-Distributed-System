#!/usr/bin/env bash
#
# ./gradlew clean build
# docker-compose build
# docker-compose up -d
#
# Sample usage:
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
: ${HOST=localhost}
: ${PORT=8080}

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
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

function testUrl() {
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
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

function recreateComposite() {
    local characterId=$1
    local composite=$2

    assertCurl 200 "curl -X DELETE http://$HOST:$PORT/characters/${characterId} -s"
    curl -X POST http://$HOST:$PORT/characters -H "Content-Type: application/json" --data "$composite"
}

function setupTestdata() {

    body='{"id":"1","name":"character 1","race":"race 1","religion":"religion 1","serviceAddress":"address 1","items":[],"spells":[],"stats":[]}'
    recreateComposite "1" "$body"

    body='{"id":"113","name":"character 2","race":"race 113","religion":"religion 113","serviceAddress":"address 113","items":[{"itemId":1,"quantity":1}],"spells":[{"spellId":1,"level":1}],"stats":[{"statId":1,"value":1}]}'
    recreateComposite "113" "$body"
}

set -e

echo "Start:" `date`

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

waitForService curl -X DELETE http://$HOST:$PORT/characters/13

setupTestdata

# Verify that a normal request works, expect empty items, spells, and stats
assertCurl 200 "curl http://$HOST:$PORT/characters/1 -s"
assertEqual "1" "$(echo $RESPONSE | ./jq.exe -r .id)"
assertEqual 0 "$(echo $RESPONSE | ./jq.exe ".items | length")"
assertEqual 0 "$(echo $RESPONSE | ./jq.exe ".spells | length")"
assertEqual 0 "$(echo $RESPONSE | ./jq.exe ".stats | length")"

assertCurl 200 "curl http://$HOST:$PORT/product-composite/2 -s"
assertEqual 1 $(echo $RESPONSE | jq .productId)
assertEqual 1 $(echo $RESPONSE | jq ".items | length")
assertEqual 1 $(echo $RESPONSE | jq ".spells | length")
assertEqual 1 $(echo $RESPONSE | jq ".stats | length")

# Verify that a 404 (Not Found) error is returned for a non-existing characterId (13)
assertCurl 404 "curl http://$HOST:$PORT/characters/13 -s"



# Verify that a 422 (Unprocessable Entity) error is returned for a characterId that is out of range (-1)
assertCurl 422 "curl http://$HOST:$PORT/characters/-1 -s"
assertEqual "\"Invalid characterId: -1\"" "$(echo $RESPONSE | ./jq.exe .message)"

# Verify that a 400 (Bad Request) error error is returned for a characterId that is not a number, i.e. invalid format
assertCurl 400 "curl http://$HOST:$PORT/characters/invalidCharacterId -s"
assertEqual "\"Type mismatch.\"" "$(echo $RESPONSE | ./jq.exe .message)"

if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi

echo "End:" `date`
