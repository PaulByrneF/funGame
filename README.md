# funGame
This is a funGame


To start, please run the spring boot project on a server to support the API calls.

#API Specifications

POST createGame
Endpoint: http://localhost:8080/game/start
Accepts: Application_JSON

{
    "login": "user1"
}



Endpoint: http://localhost:8080/game/join
POST joinGame
Accepts: Application_JSON

{
    "player": {
        "login": "user2"
    },
    "gameId": "7401b696-1a6d-4a07-9823-c1f0e3d21fa3"
}




EndPoint: http://localhost:8080/game/join/random
POST joinRandom
Accepts: Application_JSON
{
    "login": "user3"
}

Endpoint: http://localhost:8080/game/move
POST makeMove
Accepts: Application_JSON

{
    "token": "X",
    "coorY": 6,
    "gameId": "8e3fe0b0-a31c-4565-ab66-8a5169068c02"
}



Enpoint: http://localhost:8080/game/{:gameId}
GET getGame
