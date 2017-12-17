package com.github.jamiebuckley.kalahva.logic;

import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.models.Pit;
import org.junit.Test;

import java.util.stream.Collectors;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameLogicTest {

    @Test
    public void testInitialize() {
       GameLogic gameLogic = new GameLogic();
       Game game = new Game();
       gameLogic.initialize(game);
       assertNotNull(game.getPits());
       assertEquals(14, game.getPits().size());

       long numberOfHouses = game.getPits().stream().filter(p -> p.getType() == Pit.Type.HOUSE).count();
       assertEquals(12, numberOfHouses);

       assertEquals(Pit.Type.STORE, game.getPits().get(GameLogic.PLAYER_ONE_STORE).getType());
       assertEquals(Pit.Type.STORE, game.getPits().get(GameLogic.PLAYER_TWO_STORE).getType());

       assertTrue(game.getPits()
               .stream().filter(p -> p.getType().equals(Pit.Type.HOUSE))
               .allMatch(p -> p.getSeeds() == 6));
    }

    @Test
    public void testMakePlay() throws GameLogic.GameException {
        GameLogic gameLogic = new GameLogic();
        Game game = new Game();
        game.setPlayerOne(0L);
        game.setPlayerTwo(1L);
        gameLogic.initialize(game);

        Game gameAfterPlay = gameLogic.makePlay(game, 0L, 0);
        assertEquals(0, game.getPits().get(0).getSeeds());

        //The seeds have been spread over the next five pits
        boolean allHaveSeven = game.getPits().subList(1, 6).stream()
                .allMatch(p -> p.getSeeds() == 7);
        assertTrue(allHaveSeven);

        //one in the store
        assertEquals(1, game.getPits().get(GameLogic.PLAYER_ONE_STORE).getSeeds());

        //still player one's turn (due to store ending)
        assertFalse(game.getPlayerTurn());
    }

    @Test
    public void testCapture() throws GameLogic.GameException {
        GameLogic gameLogic = new GameLogic();
        Game game = new Game();
        game.setPlayerOne(0L);
        game.setPlayerTwo(1L);

        gameLogic.initialize(game);

        game.getPits().get(8).setSeeds(0);
        game.getPits().get(7).setSeeds(1);
        game.setPlayerTurn(true);

        Game gameAfterPlayTwo = gameLogic.makePlay(game, 1L, 7);
        assertEquals(1, 1);
        assertEquals(0, game.getPits().get(4).getSeeds());
        assertEquals(6, game.getPits().get(GameLogic.PLAYER_TWO_STORE).getSeeds());
    }
}
