
### Create a Challenge

POST http://localhost:8080/challenge 

Header: 
```
Key: Content-Type 
Value: application/json
```

Payload: 

```
{
    "pagesPerDay": 15,
    "pagesAheadOfPlan": 0,
    "startPagesAheadOfPlan": 0,
    "pagesSinceStart": 0,
    "pagesEverRead": 0,
    "dateStarted": "21/05/2018"
}
```
