package dungeonmania.Entities.MovingEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingStrategies.JinMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Jin extends MovingEntity{
    private MovingStrategy strategy;

    public Jin(Position position, int id) {
        super(position, "jin", id, 10000, 10000);
    }
    
    public void move(GameMap map, Direction direction) {
        strategy = new JinMove();
        strategy.move(map, this);
    }

    // shoot laser beam killing everything he looks at
    public void shootLaser(GameMap map) {
        if (getDirection() == Direction.UP) {
            for (Entity e : map.getMoveableEntities()) {
                if (e.getPosition().getX() == getPosition().getX() && e.getPosition().getY() <= getPosition().getY()) {
                    if (e != this) {
                        map.removeEntity(e);
                    }
                }
            }
        }
        else if (getDirection() == Direction.DOWN) {
            for (Entity e : map.getMoveableEntities()) {
                if (e.getPosition().getX() == getPosition().getX() && e.getPosition().getY() >= getPosition().getY()) {
                    if (e != this) {
                        map.removeEntity(e);
                    }
                }
            }
        }
        else if (getDirection() == Direction.LEFT) {
            for (Entity e : map.getMoveableEntities()) {
                if (e.getPosition().getX() <= getPosition().getX() && e.getPosition().getY() == getPosition().getY()) {
                    if (e != this) {
                        map.removeEntity(e);
                    }
                }
            }
        }
        else if (getDirection() == Direction.RIGHT) {
            for (Entity e : map.getMoveableEntities()) {
                if (e.getPosition().getX() >= getPosition().getX() && e.getPosition().getY() == getPosition().getY()) {
                    if (e != this) {
                        map.removeEntity(e);
                    }
                }
            }
        }
    }   
}
