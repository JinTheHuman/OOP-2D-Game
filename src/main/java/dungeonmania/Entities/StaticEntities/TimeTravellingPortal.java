package dungeonmania.Entities.StaticEntities;

import dungeonmania.DungeonManiaController;
import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.util.Position;

public class TimeTravellingPortal extends StaticEntity {

    public TimeTravellingPortal(Position position, int id) {
        super(position, "time_travelling_portal", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        return (e instanceof Player);
    }

    public boolean canStepOn(GameMap m, MovingEntity e, DungeonManiaController dmc) {
        dmc.rewind(29);
        return (e instanceof Player);
    }
}
