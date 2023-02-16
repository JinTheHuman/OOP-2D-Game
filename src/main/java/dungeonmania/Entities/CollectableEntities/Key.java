package dungeonmania.Entities.CollectableEntities;

import java.util.List;

import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;

public class Key extends CollectableEntity {
    private int keyId;

    public Key(Position position, int id, int keyId) {
        super(position, "key", id);
        this.keyId = keyId;
    }

    @Override
    public boolean collect(List<Entity> inventory) {
        if (!inventory.stream().anyMatch(e -> e instanceof Key)) {
            return super.collect(inventory);
        }
        return false;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
}