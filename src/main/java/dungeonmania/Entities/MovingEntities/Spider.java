package dungeonmania.Entities.MovingEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.SpiderMove;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Spider extends MovingEntity {
    private int spawnrate;
    private Position centre;
    // rotation 0 is clockwise
    // rotation 1 is anti clockwise
    private int rotation = 0;
    private MovingStrategy strategy;

    public int getSpawnrate() {
        return spawnrate;
    }

    public Spider(Position position, int id, double health, double attackDamage) {
        super(position, "spider", id, health, attackDamage);
        centre = position;
        this.strategy = new SpiderMove();
    }

    public Position getCentre() {
        return centre;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void move(GameMap map, Direction direction) {
        strategy = new SpiderMove();
        strategy.move(map, this);
    }

}
