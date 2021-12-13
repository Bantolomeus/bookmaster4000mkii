#!/bin/sh

curl -X POST \
  http://localhost:8080/books \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Harry Potter",
    "author": "JKR",
    "pagesTotal": 999,
    "currentPage": 0,
    "dateStarted": "",
    "readTime": 0
}'
