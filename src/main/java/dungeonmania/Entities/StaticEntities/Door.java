package dungeonmania.Entities.StaticEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    int keyId;
    Boolean isLocked = true;

    public Door(Position position, int id, int keyId) {
        super(position, "door", id);
        this.keyId = keyId;
    }

    public int getKey() {
        return keyId;
    }

    public Boolean isLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        if (!this.isLocked()) {
            return true;
        }

        if (e instanceof Spider) {
            return true;
        }

        if (e instanceof Player) {
            Player p = (Player) e;

            if (!p.getItems("sun_stone").isEmpty()) {
                this.setIsLocked(false);
                return true;
            }

            if (p.getItems("key").isEmpty())
                return false;

            Key k = (Key) p.getItems("key").get(0);
            // if keyId does not match
            if (k.getKeyId() != keyId)
                return false;

            // otherwise if keyId matches
            this.setIsLocked(false);
            p.useItem("key", 1);
            return true;
        }
        return false;
    }

}
