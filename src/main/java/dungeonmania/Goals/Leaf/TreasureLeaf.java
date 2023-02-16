package dungeonmania.Goals.Leaf;

import java.util.List;

import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;

public class TreasureLeaf extends LeafNode {
    public boolean evaluate(GameMap map, JSONObject config) {
        int treasureGoal = config.getInt("treasure_goal");

        List<Entity> inventory = map.getPlayer().getInventory();
        int treasureCollected = 0;
        for (Entity item : inventory) {
            if (item.getType().equals("treasure") || item.getType().equals("sun_stone"))
                treasureCollected++;
        }
        if (treasureCollected >= treasureGoal)
            return true;

        return false;
    }
}
