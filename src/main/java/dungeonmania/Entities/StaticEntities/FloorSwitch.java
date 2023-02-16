package dungeonmania.Entities.StaticEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.GameMap;
import dungeonmania.Entities.CollectableEntities.Bomb;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.util.Position;

public class FloorSwitch extends StaticEntity {
    private List<Bomb> plantedBombs = new ArrayList<>();
    private boolean isActive = false;

    public FloorSwitch(Position position, int id) {
        super(position, "switch", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        return true;
    }

    public void trigger(GameMap gameMap) {
        if (isActive) {
            isActive = false;
        } else {
            isActive = true;
            activateBombs(gameMap);
        }
    }

    public void addBomb(Bomb bomb, GameMap gameMap) {
        if (!isActive) {
            plantedBombs.add(bomb);
        } else {
            bomb.explode(gameMap);
        }
    }

    public void removeBomb(Bomb bomb) {
        plantedBombs.remove(bomb);
    }

    public void activateBombs(GameMap gameMap) {
        for (Bomb bomb : plantedBombs) {
            bomb.explode(gameMap);
        }
        plantedBombs.clear();
    }
}
