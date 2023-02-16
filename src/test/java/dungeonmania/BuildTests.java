package dungeonmania;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.BuildableEntities.Shield;
import dungeonmania.Entities.BuildableEntities.Bow;
import dungeonmania.Entities.BuildableEntities.MidnightArmour;
import dungeonmania.Entities.BuildableEntities.Sceptre;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.Entities.CollectableEntities.*;
import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.IllegalArgumentException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class BuildTests {
    @Test
    @DisplayName("Test invalid resources for shield")
    public void testInvalidShield() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Shield.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);

        Wood wood2 = new Wood(null, 3);
        p.addInventory(wood2);
        assertEquals(p.getInventory().size(), 2);
        assertEquals(Shield.isBuildable(p.getInventory()), null);
    }

    @Test
    @DisplayName("Test invalid resources for bow")
    public void testInvalidBow() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Bow.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);

        Wood wood2 = new Wood(null, 3);
        p.addInventory(wood2);
        assertEquals(p.getInventory().size(), 2);
        assertEquals(Bow.isBuildable(p.getInventory()), null);
    }

    @Test
    @DisplayName("Test building a shield with treasure")
    public void testBuildShieldTreasure() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Shield.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);
        Wood wood2 = new Wood(null, 3);
        p.addInventory(wood2);
        assertEquals(p.getInventory().size(), 2);
        Treasure treasure = new Treasure(null, 4);
        p.addInventory(treasure);
        assertEquals(p.getInventory().size(), 3);


        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("wood", 2);
        response.put("treasure", 1);
        assertEquals(Shield.isBuildable(p.getInventory()), response);
    }

    @Test
    @DisplayName("Test building a shield with key")
    public void testBuildShieldKey() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Shield.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);
        Wood wood2 = new Wood(null, 3);
        p.addInventory(wood2);
        assertEquals(p.getInventory().size(), 2);
        Key key = new Key(null, 4, 1);
        p.addInventory(key);
        assertEquals(p.getInventory().size(), 3);


        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("wood", 2);
        response.put("key", 1);
        assertEquals(Shield.isBuildable(p.getInventory()), response);
    }

    @Test
    @DisplayName("Test building a bow")
    public void testBuildBow() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Bow.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);
        Arrows arrow1 = new Arrows(null, 3);
        p.addInventory(arrow1);
        assertEquals(p.getInventory().size(), 2);
        Arrows arrow2 = new Arrows(null, 4);
        p.addInventory(arrow2);
        assertEquals(p.getInventory().size(), 3);
        Arrows arrow3 = new Arrows(null, 5);
        p.addInventory(arrow3);
        assertEquals(p.getInventory().size(), 4);

        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("wood", 1);
        response.put("arrow", 3);
        assertEquals(Bow.isBuildable(p.getInventory()), response);
    }


    @Test
    @DisplayName("Test building a bow with controller function")
    public void testControllerBuildBow() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("buildable_test", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 2));

        // check it throws error when invalid argument given
        assertThrows(IllegalArgumentException.class, () -> dmc.build("laptop"));
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        DungeonResponse newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));


        newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 4));
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));


        newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 5));
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(3, 5));
        assertDoesNotThrow(() -> dmc.build("bow"));
        // try build another bow
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));
    }

    @Test
    @DisplayName("Test building a shield with key and controller function")
    public void testControllerBuildShieldWithKey() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("buildable_test", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 2));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        DungeonResponse newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(3, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(4, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(4, 4));
        assertDoesNotThrow(() -> dmc.build("shield"));
        // try build another shield
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Test building a shield with treasure and controller function")
    public void testControllerBuildShieldWithTreasure() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("buildable_test", "c_movementTest_testMovementDown");
        EntityResponse initPlayer = getPlayer(initDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 2));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        DungeonResponse newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(2, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(3, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(4, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.RIGHT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(5, 3));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));


        newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(5, 4));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.DOWN);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(5, 5));
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));

        newDungonRes = dmc.tick(Direction.LEFT);
        initPlayer = getPlayer(newDungonRes).get();
        assertEquals(initPlayer.getPosition(), new Position(4, 5));
        assertDoesNotThrow(() -> dmc.build("shield"));
        // try build another shield
        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @DisplayName("Test build sceptre with wood/key")
    public void testBuildKeySceptre() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Sceptre.isBuildable(p.getInventory()), null);

        Wood wood1 = new Wood(null, 2);
        p.addInventory(wood1);
        assertEquals(p.getInventory().size(), 1);

        Key key = new Key(null, 3, 3);
        p.addInventory(key);
        assertEquals(p.getInventory().size(), 2);

        assertEquals(Sceptre.isBuildable(p.getInventory()), null);

        SunStone sunStone = new SunStone(null, 4);
        p.addInventory(sunStone);
        assertEquals(p.getInventory().size(), 3);

        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("wood", 1);
        response.put("key", 1);
        response.put("sun_stone", 1);

        assertEquals(Sceptre.isBuildable(p.getInventory()), response);
    }

    @Test
    @DisplayName("Test build sceptre with arrows/treasure")
    public void testBuildTreasureSceptre() {
        Player p = new Player(null, 1, 1, 0);
        assertEquals(p.getInventory().size(), 0);
        assertEquals(Sceptre.isBuildable(p.getInventory()), null);

        Treasure treasure = new Treasure(null, 2);
        p.addInventory(treasure);
        assertEquals(p.getInventory().size(), 1);

        Arrows arrow1 = new Arrows(null, 3);
        p.addInventory(arrow1);
        assertEquals(p.getInventory().size(), 2);

        SunStone sunStone = new SunStone(null, 4);
        p.addInventory(sunStone);
        assertEquals(p.getInventory().size(), 3);

        assertEquals(Sceptre.isBuildable(p.getInventory()), null);

        Arrows arrow2 = new Arrows(null, 3);
        p.addInventory(arrow2);
        assertEquals(p.getInventory().size(), 4);

        Map<String, Integer> response = new HashMap<String, Integer>();
        response.put("arrow", 2);
        response.put("treasure", 1);
        response.put("sun_stone", 1);

        assertEquals(Sceptre.isBuildable(p.getInventory()), response);
    }

    @Test
    @DisplayName("Test bribe mercenary with sceptre")
    public void testBribeMercenarySceptre() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("sceptre_Bribe",
                "sceptreConfig");
        
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        DungeonResponse newDungeonRes = dmc.tick(Direction.RIGHT);
        assertDoesNotThrow(() -> dmc.build("sceptre"));
        

        List<EntityResponse> mercenaryList = getEntities(newDungeonRes, "mercenary");
        EntityResponse mercenary = mercenaryList.get(0);
        assertTrue(mercenary.isInteractable());
        assertTrue(mercenaryList.size() == 1);

        newDungeonRes = dmc.interact(mercenary.getId());
        mercenaryList = getEntities(newDungeonRes, "mercenary");
        mercenary = mercenaryList.get(0);
        assertFalse(mercenary.isInteractable());
        assertTrue(mercenaryList.size() == 1);
    }

    @Test
    @DisplayName("Test bribe assassin with sceptre")
    public void testBribeAssasinSceptre() {

    }

    @Test
    @DisplayName("Test build midnight armour")
    public void testBuildMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("midnightArmour", "sceptreConfig");
        
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));


        assertDoesNotThrow(() -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test build midnight armour with zombie")
    public void testBuildMidnightArmourWithZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("midnightArmourZombie", "sceptreConfig");
        
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
        dmc.tick(Direction.RIGHT);
        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));


        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test midnight armour in battle")
    public void testBattleMidnightArmour() {

    }
    
    @Test
    @DisplayName("Test midnight armour durability")
    public void testMidnightArmourDurability() {

    }
}
