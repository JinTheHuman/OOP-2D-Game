package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.Arrows;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.CollectableEntities.Treasure;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.Entities.StaticEntities.Exit;
import dungeonmania.util.Position;

public class GameMapTests {
    @Test
    @DisplayName("Test getting player")
    public void testGetPlayer() {
        Player expectedPlayer = new Player(new Position(1, 2), 1, 2, 3);
        GameMap gameMap = new GameMap(new ArrayList<Entity>(), expectedPlayer);
        Player actualPlayer = gameMap.getPlayer();
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    @DisplayName("Test getting collectableEntities")
    public void testGetCollectableEntities() {
        Player expectedPlayer = new Player(new Position(1, 2), 1, 2, 3);
        
        Arrows arrow1= new Arrows(new Position(2, 3), 2);
        Arrows arrow2 = new Arrows(new Position(2, 4), 3);
        Arrows arrow3 = new Arrows(new Position(33, 4), 4);
        Key key1 = new Key(new Position(2, 22), 5, 4);
        Key key2 = new Key(new Position(5, 33), 7, 7);
        Treasure treasure = new Treasure(new Position(4, 8), 8);

        Spider spider = new Spider(new Position(3, 9), 9, 6, 5);

        Exit exit = new Exit(new Position(5, 9), 10);

        ArrayList<Entity> entitiesList = new ArrayList<Entity>();
        entitiesList.add(arrow1);
        entitiesList.add(arrow2);
        entitiesList.add(arrow3);
        entitiesList.add(key1);
        entitiesList.add(key2);
        entitiesList.add(treasure);
        entitiesList.add(exit);
        entitiesList.add(spider);

        GameMap gameMap = new GameMap(entitiesList, expectedPlayer);
        List<Entity> actualResponse = gameMap.getCollectableEntities();
        List<Entity> expectedResponse = Arrays.asList(arrow1, arrow2, arrow3, key1, key2, treasure);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test getting moveableEntities")
    public void testGetMoveableEntities() {
        Player expectedPlayer = new Player(new Position(1, 2), 1, 2, 3);
        
        Arrows arrow1= new Arrows(new Position(2, 3), 2);
        Spider spider = new Spider(new Position(3, 9), 9, 6, 5);
        Exit exit = new Exit(new Position(5, 9), 10);

        ArrayList<Entity> entitiesList = new ArrayList<Entity>();
        entitiesList.add(arrow1);
        entitiesList.add(spider);
        entitiesList.add(exit);

        GameMap gameMap = new GameMap(entitiesList, expectedPlayer);
        List<Entity> actualResponse = gameMap.getMoveableEntities();
        List<Entity> expectedResponse = Arrays.asList(spider);
        assertEquals(expectedResponse, actualResponse);
    }


    @Test
    @DisplayName("Test getting staticEntities")
    public void testGetStaticEntities() {
        Player expectedPlayer = new Player(new Position(1, 2), 1, 2, 3);
        
        Arrows arrow1= new Arrows(new Position(2, 3), 2);
        Spider spider = new Spider(new Position(3, 9), 9, 6, 5);
        Exit exit = new Exit(new Position(5, 9), 10);

        ArrayList<Entity> entitiesList = new ArrayList<Entity>();
        entitiesList.add(arrow1);
        entitiesList.add(spider);
        entitiesList.add(exit);

        GameMap gameMap = new GameMap(entitiesList, expectedPlayer);
        List<Entity> actualResponse = gameMap.getStaticEntities();
        List<Entity> expectedResponse = Arrays.asList(exit);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Test getting position of all entities at a specific position")
    public void testGetEntitiesAtPosition() {
        Player expectedPlayer = new Player(new Position(1, 2), 1, 2, 3);
        
        Arrows arrow1= new Arrows(new Position(3, 3), 2);
        Arrows arrow2 = new Arrows(new Position(2, 4), 3);
        Arrows arrow3 = new Arrows(new Position(33, 4), 4);
        Spider spider = new Spider(new Position(3, 3), 9, 6, 5);
        Exit exit = new Exit(new Position(3, 3), 10);

        ArrayList<Entity> entitiesList = new ArrayList<Entity>();
        entitiesList.add(arrow1);
        entitiesList.add(arrow2);
        entitiesList.add(arrow3);
        entitiesList.add(spider);
        entitiesList.add(exit);

        GameMap gameMap = new GameMap(entitiesList, expectedPlayer);
        List<Entity> actualResponse = gameMap.getEntitiesAtPosition(new Position(3, 3));
        List<Entity> expectedResponse = Arrays.asList(arrow1, spider, exit);
        assertEquals(expectedResponse, actualResponse);
    }
}
