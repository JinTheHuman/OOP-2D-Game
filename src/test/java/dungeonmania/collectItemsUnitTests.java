package dungeonmania;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.Arrows;
import dungeonmania.Entities.CollectableEntities.Bomb;
import dungeonmania.Entities.CollectableEntities.InvincibilityPotion;
import dungeonmania.Entities.CollectableEntities.InvisibilityPotion;
import dungeonmania.Entities.CollectableEntities.Key;
import dungeonmania.Entities.CollectableEntities.Sword;
import dungeonmania.Entities.CollectableEntities.Treasure;
import dungeonmania.Entities.CollectableEntities.Wood;
import dungeonmania.Entities.MovingEntities.Player;

public class collectItemsUnitTests {
    @Test
    @DisplayName("Test treasure can be collected")
    public void testCollectTreasure() {
        Player p = new Player(null, 1, 1, 0);
        Treasure treasure = new Treasure(null, 1);
        List<Entity> inventory = p.getInventory();
        treasure.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test key can be collected")
    public void testCollectKey() {
        Player p = new Player(null, 1, 1, 0);
        Key key = new Key(null, 1, 1);
        List<Entity> inventory = p.getInventory();
        key.collect(inventory);
        assert (inventory.size() == 1);

    }

    @Test
    @DisplayName("Test a second key can not be collected")
    public void testCollectMultipleKeys() {
        Player p = new Player(null, 1, 1, 0);
        Key key = new Key(null, 1, 1);
        Key secondKey = new Key(null, 2, 2);
        List<Entity> inventory = p.getInventory();
        key.collect(inventory);
        assert (inventory.size() == 1);

        secondKey.collect(inventory);
        assert (inventory.size() == 1);
        assertFalse(inventory.contains(secondKey));
    }

    @Test
    @DisplayName("Test invincibility potion can be collected")
    public void testCollectInvincibilityPotion() {
        Player p = new Player(null, 1, 1, 0);
        InvincibilityPotion invincibilityPotion = new InvincibilityPotion(null, 1, 1);
        List<Entity> inventory = p.getInventory();
        invincibilityPotion.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test invisibility potion can be collected")
    public void testCollectInvisibilityPotion() {
        Player p = new Player(null, 1, 1, 0);
        InvisibilityPotion invisibilityPotion = new InvisibilityPotion(null, 1, 1);
        List<Entity> inventory = p.getInventory();
        invisibilityPotion.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test wood can be collected")
    public void testCollectWood() {
        Player p = new Player(null, 1, 1, 0);
        Wood wood = new Wood(null, 1);
        List<Entity> inventory = p.getInventory();
        wood.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test arrows can be collected")
    public void testCollectArrows() {
        Player p = new Player(null, 1, 1, 0);
        Arrows arrows = new Arrows(null, 1);
        List<Entity> inventory = p.getInventory();
        arrows.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test bomb can be collected")
    public void testCollectBomb() {
        Player p = new Player(null, 1, 1, 0);
        Bomb bomb = new Bomb(null, 1, 2);
        List<Entity> inventory = p.getInventory();
        bomb.collect(inventory);
        assert (inventory.size() == 1);
    }

    @Test
    @DisplayName("Test sword can be collected")
    public void testCollectSword() {
        Player p = new Player(null, 1, 1, 0);
        Sword sword = new Sword(null, 1, 2, 1);
        List<Entity> inventory = p.getInventory();
        sword.collect(inventory);
        assert (inventory.size() == 1);
    }

}
