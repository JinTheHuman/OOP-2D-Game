package dungeonmania.Entities.MovingEntities;

import java.util.Random;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RandomMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RunawayMove;
import dungeonmania.States.State;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity {
    private double healthIncreaseAmount;
    private double healthIncreaseRate;
    private MovingStrategy strategy;

    public Hydra(Position position, int id, double health, double attack, double healthIncreaseAmount, double healthIncreaseRate) {
        super(position, "hydra", id, health, attack);
        this.healthIncreaseAmount = healthIncreaseAmount;
        this.healthIncreaseRate = healthIncreaseRate;
    }

    public double getHealthIncreaseAmount() {
        return healthIncreaseAmount;
    }

    public void move(GameMap map, Direction direction) {
        Player p = map.getPlayer();
        State currState = p.getCurrState();
        if (currState == p.getInvincibleState()) {
            strategy = new RunawayMove();
            strategy.move(map, this);
        } else {
            strategy = new RandomMove();
            strategy.move(map, this);
        }
    }

    // return true if hydra will heal in a battle otherwise false
    public boolean battleOutcome() {
        Random rand = new Random();
        double x = rand.nextDouble();
        if (x <= healthIncreaseRate) {
            return true;
        }
        return false;
    }
}
