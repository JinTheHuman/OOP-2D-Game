package dungeonmania.Entities.MovingEntities.MovingStrategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class RunawayMove implements MovingStrategy, Serializable {
    public void move(GameMap map, MovingEntity e) {
        Player p = map.getPlayer();
        Position playerPos = p.getPosition();
        Position pos = e.getPosition();

        for (Entity entity : map.getEntitiesAtPosition(pos)) {
            if (entity instanceof SwampTile) {
                SwampTile s = (SwampTile) entity;
                if (!s.canMoveOff(e)) {
                    return;
                }
            }
        }

        List<Direction> possibleMoves = new ArrayList<Direction>();
        for (Direction d : Direction.values()) {
            e.setDirection(d);
            if (map.canStepInto(e, pos.translateBy(d))) {
                possibleMoves.add(d);
            }
        }

        if (possibleMoves.size() != 0) {
            Direction bestMove = possibleMoves.get(0);

            for (Direction d : possibleMoves) {
                if (e.getDistanceBetween(playerPos, pos.translateBy(d)) > e.getDistanceBetween(playerPos,
                        pos.translateBy(bestMove))) {
                    bestMove = d;
                }
            }
            e.setDirection(bestMove);
            e.setPosition(pos.translateBy(bestMove));
        }
    }
}
