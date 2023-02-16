package dungeonmania.Entities.StaticEntities;

import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.BribableEntities.Mercenary;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Portal extends StaticEntity {

    private String colour;

    public Portal(Position position, int id, String colour) {
        super(position, "portal", id);
        this.colour = colour;
        // TODO Auto-generated constructor stub
    }

    public String getColour() {
        return colour;
    }

    @Override
    public boolean canStepOn(GameMap m, MovingEntity e) {
        return true;
    }

    public void teleport(GameMap m, MovingEntity e) {
        if (!(e instanceof Player | e instanceof Mercenary)) {
            return;
        }
        for (Entity portalEntity : m.getEntities("portal")) {
            Portal portal = (Portal) portalEntity;
            if (portal.getColour().equals(colour) && !this.equals(portal)) {
                if (m.canStepInto(e, portal.getPosition().translateBy(e.getDirection()))) {
                    e.setPosition(portal.getPosition().translateBy(e.getDirection()));
                } else if (m.canStepInto(e, portal.getPosition().translateBy(Direction.UP))) {
                    e.setDirection(Direction.UP);
                    e.setPosition(portal.getPosition().translateBy(Direction.UP));
                } else if (m.canStepInto(e, portal.getPosition().translateBy(Direction.DOWN))) {
                    e.setDirection(Direction.DOWN);
                    e.setPosition(portal.getPosition().translateBy(Direction.DOWN));
                } else if (m.canStepInto(e, portal.getPosition().translateBy(Direction.LEFT))) {
                    e.setDirection(Direction.LEFT);
                    e.setPosition(portal.getPosition().translateBy(Direction.LEFT));
                } else if (m.canStepInto(e, portal.getPosition().translateBy(Direction.RIGHT))) {
                    e.setDirection(Direction.RIGHT);
                    e.setPosition(portal.getPosition().translateBy(Direction.RIGHT));
                }
                tryTeleport(e.getPosition(), m, e);
                return;
            }
        }
    }

    public void tryTeleport(Position p, GameMap m, MovingEntity e) {
        for (Entity entity : m.getEntitiesAtPosition(p)) {
            if (entity instanceof Portal && this != entity) {
                Portal portal = (Portal) entity;
                portal.teleport(m, e);
            }
        }
    }
}
