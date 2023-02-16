package dungeonmania.Entities.MovingEntities;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import dungeonmania.Battle;
import dungeonmania.DungeonManiaController;
import dungeonmania.GameMap;
import dungeonmania.Entities.Entity;
import dungeonmania.Entities.CollectableEntities.CollectableEntity;
import dungeonmania.Entities.CollectableEntities.Potion;
import dungeonmania.Entities.StaticEntities.Poo;
import dungeonmania.Entities.StaticEntities.StaticEntity;
import dungeonmania.Entities.StaticEntities.SwampTile;
import dungeonmania.Entities.StaticEntities.TimeTravellingPortal;
import dungeonmania.States.InvincibleState;
import dungeonmania.States.InvisibleState;
import dungeonmania.States.NormalState;
import dungeonmania.States.State;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.LinkedList;

public class Player extends MovingEntity {
    private List<Entity> inventory = new ArrayList<Entity>();
    private Queue<Potion> potionsQueue = new LinkedList<Potion>();
    private State normalState;
    private State invincibleState;
    private State invisibleState;
    private State currState;
    private Entity currPotion = null;

    // private ArrayList<Item> weapons = new ArrayList<>();
    private ArrayList<MovingEntity> allies = new ArrayList<>();
    private int enemiesDestroyed;
    Position previousPosition;

    public Player(Position position, int id, double health, double attackDamage) {
        super(position, "player", id, health, attackDamage);
        enemiesDestroyed = 0;
        normalState = new NormalState(this);
        invincibleState = new InvincibleState(this);
        invisibleState = new InvisibleState(this);
        currState = normalState;
    }

    public List<Entity> getInventory() {
        return inventory;
    }

    public void addInventory(Entity item) {
        inventory.add(item);
    }

    public void removeInventory(Entity item) {
        inventory.remove(item);
    }

    public int getEnemiesDestroyed() {
        return enemiesDestroyed;
    }

    public void addEnemiesDestroyed() {
        enemiesDestroyed++;
    }

    public ArrayList<MovingEntity> getAllies() {
        return allies;
    }

    public Queue<Potion> getPotionsQueue() {
        return potionsQueue;
    }

    public void addPotionsQueue(Potion potion) {
        potionsQueue.add(potion);
    }

    public State getCurrState() {
        return currState;
    }

    public Entity getCurrPotion() {
        return currPotion;
    }

    public void setCurrPotion(Entity potion) {
        this.currPotion = potion;
    }

    public State getNormalState() {
        return normalState;
    }

    public State getInvincibleState() {
        return invincibleState;
    }

    public State getInvisibleState() {
        return invisibleState;
    }

    public void setCurrState(State state) {
        this.currState = state;
    }

    public void setCurrState(Potion potion) {
        State newState = potion.getState(this);
        potion.setPotion(this);
        if (newState != null) {
            this.currState = newState;
            currState.usePotion(potion);
        } else {
            this.currState = normalState;
        }
    }

    /**
     * 
     * @param map
     * @param direction
     * @param battles
     * @return True if player dies otherwise false
     */
    public boolean move(GameMap map, Direction direction, List<Battle> battles, DungeonManiaController dmc) {
        Position pos = getPosition().translateBy(direction);

        List<Entity> EntitiesAtPos = map.getEntitiesAtPosition(pos);
        setDirection(direction);
        boolean canMove = true;
        for (Entity entity : EntitiesAtPos) {
            if (entity instanceof StaticEntity) {
                StaticEntity s = (StaticEntity) entity;
                if (entity instanceof TimeTravellingPortal) {
                    TimeTravellingPortal ttp = (TimeTravellingPortal) entity;
                    ttp.canStepOn(map, this, dmc);
                    continue;
                }
                else if (s.canStepOn(map, this)) {
                    continue;
                } else {
                    canMove = false;
                    break;
                }
            } else if (entity instanceof CollectableEntity) {
                CollectableEntity c = (CollectableEntity) entity;
                if (c.collect(inventory)) {
                    map.removeEntity(entity);
                }
            }
        }

        for (Entity entity : map.getEntitiesAtPosition(getPosition())) {
            if (entity instanceof SwampTile) {
                SwampTile s = (SwampTile) entity;
                if (!s.canMoveOff(this)) {
                    canMove = false;
                    break;
                }
            }
        }

        if (canMove == true) {
            previousPosition = new Position(getPosition().getX(), getPosition().getY());
            setPosition(pos);

            // Check Battles check for player invisibility
            State currState = this.getCurrState();
            if (currState != this.getInvisibleState()) {
                for (Entity e : map.getEntitiesAtPosition(map.getPlayer().getPosition())) {
                    if (e instanceof MovingEntity) {

                        MovingEntity enemy = (MovingEntity) e;
                        if (this.getAllies().contains(enemy)) {
                            continue;
                        }

                        Battle newBattle = new Battle(this, enemy, this.getHealth(), enemy.getHealth());

                        // if enemy is killed, and player wins the battle
                        if (newBattle.runBattle()) {
                            this.addEnemiesDestroyed();
                            map.removeEntity(newBattle.getEnemy());
                        }

                        battles.add(newBattle);

                        if (this.getHealth() <= 0) {
                            map.killPlayer();
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    public boolean move(GameMap map, Direction direction, List<Battle> battles, Boolean isPast, List<Entity> rewindedDestroyedEntities, DungeonManiaController dmc) {
        Position pos = getPosition().translateBy(direction);

        List<Entity> EntitiesAtPos = map.getEntitiesAtPosition(pos);
        setDirection(direction);
        boolean canMove = true;
        for (Entity entity : EntitiesAtPos) {
            if (entity instanceof StaticEntity) {
                StaticEntity s = (StaticEntity) entity;
                if (entity instanceof TimeTravellingPortal) {
                    TimeTravellingPortal ttp = (TimeTravellingPortal) entity;
                    ttp.canStepOn(map, this, dmc);
                    continue;
                }
                else if (s.canStepOn(map, this)) {
                    continue;
                } else {
                    canMove = false;
                    break;
                }
            } else if (entity instanceof CollectableEntity) {
                CollectableEntity c = (CollectableEntity) entity;
                if (c.collect(inventory)) {
                    if (isPast) rewindedDestroyedEntities.add(c);
                    map.removeEntity(entity);
                }
            }
        }

        for (Entity entity : map.getEntitiesAtPosition(getPosition())) {
            if (entity instanceof SwampTile) {
                SwampTile s = (SwampTile) entity;
                if (!s.canMoveOff(this)) {
                    canMove = false;
                    break;
                }
            }
            else if (entity instanceof Poo) {
                Poo poo = (Poo) entity;
                if (!poo.canMoveOff(this)) {
                    canMove = false;
                    break;
                }
            }
        }

        if (canMove == true) {
            previousPosition = new Position(getPosition().getX(), getPosition().getY());
            setPosition(pos);

            // Check Battles check for player invisibility
            State currState = this.getCurrState();
            if (currState != this.getInvisibleState()) {
                for (Entity e : map.getEntitiesAtPosition(map.getPlayer().getPosition())) {
                    if (e instanceof MovingEntity) {

                        MovingEntity enemy = (MovingEntity) e;
                        if (this.getAllies().contains(enemy)) {
                            continue;
                        }

                        Battle newBattle = new Battle(this, enemy, this.getHealth(), enemy.getHealth());

                        // if enemy is killed, and player wins the battle
                        if (newBattle.runBattle()) {
                            this.addEnemiesDestroyed();
                            map.removeEntity(newBattle.getEnemy());
                        }

                        battles.add(newBattle);

                        if (this.getHealth() <= 0) {
                            map.killPlayer();
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    public void useItem(String type, int num) {
        List<Entity> itemsToRemove = inventory.stream().filter(e -> (e.getType().startsWith(type)))
                .collect(Collectors.toList());
        for (int i = 0; i < num; i++) {
            inventory.remove(itemsToRemove.get(i));
        }
    }

    public boolean useItem(Map<String, Integer> consumedItems) {
        for (Map.Entry<String, Integer> pair : consumedItems.entrySet()) {
            String item = pair.getKey();
            int itemAmount = pair.getValue();

            ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
            for (Entity entity : inventory) {
                if (entity.getType().equals(item)) {
                    entitiesToRemove.add(entity);
                }
            }
            if (entitiesToRemove.size() < itemAmount)
                return false;
            for (int i = 0; i < itemAmount; i++) {
                inventory.remove(entitiesToRemove.get(i));
            }
        }
        return true;
    }

    // returns the number of items of the given type in the players inventory
    public int getNumberofItems(String type) {
        return inventory.stream().filter(e -> (e.getType().startsWith(type))).collect(Collectors.toList()).size();
    }

    // returns a list of all the items of the given type in the players inventory
    public List<Entity> getItems(String type) {
        return inventory.stream().filter(e -> (e.getType().startsWith(type))).collect(Collectors.toList());
    }

    public void addToAllies(MovingEntity entity) {
        allies.add(entity);
    }

    public void removeFromAllies(MovingEntity entity) {
        allies.remove(entity);
    }

    public Position getPrevPosition() {
        return previousPosition;
    }

    @Override
    public void move(GameMap map, Direction direction) {
        // TODO Auto-generated method stub
        Position pos = getPosition().translateBy(direction);

        List<Entity> EntitiesAtPos = map.getEntitiesAtPosition(pos);
        setDirection(direction);
        boolean canMove = true;
        for (Entity entity : EntitiesAtPos) {
            if (entity instanceof StaticEntity) {
                StaticEntity s = (StaticEntity) entity;
                if (s.canStepOn(map, this)) {
                    continue;
                } else {
                    canMove = false;
                    break;
                }
            } else if (entity instanceof CollectableEntity) {
                CollectableEntity c = (CollectableEntity) entity;
                if (c.collect(inventory)) {
                    map.removeEntity(entity);
                }
            }
        }
        if (canMove == true) {
            previousPosition = new Position(getPosition().getX(), getPosition().getY());
            setPosition(pos);
        }
    }
}
