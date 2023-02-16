package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DijstraTest {
    @Test
    @DisplayName("Test Merc Movement")
    public void testMercMove() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("dijstra_simple_test",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        Position actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        Position expectedPos = new Position(5, 2);

        assertEquals(expectedPos, actualPos);

        dmc = new DungeonManiaController();
        dmc.newGame("dijkstra_assassin",
                "m3_config");
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualPos = getEntities(actualDungonRes, "assassin").get(0).getPosition();

        assertEquals(expectedPos, actualPos);
    }

    @Test
    @DisplayName("Test Merc Movement Portal")
    public void testMercMovePortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("dijstra_portal_test",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.LEFT);
        Position actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        Position expectedPos = new Position(2, 1);

        assertEquals(expectedPos, actualPos);
    }

    @Test
    @DisplayName("Test Merc Movement next to player")
    public void testMercNextToPlayer() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("dijstra_next_to_test",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);

        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);
    }

    @Test
    public void testSwampTileDijkstra() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("dijkstra_swamptile",
                "dijkstra_swamptile");
        DungeonResponse actualDungonRes = dmc.tick(Direction.UP);
        Position expectedPosition = new Position(2, 5);
        Position actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);
    }

    @Test
    public void testGoesThroughSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("test_swamp_good",
                "dijkstra_swamptile");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        Position expectedPosition = new Position(0, 5);
        Position actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);
        
        actualDungonRes = dmc.tick(Direction.DOWN);
        actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);

        actualDungonRes = dmc.tick(Direction.DOWN);
        actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);

        actualDungonRes = dmc.tick(Direction.DOWN);
        actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);

        actualDungonRes = dmc.tick(Direction.DOWN);
        expectedPosition = new Position(0, 4);
        actualPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);

    }
}
