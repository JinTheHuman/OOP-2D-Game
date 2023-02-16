package dungeonmania.Entities.StaticEntities;

import java.util.HashMap;
import java.util.Map;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Poo extends StaticEntity{
    private final int movement_factor = 10;
    private Map<MovingEntity, Integer> entitiesOn = new HashMap<>();
    
    public Poo(Position position, int id) {
        super(position, "poo", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        entitiesOn.put(e, 0);
        return true;
    }

    public boolean canMoveOff(MovingEntity e) {
        if (entitiesOn.get(e) == movement_factor) {
            return true;
        }
        entitiesOn.put(e, entitiesOn.get(e) + 1);
        return false;
    }

    @Override
    public double getWeight() {
        return movement_factor;
    }
    
}
