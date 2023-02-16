package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.Boulder;
import dungeonmania.Entities.StaticEntities.Door;
import dungeonmania.Entities.StaticEntities.Exit;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.Entities.StaticEntities.Wall;
import dungeonmania.Entities.StaticEntities.ZombieToastSpawner;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class canStepOnUnitTests {
    @Test
    public void testCanStepOnWall() {
        Wall w = new Wall(new Position(1, 1), 1);
        Player p = new Player(new Position(2, 1), 1, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        assertEquals(w.canStepOn(m, p), false);
    }

    @Test
    public void testCanStepOnBoulder() {

        Boulder b = new Boulder(new Position(1, 1), 1);
        Player p = new Player(new Position(0, 1), 1, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        p.setDirection(Direction.RIGHT);

        m.addEntity(p);
        m.addEntity(b);

        assertEquals(b.canStepOn(m, p), true);

        Boulder b2 = new Boulder(new Position(3, 1), 1);
        m.addEntity(b2);

        assertEquals(b.canStepOn(m, p), false);
    }

    @Test
    public void testCanStepOnFloorSwitch() {
        FloorSwitch f = new FloorSwitch(new Position(1, 1), 1);
        Player p = new Player(new Position(2, 1), 1, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        assertEquals(f.canStepOn(m, p), true);
    }

    @Test
    public void testCanStepOnExit() {
        Exit exit = new Exit(new Position(1, 1), 1);
        Player p = new Player(new Position(2, 1), 1, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        assertEquals(exit.canStepOn(m, p), true);
    }

    @Test
    public void testCanStepOnZombieToastSpawner() {
        ZombieToastSpawner z = new ZombieToastSpawner(new Position(1, 1), 1);
        Player p = new Player(new Position(2, 1), 1, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        assertEquals(z.canStepOn(m, p), false);
    }

    @Test
    public void testCanStepOnDoor() {
        Key k = new Key(new Position(1, 1), 0, 1);
        Door d = new Door(new Position(1, 1), 1, 1);
        Player p = new Player(new Position(2, 1), 2, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);

        m.addEntity(k);
        m.addEntity(d);

        // Test stepping on door with no key
        assertEquals(d.canStepOn(m, p), false);

        // Test stepping on door with key
        k.collect(p.getInventory());
        assertEquals(true, d.canStepOn(m, p));

        // Test stepping on unlocked door
        p.getInventory().clear();
        assertEquals(d.canStepOn(m, p), true);

        // Test stepping on door with wrong key
        Door d2 = new Door(new Position(1, 9), 1, 1);
        Key wrongKey = new Key(new Position(2, 1), 3, 5);
        wrongKey.collect(p.getInventory());
        assertEquals(d2.canStepOn(m, p), false);
    }
}
