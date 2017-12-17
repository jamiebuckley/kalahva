package com.github.jamiebuckley.kalahva.logic;

import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.models.Pit;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
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
}
