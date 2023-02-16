package dungeonmania.Entities.StaticEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public abstract class StaticEntity extends Entity {

    public StaticEntity(Position position, String type, int id) {
        super(position, type, id);
    }

    public abstract boolean canStepOn(GameMap m, MovingEntity e);

    public double getWeight() {
        return 1;
    }
}
