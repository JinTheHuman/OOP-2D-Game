package dungeonmania.Entities.StaticEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.util.Position;

public class Wall extends StaticEntity {

    public Wall(Position position, int id) {
        super(position, "wall", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        if (e instanceof Spider) {
            return true;
        }
        
        return false;
    }

}
