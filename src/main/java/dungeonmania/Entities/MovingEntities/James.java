package dungeonmania.Entities.MovingEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RandomMove;
import dungeonmania.Entities.StaticEntities.Poo;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class James extends MovingEntity{
    private MovingStrategy strategy;
    public James(Position position, int id) {
        super(position, "james", id, 1, 0);
    }

    @Override
    public void move(GameMap map, Direction direction) {
        strategy = new RandomMove();
        strategy.move(map, this);    
    }
    
    public void explode(GameMap map, int id) {
        for (List<Entity> l : getEverythingAround(map, getPosition())) {
            for (Entity e : l) {
                map.removeEntity(e);
            }
        }
        map.removeEntity(this);
        map.addEntity(new Poo(getPosition(), id));
    }

    private List<List<Entity>> getEverythingAround(GameMap map, Position position) {
        int x = position.getX();
        int y = position.getY();

        List<List<Entity>> around = new ArrayList<>();
        around.add(map.getEntitiesAtPosition(new Position(x-1, y)));
        around.add(map.getEntitiesAtPosition(new Position(x-1, y-1)));
        around.add(map.getEntitiesAtPosition(new Position(x-1, y+1)));
        around.add(map.getEntitiesAtPosition(new Position(x, y-1)));
        around.add(map.getEntitiesAtPosition(new Position(x, y+1)));
        around.add(map.getEntitiesAtPosition(new Position(x+1, y)));
        around.add(map.getEntitiesAtPosition(new Position(x+1, y-1)));
        around.add(map.getEntitiesAtPosition(new Position(x+1, y+1)));

        return around;
    }

}
