package dungeonmania.Goals.Leaf;

import java.util.List;
import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

public class BouldersLeaf extends LeafNode {
    // check if a boulder is on all floor switches
    public boolean evaluate(GameMap map, JSONObject config) {
        List<Entity> boulderList = map.getEntities("boulder");
        List<Entity> switchList = map.getEntities("switch");

        for (Entity switchEntity : switchList) {
            Position switchPosition = switchEntity.getPosition();

            boolean isBoulderOnSwitch = false; 
            for (Entity boulderEntity : boulderList) {
                Position boulderPosition = boulderEntity.getPosition();
                if ((switchPosition.getX() == boulderPosition.getX()) && (switchPosition.getY() == boulderPosition.getY())) {
                    isBoulderOnSwitch = true;
                    break;
                }
            }
            // if there is no boulder on the switch, then return false
            if (!isBoulderOnSwitch) return false;
        }
        return true;
    }
}
