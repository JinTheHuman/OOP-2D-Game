package dungeonmania.Entities.CollectableEntities;

import dungeonmania.util.Position;

public class Treasure extends CollectableEntity {

    public Treasure(Position position, int id) {
        super(position, "treasure", id);
    }

    public boolean isSpecial() {
        return false;
    }
}
