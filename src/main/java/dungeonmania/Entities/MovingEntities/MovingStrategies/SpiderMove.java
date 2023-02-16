package dungeonmania.Entities.MovingEntities.MovingStrategies;


import java.io.Serializable;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SpiderMove implements MovingStrategy, Serializable {
    public void move(GameMap map, MovingEntity e) {
        Spider s = (Spider) e;
        int rotation = s.getRotation();
        Position centre = s.getCentre();
        Position pos = s.getPosition();

        for (Entity entity : map.getEntitiesAtPosition(pos)) {
            if (entity instanceof SwampTile) {
                SwampTile st = (SwampTile) entity;
                if (!st.canMoveOff(e)) {
                    return;
                }
            }
        }

        int x = centre.getX();
        int y = centre.getY();
        if (pos.equals(centre)) {
            e.moveHelper(map, Direction.UP, centre);
        }
        else if (pos.equals(new Position(x,y-1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.RIGHT, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.LEFT, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x+1,y-1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.DOWN, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.LEFT, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x+1,y))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.DOWN, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.UP, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x+1,y+1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.LEFT, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.UP, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x,y+1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.LEFT, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.RIGHT, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x-1,y+1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.UP, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.RIGHT, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x-1,y))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.UP, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.DOWN, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
        else if (pos.equals(new Position(x-1,y-1))) {
            if (rotation == 0) {
                if (e.moveHelper(map, Direction.RIGHT, pos)) {
                    return;
                }
                else {
                    s.setRotation(1);
                    move(map, e);
                }
            }
            else {
                if (e.moveHelper(map, Direction.DOWN, pos)) {
                    return;
                }
                else {
                    s.setRotation(0);
                    move(map, e);
                }
            }
        }
    }
}
