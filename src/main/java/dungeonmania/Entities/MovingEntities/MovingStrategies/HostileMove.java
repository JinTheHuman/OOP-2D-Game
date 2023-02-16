package dungeonmania.Entities.MovingEntities.MovingStrategies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.Door;
import dungeonmania.Entities.StaticEntities.Exit;
import dungeonmania.Entities.StaticEntities.FloorSwitch;
import dungeonmania.Entities.StaticEntities.Portal;
import dungeonmania.Entities.StaticEntities.StaticEntity;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class HostileMove implements MovingStrategy, Serializable {
    // public void oldmove(GameMap map, MovingEntity e) {
    // Player player = map.getPlayer();
    // Position playerPos = player.getPosition();
    // Position pos = e.getPosition();

    // List<Direction> possibleMoves = new ArrayList<Direction>();
    // for (Direction d : Direction.values()) {
    // e.setDirection(d);
    // if (map.canStepInto(e, pos.translateBy(d))) {
    // possibleMoves.add(d);
    // }
    // }

    // if (possibleMoves.size() != 0) {
    // Direction bestMove = possibleMoves.get(0);

    // for (Direction d : possibleMoves) {
    // if (e.getDistanceBetween(playerPos, pos.translateBy(d)) <
    // e.getDistanceBetween(playerPos,
    // pos.translateBy(bestMove))) {
    // bestMove = d;
    // }
    // }
    // e.setDirection(bestMove);
    // e.setPosition(pos.translateBy(bestMove));
    // }
    // }

    public void move(GameMap map, MovingEntity e) {
        Map<Position, Double> dist = new HashMap<Position, Double>();
        Map<Position, Position> prev = new HashMap<Position, Position>();
        Queue<Position> queue = new LinkedList<Position>();
        // Populate maps with all positions
        for (int y = -10; y < 10; y++) {
            for (int x = -10; x < 10; x++) {
                Position newPos = new Position(x, y);
                dist.put(newPos, Double.POSITIVE_INFINITY);
                prev.put(newPos, null);
                queue.add(newPos);
            }
        }
        dist.put(e.getPosition(), 0.0);

        while (!queue.isEmpty()) {
            Position u = getNextSmallestDist(queue, dist);

            queue.remove(u);
            for (Position p : getNeighbours(dist, u, map)) {
                if (dist.get(u) + cost(map, u, p) < dist.get(p)) {
                    dist.put(p, dist.get(u) + cost(map, u, p));
                    prev.put(p, u);
                }
            }
        }
        Position curr = prev.get(map.getPlayer().getPosition());
        if (curr.equals(e.getPosition())) {
            for (Entity entity : map.getEntitiesAtPosition(e.getPosition())) {
                if (entity instanceof SwampTile) {
                    SwampTile s = (SwampTile) entity;
                    if (!s.notOn(e)) {
                        if (!s.canMoveOff(e)) {
                            return;
                        }
                    } else return;
                }
            }
            e.setDirection(getDirection(e.getPosition(), map.getPlayer().getPosition()));
            e.setPosition(map.getPlayer().getPosition());
            return;
        }
        while (!prev.get(curr).equals(e.getPosition())) {
            curr = prev.get(curr);
        }

        // If curr is a portal we have to set the pos of the merc to the initial portal
        // not the destination portal
        if (!Position.isAdjacent(e.getPosition(), curr)) {
            // Find other portal
            Entity portal = map.getEntitiesAtPosition(curr).stream().filter(x -> (x instanceof Portal)).findFirst()
                    .orElse(null);
            if (portal != null) {
                Portal p = (Portal) portal;
                for (Entity portalEntity : map.getEntities("portal")) {
                    Portal Otherportal = (Portal) portalEntity;
                    if (Otherportal.getColour().equals(p.getColour()) && !p.equals(Otherportal)) {
                        e.setDirection(getDirection(e.getPosition(), Otherportal.getPosition()));
                        e.setPosition(Otherportal.getPosition());
                    }
                }
            }
        } else {
            for (Entity entity : map.getEntitiesAtPosition(e.getPosition())) {
                if (entity instanceof SwampTile) {
                    SwampTile s = (SwampTile) entity;
                    if (!s.notOn(e)) {
                        if (!s.canMoveOff(e)) {
                            return;
                        }
                    } else return;
                }
            }
            e.setDirection(getDirection(e.getPosition(), curr));
            e.setPosition(curr);
        }
    }

    private double cost(GameMap m, Position u, Position p) {
        // Get all static entities
        List<Position> poss = m.getStaticEntities().stream().map(Entity::getPosition).collect(Collectors.toList());
        // If target Position is a static entity
        if (poss.contains(p)) {
            // get the static entity at the position
            Entity e = m.getStaticEntities().stream().filter((x) -> (x.getPosition().equals(p))).findFirst()
                    .orElse(null);
            // Check the static entity is accessible
            if (e instanceof Exit || e instanceof FloorSwitch || e instanceof Portal || e instanceof SwampTile) {
                StaticEntity s = (StaticEntity) e;
                return s.getWeight();
            } else if (e instanceof Door) {
                Door d = (Door) e;
                if (!d.isLocked()) {
                    return d.getWeight();
                }
            }
            // return infinity if static entity cannot be stepped on
            return Double.POSITIVE_INFINITY;
        }
        return 1;
    }

    private Position getNextSmallestDist(Queue<Position> q, Map<Position, Double> m) {
        Entry<Position, Double> min = null;
        for (Entry<Position, Double> entry : m.entrySet()) {
            if ((min == null || min.getValue() > entry.getValue()) && q.contains(entry.getKey())) {
                min = entry;
            }
        }
        return min.getKey();
    }

    private List<Position> getNeighbours(Map<Position, Double> m, Position u, GameMap map) {
        List<Position> ret = new ArrayList<Position>();
        for (Direction d : Direction.values()) {
            Position adjPos = u.translateBy(d);
            if (m.containsKey(adjPos)) {
                Entity e = map.getEntitiesAtPosition(adjPos).stream().filter(x -> (x instanceof Portal)).findFirst()
                        .orElse(null);
                if (e != null) {
                    Portal p = (Portal) e;
                    for (Entity portalEntity : map.getEntities("portal")) {
                        Portal portal = (Portal) portalEntity;
                        if (portal.getColour().equals(p.getColour()) && !p.equals(portal)) {
                            ret.add(portal.getPosition());
                            break;
                        }
                    }
                } else {
                    ret.add(adjPos);
                }
            }
        }
        return ret;
    }

    private Direction getDirection(Position oldPos, Position newPos) {
        for (Direction d : Direction.values()) {
            if (oldPos.translateBy(d).equals(newPos)) {
                return d;
            }
        }
        return null;
    }
}
