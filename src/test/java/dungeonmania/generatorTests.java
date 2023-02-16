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

public class generatorTests {
    @Test
    public void generatorTests() {
        // assert hp goes up
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.generateDungeon(0, 0, 9, 9, "c_zombies");
        assertTrue(getEntities(initDungonRes, "exit").size() == 1);
        assertTrue(getEntities(initDungonRes, "player").size() == 1);
        assertTrue(getEntities(initDungonRes, "wall").size() >= 10);
    }
}
