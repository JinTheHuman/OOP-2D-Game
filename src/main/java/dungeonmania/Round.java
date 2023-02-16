package dungeonmania;

import java.io.Serializable;
import java.util.List;

import dungeonmania.Entities.*;

public class Round implements Serializable {
    private double deltaPlayerHealth;
    private double deltaEnemyHealth;
    private List<Entity> weaponryUsed;

    public Round(double deltaPlayerHealth, double deltaEnemyHealth, List<Entity> weaponryUsed) {
        this.deltaPlayerHealth = deltaPlayerHealth;
        this.deltaEnemyHealth = deltaEnemyHealth;
        this.weaponryUsed = weaponryUsed;
    }

    public double getDeltaPlayerHealth() {
        return deltaPlayerHealth;
    }

    public double getDeltaEnemyHealth() {
        return deltaEnemyHealth;
    }

    public List<Entity> getWeaponryUsed() {
        return weaponryUsed;
    }

    public void addWeaponryUsed(Entity weapon) {
        weaponryUsed.add(weapon);
    }
}
