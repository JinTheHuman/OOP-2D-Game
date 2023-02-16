package dungeonmania;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import dungeonmania.Entities.Entity;
import dungeonmania.Entities.MovingEntities.Player;
import dungeonmania.Goals.Goals;

public class TimeTravel {
    List<String> stateList = new ArrayList<String>();

    public void saveState(Goals goals, GameMap gameMap, List<Battle> battles) throws IllegalArgumentException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
    
            oos.writeObject(gameMap);
            oos.writeObject(goals);
            oos.writeObject(battles);
            oos.close();
            stateList.add(Base64.getEncoder().encodeToString(baos.toByteArray()));
        }
        catch (IllegalArgumentException | IOException e) {
			System.err.println ("Unable to save state" + e);
		}
    }

    public boolean rewindState(int tick, Goals goals, GameMap gameMap, List<Battle> battles, Player newPlayer, DungeonManiaController dmc)  {
        try {
            byte [] data = Base64.getDecoder().decode(stateList.get(tick));
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            gameMap = (GameMap) ois.readObject();
            goals = (Goals) ois.readObject();
            battles = (List<Battle>) ois.readObject();

            // add stored player position to entityList and make current player the new player.
            Player oldPlayer = gameMap.getPlayer();
            Player oldPlayerCopy = new Player(oldPlayer.getPosition(), dmc.getNewEntityId(), oldPlayer.getHealth(), oldPlayer.getAttackDamage());
            gameMap.addEntity(oldPlayerCopy);
            gameMap.setPlayer(newPlayer);

            List<Entity> enititesToRemove = new ArrayList<>();
            for (Entity entity : gameMap.getEntityList()) {
                for (Entity entity2 : dmc.getRewindedDestroyedEntities()) {
                    if (entity.getId() == entity2.getId()) enititesToRemove.add(entity);
                }
            }
            gameMap.getEntityList().removeAll(enititesToRemove);

            dmc.setGameMap(gameMap);

            // reevaluate goals
            boolean response = dmc.getGoals().evaluate(gameMap, dmc.getConfig());
            dmc.setGoals(dmc.getGoals());
            dmc.setBattles(battles);
            ois.close();
            
            return response;
        }
        catch (IOException | ClassNotFoundException e) {
			System.err.println ("Unable to load state" + e);
		}
        return false;
    }
}
