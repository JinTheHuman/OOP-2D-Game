package dungeonmania.Entities.BuildableEntities;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import java.util.Collections;

import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;

public class BuildableEntityFactory {
    public static Map<BuildableEntity, Map<String, Integer>> getBuildableEntity(int id, String buildable, List<Entity> inventory, JSONObject config, GameMap gameMap) throws IllegalArgumentException, InvalidActionException{
        if (buildable.equals("bow")) {

            Map<String, Integer> response = Bow.isBuildable(inventory);
            if (response != null) {
                int bow_durability = config.getInt("bow_durability");
                Bow bow = new Bow(id, bow_durability);

                return Collections.singletonMap(bow, response);
            }
            else throw new InvalidActionException("Player does not have sufficient items to craft bow");
        }
        else if (buildable.equals("shield")) {

            Map<String, Integer> response = Shield.isBuildable(inventory);
            if (response != null) {
                int shield_durability = config.getInt("shield_durability");
                int shield_defence = config.getInt("shield_defence");
                Shield shield = new Shield(id, shield_durability, shield_defence);

                return Collections.singletonMap(shield, response);
            }

            else throw new InvalidActionException("Player does not have sufficient items to craft shield");
        }
        else if (buildable.equals("sceptre")) {

            Map<String, Integer> response = Sceptre.isBuildable(inventory);
            if (response != null) {
                int mind_control_duration = config.getInt("mind_control_duration");

                Sceptre sceptre = new Sceptre(id, mind_control_duration);

                return Collections.singletonMap(sceptre, response);
            }

            else throw new InvalidActionException("Player does not have sufficient items to craft sceptre");
        }
        else if (buildable.equals("midnight_armour")) {

            Map<String, Integer> response = MidnightArmour.isBuildable(inventory, gameMap);
            if (response != null) {

                int midnight_armour_attack = config.getInt("midnight_armour_attack");
                int midnight_armour_defence = config.getInt("midnight_armour_defence");

                MidnightArmour midnightArmour = new MidnightArmour(id, midnight_armour_attack, midnight_armour_defence);

                return Collections.singletonMap(midnightArmour, response);
            }

            else throw new InvalidActionException("Player does not have sufficient items to craft midnight armour");
        }
        else throw new IllegalArgumentException("Invalid buildable string");
    }
}
