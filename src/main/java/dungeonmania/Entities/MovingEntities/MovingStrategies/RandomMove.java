package dungeonmania.Entities.MovingEntities.MovingStrategies;

import java.io.Serializable;
import java.util.Random;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RandomMove implements MovingStrategy, Serializable {
    public void move(GameMap map, MovingEntity e) {
        Random rand = new Random();
        Position pos = e.getPosition();
        int randomint = rand.nextInt(8);
        switch (randomint) {
            case 0:
                pos = pos.translateBy(Direction.DOWN);
                break;
            case 1:
                pos = pos.translateBy(Direction.UP);
                break;
            case 2:
                pos = pos.translateBy(Direction.LEFT);
                break;
            case 3:
                pos = pos.translateBy(Direction.RIGHT);
                break;
            case 4:
                pos = pos.translateBy(Direction.UP).translateBy(Direction.RIGHT);
                break;
            case 5:
                pos = pos.translateBy(Direction.DOWN).translateBy(Direction.RIGHT);
                break;
            case 6:
                pos = pos.translateBy(Direction.UP).translateBy(Direction.LEFT);
                break;
            case 7:
                pos = pos.translateBy(Direction.DOWN).translateBy(Direction.LEFT);
                break;
        }

        for (Entity entity : map.getEntitiesAtPosition(e.getPosition())) {
            if (entity instanceof SwampTile) {
                SwampTile s = (SwampTile) entity;
                if (!s.canMoveOff(e)) {
                    return;
                }
            }
        }

        if (map.canStepInto(e, pos)) {
            e.setPosition(pos);
        }
    }
}
