package dungeonmania.Entities.MovingEntities.BribableEntities;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.MovingStrategies.AllyMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.HostileMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RandomMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RunawayMove;
import dungeonmania.States.State;

public class Mercenary extends BribableEntity {
    private MovingStrategy strategy;

    public Mercenary(Position position, int id, double health, double attackDamage, int bribeAmount, int bribeRadius) {
        super(position, "mercenary", id, health, attackDamage, bribeAmount, bribeRadius);
    }

    public boolean bribe(Player player) {
        // if there is enough coins in players inventory to bribe
        // useItem, add mercenary to allies and change hostile to false
        if (player.getNumberofItems("treasure") >= getBribeAmount()) {
            player.useItem("treasure", getBribeAmount());
            player.addToAllies(this);
            setHostile(false);
            return true;
        }

        return false;
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
        } else if (currState == p.getInvisibleState()) {
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
}
