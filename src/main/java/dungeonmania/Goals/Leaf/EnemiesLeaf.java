package dungeonmania.Goals.Leaf;

import java.util.List;

import org.json.JSONObject;
import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;

public class EnemiesLeaf extends LeafNode  {
    public boolean evaluate(GameMap map, JSONObject config) {
        List<Entity> spawnerEntities = map.getEntities("zombie_toast_spawner");
        // check that there are no spawners
        if (spawnerEntities.size() != 0) return false;

        int enemy_goal = config.getInt("enemy_goal");
        int enemiesDestroyed = map.getPlayer().getEnemiesDestroyed();
        if (enemiesDestroyed >= enemy_goal) return true;

        return false;
    }
}
