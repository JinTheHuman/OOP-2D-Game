package dungeonmania.Entities.MovingEntities.MovingStrategies;

import java.io.Serializable;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;

public class AllyMove implements MovingStrategy, Serializable {
    public void move(GameMap map, MovingEntity e) {
        Player p = map.getPlayer();
        e.setDirection(p.getDirection());
        e.setPosition(p.getPrevPosition());
    }
}
