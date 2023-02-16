package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.Bomb;
import dungeonmania.Entities.CollectableEntities.Wood;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.Boulder;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class plantBombUnitTests {
    @Test
    @DisplayName("Test bomb will be planted at player position")
    public void testPlantBomb() {
        Bomb b = new Bomb(new Position(1, 1), 1, 0);
        Player p = new Player(new Position(2, 1), 2, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);
        m.addEntity(b);
        List<Entity> entities = m.getEntitiesAtPosition(new Position(1, 1));
        assertTrue(entities.contains(b));
        b.collect(p.getInventory());
        b.plantBomb(p, m);
        entities = m.getEntitiesAtPosition(new Position(2, 1));
        assertTrue(entities.contains(b));
    }

    @Test
    @DisplayName("Test bomb will destroy nearby entities when planted next to a active switch")
    public void testBombDestroysEntities() {
        Player p = new Player(new Position(1, 1), 5, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);
        Wood w = new Wood(new Position(2, 2), 1);
        FloorSwitch s = new FloorSwitch(new Position(2, 1), 2);
        Boulder boulder = new Boulder(new Position(2, 0), 3);
        Bomb bomb = new Bomb(new Position(1, 1), 4, 1);
        m.addEntity(w);
        m.addEntity(s);
        m.addEntity(boulder);
        m.addEntity(bomb);
        bomb.collect(p.getInventory());
        bomb.plantBomb(p, m);
        p.move(m, Direction.UP);
        p.move(m, Direction.UP);
        p.move(m, Direction.RIGHT);
        p.move(m, Direction.DOWN);
        p.move(m, Direction.DOWN);
        List<Entity> entities = m.getEntitiesAtPosition(new Position(2, 2));
        assertFalse(entities.contains(w));
    }

    @Test
    @DisplayName("Test bomb will not explode when near an inactive switch")
    public void testBombNextToInactiveSwitch() {
        Player p = new Player(new Position(1, 1), 5, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);
        Wood w = new Wood(new Position(2, 2), 1);
        FloorSwitch s = new FloorSwitch(new Position(2, 1), 2);
        Bomb bomb = new Bomb(new Position(1, 1), 4, 1);
        m.addEntity(w);
        m.addEntity(s);
        m.addEntity(bomb);
        bomb.collect(p.getInventory());
        bomb.plantBomb(p, m);
        List<Entity> entities = m.getEntitiesAtPosition(new Position(2, 2));
        assertTrue(entities.contains(w));
        entities = m.getEntitiesAtPosition(new Position(1, 1));
        assertTrue(entities.contains(bomb));
    }

    @Test
    @DisplayName("Test planted bombs cannot be picked up")
    public void testPlantedBombPickUp() {
        Bomb b = new Bomb(new Position(1, 1), 1, 0);
        Player p = new Player(new Position(2, 1), 2, 100, 100);
        GameMap m = new GameMap(new ArrayList<Entity>(), p);
        m.addEntity(b);
        List<Entity> entities = m.getEntitiesAtPosition(new Position(1, 1));
        assertTrue(entities.contains(b));
        b.collect(p.getInventory());
        b.plantBomb(p, m);
        entities = m.getEntitiesAtPosition(new Position(2, 1));
        assertTrue(entities.contains(b));
        assertFalse(b.collect(p.getInventory()));
    }
}
