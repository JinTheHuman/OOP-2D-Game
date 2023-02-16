package dungeonmania.Entities.BuildableEntities;

import dungeonmania.Entities.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shield extends BuildableEntity implements Weapon {
    int defence;

    public Shield(int id, int durability, int defence) {
        super("shield", id, durability);
        this.defence = defence;
    }

    // returns a map of resources used
    // if not buildable, returns null
    public static Map<String, Integer> isBuildable(List<Entity> inventory) {
        int woodAmount = 0;
        int treasureAmount = 0;
        int keyAmount = 0;
        int replacementAmount = 0;
        for (Entity entity : inventory) {
            if (entity.getType().equals("wood"))
                woodAmount++;
            else if (entity.getType().equals("treasure"))
                treasureAmount++;
            else if (entity.getType().equals("key"))
                keyAmount++;
            else if (entity.getType().equals("sun_stone"))
                replacementAmount++;
        }

        if (!(woodAmount >= 2 && (treasureAmount >= 1 || keyAmount >= 1 || replacementAmount >= 1)))
            return null;
        // else if there are sufficient resources
        else {
            Map<String, Integer> resourcesRequired = new HashMap<String, Integer>();
            resourcesRequired.put("wood", 2);

            if (replacementAmount == 0) {
                if (treasureAmount >= 1)
                    resourcesRequired.put("treasure", 1);
                else
                    resourcesRequired.put("key", 1);
            }
            return resourcesRequired;
        }
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    };
}
