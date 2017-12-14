package com.github.jamiebuckley.kalahva.controllers;

import com.github.jamiebuckley.kalahva.logic.GameLogic;
import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class GameController {

  private final DataService dataService;

  private final GameLogic gameLogic;


  /**
   * Simple class to store error responses
   */
  public static class GameErrorResponse {

    private String error;

    public GameErrorResponse(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }

    public void setError(String error) {
      this.error = error;
    }
  }

  @Autowired
  public GameController(DataService dataService, GameLogic gameLogic) {
    this.dataService = dataService;
    this.gameLogic = gameLogic;
  }

  @RequestMapping("/player")
  public Long getPlayerId() {
    return dataService.getNextPlayerId();
  }

  @RequestMapping("/game")
  public List<Game> getGames() {
    return dataService.getGames();
  }

  @RequestMapping(path = "/game", method = RequestMethod.POST)
  public @ResponseBody Game createGame(@RequestParam("playerId") Long playerId) {
    return dataService.createGame(playerId);
  }

  @RequestMapping(path = "/game/{gameId}/player/{playerIndex}/{playerId}", method = RequestMethod.PUT)
  public ResponseEntity<?> joinGame(@PathVariable Long gameId, @PathVariable int playerIndex, @PathVariable Long playerId) {
    Optional<Game> maybeGame = dataService.getGame(gameId);
    if (!maybeGame.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Game game = maybeGame.get();
    if (game.getPlayerTwo() != null) {
      return new ResponseEntity<>("Cannot join a game with two players",
          HttpStatus.UNPROCESSABLE_ENTITY);
    }
    if (playerIndex == 0) {
      game.setPlayerOne(playerId);
    } else if (playerIndex == 1) {
      game.setPlayerTwo(playerId);
    } else {
      return new ResponseEntity<>("Unknown player index",
          HttpStatus.UNPROCESSABLE_ENTITY);
    }
    gameLogic.updateGameState(game);
    return dataService.updateGame(game)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping("/game/{gameId}")
  public ResponseEntity<?> getGame(@PathVariable Long gameId) {
    return dataService.getGame(gameId)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @RequestMapping("/game/{gameId}/state")
  public ResponseEntity<?> getGameState(@PathVariable Long gameId) {
    Optional<ResponseEntity<Game.State>> response =
        dataService.getGame(gameId).map(Game::getState)
            .map(ResponseEntity::ok);
    if (response.isPresent()) {
      return response.get();
    }
    return error("the game was not found", HttpStatus.NOT_FOUND);
  }

  @RequestMapping(path = "/game/{gameId}/{playerId}/play/{index}", method = RequestMethod.POST)
  public ResponseEntity<?> makePlay(@PathVariable Long gameId, @PathVariable Long playerId, @PathVariable int index) {
    return dataService.getGame(gameId)
        .map(g -> {
          try {
            Game gameAfterPlay = gameLogic.makePlay(g, playerId, index);
            dataService.updateGame(gameAfterPlay);
            return ResponseEntity.ok(gameAfterPlay);
          } catch (GameLogic.GameException e) {
            return error(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
        })
        .orElse(error("The game was not found", HttpStatus.NOT_FOUND));
  }


  private ResponseEntity<GameErrorResponse> error(String message, HttpStatus httpStatus) {
    return new ResponseEntity<>(new GameErrorResponse(message), httpStatus);
  }
}
