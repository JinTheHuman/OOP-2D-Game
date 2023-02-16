package dungeonmania;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.BuildableEntities.Bow;
import dungeonmania.Entities.BuildableEntities.BuildableEntity;
import dungeonmania.Entities.BuildableEntities.BuildableEntityFactory;
import dungeonmania.Entities.BuildableEntities.MidnightArmour;
import dungeonmania.Entities.BuildableEntities.Sceptre;
import dungeonmania.Entities.BuildableEntities.Shield;
import dungeonmania.Entities.CollectableEntities.Bomb;
import dungeonmania.Entities.CollectableEntities.Potion;
import dungeonmania.Entities.CollectableEntities.Sword;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Entities.MovingEntities.Spider;
import dungeonmania.Entities.MovingEntities.SpiderSpawner;
import dungeonmania.Entities.MovingEntities.ZombieToast;
import dungeonmania.Entities.MovingEntities.BribableEntities.BribableEntity;
import dungeonmania.Entities.MovingEntities.BribableEntities.Mercenary;
import dungeonmania.Entities.MovingEntities.MovingStrategies.MovingStrategy;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.Entities.MovingEntities.James;
import dungeonmania.Entities.MovingEntities.Jin;
import dungeonmania.Entities.MovingEntities.MovingEntity;
import dungeonmania.Entities.StaticEntities.Portal;
import dungeonmania.Entities.StaticEntities.ZombieToastSpawner;
import dungeonmania.Goals.Goals;
import dungeonmania.States.State;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.FileLoader;
import dungeonmania.util.Position;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import java.lang.IllegalArgumentException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class DungeonManiaController {
    private int entityId = 0;
    private int dungeonId = 0;

    private int totalTickNum = 0;
    private int currTickNum = 0;

    private GameMap gameMap;
    private JSONObject dungeon;
    private JSONObject config;
    private String currDungeonName;
    private Goals goals;
    private TimeTravel timeTravel;
    private List<Battle> battles = new ArrayList<Battle>();
    private int ticking = 0;
    private List<Entity> rewindedDestroyedEntities = new ArrayList<Entity>();

    public GameMap getGameMap() {
        return gameMap;
    }

    public Goals getGoals() {
        return goals;
    }

    // helper function to generate unique IDs
    public int getNewEntityId() {
        entityId += 1;
        return entityId;
    }

    private int getNewDungeonId() {
        dungeonId += 1;
        return dungeonId;
    }

    private int getDungeonId() {
        return dungeonId;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setBattles(List<Battle> battles) {
        this.battles = battles;
    }

    public List<Entity> getRewindedDestroyedEntities() {
        return rewindedDestroyedEntities;
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    /**
     * /dungeons
     */
    public static List<String> dungeons() {
        return FileLoader.listFileNamesInResourceDirectory("dungeons");
    }

    /**
     * /configs
     */
    public static List<String> configs() {
        return FileLoader.listFileNamesInResourceDirectory("configs");
    }

    // helper function to read in JSON Object
    private JSONObject getJSONFromItem(String filePath) {
        try {
            JSONObject obj = new JSONObject(FileLoader.loadResourceFile(filePath + ".json"));
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * /game/new
     */
    public DungeonResponse newGame(String dungeonName, String configName) throws IllegalArgumentException {
        List<String> configFiles = configs();
        List<String> dungeonFiles = dungeons();
        if (!configFiles.contains(configName) || !dungeonFiles.contains(dungeonName)) {
            throw new IllegalArgumentException();
        }

        dungeon = getJSONFromItem("/dungeons/" + dungeonName);
        config = getJSONFromItem("/configs/" + configName);

        JSONArray entities = dungeon.getJSONArray("entities");

        ArrayList<Entity> entityList = new ArrayList<Entity>();
        Entity playerPointer = null;

        for (int i = 0; i < entities.length(); i++) {
            // get JSONObject in each iteration of the array
            JSONObject entityObj = entities.getJSONObject(i);

            Position newPosition = new Position((int) entityObj.get("x"), (int) entityObj.get("y"));
            Entity newEntity = Entity.createEntity(newPosition, entityObj.get("type").toString(), getNewEntityId(),
                    entityObj, config);

            if (newEntity.getType().equals("player")) {
                playerPointer = newEntity;
            } else {
                entityList.add(newEntity);
            }
        }

        gameMap = new GameMap(entityList, playerPointer);
        totalTickNum = 0;
        currTickNum = 0;
        goals = new Goals(dungeon);
        timeTravel = new TimeTravel();
        timeTravel.saveState(goals, gameMap, battles);
        ticking = 0;

        return getDungeonResponseModel();
    }

    /**
     * /game/dungeonResponseModel
     */
    public DungeonResponse getDungeonResponseModel() {
        String dId = String.valueOf(dungeonId);
        List<Entity> dungeonEntities = gameMap.getEntityList();
        Player player = (Player) gameMap.getPlayer();
        List<EntityResponse> entities = new ArrayList<EntityResponse>();
        List<ItemResponse> inventory = new ArrayList<ItemResponse>();
        List<BattleResponse> dungeonBattles = new ArrayList<BattleResponse>();
        List<String> buildables = new ArrayList<>();

        for (Entity e : dungeonEntities) {
            boolean isInteractable = false;
            if (e instanceof ZombieToastSpawner)
                isInteractable = true;
            else if (e instanceof BribableEntity) {
                BribableEntity b = (BribableEntity) e;
                isInteractable = b.isHostile();
            } else if (e instanceof Jin || e instanceof James) {
                isInteractable = true;
            }

            EntityResponse eRes = new EntityResponse(
                    "Entity" + String.valueOf(e.getId()), e.getType(), e.getPosition(),
                    isInteractable);
            entities.add(eRes);
        }

        if (player != null) {
            EntityResponse PlayerRes = new EntityResponse(
                    "Entity" + String.valueOf(player.getId()), player.getType(), player.getPosition(),
                    false);
            entities.add(PlayerRes);
            List<Entity> playerInventory = player.getInventory();
            for (Entity i : playerInventory) {
                ItemResponse iRes = new ItemResponse(
                        "Entity" + String.valueOf(i.getId()), i.getType());
                inventory.add(iRes);
            }

            Map<String, Integer> bowIsBuildable = Bow.isBuildable(playerInventory);
            Map<String, Integer> shieldIsBuildable = Shield.isBuildable(playerInventory);
            Map<String, Integer> midnightArmourIsBuildable = MidnightArmour.isBuildable(playerInventory, gameMap);
            Map<String, Integer> sceptre = Sceptre.isBuildable(playerInventory);

            if (bowIsBuildable != null)
                buildables.add("bow");
            if (shieldIsBuildable != null)
                buildables.add("shield");
            if (midnightArmourIsBuildable != null)
                buildables.add("midnight_armour");
            if (sceptre != null)
                buildables.add("sceptre");
        }

        for (Battle b : battles) {
            List<RoundResponse> dungeonRounds = new ArrayList<RoundResponse>();
            List<Round> rounds = b.getRounds();
            for (Round r : rounds) {
                List<ItemResponse> weaponry = new ArrayList<ItemResponse>();
                List<Entity> weapons = r.getWeaponryUsed();
                for (Entity w : weapons) {
                    weaponry.add(new ItemResponse("Entity" + String.valueOf(w.getId()), w.getType()));
                }
                ;
                dungeonRounds.add(new RoundResponse(r.getDeltaPlayerHealth(), r.getDeltaEnemyHealth(), weaponry));
            }
            dungeonBattles.add(new BattleResponse(b.getEnemy().getType(), dungeonRounds, b.getInitialPlayerHealth(),
                    b.getInitialEnemyHealth()));
        }

        String goalString = goals.getRemainingGoalsString();

        return new DungeonResponse(
                "Dungeon" + dId,
                currDungeonName,
                entities,
                inventory,
                dungeonBattles,
                buildables,
                goalString);
    }

    /**
     * /game/tick/item
     */
    public DungeonResponse tick(String itemUsedId) throws IllegalArgumentException, InvalidActionException {
        Player player = gameMap.getPlayer();
        List<Entity> inventory = player.getInventory();
        Entity item = null;
        for (Entity e : inventory) {
            if (itemUsedId.equals("Entity" + String.valueOf(e.getId()))) {
                item = e;
            }
        }
        if (item == null) {
            throw new InvalidActionException("itemUsed must be in the player's inventory");
        }
        if (item instanceof Bomb) {
            Bomb bomb = (Bomb) item;
            bomb.plantBomb(player, gameMap);
        } else if (item instanceof Potion) {
            State currState = player.getCurrState();
            currState.usePotion((Potion) item);
        } else {
            throw new IllegalArgumentException(
                    "itemUsed must be one of bomb, invincibility_potion, invisibility_potion");
        }

        player.removeInventory(item);
        this.tick((Direction) null);

        return getDungeonResponseModel();
    }

    public boolean playersBattle(Player p) {
        if (p.getCurrState() == p.getInvisibleState() ||
                p.getNumberofItems("midnight_armour") > 0 ||
                p.getNumberofItems("sun_stone") > 0)
            return false;
        return true;
    }

    /**
     * /game/tick/movement
     */
    public DungeonResponse tick(Direction movementDirection) {
        // we are in the past
        if (currTickNum < totalTickNum) {
            // if game is won
            if (timeTravel.rewindState(currTickNum + 1, goals, gameMap, battles, gameMap.getPlayer(), this)) {
                return getDungeonResponseModel();
            }
        } else {
            List<Entity> playerList = gameMap.getEntities("player");
            for (Entity entity : playerList) {
                gameMap.removeEntity(entity);
            }
        }

        Player player = gameMap.getPlayer();

        Sceptre sceptre = (Sceptre) player.getInventory().stream().filter(e -> e instanceof Sceptre).findFirst()
                .orElse(null);
        if (sceptre != null) {
            sceptre.free(player);
        }
        if (movementDirection != null) {
            if (player.move(gameMap, movementDirection, battles, ticking != 0, rewindedDestroyedEntities, this)) {
                return getDungeonResponseModel();
            }
        }

        // only move entities if we are not in the pass
        if (!(currTickNum < totalTickNum)) {
            SpiderSpawner s = new SpiderSpawner();
            s.spawn(gameMap, getNewEntityId(), config, totalTickNum);

            for (Entity e : gameMap.getMoveableEntities()) {
                MovingEntity m = (MovingEntity) e;
                m.move(gameMap, movementDirection);
            }

            for (Entity e : gameMap.getEntities("zombie_toast_spawner")) {
                ZombieToastSpawner z = (ZombieToastSpawner) e;
                z.spawn2(gameMap, getNewEntityId(), config, totalTickNum);
            }
        }

        // Check Battles check for player invisibility
        State currState = player.getCurrState();
        if (currState != player.getInvisibleState()) {
            for (Entity e : gameMap.getEntitiesAtPosition(gameMap.getPlayer().getPosition())) {
                if (e instanceof MovingEntity) {
                    if (e instanceof Player) {
                        Player oldPlayer = (Player) e;
                        if (!playersBattle(oldPlayer) || !playersBattle(player)) {
                            continue;
                        }

                    }
                    MovingEntity enemy = (MovingEntity) e;

                    Battle newBattle = new Battle(player, enemy, player.getHealth(), enemy.getHealth());

                    // if enemy is killed, and player wins the battle
                    if (newBattle.runBattle()) {
                        player.addEnemiesDestroyed();
                        if (ticking != 0)
                            rewindedDestroyedEntities.add(newBattle.getEnemy());
                        gameMap.removeEntity(newBattle.getEnemy());
                    }

                    battles.add(newBattle);

                    if (player.getHealth() <= 0) {
                        gameMap.killPlayer();
                        return getDungeonResponseModel();
                    }

                }
            }
        }

        // Check Portals
        for (Entity p : gameMap.getEntities("portal")) {

            if (player.getPosition().equals(p.getPosition())) {
                Portal portal = (Portal) p;
                portal.teleport(gameMap, player);
            }

            for (Entity e : gameMap.getEntitiesAtPosition(p.getPosition())) {
                if (e instanceof MovingEntity) {
                    MovingEntity m = (MovingEntity) e;
                    Portal portal = (Portal) p;
                    portal.teleport(gameMap, m);
                }
            }
        }

        player.getCurrState().tick();
        goals.evaluate(gameMap, config);
        timeTravel.saveState(goals, gameMap, battles);

        // if we are not in the past
        if (!(currTickNum < totalTickNum)) {
            if (ticking > 0) {
                totalTickNum += ticking;
                ticking = 0;
                rewindedDestroyedEntities.clear();
            }
            totalTickNum++;
            currTickNum = totalTickNum;
        }
        // if in the past
        else {
            currTickNum++;
        }

        return getDungeonResponseModel();
    }

    /**
     * /game/build
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        Player player = gameMap.getPlayer();
        Map<BuildableEntity, Map<String, Integer>> entityMaterialMap = BuildableEntityFactory
                .getBuildableEntity(getNewEntityId(), buildable, player.getInventory(), config, gameMap);

        // get first (and only) key in singleton map
        Entity entity = entityMaterialMap.keySet().iterator().next();
        // get map of resources used
        Map<String, Integer> materialsRequired = entityMaterialMap.get(entity);

        // remove materials used
        player.useItem(materialsRequired);
        // add new buildableEntity to inventory
        player.addInventory(entity);

        return getDungeonResponseModel();
    }

    /**
     * /game/interact
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        // error check
        // destroy zombie spawners
        // bribe mercenarys
        int idNum = Integer.parseInt(entityId.replaceAll("[^0-9]", ""));
        Entity clickedEntity = gameMap.getEntitybyId(idNum);
        if (clickedEntity == null) {
            throw new IllegalArgumentException();
        }

        Player player = gameMap.getPlayer();

        if (clickedEntity instanceof ZombieToastSpawner) {
            ZombieToastSpawner zombieToastSpawner = (ZombieToastSpawner) clickedEntity;
            if (!zombieToastSpawner.isplayerIsAdjacent(player)) {
                throw new InvalidActionException("Player not adjacent to Zombie Toast Spawner");
            }

            if (!player.getInventory().stream().anyMatch(e -> (e instanceof Sword))) {
                throw new InvalidActionException("Player has no sword to destroy Zombie Toast Spawner");
            }

            gameMap.removeEntity(clickedEntity);
        }

        else if (clickedEntity instanceof BribableEntity) {
            BribableEntity b = (BribableEntity) clickedEntity;

            // if player has sceptre
            Sceptre sceptre = (Sceptre) player.getInventory().stream().filter(e -> e instanceof Sceptre).findFirst()
                    .orElse(null);

            if (sceptre != null) {
                sceptre.mindControl(b, player);
                return getDungeonResponseModel();
            }

            if (!b.isWithinBribeRadius(player)) {
                throw new InvalidActionException("Too far to bribe");
            } else if (!b.bribe(player) && sceptre == null) {
                throw new InvalidActionException("Items in inventory insufficient to bribe");
            }
        }

        if (clickedEntity instanceof Jin) {
            Jin jin = (Jin) clickedEntity;
            jin.shootLaser(gameMap);
        } else if (clickedEntity instanceof James) {
            James james = (James) clickedEntity;
            james.explode(gameMap, getNewEntityId());
        }

        return getDungeonResponseModel();
    }

    /**
     * /game/save
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        // position of entities on the map.
        // player inventory
        // potions effects
        // allies (bribing/mind-control)
        // opened doors
        try {
            String path = getPathForNewFile("saves", name);
            // create save file
            FileOutputStream out = new FileOutputStream(path);

            ObjectOutputStream objectOut = new ObjectOutputStream(out);

            objectOut.writeObject(gameMap);
            objectOut.writeObject(goals);
            objectOut.writeObject(battles);

            objectOut.close();
            System.out.println("Game data created as " + name);
        } catch (Exception e) {
            System.err.println("Unable to create game data " + e);
        }

        return getDungeonResponseModel();
    }

    /**
     * /game/load
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        if (!allGames().contains(name)) {
            throw new IllegalArgumentException("NAME DOESNT EXIST");
        }
        try {
            String path = getPathForNewFile("saves", name);
            FileInputStream in = new FileInputStream(path);
            ObjectInputStream objectIn = new ObjectInputStream(in);

            this.gameMap = (GameMap) objectIn.readObject();
            this.goals = (Goals) objectIn.readObject();
            this.battles = (List<Battle>) objectIn.readObject();

            objectIn.close();
        } catch (Exception e) {
            System.err.println("Unable to read game data " + e);
        }
        return getDungeonResponseModel();
    }

    /**
     * /games/all
     */
    public List<String> allGames() {
        String path = getPathForNewFile("saves");
        
        File f = new File(path);
        List<String> pathnames = Arrays.asList(f.list());
        return pathnames;
    }

    public DungeonResponse generateDungeon(int xStart, int yStart, int xEnd, int yEnd, String configName)
            throws IllegalArgumentException {
        List<String> configFiles = configs();
        if (!configFiles.contains(configName)) {
            throw new IllegalArgumentException();
        }
        JSONObject randomMap = DungeonGenerator.generateDungeon(xStart, yStart, xEnd, yEnd);

        dungeon = randomMap;
        config = getJSONFromItem("/configs/" + configName);

        JSONArray entities = dungeon.getJSONArray("entities");

        ArrayList<Entity> entityList = new ArrayList<Entity>();
        Entity playerPointer = null;

        for (int i = 0; i < entities.length(); i++) {
            // get JSONObject in each iteration of the array
            JSONObject entityObj = entities.getJSONObject(i);

            Position newPosition = new Position((int) entityObj.get("x"), (int) entityObj.get("y"));
            Entity newEntity = Entity.createEntity(newPosition, entityObj.get("type").toString(), getNewEntityId(),
                    entityObj, config);

            if (newEntity.getType().equals("player")) {
                playerPointer = newEntity;
            } else {
                entityList.add(newEntity);
            }
        }

        gameMap = new GameMap(entityList, playerPointer);

        goals = new Goals(dungeon);

        return getDungeonResponseModel();
    }

    public DungeonResponse rewind(int ticks) {
        if (ticks <= 0 || ticks > currTickNum) {
            throw new IllegalArgumentException("Unable to rewind");
        }
        currTickNum = currTickNum - ticks;
        ticking = ticks;
        if (timeTravel.rewindState(currTickNum, goals, gameMap, battles, gameMap.getPlayer(), this))
            return getDungeonResponseModel();

        return getDungeonResponseModel();
    }

    // get path to resources folder
    private String getPathForNewFile(String directory, String newFile) throws IOException, NullPointerException {
        if (!directory.startsWith("/"))
            directory = "/" + directory;
        try {
            if (FileLoader.class.getResource(directory) == null) {
                Path root = Paths.get(FileLoader.class.getResource("").toURI());
                File f1 = new File(root.toString() + "/../../saves");  
                f1.mkdir();  
            }
            Path root = Paths.get(FileLoader.class.getResource(directory).toURI());
            System.out.println(root.toString());
            return root.toString() + "/" + newFile;
        } catch (URISyntaxException e) {
            throw new FileNotFoundException(directory);
        }
    }

    // get path to resources folder
    private String getPathForNewFile(String directory) {
        if (!directory.startsWith("/"))
            directory = "/" + directory;
        try {
            Path root = Paths.get(FileLoader.class.getResource(directory).toURI());
            return root.toString();
        } catch (URISyntaxException e) {
            System.out.println("URI is a malformed URL");
        }
        return null;
    }
}
