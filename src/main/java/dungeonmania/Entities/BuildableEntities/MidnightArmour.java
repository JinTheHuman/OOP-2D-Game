package dungeonmania.Entities.BuildableEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Weapon;

public class MidnightArmour extends BuildableEntity implements Weapon {
    private int midnight_armour_attack;
    private int midnight_armour_defence;

    public MidnightArmour(int id, int midnight_armour_attack, int midnight_armour_defence) {
        super("midnight_armour", id, Integer.MAX_VALUE);
        this.midnight_armour_attack = midnight_armour_attack;
        this.midnight_armour_defence = midnight_armour_defence;
    }

    public int getAttack() {
        return midnight_armour_attack;
    }

    public int getDefence() {
        return midnight_armour_defence;
    }

    // returns a map of resources used
    // if not buildable, returns null
    public static Map<String, Integer> isBuildable(List<Entity> inventory, GameMap gameMap) {
        // check if there are any zombies in the dungeon
        List<Entity> zombieList = gameMap.getEntities("zombie_toast");
        if (zombieList.size() > 0) return null;

        int swordAmount = 0;
        int sunstoneAmount = 0;

        for (Entity entity : inventory) {
            if (entity.getType().equals("sword"))
                swordAmount++;
            else if (entity.getType().equals("sun_stone"))
                sunstoneAmount++;
        }
        if ((swordAmount < 1 || sunstoneAmount < 1)) return null;
        else {
            Map<String, Integer> resourcesRequired = new HashMap<String, Integer>();

            resourcesRequired.put("sword", 1);
            resourcesRequired.put("sun_stone", 1);

            return resourcesRequired;
        }
    }
}
