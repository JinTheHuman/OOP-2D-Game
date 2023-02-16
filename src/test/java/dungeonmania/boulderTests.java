package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class boulderTests {
    @Test
    public void testCanStepOnWall() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("boulders_testDungeon",
                "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // move downward
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(3, 2),
                false);
        EntityResponse expectedBoulder = new EntityResponse("Entity4", "boulder", new Position(4, 2), false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        EntityResponse actualBoulder = getEntities(actualDungonRes, "boulder").get(0);
        EntityResponse actualBoulderRes = new EntityResponse(String.valueOf(actualBoulder.getId()),
                actualBoulder.getType(),
                actualBoulder.getPosition(), false);

        assertEquals(expectedBoulder, actualBoulderRes);
        assertEquals(expectedPlayer, actualPlayer);

        actualDungonRes = dmc.tick(Direction.RIGHT);

        assertEquals(expectedBoulder, actualBoulderRes);
        assertEquals(expectedPlayer, actualPlayer);
    }
}
