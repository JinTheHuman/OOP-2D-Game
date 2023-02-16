package dungeonmania.Goals.Leaf;

import java.util.List;
import org.json.JSONObject;

import dungeonmania.Entities.Entity;
import dungeonmania.GameMap;
import dungeonmania.util.Position;

public class ExitLeaf extends LeafNode  {
    // check if player is on the exit
    public boolean evaluate(GameMap map, JSONObject config) {
        Position playerPosition = map.getPlayer().getPosition();
        List<Entity> exitEntities = map.getEntities("exit");

        for (Entity exit : exitEntities) {
            Position exitPosition = exit.getPosition();

            // if player is on an exit
            if ((exitPosition.getX() == playerPosition.getX()) && (exitPosition.getY() == playerPosition.getY())) return true;
        }
        return false;
    }
}
