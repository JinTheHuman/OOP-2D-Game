package dungeonmania.Entities.MovingEntities.BribableEntities;

import java.util.Random;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.MovingStrategies.AllyMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.HostileMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RandomMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RunawayMove;
import dungeonmania.States.State;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Assassin extends BribableEntity {
    private MovingStrategy strategy;
    private double bribeFailRate;
    private int reconRadius;

    public Assassin(Position position, int id, double health, double attack, int bribeAmount, int bribeRadius,
            double bribeFailRate, int reconRadius) {
        super(position, "assassin", id, health, attack, bribeAmount, bribeRadius);
        this.bribeFailRate = bribeFailRate;
        this.reconRadius = reconRadius;
    }

    public boolean withinReconRadius(Player player) {
        return getDistanceBetween(this.getPosition(), player.getPosition()) <= reconRadius;
    }

    public void move(GameMap map, Direction direction) {
        Player p = map.getPlayer();
        State currState = p.getCurrState();
        if (currState == p.getInvincibleState()) {
            if (isHostile()) {
                strategy = new RunawayMove();
                strategy.move(map, this);
            } else {
                if (getDistanceBetween(p.getPosition(), this.getPosition()) >= 2) {
                    strategy = new HostileMove();
                    strategy.move(map, this);
                } else {
                    strategy = new AllyMove();
                    strategy.move(map, this);
                }
            }
        } else if (currState == p.getInvisibleState() && !withinReconRadius(p)) {
            strategy = new RandomMove();
            strategy.move(map, this);
        } else {
            if (isHostile()) {
                strategy = new HostileMove();
                strategy.move(map, this);
            } else {
                if (getDistanceBetween(p.getPosition(), this.getPosition()) >= 2) {
                    strategy = new HostileMove();
                    strategy.move(map, this);
                } else {
                    strategy = new AllyMove();
                    strategy.move(map, this);
                }
            }
        }
    }

    public boolean bribe(Player player) {
        // if there is enough coins in players inventory to bribe
        // useItem, add mercenary to allies and change hostile to false
        Random rand = new Random();
        double x = rand.nextDouble();

        if (player.getNumberofItems("treasure") >= getBribeAmount()) {
            player.useItem("treasure", getBribeAmount());
            if (x <= bribeFailRate) {
                return false;
            } else {
                player.addToAllies(this);
                setHostile(false);
                return true;
            }
        }
        return false;
    }

}
