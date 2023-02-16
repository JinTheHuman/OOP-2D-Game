package dungeonmania.Entities;

import java.io.Serializable;

import org.json.JSONObject;
import dungeonmania.Entities.BuildableEntities.*;
import dungeonmania.Entities.CollectableEntities.*;
import dungeonmania.Entities.MovingEntities.*;
import dungeonmania.Entities.MovingEntities.BribableEntities.Assassin;
import dungeonmania.Entities.MovingEntities.BribableEntities.Mercenary;
import dungeonmania.Entities.StaticEntities.*;

import dungeonmania.util.Position;

public abstract class Entity implements Serializable {
    private Position position;
    private String type;
    private int id;

    public Entity(Position position, String type, int id) {
        this.position = position;
        this.type = type;
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    /*
     * reads in type of entity, and creates an entity of that type
     */
    public static Entity createEntity(Position position, String type, int id, JSONObject entityObj, JSONObject config) {
        switch (type) {
            case "player":
                double attack = config.getInt("player_attack");
                double health = config.getInt("player_health");
                return new Player(position, id, health, attack);
            case "wall":
                return new Wall(position, id);
            case "exit":
                return new Exit(position, id);
            case "boulder":
                return new Boulder(position, id);
            case "switch":
                return new FloorSwitch(position, id);
            case "door":
                int doorKeyId = entityObj.getInt("key");
                return new Door(position, id, doorKeyId);
            case "portal":
                String colour = entityObj.get("colour").toString();
                return new Portal(position, id, colour);
            case "zombie_toast_spawner":
                return new ZombieToastSpawner(position, id);
            case "spider":
                double spider_health = config.getInt("spider_health");
                double spider_attack = config.getInt("spider_attack");
                return new Spider(position, id, spider_health, spider_attack);
            case "zombie_toast":
                double zombie_health = config.getInt("zombie_health");
                double zombie_attack = config.getInt("zombie_attack");
                return new ZombieToast(position, id, zombie_health, zombie_attack);
            case "mercenary":
                double mercenary_health = config.getInt("mercenary_health");
                double mercenary_attack = config.getInt("mercenary_attack");
                int bribeAmount = config.getInt("bribe_amount");
                int bribeRadius = config.getInt("bribe_radius");
                return new Mercenary(position, id, mercenary_health, mercenary_attack, bribeAmount, bribeRadius);
            case "treasure":
                return new Treasure(position, id);
            case "key":
                int keyId = entityObj.getInt("key");
                return new Key(position, id, keyId);
            case "invincibility_potion":
                int invincibility_duration = config.getInt("invincibility_potion_duration");
                return new InvincibilityPotion(position, id, invincibility_duration);
            case "invisibility_potion":
                int invisibility_duration = config.getInt("invisibility_potion_duration");
                return new InvisibilityPotion(position, id, invisibility_duration);
            case "wood":
                return new Wood(position, id);
            case "arrow":
                return new Arrows(position, id);
            case "bomb":
                int bomb_radius = config.getInt("bomb_radius");
                return new Bomb(position, id, bomb_radius);
            case "sword":
                int sword_durability = config.getInt("sword_durability");
                int sword_attack = config.getInt("sword_attack");
                return new Sword(position, id, sword_durability, sword_attack);
            case "bow":
                int bow_durability = config.getInt("bow_durability");
                return new Bow(id, bow_durability);
            case "shield":
                int shield_durability = config.getInt("shield_durability");
                int shield_defence = config.getInt("shield_defence");
                return new Shield(id, shield_durability, shield_defence);
            case "swamp_tile":
                int movementFactor = entityObj.getInt("movement_factor");
                return new SwampTile(position, id, movementFactor);
            case "sun_stone":
                return new SunStone(position, id);
            case "time_turner":
                return new TimeTurner(position, id);
            case "time_travelling_portal":
                return new TimeTravellingPortal(position, id);
            case "hydra":
                double hydra_health = config.getInt("hydra_health");
                double hydra_attack = config.getInt("hydra_attack");
                double healthIncreaseAmount = config.getInt("hydra_health_increase_amount");
                double healthIncreaseRate = config.getDouble("hydra_health_increase_rate");
                return new Hydra(position, id, hydra_health, hydra_attack, healthIncreaseAmount, healthIncreaseRate);
            case "assassin":
                double assassin_health = config.getInt("assassin_health");
                double assassin_attack = config.getInt("assassin_attack");
                int bribeAmount1 = config.getInt("bribe_amount");
                int bribeRadius1 = config.getInt("bribe_radius");
                double bribeFailRate = config.getDouble("assassin_bribe_fail_rate");
                int reconRadius = config.getInt("assassin_recon_radius");
                return new Assassin(position, id, assassin_health, assassin_attack, bribeAmount1, bribeRadius1, bribeFailRate, reconRadius);
            case "sceptre":
                int mind_control_duration = config.getInt("mind_control_duration");
                return new Sceptre(id, mind_control_duration);
            case "midnight_armour":
                int midnight_armour_attack = config.getInt("midnight_armour_attack");
                int midnight_armour_defence = config.getInt("midnight_armour_defence");
                return new MidnightArmour(id, midnight_armour_attack, midnight_armour_defence);
            case "jin":
                return new Jin(position, id);
            case "james":
                return new James(position, id);
            case "poo":
                return new Poo(position, id);
            default:
                return null;
        }
    }
}