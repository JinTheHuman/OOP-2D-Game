package dungeonmania.Entities.BuildableEntities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Entities.*;

public class Bow extends BuildableEntity implements Weapon {
    public Bow(int id, int durability) {
        super("bow", id, durability);
    }

    // returns a map of resources used
    // if not buildable, returns null
    public static Map<String, Integer> isBuildable(List<Entity> inventory) {
        int woodAmount = 0;
        int arrowAmount = 0;
        for (Entity entity : inventory) {
            if (entity.getType().equals("wood")) woodAmount++;
            else if (entity.getType().equals("arrow")) arrowAmount++;
        }
        if (!(arrowAmount >= 3 && woodAmount >= 1)) return null;
        // else if there are sufficient resources
        else {
            Map<String, Integer> resourcesRequired = new HashMap<String, Integer>();
            resourcesRequired.put("wood", 1);
            resourcesRequired.put("arrow", 3);
            return resourcesRequired;
        }
    }
}