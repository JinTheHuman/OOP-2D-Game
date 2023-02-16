package dungeonmania.Entities.StaticEntities;


import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class Exit extends StaticEntity {

    public Exit(Position position, int id) {
        super(position, "exit", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        return true;
    }

}
