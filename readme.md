API

*1. Get a player ID*
```
curl http://localhost:8080/player
```

*2. Get all games*
```
curl http://localhost:8080/game
```

*3. Create a game*
```
curl -XPOST http://localhost:8080/game?playerId=:yourId
```

*4. Join a game*
```
curl -XPUT http://localhost:8080/game/:gameId/:playerIndex/:yourId
```

*5. Make a play*
```
curl -XPOST http://localhost:8080/game/:gameId/:playerId/play/0
```

*Example Sequence*
```
curl http://localhost:8080/player
#0
curl http://localhost:8080/player
#1
curl -XPOST http://localhost:8080/game?playerId=0 | jq
curl -XPOST http://localhost:8080/game?playerId=0 | jq
curl -XPUT http://localhost:8080/game/0/player/1/1 | jq
curl -XPOST http://localhost:8080/game/0/0/play/0 | jq


```
