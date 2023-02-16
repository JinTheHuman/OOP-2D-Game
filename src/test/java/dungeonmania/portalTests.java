package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getPlayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class portalTests {
    @Test
    @DisplayName("Basic portal test")
    public void testBasicPortal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("portalTest", "c_spiderTest_basicMovement");

        EntityResponse player = getPlayer(initDungonRes).get();

        initDungonRes = dmc.tick(Direction.RIGHT);
        player = getPlayer(initDungonRes).get();
        Position newPos = player.getPosition();
        assertEquals(newPos, new Position(51, 40));
    }

    @Test
    @DisplayName("Teleport to portal surrounded by walls")
    public void testPortalWalls() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("portalTest", "c_spiderTest_basicMovement");

        EntityResponse player = getPlayer(initDungonRes).get();

        initDungonRes = dmc.tick(Direction.DOWN);
        player = getPlayer(initDungonRes).get();
        Position newPos = player.getPosition();
        assertEquals(newPos, new Position(1, 2));
    }
}
