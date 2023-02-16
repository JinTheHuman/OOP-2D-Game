package dungeonmania.Entities.StaticEntities;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.GameMap;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.Entities.MovingEntities.ZombieToast;
import dungeonmania.util.Position;
import dungeonmania.Entities.Entity;

public class Boulder extends StaticEntity {

    public Boulder(Position position, int id) {
        super(position, "boulder", id);
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        if (e instanceof Spider | e instanceof ZombieToast) {
            return false;
        }

        Position p = this.getPosition().translateBy(e.getDirection());

        List<Entity> entitiesAtPosition = m.getEntitiesAtPosition(p);

        if (entitiesAtPosition.stream().anyMatch(n -> (n instanceof Portal || n instanceof Boulder ||
                n instanceof Wall || n instanceof MovingEntity
                || n instanceof ZombieToastSpawner))) {

            return false;
        }

        Door d = (Door) entitiesAtPosition.stream().filter(n -> (n instanceof Door)).findFirst().orElse(null);
        if (d != null) {
            if (d.isLocked()) {
                return false;
            }
        }

        List<Entity> oldPosition = m.getEntitiesAtPosition(getPosition());
        this.setPosition(getPosition().translateBy(e.getDirection()));
        triggerSwitch(oldPosition, m);
        triggerSwitch(entitiesAtPosition, m);

        return true;
    }

    private void triggerSwitch(List<Entity> entities, GameMap gameMap) {
        List<Entity> floorSwitch = entities.stream().filter(e -> e instanceof FloorSwitch).collect(Collectors.toList());
        for (Entity e : floorSwitch) {
            FloorSwitch s = (FloorSwitch) e;
            s.trigger(gameMap);
        }
    }
}
