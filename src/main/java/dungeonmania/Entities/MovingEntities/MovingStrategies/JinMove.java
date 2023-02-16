package dungeonmania.Entities.MovingEntities.MovingStrategies;

import java.util.Random;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class JinMove implements MovingStrategy{

    @Override
    public void move(GameMap map, MovingEntity e) {
        Random rand = new Random();
        Position pos = e.getPosition();
        int randomint = rand.nextInt(4);
        Direction dir = null;
        switch (randomint) {
            case 0:
                dir = Direction.DOWN;
                pos = pos.translateBy(dir);
                break;
            case 1:
                dir = Direction.UP;
                pos = pos.translateBy(dir);
                break;
            case 2:
                dir = Direction.LEFT;
                pos = pos.translateBy(dir);
                break;
            case 3:
                dir = Direction.RIGHT;
                pos = pos.translateBy(dir);
                break;
        }

        if (map.canStepInto(e, pos)) {
            e.setDirection(dir);
            e.setPosition(pos);
        }
    }
    
}
