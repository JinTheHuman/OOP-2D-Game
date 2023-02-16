package dungeonmania.Entities.StaticEntities;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class SwampTile extends StaticEntity {
    private int movementFactor;
    private Map<MovingEntity, Integer> entitiesOn = new HashMap<>();

    public SwampTile(Position position, int id, int movementFactor) {
        super(position, "swamp_tile", id);
        this.movementFactor = movementFactor;
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        entitiesOn.put(e, 0);
        return true;
    }

    public boolean notOn(MovingEntity e) {
        if (entitiesOn.get(e) == null) {
            entitiesOn.put(e, 1);
            return true;
        }
        return false;
    }

    public boolean canMoveOff(MovingEntity e) {
        if (entitiesOn.get(e) == movementFactor) {
            return true;
        }
        entitiesOn.put(e, entitiesOn.get(e) + 1);
        return false;
    }

    @Override
    public double getWeight() {
        return movementFactor;
    }

}
