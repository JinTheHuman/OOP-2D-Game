package dungeonmania.Entities.BuildableEntities;

import dungeonmania.Entities.*;

public class BuildableEntity extends Entity {
    private int durability;

    public BuildableEntity(String type, int id, int durability) {
        super(null, type, id);
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public void changeDurability() {
        durability -= 1;
    }
}
