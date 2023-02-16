package dungeonmania.Entities.StaticEntities;

import org.json.JSONObject;
import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.Entities.MovingEntities.ZombieToast;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends StaticEntity {

    public ZombieToastSpawner(Position position, int id) {
        super(position, "zombie_toast_spawner", id);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        if (e instanceof Spider) {
            return true;
        }
        return false;
    }

    public void spawn2(GameMap map, int id, JSONObject config, int tickNum) {
        if (config.getInt("zombie_spawn_rate") == 0) {
            return;
        }

        if (tickNum % config.getInt("zombie_spawn_rate") == 0) {
            ZombieToast z = this.spawn(map, id, config);
            if (z != null) {
                map.addEntity(z);
            }
        }

    }

    public ZombieToast spawn(GameMap map, int id, JSONObject config) {
        int poo = checkSpawnable(map);
        if (poo == 1) {
            ZombieToast zombie = new ZombieToast(getPosition().translateBy(Direction.UP), id,
                    config.getInt("zombie_health"), config.getInt("zombie_attack"));
            return zombie;
        } else if (poo == 2) {
            ZombieToast zombie = new ZombieToast(getPosition().translateBy(Direction.DOWN), id,
                    config.getInt("zombie_health"), config.getInt("zombie_attack"));
            return zombie;
        } else if (poo == 3) {
            ZombieToast zombie = new ZombieToast(getPosition().translateBy(Direction.LEFT), id,
                    config.getInt("zombie_health"), config.getInt("zombie_attack"));
            return zombie;
        } else if (poo == 4) {
            ZombieToast zombie = new ZombieToast(getPosition().translateBy(Direction.RIGHT), id,
                    config.getInt("zombie_health"), config.getInt("zombie_attack"));
            return zombie;
        } else
            return null;
    }

    public int checkSpawnable(GameMap map) {
        ZombieToast zombie1 = new ZombieToast(getPosition(), 0, 0, 0);

        if (map.canStepInto(zombie1, getPosition().translateBy(Direction.UP))) {
            return 1;
        } else if (map.canStepInto(zombie1, getPosition().translateBy(Direction.DOWN))) {
            return 2;
        } else if (map.canStepInto(zombie1, getPosition().translateBy(Direction.LEFT))) {
            return 3;
        } else if (map.canStepInto(zombie1, getPosition().translateBy(Direction.RIGHT))) {
            return 4;
        }
        return 0;
    }

    public boolean isplayerIsAdjacent(Player player) {
        Position playerPos = player.getPosition();

        for (Direction d : Direction.values()) {
            if (getPosition().translateBy(d).equals(playerPos)) {
                return true;
            }
        }

        return false;
    }
}
