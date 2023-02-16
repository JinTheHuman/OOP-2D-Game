package dungeonmania.Entities.CollectableEntities;

import java.util.List;

import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

public class CollectableEntity extends Entity {

    public CollectableEntity(Position position, String type, int id) {
        super(position, type, id);
    }

    public boolean collect(List<Entity> inventory) {
        inventory.add(this);
        return true;
    }
}
