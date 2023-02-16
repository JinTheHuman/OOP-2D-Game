package dungeonmania.Entities.MovingEntities.MovingStrategies;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;

public interface MovingStrategy {
    public void move(GameMap map, MovingEntity e);
}
