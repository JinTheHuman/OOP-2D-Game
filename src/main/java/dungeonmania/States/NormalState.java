package dungeonmania.States;

import java.io.Serializable;

import dungeonmania.Entities.CollectableEntities.Potion;
import dungeonmania.Entities.MovingEntities.Player;

public class NormalState implements State, Serializable {
    private Player player;

    public NormalState(Player player) {
        this.player = player;
    }

    public void usePotion(Potion potion) {
        player.setCurrState(potion);
    }

    public void tick() {
    }
}
