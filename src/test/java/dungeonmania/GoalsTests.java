package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static dungeonmania.TestUtils.*;

import java.io.IOException;
import java.util.List;

import dungeonmania.response.models.*;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Goals.Goals;
import dungeonmania.response.models.DungeonResponse;

public class GoalsTests {
     // helper function to read in JSON Object
     private JSONObject getJSONFromItem(String filePath) {
        try {
            JSONObject obj = new JSONObject(FileLoader.loadResourceFile(filePath + ".json"));
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Test
    @DisplayName("Test basic exit goal")
    public void testBasicExitGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("zombieMovementTest", "c_spiderTest_basicMovement");
        JSONObject config = getJSONFromItem("/configs/c_spiderTest_basicMovement");
        GameMap map = dmc.getGameMap();
        Player player = map.getPlayer();

        Goals goals = dmc.getGoals();
        assertFalse(goals.evaluate(map, config));

        List<EntityResponse> exitResponse = getEntities(initDungonRes, "exit");
        assertEquals(exitResponse.size(), 1);
        Position exitPosition = exitResponse.get(0).getPosition();
        // move player to exit position.
        player.setPosition(exitPosition);
        assertTrue(goals.evaluate(map, config));
    }

    @Test
    @DisplayName("Test complex AND goal")
    public void testComplexANDGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_ANDeasierComplexGoalsTest", "c_spiderTest_basicMovement");

        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));

        List<EntityResponse> exitResponse = getEntities(initDungonRes, "exit");
        assertEquals(exitResponse.size(), 1);
        List<EntityResponse> treasureResponse = getEntities(initDungonRes, "treasure");
        assertEquals(treasureResponse.size(), 1);

        initDungonRes = dmc.tick(Direction.DOWN);
        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertFalse(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));


        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(getGoals(initDungonRes), "");
    }

    @Test
    @DisplayName("Test complex OR goal")
    public void testComplexORGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse initDungonRes = dmc.newGame("d_OReasierComplexGoalsTest", "c_spiderTest_basicMovement");

        assertTrue(getGoals(initDungonRes).contains(":exit"));
        assertTrue(getGoals(initDungonRes).contains(":treasure"));
        assertFalse(getGoals(initDungonRes).contains(":boulders"));
        assertFalse(getGoals(initDungonRes).contains(":enemies"));

        List<EntityResponse> exitResponse = getEntities(initDungonRes, "exit");
        assertEquals(exitResponse.size(), 1);
        List<EntityResponse> treasureResponse = getEntities(initDungonRes, "treasure");
        assertEquals(treasureResponse.size(), 1);

        initDungonRes = dmc.tick(Direction.DOWN);
        assertEquals(getGoals(initDungonRes), "");
    }
}
