package dungeonmania.Entities.BuildableEntities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Weapon;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.BribableEntities.BribableEntity;

public class Sceptre extends BuildableEntity implements Weapon {
    private int mind_control_duration;
    private Map<BribableEntity, Integer> targets = new HashMap<>();

    public Sceptre(int id, int mind_control_duration) {
        super("sceptre", id, Integer.MAX_VALUE);
        this.mind_control_duration = mind_control_duration;
    }

    // returns a map of resources used
    // if not buildable, returns null
    public static Map<String, Integer> isBuildable(List<Entity> inventory) {
        int woodAmount = 0;
        int arrowAmount = 0;
        int treasureAmount = 0;
        int keyAmount = 0;
        int sunstoneAmount = 0;

        for (Entity entity : inventory) {
            if (entity.getType().equals("wood"))
                woodAmount++;
            else if (entity.getType().equals("arrow"))
                arrowAmount++;
            else if (entity.getType().equals("treasure"))
                treasureAmount++;
            else if (entity.getType().equals("key"))
                keyAmount++;
            else if (entity.getType().equals("sun_stone"))
                sunstoneAmount++;
        }
        if ((woodAmount < 1 && arrowAmount < 2) || (keyAmount < 1 && treasureAmount < 1 && sunstoneAmount < 2)
                || (sunstoneAmount < 1))
            return null;
        else {
            Map<String, Integer> resourcesRequired = new HashMap<String, Integer>();

            if (woodAmount >= 1)
                resourcesRequired.put("wood", 1);
            else
                resourcesRequired.put("arrow", 2);

            if (sunstoneAmount < 2) {
                if (keyAmount >= 1)
                    resourcesRequired.put("key", 1);
                else
                    resourcesRequired.put("treasure", 1);
            }

            resourcesRequired.put("sun_stone", 1);

            return resourcesRequired;
        }
    }

    public void mindControl(BribableEntity b, Player player) {
        targets.put(b, 0);
        player.addToAllies(b);
        b.setHostile(false);
    }

    public void free(Player player) {
        for (Map.Entry<BribableEntity, Integer> entry : targets.entrySet()) {
            if (entry.getValue() == mind_control_duration) {
                targets.remove(entry.getKey());
                player.removeFromAllies(entry.getKey());
                entry.getKey().setHostile(false);
            } else {
                targets.put(entry.getKey(), entry.getValue() + 1);
            }
        }
    }

}
