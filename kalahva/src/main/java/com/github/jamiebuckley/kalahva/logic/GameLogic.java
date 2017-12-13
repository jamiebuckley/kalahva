package com.github.jamiebuckley.kalahva.logic;

import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.models.Pit;
import com.github.jamiebuckley.kalahva.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class GameLogic {

  private static int PLAYER_ONE = 0;
  private static int PLAYER_TWO = 1;

  private static int PLAYER_ONE_STORE = 6;
  private static int PLAYER_TWO_STORE = 13;
  private static int NUMBER_OF_PITS = 14;
  private static int NUMBER_OF_SEEDS = 6;

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

  public void initialize(Game game, String playerOneId, String playerTwoId) {
    List<Pit> pits = new ArrayList<>();
    for (int i = 0; i < NUMBER_OF_PITS; i++) {
      Pit pit = new Pit();
      if (i == PLAYER_ONE_STORE || i == PLAYER_TWO_STORE) {
        pit.setType(Pit.Type.STORE);
      }

      String owner = (i <= PLAYER_ONE_STORE) ? playerOneId : playerTwoId;
      pit.setOwner(owner);
      pit.setSeeds(NUMBER_OF_SEEDS);
    }
    game.setPits(pits);
    game.setState(Game.State.PLAYING);
  }

  private void checkLegalMove(Game game, Long playerId, int index) throws GameException {
    int playerIndex = getPlayerIndex(game, playerId);
    if (index == PLAYER_ONE_STORE || index == PLAYER_TWO_STORE) {
      throw new IllegalGameMoveException("A player cannot select a store");
    }
    if (playerIndex == PLAYER_ONE && index > PLAYER_ONE_STORE || playerIndex == PLAYER_TWO && index < PLAYER_ONE_STORE) {
      throw new IllegalGameMoveException("A player cannot select an opponents house");
    }
    if (game.getPits().size() < index || index < 0) {
      throw new IllegalGameMoveException("Cannot select an index out of range of the game board");
    }
    if (game.getPits().get(index).getSeeds() == 0) {
      throw new IllegalGameMoveException("Must select a house with seeds");
    }
  }

  private void applyPlay(Game game, Long playerId, int index) {
    int playerIndex = getPlayerIndex(game, playerId);

    Pit selectedPit = game.getPits().get(index);

    //pick up the seeds
    int selectedSeeds = selectedPit.getSeeds();
    selectedPit.setSeeds(0);

    int seedsInHand = selectedSeeds;
    int currentPit = index;
    while (seedsInHand > 0) {
      selectedPit = game.getPits().get(currentPit);

      //no seeds on opponent's store
      if (playerIndex == PLAYER_ONE && index == PLAYER_TWO_STORE || playerIndex == PLAYER_TWO && index == PLAYER_ONE_STORE) {
        continue;
      }
      selectedPit.setSeeds(selectedPit.getSeeds() + 1);

      //remove one seed and move to the next pit
      seedsInHand--;
      currentPit++;
    }
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
   * @return The index of the player: 0 or 1
   */
  private int getPlayerIndex(Game game, Long playerId) {
    return asList(game.getPlayerOne(), game.getPlayerTwo()).indexOf(playerId);
  }
}
