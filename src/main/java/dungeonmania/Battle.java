package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.Weapon;
import dungeonmania.Entities.BuildableEntities.Bow;
import dungeonmania.Entities.BuildableEntities.MidnightArmour;
import dungeonmania.Entities.BuildableEntities.Shield;
import dungeonmania.Entities.CollectableEntities.Sword;
import dungeonmania.Entities.MovingEntities.Hydra;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;

public class Battle implements Serializable {
    private Player player;
    private MovingEntity enemy;
    private double initialPlayerHealth;
    private double initialEnemyHealth;
    private List<Round> rounds;

    public Battle(Player player, MovingEntity enemy, double initialPlayerHealth, double initialEnemyHealth) {
        this.player = player;
        this.enemy = enemy;
        this.initialPlayerHealth = initialPlayerHealth;
        this.initialEnemyHealth = initialEnemyHealth;
        this.rounds = new ArrayList<Round>();
    }

    /**
     * 
     * @return true if enemy died otherwise false
     */
    public boolean runBattle() {

        int attackFactor = 1;
        int extraDamage = 0;
        int extraDefence = 0;

        List<Entity> weapons = player.getInventory().stream().filter(item -> (item instanceof Weapon))
                .collect(Collectors.toList());

        for (Entity w : weapons) {
            if (w instanceof Bow) {
                attackFactor = 2;
            } else if (w instanceof Sword) {
                Sword s = (Sword) w;
                extraDamage += s.getAttack();
            } else if (w instanceof Shield) {
                Shield s = (Shield) w;
                extraDefence += s.getDefence();
            } else if (w instanceof MidnightArmour) {
                MidnightArmour s = (MidnightArmour) w;
                extraDefence += s.getDefence();
                extraDamage += s.getAttack();
            }
            Weapon weapon = (Weapon) w;
            weapon.changeDurability();
            if (weapon.getDurability() <= 0) {
                player.useItem(w.getType(), 1);
            }
        }

        for (MovingEntity m : player.getAllies()) {
            extraDamage += m.getAttackDamage();
        }

        if (player.getCurrState() == player.getInvincibleState()) {
            Entity potion = player.getCurrPotion();
            weapons.add(potion);
            rounds.add(new Round(0, -enemy.getHealth(), weapons));
            return true;
        }

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            Double playerDmgTaken = (enemy.getAttackDamage() - extraDefence) / 10;
            Double enemyDmgTaken = (attackFactor * (player.getAttackDamage() + extraDamage) / 5);

            if (enemy instanceof Hydra) {
                Hydra h = (Hydra) enemy;
                if (h.battleOutcome()) {
                    enemyDmgTaken = -(h.getHealthIncreaseAmount());
                }
            }

            player.takeDamage(playerDmgTaken);
            enemy.takeDamage(enemyDmgTaken);

            rounds.add(new Round(-playerDmgTaken, -enemyDmgTaken, weapons));
        }

        if (enemy.getHealth() > 0) {
            return false;
        }
        return true;
    }

    public MovingEntity getPlayer() {
        return player;
    }

    public MovingEntity getEnemy() {
        return enemy;
    }

    public double getInitialPlayerHealth() {
        return initialPlayerHealth;
    }

    public double getInitialEnemyHealth() {
        return initialEnemyHealth;
    }

    public List<Round> getRounds() {
        return rounds;
    }

}
