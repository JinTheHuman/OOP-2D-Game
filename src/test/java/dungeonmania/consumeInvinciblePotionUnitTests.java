package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class consumeInvinciblePotionUnitTests {
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
            String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType +
                "_health", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), 0);
            assertEquals(round.getDeltaEnemyHealth(), -battle.getInitialEnemyHealth());
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }
        assertEquals(1, rounds.size());
        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    private double getDistanceBetween(Position p1, Position p2) {
        return Math.sqrt(Math.pow((p2.getX() - p1.getX()), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    @Test
    public void testInvincibleBattle() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testInvincibilityPotionMerc",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(getPlayer(actualDungonRes).get().getPosition(), new Position(1, 2));
        assertEquals(1, getInventory(actualDungonRes, "invincibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invincibility_potion").get(0).getId();
        actualDungonRes = dmc.tick(potionId);
        assertEquals(1, actualDungonRes.getBattles().size());
        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);
        assertBattleCalculations("spider", actualBattleResponse, true, "c_battleTests_basicMercenaryMercenaryDies");
    }

    @Test
    public void testInvincibleZombieMovement() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testInvincibilityPotion",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        dmc.interact(getEntities(actualDungonRes, "mercenary").get(0).getId());
        actualDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(1, getInventory(actualDungonRes, "invincibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invincibility_potion").get(0).getId();
        EntityResponse toast = getEntities(actualDungonRes, "zombie_toast").get(0);
        double initialDist = getDistanceBetween(getPlayer(actualDungonRes).get().getPosition(), toast.getPosition());
        actualDungonRes = dmc.tick(potionId);
        toast = getEntities(actualDungonRes, "zombie_toast").get(0);
        double newDist = getDistanceBetween(getPlayer(actualDungonRes).get().getPosition(), toast.getPosition());
        assertTrue(initialDist < newDist);
    }

    // @Test
    // public void testInvincibleAllyMovement() throws IllegalArgumentException,
    // InvalidActionException {
    // DungeonManiaController dmc = new DungeonManiaController();
    // dmc.newGame("testInvincibilityPotion",
    // "c_battleTests_basicMercenaryMercenaryDies");
    // DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
    // dmc.interact(getEntities(actualDungonRes, "mercenary").get(0).getId());
    // actualDungonRes = dmc.tick(Direction.DOWN);
    // assertEquals(1, getInventory(actualDungonRes,
    // "invincibility_potion").size());
    // String potionId = getInventory(actualDungonRes,
    // "invincibility_potion").get(0).getId();
    // EntityResponse merc = getEntities(actualDungonRes, "mercenary").get(0);
    // double initialDist =
    // getDistanceBetween(getPlayer(actualDungonRes).get().getPosition(),
    // merc.getPosition());
    // actualDungonRes = dmc.tick(potionId);
    // merc = getEntities(actualDungonRes, "mercenary").get(0);
    // double newDist =
    // getDistanceBetween(getPlayer(actualDungonRes).get().getPosition(),
    // merc.getPosition());
    // assertTrue(initialDist < newDist);
    // }

    @Test
    public void testInvincibleSpiderMovement() {

    }

    @Test
    public void testInvincibleDuration() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        dmc.newGame("testInvincibilityPotionMerc",
                "c_battleTests_basicMercenaryMercenaryDies");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);

        assertEquals(1, getInventory(actualDungonRes, "invincibility_potion").size());
        String potionId = getInventory(actualDungonRes, "invincibility_potion").get(0).getId();
        Position oldMercPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        actualDungonRes = dmc.tick(potionId);
        Position newMercPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertNotEquals(oldMercPos, newMercPos);
        actualDungonRes = dmc.tick(Direction.DOWN);
        newMercPos = getEntities(actualDungonRes, "mercenary").get(0).getPosition();
        assertEquals(oldMercPos, newMercPos);
    }
}
