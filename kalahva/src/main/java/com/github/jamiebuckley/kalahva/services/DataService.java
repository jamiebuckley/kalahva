package com.github.jamiebuckley.kalahva.services;

import com.github.jamiebuckley.kalahva.models.Game;

import java.util.List;
import java.util.Optional;

public interface DataService {
  class GameNotFoundException extends Exception {
    public GameNotFoundException(String message) {
      super(message);
    }
  }

  Long getNextPlayerId();

  Optional<Game> getGame(Long id);

  Game createGame(Long playerOneId);

  Game updateGame(Game game) throws GameNotFoundException;

  List<Game> getGames();

  void deleteGame(Long id) throws GameNotFoundException;
}
