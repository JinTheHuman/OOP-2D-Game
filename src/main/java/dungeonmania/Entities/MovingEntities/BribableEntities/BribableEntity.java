package dungeonmania.Entities.MovingEntities.BribableEntities;

import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.util.Position;

public abstract class BribableEntity extends MovingEntity{
    private int bribeAmount;
    private int bribeRadius;
    private boolean hostile = true;

    public BribableEntity(Position position, String type, int id, double health, double attackDamage, int bribeAmount, int bribeRadius) {
        super(position, type, id, health, attackDamage);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public boolean isWithinBribeRadius(Player player) {
        return getDistanceBetween(this.getPosition(), player.getPosition()) <= getBribeRadius();
    }

    public void setHostile(boolean hostile) {
        this.hostile = hostile;
    }

    public boolean isHostile() {
        return hostile;
    }

    public abstract boolean bribe(Player player);
    
}
