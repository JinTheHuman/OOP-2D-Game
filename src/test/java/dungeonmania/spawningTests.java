package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.Test;

public class spawningTests {
    @Test
    public void testSpidersSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_movementTest_testMovementDown",
                "c_spiderSpawn");
        for (int i = 1; i < 10; i++) {
            res = dmc.tick(Direction.UP);
            assertEquals(getEntities(res, "spider").size(), i);
        }

    }

    @Test
    public void testZombiesSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("zombies",
                "c_zombies");
        for (int i = 1; i < 10; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(getEntities(res, "zombie_toast").size(), i);
        }
    }

    @Test
    public void testZombiesDontSpawnIfNowhereToSpawn() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieNoSpawn",
                "c_zombies");
        for (int i = 1; i < 10; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(getEntities(res, "zombie_toast").size(), 0);
        }
    }

    @Test
    public void testZombieSpawnRight() {
        //          wall    wall   wall
        //  wall  zspawner         wall
        //          wall    wall   wall
        // test it spawns to the right of the zombie spawner
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("walls_around_spawner",
                "c_zombies");
        res = dmc.tick(Direction.LEFT);
        assertEquals(getEntities(res, "zombie_toast").size(), 1);
        assertEquals(new Position(3, 1), getEntities(res, "zombie_toast").get(0).getPosition());
    }

    @Test
    public void testZombieSpawnDown() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("test_wall_not_down",
                "c_zombies");
        res = dmc.tick(Direction.LEFT);
        assertEquals(getEntities(res, "zombie_toast").size(), 1);
        assertEquals(new Position(2, 2), getEntities(res, "zombie_toast").get(0).getPosition());
    }

    @Test
    public void testZombieSpawnLeft() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("test_wall_not_left",
                "c_zombies");
        res = dmc.tick(Direction.LEFT);
        assertEquals(getEntities(res, "zombie_toast").size(), 1);
        assertEquals(new Position(1, 1), getEntities(res, "zombie_toast").get(0).getPosition());
    }

    @Test
    public void test2ZombieSpawners() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("2ZombieSpawners",
                "c_zombies");
        for (int i = 1; i < 10; i++) {
            res = dmc.tick(Direction.LEFT);
            assertEquals(getEntities(res, "zombie_toast").size(), 2 * i);
        }
    }

    @Test
    public void testSpiderSpawnRate5() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("2ZombieSpawners",
                "c_spawnrate5");
        int numSpiders = 0;

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "spider").size(), numSpiders += 1);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "spider").size(), numSpiders += 1);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "spider").size(), numSpiders += 1);

    }

    @Test
    public void testZombieSpawnRate5() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("2ZombieSpawners",
                "c_spawnrate5");
        int numZombies = 0;
 
        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "zombie_toast").size(), numZombies += 2);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "zombie_toast").size(), numZombies += 2);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.LEFT);
        }
        assertEquals(getEntities(res, "zombie_toast").size(), numZombies += 2);

    }
}
