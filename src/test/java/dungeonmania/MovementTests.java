package dungeonmania;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static dungeonmania.TestUtils.getInventory;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovementTests {
    @Test
    public void testRegularMovement() {
        // have player move in all directions
        // confirm position

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_testMovementDown",
                "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        // move downward
        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2),
                false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        // move upward
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        actualDungonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        // move left
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(0, 1), false);

        actualDungonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        // move right
        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 1), false);

        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    public void testCannotMove() {
        // place objects player cannot move through and test it does not move
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_movementTest_blockedMovement",
                "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();

        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(5, 5),
                false);

        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        actualDungonRes = dmc.tick(Direction.UP);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        actualDungonRes = dmc.tick(Direction.LEFT);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    public void testSpiderMovementBoulder() {
        // test that spider will turn around at boulder
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_spiderTest_blockedMovement", "c_spiderTest_basicMovement");
        Position pos = getEntities(res, "spider").get(0).getPosition();

        List<Position> movementTrajectory = new ArrayList<Position>();
        int x = pos.getX();
        int y = pos.getY();
        int nextPositionElement = 0;
        movementTrajectory.add(new Position(x, y - 1)); // 1
        movementTrajectory.add(new Position(x + 1, y - 1)); // 2
        movementTrajectory.add(new Position(x + 1, y)); // 3
        movementTrajectory.add(new Position(x + 1, y + 1)); // 4
        movementTrajectory.add(new Position(x + 1, y)); // 5
        movementTrajectory.add(new Position(x + 1, y - 1)); // 6
        movementTrajectory.add(new Position(x, y - 1)); // 7
        movementTrajectory.add(new Position(x - 1, y - 1)); // 8
        movementTrajectory.add(new Position(x - 1, y)); // 9
        movementTrajectory.add(new Position(x - 1, y + 1)); // 10
        movementTrajectory.add(new Position(x - 1, y)); // 11
        movementTrajectory.add(new Position(x - 1, y - 1)); // 12

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 11; ++i) {
            res = dmc.tick(Direction.UP);
            assertEquals(movementTrajectory.get(nextPositionElement), getEntities(res, "spider").get(0).getPosition());
            nextPositionElement++;
        }
    }

    @Test
    public void testSpiderMovementStaticEntities() {
        // test that spider can move through walls, doors, switches, portals, exits
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testSpiderMovement", "c_spiderTest_basicMovement");
        Position previousPosition = getEntities(res, "spider").get(0).getPosition();

        // Assert Circular Movement of Spider
        for (int i = 0; i <= 8; ++i) {
            res = dmc.tick(Direction.UP);
            Position newPosition = getEntities(res, "spider").get(0).getPosition();
            assertNotEquals(previousPosition, newPosition);
            previousPosition = newPosition;
        }
    }

    @Test
    public void testZombieMovementBlocked() {
        // test that it is also has movement constraints

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieMovementTest_wallBlockingMovementTest",
                "c_spiderTest_basicMovement");

        Position expectedPosition = new Position(5, 5);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(expectedPosition, getEntities(res, "zombie_toast").get(0).getPosition());
        }
    }

    @Test
    public void testHostileMercenaryMovement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("testMercenary",
                "c_spiderTest_basicMovement");

        res = dmc.tick(Direction.LEFT);

        Position expectedPosition = new Position(7, 1);

        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        expectedPosition = new Position(6, 1);

        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        expectedPosition = new Position(5, 1);

        assertEquals(expectedPosition, getEntities(res, "mercenary").get(0).getPosition());
    }

    @Test
    public void testAssassinMovementWhileInvis() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("dijkstra_assassin_potion",
                "m3_config");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(actualDungonRes, "invisibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invisibility_potion").get(0).getId();
        actualDungonRes = dmc.tick(potionId);
        Position actualPos = getEntities(actualDungonRes, "assassin").get(0).getPosition();

        Position expectedPos = new Position(4, 2);
        assertEquals(expectedPos, actualPos);
    }

    @Test
    public void testSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_testSwampTile",
                "c_spiderTest_basicMovement");
        EntityResponse initPlayer = getPlayer(res).get();

        // move onto swamp tile
        res = dmc.tick(Direction.DOWN);

        EntityResponse expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 2),
                false);

        assertEquals(expectedPlayer, getPlayer(res).get());

        // wait 1 tick
        res = dmc.tick(Direction.DOWN);

        assertEquals(expectedPlayer, getPlayer(res).get());

        // wait 2 tick
        res = dmc.tick(Direction.DOWN);

        assertEquals(expectedPlayer, getPlayer(res).get());

        // can get off
        res = dmc.tick(Direction.DOWN);

        expectedPlayer = new EntityResponse(initPlayer.getId(), initPlayer.getType(), new Position(1, 3),
                false);

        assertEquals(expectedPlayer, getPlayer(res).get());
    }

    @Test
    public void testSpideronSwampTile() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_swamptileSpider",
                "c_spiderTest_basicMovement");

        res = dmc.tick(Direction.LEFT);

        Position expectedPosition = new Position(1, 0);

        assertEquals(expectedPosition, getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        assertEquals(expectedPosition, getEntities(res, "spider").get(0).getPosition());

        res = dmc.tick(Direction.LEFT);

        assertEquals(expectedPosition, getEntities(res, "spider").get(0).getPosition());

        expectedPosition = new Position(2, 0);

        res = dmc.tick(Direction.LEFT);

        assertEquals(expectedPosition, getEntities(res, "spider").get(0).getPosition());
    }

    @Test
    public void testHydraMovementBlocked() {
        // test that it is also has movement constraints

        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("test_hydra",
                "m3_config");

        Position expectedPosition = new Position(5, 5);

        for (int i = 0; i < 10; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(expectedPosition, getEntities(res, "hydra").get(0).getPosition());
        }
    }

    @Test 
    public void testAssassinMovementWhileInvincible() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("assassin_invincible",
                "m3_config");
        Position expectedPosition = new Position(1, -3);
        res = dmc.tick(Direction.UP);
        Position actualPos = getEntities(res, "assassin").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);
        String potionId = getInventory(res, "invincibility_potion").get(0).getId();
        res = dmc.tick(potionId);

        expectedPosition = new Position(1, -4);
        actualPos = getEntities(res, "assassin").get(0).getPosition();
        assertEquals(expectedPosition, actualPos);

    }
}
