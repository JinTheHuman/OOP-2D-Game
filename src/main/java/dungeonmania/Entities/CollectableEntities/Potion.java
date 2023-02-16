package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.States.State;

public interface Potion {

    public int getDuration();

    public State getState(Player player);

    public void setPotion(Player player);
}
