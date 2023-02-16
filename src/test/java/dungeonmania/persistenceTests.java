package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static dungeonmania.TestUtils.getPlayer;
import static dungeonmania.TestUtils.getEntities;
import static dungeonmania.TestUtils.getInventory;
import static dungeonmania.TestUtils.getGoals;
import static dungeonmania.TestUtils.countEntityOfType;
import static dungeonmania.TestUtils.getValueFromConfigFile;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class persistenceTests {
        @Test
        @DisplayName("Test preserve the position of all entities")
        public void testPreserveEntityPositions() {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
                Position saveBoulderPos = getEntities(actualDungonRes, "boulder").get(0).getPosition();
                Position savePlayerPos = getEntities(actualDungonRes, "player").get(0).getPosition();
                Position saveSpiderPos = getEntities(actualDungonRes, "spider").get(0).getPosition();
                Position saveMercPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
                Position saveZombiePos = getEntities(actualDungonRes, "zombie_toast").get(0).getPosition();
                Position saveTreasurePos = getEntities(actualDungonRes, "treasure").get(0).getPosition();
                dmc.saveGame("testPreserveEntity");
                DungeonManiaController newDmc = new DungeonManiaController();
                DungeonResponse newDungeonRes = newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                Position newPlayerPos = getEntities(newDungeonRes, "player").get(0).getPosition();
                assertNotEquals(savePlayerPos, newPlayerPos);
                DungeonResponse loadDungeonRes = newDmc.loadGame("testPreserveEntity");
                Position loadBoulderPos = getEntities(loadDungeonRes, "boulder").get(0).getPosition();
                Position loadPlayerPos = getEntities(loadDungeonRes, "player").get(0).getPosition();
                Position loadSpiderPos = getEntities(loadDungeonRes, "spider").get(0).getPosition();
                Position loadMercPos = getEntities(loadDungeonRes, "mercenary").get(0).getPosition();
                Position loadZombiePos = getEntities(loadDungeonRes, "zombie_toast").get(0).getPosition();
                Position loadTreasurePos = getEntities(loadDungeonRes, "treasure").get(0).getPosition();
                // assert entities in loaded dungeon has same positions as when saved
                assertEquals(saveBoulderPos, loadBoulderPos);
                assertEquals(savePlayerPos, loadPlayerPos);
                assertEquals(saveSpiderPos, loadSpiderPos);
                assertEquals(saveMercPos, loadMercPos);
                assertEquals(saveZombiePos, loadZombiePos);
                assertEquals(saveTreasurePos, loadTreasurePos);
        }

        @Test
        @DisplayName("Test preserve potion ticks")
        public void testPreservePotionTicks() throws IllegalArgumentException, InvalidActionException {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistenceSpider",
                                "c_invincibilityPotionLongerDuration");
                DungeonResponse dungeonResponse = dmc.tick(Direction.RIGHT);
                dungeonResponse = dmc.tick(getInventory(dungeonResponse, "invincibility_potion").get(0).getId());
                DungeonResponse saveRes = dmc.saveGame("testPreservePotions");
                Position mercPos = getEntities(saveRes, "spider").get(0).getPosition();
                Position playerPos = getEntities(saveRes, "player").get(0).getPosition();
                assertEquals(mercPos, new Position(1, 0));
                assertEquals(playerPos, new Position(2, 1));
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                newDmc.loadGame("testPreservePotions");
                DungeonResponse loadedDungeonRes = newDmc.tick(Direction.LEFT);
                BattleResponse actualBattleResponse = loadedDungeonRes.getBattles().get(0);
                List<RoundResponse> rounds = actualBattleResponse.getRounds();
                assertTrue(rounds.size() == 1);
                assertEquals(rounds.get(0).getWeaponryUsed().get(0).getType(), "invincibility_potion");
        }

        @Test
        @DisplayName("Test preserve bribing")
        public void testPreserveAllies() throws IllegalArgumentException, InvalidActionException {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_invincibilityPotionLongerDuration");
                DungeonResponse dungeonResponse = dmc.tick(Direction.LEFT);
                DungeonResponse bribeResponse = dmc.interact(getEntities(dungeonResponse, "mercenary").get(0).getId());
                assertFalse(getEntities(bribeResponse, "mercenary").get(0).isInteractable());
                dmc.saveGame("testPreserveBribing");
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                DungeonResponse loadedDungeonRes = newDmc.loadGame("testPreserveBribing");
                assertFalse(getEntities(loadedDungeonRes, "mercenary").get(0).isInteractable());
        }

        @Test
        @DisplayName("Test preserve inventory")
        public void testPreserveInventory() {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_invincibilityPotionLongerDuration");
                DungeonResponse dungeonResponse = dmc.tick(Direction.LEFT);
                assertTrue(getInventory(dungeonResponse, "treasure").size() == 1);
                dmc.saveGame("testPreserveInventory");
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                DungeonResponse loadedDungeonRes = newDmc.loadGame("testPreserveInventory");
                assertTrue(getInventory(loadedDungeonRes, "treasure").size() == 1);
        }

        @Test
        @DisplayName("Test preserve planted bomb can explode")
        public void testPreservePlantedBombs() throws IllegalArgumentException, InvalidActionException {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_invincibilityPotionLongerDuration");
                DungeonResponse dungeonResponse = dmc.tick(Direction.UP);
                assertTrue(getInventory(dungeonResponse, "bomb").size() == 1);
                dmc.tick(Direction.DOWN);
                dmc.tick(Direction.RIGHT);
                dmc.tick(Direction.DOWN);
                dmc.tick(Direction.DOWN);
                DungeonResponse bombRes = dmc.tick(getInventory(dungeonResponse, "bomb").get(0).getId());
                assertTrue(getEntities(bombRes, "bomb").size() == 1);
                dmc.saveGame("testPreserveBomb");
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                DungeonResponse loadedDungeonRes = newDmc.loadGame("testPreserveBomb");
                Position bombPos = getEntities(loadedDungeonRes, "bomb").get(0).getPosition();
                assertEquals(bombPos, new Position(2, 3));
                newDmc.tick(Direction.UP);
                newDmc.tick(Direction.UP);
                newDmc.tick(Direction.LEFT);
                DungeonResponse bombExpodedRes = newDmc.tick(Direction.DOWN);
                assertTrue(getEntities(bombExpodedRes, "switch").size() == 0);
                assertTrue(getEntities(bombExpodedRes, "bomb").size() == 0);
                assertTrue(getEntities(bombExpodedRes, "boulder").size() == 0);
        }

        @Test
        @DisplayName("Test preserve opened doors")
        public void testPreserveOpenedDoors() {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_invincibilityPotionLongerDuration");
                dmc.tick(Direction.LEFT);
                DungeonResponse pickedKeyRes = dmc.tick(Direction.LEFT);
                assertTrue(getInventory(pickedKeyRes, "key").size() == 1);
                DungeonResponse openDoorRes = dmc.tick(Direction.LEFT);
                Position playerPos = getPlayer(openDoorRes).get().getPosition();
                Position doorPos = getEntities(openDoorRes, "door").get(0).getPosition();
                assertEquals(playerPos, doorPos);
                dmc.tick(Direction.DOWN);
                dmc.saveGame("testPreserveDoor");
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                newDmc.loadGame("testPreserveDoor");
                DungeonResponse loadedDoorRes = newDmc.tick(Direction.UP);
                Position loadedPlayerPos = getPlayer(loadedDoorRes).get().getPosition();
                Position loadedDoorPos = getEntities(loadedDoorRes, "door").get(0).getPosition();
                assertEquals(loadedPlayerPos, loadedDoorPos);
        }

        @Test
        @DisplayName("Test preserve mind control")
        public void testPreserveMindControl() throws IllegalArgumentException, InvalidActionException {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistenceMindControl",
                                "sceptreConfig");
                dmc.tick(Direction.LEFT);
                dmc.tick(Direction.LEFT);
                dmc.tick(Direction.LEFT);
                DungeonResponse buildSceptreRes = dmc.build("sceptre");
                assertTrue(getInventory(buildSceptreRes, "sceptre").size() == 1);
                DungeonResponse mindControlRes = dmc.interact(getEntities(buildSceptreRes, "mercenary").get(0).getId());
                assertFalse(getEntities(mindControlRes, "mercenary").get(0).isInteractable());
                dmc.saveGame("testPreserveMindControl");
                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistenceMindControl",
                                "c_movementTest_testMovementDown");
                DungeonResponse loadedDungeonRes = newDmc.loadGame("testPreserveMindControl");
                assertFalse(getEntities(loadedDungeonRes, "mercenary").get(0).isInteractable());
        }

        @Test
        @DisplayName("Test preserve completed subgoals")
        public void testPreserveSubgoals() {
                DungeonManiaController dmc = new DungeonManiaController();
                dmc.newGame("testPersistence",
                                "c_invincibilityPotionLongerDuration");

                DungeonResponse dungeonResponse = dmc.tick(Direction.RIGHT);
                String initialGoals = dungeonResponse.getGoals();
                assert (initialGoals.contains(":exit") && initialGoals.contains(":treasure"));

                dmc.tick(Direction.LEFT);
                dungeonResponse = dmc.tick(Direction.LEFT);
                assertTrue(getInventory(dungeonResponse, "treasure").size() == 1);

                DungeonResponse saveRes = dmc.saveGame("testGoals");
                String savedGoals = saveRes.getGoals();
                assert (savedGoals.contains(":exit") && !savedGoals.contains(":treasure"));

                DungeonManiaController newDmc = new DungeonManiaController();
                newDmc.newGame("testPersistence",
                                "c_movementTest_testMovementDown");
                DungeonResponse loadedDungeonRes = newDmc.loadGame("testGoals");
                String loadedGoals = loadedDungeonRes.getGoals();
                assert (loadedGoals.contains(":exit") && !loadedGoals.contains(":treasure"));
        }
}
