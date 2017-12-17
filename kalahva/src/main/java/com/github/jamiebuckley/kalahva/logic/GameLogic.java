package com.github.jamiebuckley.kalahva.logic;

import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.models.Pit;
import com.github.jamiebuckley.kalahva.util.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GameLogic {

  public static boolean PLAYER_ONE = false;
  public static boolean PLAYER_TWO = true;

  public static int PLAYER_ONE_STORE = 6;
  public static int PLAYER_TWO_STORE = 13;
  public static int NUMBER_OF_PITS = 14;
  public static int NUMBER_OF_SEEDS = 6;

  public class GameException extends Exception {
    public GameException(String message) {
      super(message);
    }
  }

  public class IllegalGameMoveException extends GameException {
    public IllegalGameMoveException(String message) {
      super(message);
    }
  }

  /**
   * Checks if either players have no seeds in their stores
   */
  private boolean hasGameFinished(Game game) {
    Function<Boolean, Integer> pitsForPlayer = (player) -> game.getPits()
        .stream()
        .filter(p -> p.getOwner() == player)
        .filter(p -> p.getType() == Pit.Type.HOUSE)
        .map(Pit::getSeeds)
        .mapToInt(Integer::intValue).sum();
    return pitsForPlayer.apply(true) == 0 || pitsForPlayer.apply(false) == 0;
  }

  public void initialize(Game game) {
    List<Pit> pits = new ArrayList<>();
    for (int i = 0; i < NUMBER_OF_PITS; i++) {
      Pit pit = new Pit();

      boolean owner = (i >= PLAYER_ONE_STORE);
      pit.setOwner(owner);

      if (i == PLAYER_ONE_STORE || i == PLAYER_TWO_STORE) {
        pit.setType(Pit.Type.STORE);
      } else {
        pit.setSeeds(NUMBER_OF_SEEDS);
      }
      pits.add(pit);
    }
    game.setPits(pits);
    game.setState(Game.State.PLAYING);
  }

  public void updateGameState(Game game) {
    if (game.getState() == Game.State.FINISHED) {
      //don't update finished games
      return;
    }

    //got both players?
    if (game.getPlayerOne() != null && game.getPlayerTwo() != null) {
      //if waiting - start playing
      if (game.getState() == Game.State.WAITING) {
        initialize(game);
      }

      if (hasGameFinished(game)) {
        game.setState(Game.State.FINISHED);
        int playerOneSeeds = game.getPits().get(PLAYER_ONE_STORE).getSeeds();
        int playerTwoSeeds = game.getPits().get(PLAYER_ONE_STORE).getSeeds();
        if (playerOneSeeds == playerTwoSeeds) {
          game.setWinner(Game.Winner.DRAW);
        } else {
          game.setWinner(playerOneSeeds > playerTwoSeeds ? Game.Winner.ONE : Game.Winner.TWO);
        }
      }
    }
    else {
      //wait for a player
      game.setState(Game.State.WAITING);
    }
  }

  private void checkLegalMove(Game game, Long playerId, int index) throws GameException {
    boolean playerIndex = getPlayerIndex(game, playerId);
    if (game.getPlayerTurn() != playerIndex) {
      throw new IllegalGameMoveException("A player cannot move outside their turn");
    }
    if (index == PLAYER_ONE_STORE || index == PLAYER_TWO_STORE) {
      throw new IllegalGameMoveException("A player cannot select a store");
    }
    if (playerIndex == PLAYER_ONE && index > PLAYER_ONE_STORE || playerIndex == PLAYER_TWO && index < PLAYER_ONE_STORE) {
      throw new IllegalGameMoveException("A player cannot select an opponents house");
    }
    if (game.getPits().size() < index || index < 0) {
      throw new IllegalGameMoveException("Cannot select an index out of range of the game board");
    }
    if (game.getPits().get(index).getType() == Pit.Type.STORE) {
      throw new IllegalGameMoveException("Cannot play from a store");
    }
    if (game.getPits().get(index).getSeeds() == 0) {
      throw new IllegalGameMoveException("Must select a house with seeds");
    }
  }

  private void applyPlay(Game game, Long playerId, int index) {
    boolean playerIndex = getPlayerIndex(game, playerId);

    Pit selectedPit = game.getPits().get(index);

    //pick up the seeds
    int selectedSeeds = selectedPit.getSeeds();
    selectedPit.setSeeds(0);

    int seedsInHand = selectedSeeds;
    int currentPit = index + 1;
    while (seedsInHand > 0) {
      //go round the board
      if (currentPit > PLAYER_TWO_STORE) {
        currentPit = 0;
      }
      selectedPit = game.getPits().get(currentPit);

      //no seeds on opponent's store
      if (playerIndex == PLAYER_ONE && index == PLAYER_TWO_STORE || playerIndex == PLAYER_TWO && index == PLAYER_ONE_STORE) {
        continue;
      }
      selectedPit.setSeeds(selectedPit.getSeeds() + 1);

      //remove one seed and move to the next pit
      seedsInHand--;
      if (seedsInHand > 0) {
        currentPit++;
      }
    }

    //Take opponents seed if pit was empty, belongs to player, and wasn't a store
    if (selectedPit.getSeeds() == 1
        && selectedPit.getType() != Pit.Type.STORE
        && selectedPit.getOwner() == playerIndex) {
      int oppositePitIndex = getOppositePitIndex(currentPit);
      Pit oppositePit = game.getPits().get(oppositePitIndex);
      if (oppositePit.getType() != Pit.Type.STORE) {
        int oppositePitSeeds = oppositePit.getSeeds();
        oppositePit.setSeeds(0);
        int winningStoreIndex = (playerIndex) ?  PLAYER_TWO_STORE : PLAYER_ONE_STORE;
        Pit winnersPit = game.getPits().get(winningStoreIndex);
        winnersPit.setSeeds(winnersPit.getSeeds() + oppositePitSeeds);
      }
    }

    //Free turn if last seed was on store
    if (selectedPit.getType() != Pit.Type.STORE) {
      game.setPlayerTurn(!game.getPlayerTurn());
    }
  }

  private int getOppositePitIndex(int currentPit) {
    return PLAYER_ONE_STORE - (currentPit - PLAYER_ONE_STORE);
  }

  public Game makePlay(Game game, Long playerId, int index) throws GameException {
    ObjectUtils.notNullOrThrow(game, "Game must not be null");
    ObjectUtils.notNullOrThrow(playerId, "PlayerId must not be null");
    checkLegalMove(game, playerId, index);
    applyPlay(game, playerId, index);
    return game;
  }

  /**
   * Returns the index of the player in the current game
   * @param game The game to test the player index of
   * @param playerId The player ID to test
   * @return false (0) for playerOne, true (1) for playerTwo
   */
  private boolean getPlayerIndex(Game game, Long playerId) {
    return !game.getPlayerOne().equals(playerId);
  }
}
