package dungeonmania.Entities.CollectableEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.util.Position;

public class Bomb extends CollectableEntity {
    private int bombRadius;
    private boolean planted = false;

    public Bomb(Position position, int id, int bombRadius) {
        super(position, "bomb", id);
        this.bombRadius = bombRadius;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    @Override
    public boolean collect(List<Entity> inventory) {
        if (!planted) {
            return super.collect(inventory);
        }
        return false;
    }

    public void plantBomb(Player player, GameMap gameMap) {
        planted = true;
        this.setPosition(player.getPosition());
        gameMap.addEntity(this);
        List<Entity> allEntities = gameMap.getEntityList();
        List<Entity> adjacentEntities = allEntities.stream()
                .filter(ent -> Position.isAdjacent(ent.getPosition(), player.getPosition()))
                .collect(Collectors.toList());
        for (Entity adjE : adjacentEntities) {
            if (adjE instanceof FloorSwitch) {
                FloorSwitch floorSwitch = (FloorSwitch) adjE;
                floorSwitch.addBomb(this, gameMap);
            }
        }
    }

    public void explode(GameMap gameMap) {
        List<Position> entitiesInRange = getPosition().getSquarePositions(bombRadius);
        List<Entity> toBeDestroyed = new ArrayList<Entity>();
        for (Position p : entitiesInRange) {
            toBeDestroyed.addAll(gameMap.getEntitiesAtPosition(p));
        }
        toBeDestroyed.stream().forEach(e -> gameMap.removeEntity(e));
    }
}
