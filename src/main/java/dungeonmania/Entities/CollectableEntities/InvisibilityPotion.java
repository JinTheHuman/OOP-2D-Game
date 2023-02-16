package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.States.State;
import dungeonmania.util.Position;

public class InvisibilityPotion extends CollectableEntity implements Potion {
    private int duration;

    public InvisibilityPotion(Position position, int id, int duration) {
        super(position, "invisibility_potion", id);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public State getState(Player player) {
        return player.getInvisibleState();
    }

    public void setPotion(Player player) {
        player.setCurrPotion(this);
    }
}
