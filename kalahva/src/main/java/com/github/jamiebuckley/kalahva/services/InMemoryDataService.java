package com.github.jamiebuckley.kalahva.services;

import com.github.jamiebuckley.kalahva.models.Game;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Effectively an in-memory data store of all active games
 * Could be swapped out for a proper persistence layer
 */
@Service
public class InMemoryDataService implements DataService {

  private AtomicLong gameIdCounter = new AtomicLong();

  private AtomicLong playerIdCounter = new AtomicLong();

  private List<Game> games;

  public InMemoryDataService() {
    this.games = new ArrayList<>();
  }

  @Override
  public Long getNextPlayerId() {
    return playerIdCounter.getAndIncrement();
  }

  @Override
  public Optional<Game> getGame(Long id) {
    return this.games.stream().filter(g -> g.getId().equals(id)).findFirst();
  }

  @Override
  public Game createGame(Long playerOneId) {
    if (playerOneId == null) {
      throw new IllegalArgumentException("Parameter: PlayerOneID must not be null");
    }

    Game game = new Game();
    game.setId(gameIdCounter.getAndIncrement());
    game.setCreated(LocalDateTime.now());
    game.setUpdated(LocalDateTime.now());
    game.setPlayerOne(playerOneId.toString());
    this.games.add(game);
    return game;
  }

  @Override
  public Game updateGame(Game game) throws GameNotFoundException {
    Game existingGame = getGame(game.getId())
        .orElseThrow(() -> new GameNotFoundException("Game with ID: " + game.getId() + " was not found"));

    existingGame.setUpdated(LocalDateTime.now());
    existingGame.setPits(game.getPits());
    existingGame.setPlayerOne(game.getPlayerOne());
    existingGame.setPlayerTwo(game.getPlayerTwo());
    existingGame.setPlayerTurn(game.getPlayerTurn());
    existingGame.setState(game.getState());
    return existingGame;
  }

  @Override
  public List<Game> getGames() {
    return this.games;
  }

  @Override
  public void deleteGame(Long id) throws GameNotFoundException {
    Game existingGame = getGame(id)
        .orElseThrow(() -> new GameNotFoundException("Game with ID: " + id + " was not found"));
    games.remove(existingGame);
  }
}
