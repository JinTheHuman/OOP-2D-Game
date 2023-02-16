package dungeonmania.States;

import dungeonmania.Entities.CollectableEntities.Potion;

public interface State {
    public void usePotion(Potion potion);
    public void tick();
}
