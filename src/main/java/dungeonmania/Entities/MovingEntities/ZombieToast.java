package dungeonmania.Entities.MovingEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RandomMove;
import dungeonmania.Entities.MovingEntities.MovingStrategies.RunawayMove;
import dungeonmania.States.State;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class ZombieToast extends MovingEntity {
    private MovingStrategy strategy;
    public ZombieToast(Position position, int id, double health, double attackDamage) {
        super(position, "zombie_toast", id, health, attackDamage);
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
}
