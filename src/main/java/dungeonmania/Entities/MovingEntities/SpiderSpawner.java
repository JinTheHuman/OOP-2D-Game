package dungeonmania.Entities.MovingEntities;

import java.util.Random;

import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.util.Position;

public class SpiderSpawner {
    public SpiderSpawner() {
    }

    public void spawn(GameMap map, int id, JSONObject config, int tickNum) {
        if (config.getInt("spider_spawn_rate") == 0) {
            return;
        }

        if (tickNum % config.getInt("spider_spawn_rate") == 0) {
            Random rand = new Random();
            int random1 = rand.nextInt(50);
            int random2 = rand.nextInt(50);
            Spider spider = new Spider(new Position(random1, random2), id,
                    config.getInt("spider_health"), config.getInt("spider_attack"));
            map.addEntity(spider);
        }
    }
}
