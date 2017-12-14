package com.github.jamiebuckley.kalahva.util;

import com.github.jamiebuckley.kalahva.models.Game;

import java.util.Arrays;
import java.util.List;

/**
 * Collection of small game helpers
 */
public class GameUtils {

  public static List<Long> getPlayers(Game game) {
    return Arrays.asList(game.getPlayerOne(), game.getPlayerTwo());
  }
}
