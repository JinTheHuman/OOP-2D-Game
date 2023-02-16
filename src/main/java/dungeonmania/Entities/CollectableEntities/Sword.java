package dungeonmania.Entities.CollectableEntities;

import dungeonmania.Entities.*;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements Weapon {
    private int durability;
    private int attack;

    public Sword(Position position, int id, int durability, int attack) {
        super(position, "sword", id);
        this.durability = durability;
        this.attack = attack;
    }

    public int getDurability() {
        return durability;
    }

    public void changeDurability() {
        durability -= 1;
    }

    public int getAttack() {
        return attack;
    }
}
