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
curl -XPUT http://localhost:8080/game/:gameId/playerTwo/:yourId
```
