package dungeonmania.States;

import java.io.Serializable;
import java.util.Queue;

import dungeonmania.Entities.CollectableEntities.Potion;
import dungeonmania.Entities.MovingEntities.Player;

public class InvisibleState implements State, Serializable {
    private int duration;
    private Player player;

    public InvisibleState(Player player) {
        this.player = player;
        this.duration = 0;
    }

    public void usePotion(Potion potion) {
        if (duration == 0) {
            duration = potion.getDuration();
        } else {
            player.addPotionsQueue(potion);
        }
    }

    public void tick() {
        this.duration -= 1;
        if (duration == 0) {
            Queue<Potion> potionsQueue = player.getPotionsQueue();
            if (potionsQueue.size() == 0) {
                player.setCurrState(player.getNormalState());
            } else {
                Potion nextPotion = potionsQueue.remove();
                player.setCurrState(nextPotion);
            }
        }
    }
}
