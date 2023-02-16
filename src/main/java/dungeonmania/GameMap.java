package dungeonmania;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Entities.*;
import dungeonmania.Entities.CollectableEntities.CollectableEntity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.StaticEntities.StaticEntity;
import dungeonmania.util.Position;

public class GameMap implements Serializable {
    private List<Entity> entityList;
    private Entity player;

    public GameMap(ArrayList<Entity> entityList, Entity player) {
        this.entityList = entityList;
        this.player = player;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public void removeEntity(Entity entity) {
        entityList.remove(entity);
    }

    public void killPlayer() {
        setPlayer(null);
    }

    public List<Entity> getCollectableEntities() {
        return entityList.stream().filter(e -> (e instanceof CollectableEntity)).collect(Collectors.toList());
    }

    public List<Entity> getMoveableEntities() {
        return entityList.stream().filter(e -> (e instanceof MovingEntity)).collect(Collectors.toList());
    }

    public List<Entity> getStaticEntities() {
        return entityList.stream().filter(e -> (e instanceof StaticEntity)).collect(Collectors.toList());
    }

    public Player getPlayer() {
        return (Player) player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Entity> getEntitiesAtPosition(Position p) {
        List<Entity> ret = this.getEntityList().stream().filter(e -> e.getPosition().equals(p))
                .collect(Collectors.toList());

        if (ret.size() == 0) {
            ret.add(new Empty(p));
        }
        return ret;
    }

    public List<Entity> getEntities(String type) {
        return entityList.stream().filter(e -> (e.getType().startsWith(type))).collect(Collectors.toList());
    }

    public boolean canStepInto(MovingEntity m, Position p) {
        List<Entity> EntitiesAtPos = this.getEntitiesAtPosition(p);
        boolean canMove = true;
        for (Entity entity : EntitiesAtPos) {
            if (entity instanceof StaticEntity) {
                StaticEntity s = (StaticEntity) entity;
                if (s.canStepOn(this, m)) {
                    continue;
                } else {
                    canMove = false;
                    break;
                }
            }
        }

        return canMove;
    }

    public Entity getEntitybyId(int id) {
        return entityList.stream().filter(e -> (e.getId() == id)).findFirst().orElse(null);
    }

}