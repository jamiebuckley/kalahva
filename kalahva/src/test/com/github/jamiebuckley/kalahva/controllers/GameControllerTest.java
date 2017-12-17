package com.github.jamiebuckley.kalahva.controllers;

import com.github.jamiebuckley.kalahva.logic.GameLogic;
import com.github.jamiebuckley.kalahva.models.Game;
import com.github.jamiebuckley.kalahva.services.DataService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameControllerTest {

    private DataService testDataService;

    private GameLogic testGameLogic;

    @Before
    public void setup() {
        testDataService = mock(DataService.class);
        testGameLogic = mock(GameLogic.class);
    }

    @Test
    public void testGetNextPlayerId() {
        when(testDataService.getNextPlayerId()).thenReturn(2L);
        GameController gameController = new GameController(testDataService, testGameLogic);
        Long playerId = gameController.getPlayerId();
        assertEquals(new Long(2L), playerId);
    }

    @Test
    public void testJoinGame_404s_whenNoGameFound() {
        when(testDataService.getGame(anyLong())).thenReturn(Optional.empty());
        GameController gameController = new GameController(testDataService, testGameLogic);
        ResponseEntity<?> responseEntity = gameController.joinGame(1L, 0, 0L);
        assertEquals(404, responseEntity.getStatusCode().value());
    }

    //todo: Bunch of similar tests that test the 'thin' controller
}
