package dungeonmania.Entities.CollectableEntities;

import dungeonmania.util.Position;

public class SunStone extends CollectableEntity {
    boolean isRetained = false;

    public SunStone(Position position, int id) {
        super(position, "sun_stone", id);
    }
}
