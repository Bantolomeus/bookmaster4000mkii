#!/bin/sh

curl -X POST \
  http://localhost:8080/challenge \
  -H 'Content-Type: application/json' \
  -d '{
    "pagesPerDay": 15,
    "dateStarted": "21/05/2018"
}'
