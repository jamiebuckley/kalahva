package com.github.jamiebuckley.kalahva.controllers;

import com.github.jamiebuckley.kalahva.logic.GameLogic;
import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GameController {

  private final DataService dataService;
  private final GameLogic gameLogic;

  @Autowired
  public GameController(DataService dataService, GameLogic gameLogic) {
    this.dataService = dataService;
    this.gameLogic = gameLogic;
  }

  @RequestMapping("/game")
  public List<Game> getGames() {
    return dataService.getGames();
  }

  @RequestMapping(path = "/game", method = RequestMethod.POST)
  public @ResponseBody Game createGame(@RequestParam("playerId") Long playerId) {
    return dataService.createGame(playerId);
  }

  @RequestMapping(path = "/game/{gameId}/player/{playerId}", method = RequestMethod.PUT)
  public ResponseEntity<?> joinGame(@PathVariable Long gameId, @PathVariable Long playerId) {
    return dataService.getGame(gameId)
        .map(g -> {
          if (g.getPlayerTwo() != null) {
            return new ResponseEntity<>("Cannot join a game with two players",
                HttpStatus.UNPROCESSABLE_ENTITY);
          }
          g.setPlayerTwo(playerId);
          return ResponseEntity.ok(g);
        }).orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping("/game/{gameId}")
  public ResponseEntity<?> getGame(@PathVariable Long gameId) {
    return dataService.getGame(gameId)
        .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping("/game/{gameId}/state")
  public ResponseEntity<?> getGameState(@PathVariable Long gameId) {
    return dataService.getGame(gameId)
        .map(g -> ResponseEntity.ok(g.getState()))
        .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping(path = "/game/{gameId}/{playerId}/play/{index}", method = RequestMethod.POST)
  public ResponseEntity<?> getGameState(@PathVariable Long gameId, @PathVariable Long playerId, @PathVariable int index) {
    return dataService.getGame(gameId)
        .map(g -> {
          try {
            return ResponseEntity.ok(gameLogic.makePlay(g, playerId, index));
          } catch (GameLogic.GameException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
        })
        .orElse(ResponseEntity.notFound().build());
  }
}
