package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.TestUtils.*;

public class BattleTests {
    // check battle calculations for battles without weapons or bonuses
    private void assertBattleCalculations(String enemyType, BattleResponse battle, boolean enemyDies,
            String configFilePath) {
        List<RoundResponse> rounds = battle.getRounds();
        double playerHealth = Double.parseDouble(getValueFromConfigFile("player_health", configFilePath));
        double enemyHealth = Double.parseDouble(getValueFromConfigFile(enemyType +
                "_health", configFilePath));
        double playerAttack = Double.parseDouble(getValueFromConfigFile("player_attack", configFilePath));
        double enemyAttack = Double.parseDouble(getValueFromConfigFile(enemyType +
                "_attack", configFilePath));

        for (RoundResponse round : rounds) {
            assertEquals(round.getDeltaCharacterHealth(), -enemyAttack / 10);
            assertEquals(round.getDeltaEnemyHealth(), -playerAttack / 5);
            enemyHealth += round.getDeltaEnemyHealth();
            playerHealth += round.getDeltaCharacterHealth();
        }

        if (enemyDies) {
            assertTrue(enemyHealth <= 0);
        } else {
            assertTrue(playerHealth <= 0);
        }
    }

    @Test
    @DisplayName("Test multi-round combat without any weapons")
    public void testBasicCombat() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("battle_tests",
                "c_battleTests_basicMercenaryMercenaryDies");

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.LEFT);
        actualDungonRes = dmc.tick(Direction.RIGHT);

        EntityResponse expectedPlayer = new EntityResponse("Entity1", "player", new Position(3, 2), false);
        EntityResponse actualPlayer = getPlayer(actualDungonRes).get();

        assertEquals(expectedPlayer, actualPlayer);

        List<RoundResponse> expectedRoundResponses = new ArrayList<RoundResponse>();
        expectedRoundResponses.add(new RoundResponse(0.5, 2, new ArrayList<ItemResponse>()));
        expectedRoundResponses.add(new RoundResponse(0.5, 2, new ArrayList<ItemResponse>()));
        expectedRoundResponses.add(new RoundResponse(0.5, 2, new ArrayList<ItemResponse>()));

        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);

        // zombie is moving away from you
        assertBattleCalculations("zombie", actualBattleResponse, true,
                "c_battleTests_basicMercenaryMercenaryDies");

        assertTrue(getEntities(actualDungonRes, "zombie_toast").size() == 0);
    }

    @Test
    @DisplayName("Test multi-round combat against spider and player dies")
    public void testLoseSpiderCombat() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("battle_tests spider",
                "c_battleTests_spider_battle");

        // Check spider exists
        assertDoesNotThrow(() -> getEntities(initDungonRes, "spider").get(0));

        // Start battle
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);

        // check spider still exists
        assertDoesNotThrow(() -> getEntities(initDungonRes, "spider").get(0));

        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);

        // Check battle lasted 4 rounds and enemy did not die and battle calculations
        // are correct
        assertEquals(4, actualBattleResponse.getRounds().size());
        assertBattleCalculations("spider", actualBattleResponse, false,
                "c_battleTests_spider_battle");

        // Check player no longer exists
        assertEquals(Optional.empty(), getPlayer(actualDungonRes));
    }

    @Test
    @DisplayName("Test multi-round combat loss to zombie")
    public void testLosttoZombie() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("battle_tests",
                "c_battleTests_spider_battle");

        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);

        // Ensure battle with zombie exists
        try {
            actualDungonRes = dmc.tick(Direction.LEFT);
            actualDungonRes = dmc.tick(Direction.RIGHT);
            actualDungonRes = dmc.tick(Direction.LEFT);
            actualDungonRes = dmc.tick(Direction.RIGHT);
            actualDungonRes = dmc.tick(Direction.LEFT);
            actualDungonRes = dmc.tick(Direction.RIGHT);
        } catch (NullPointerException e) {
            ;
        }

        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);

        // Check battle lasted 4 rounds and enemy did not die and battle calculations
        // are correct
        assertEquals(4, actualBattleResponse.getRounds().size());
        assertBattleCalculations("zombie", actualBattleResponse, false,
                "c_battleTests_spider_battle");
        // Check player no longer exists
        assertEquals(Optional.empty(), getPlayer(actualDungonRes));
    }

    @Test
    @DisplayName("Test durability of weapons")
    public void testDurability() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("battle_tests_weapon",
                "c_battleTests_spider_battle");

        // Start battle
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.RIGHT);

        BattleResponse actualBattleResponse = actualDungonRes.getBattles().get(0);

        // Check battle lasted 4 rounds and enemy did not die and battle calculations
        // are correct
        assertEquals(3, actualBattleResponse.getRounds().size());
        for (RoundResponse r : actualBattleResponse.getRounds()) {
            assertEquals(-2.4, r.getDeltaEnemyHealth());
        }
    }

    @Test
    @DisplayName("Test fight three mercenaries one after each other and win")
    public void testWeaponCombat() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("battle_tests_three_mercs",
                "c_battleTests_spider_battle");

        // Start battle
        DungeonResponse actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.RIGHT);
        actualDungonRes = dmc.tick(Direction.RIGHT);

        List<BattleResponse> b = actualDungonRes.getBattles();
        assertEquals(3, b.size());

        for (BattleResponse br : b) {
            assertEquals(3, br.getRounds().size());
            for (RoundResponse r : br.getRounds()) {
                assertEquals(-2, r.getDeltaEnemyHealth());
                assertEquals(-0.5, r.getDeltaCharacterHealth());
            }
        }
    }

    @Test
    public void testHydraBattle() {
        // assert hp goes up
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_hydra_battle_test",
                "c_hydra_battle_tests");
        DungeonResponse actualDungonRes = dmc.tick(Direction.DOWN);
        List<BattleResponse> b = actualDungonRes.getBattles();
        assertEquals(1, b.size());
        // 5/0.3 rounded up
        assertEquals(17, b.get(0).getRounds().size());
        assertEquals(1, countEntityOfType(actualDungonRes, "hydra"));
    }
}
