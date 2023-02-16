package dungeonmania.Entities.MovingEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public abstract class MovingEntity extends Entity {
    private double health;
    private double attackDamage;
    private Direction direction;

    public MovingEntity(Position position, String type, int id, double health, double attackDamage) {
        super(position, type, id);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void takeDamage(Double dmg) {
        this.setHealth(this.getHealth() - dmg);
    }

    public boolean moveHelper(GameMap map, Direction direction, Position position) {
        Position pos = position.translateBy(direction);
        if (map.canStepInto(this, pos)) {
            setPosition(pos);
            return true;
        }
        return false;
    }

    public abstract void move(GameMap map, Direction direction);

    public double getDistanceBetween(Position p1, Position p2) {
        return Math.sqrt(Math.pow((p2.getX() - p1.getX()), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }
}