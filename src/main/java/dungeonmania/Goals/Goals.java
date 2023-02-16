package dungeonmania.Goals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Goals.Leaf.*;

public class Goals implements Serializable {
    String parsedString = "";
    String remainingGoalsString = "";
    // List<String> levelOrderList = new ArrayList<String>();
    List<String> levelOrderList = new ArrayList<String>();
    boolean exit;
    boolean enemies;
    boolean boulders;
    boolean treasure;
    Node root;

    /**
     * Goals is initialized when a new game is created.
     * Constructor to initialize all goals achieved to false
     */
    public Goals(JSONObject dungeonJSON) {
        exit = false;
        enemies = false;
        boulders = false;
        treasure = false;
        parse(dungeonJSON.getJSONObject("goal-condition"));
        remainingGoalsString = parsedString;

        root = createTree(dungeonJSON.getJSONObject("goal-condition"));
    }

    public String getRemainingGoalsString() {
        return remainingGoalsString;
    }

    public boolean evaluate(GameMap map, JSONObject config) {
        boolean isGoalsCompleted = root.evaluate(map, config);
        updateRemainingGoalsString(map, config);
        // if all goals are completed
        if (isGoalsCompleted)
            remainingGoalsString = "";
        return isGoalsCompleted;
    }

    private void updateRemainingGoalsString(GameMap map, JSONObject config) {
        // check which goals are completed
        BouldersLeaf bouldersLeaf = new BouldersLeaf();
        EnemiesLeaf enemiesLeaf = new EnemiesLeaf();
        ExitLeaf exitLeaf = new ExitLeaf();
        TreasureLeaf treasureLeaf = new TreasureLeaf();
        boulders = bouldersLeaf.evaluate(map, config);
        enemies = enemiesLeaf.evaluate(map, config);
        exit = exitLeaf.evaluate(map, config);
        treasure = treasureLeaf.evaluate(map, config);
        System.out.println(boulders);
        if (boulders) {
            remainingGoalsString = parsedString.replaceAll(":boulders", "");
        }
        else {
            remainingGoalsString = parsedString;
        }
        if (enemies)
            remainingGoalsString = remainingGoalsString.replaceAll(":enemies", "");
        if (exit)
            remainingGoalsString = remainingGoalsString.replaceAll(":exit", "");
        if (treasure)
            remainingGoalsString = remainingGoalsString.replaceAll(":treasure", "");
    }

    private void parse(JSONObject dungeonObj) {
        // get current node
        String goal = dungeonObj.get("goal").toString();

        // base case (handling basic goals)
        if (!(goal.equals("AND") || goal.equals("OR"))) {
            parsedString += ":" + goal;
            return;
        }

        // check child nodes
        JSONArray subgoalArray = dungeonObj.getJSONArray("subgoals");
        JSONObject subgoalObj1 = subgoalArray.getJSONObject(0);
        JSONObject subgoalObj2 = subgoalArray.getJSONObject(1);

        parsedString += "(";
        parse(subgoalObj1);
        parsedString += ")";

        // if current "node" is AND or OR, put it in the middle of the two subgoals.
        if ((goal.equals("AND") || goal.equals("OR"))) {
            parsedString += " " + goal + " ";
        }

        parsedString += "(";
        parse(subgoalObj2);
        parsedString += ")";
    }

    private Node createTree(JSONObject dungeonObj) {
        // get current node
        String goal = dungeonObj.get("goal").toString();
        Node node = NodeFactory.createNode(goal, null, null);

        if (goal.equals("AND") || goal.equals("OR")) {
            JSONArray subgoalArray = dungeonObj.getJSONArray("subgoals");
            JSONObject subgoalObj1 = subgoalArray.getJSONObject(0);
            JSONObject subgoalObj2 = subgoalArray.getJSONObject(1);

            node.setLeftNode(createTree(subgoalObj1));
            node.setRightNode(createTree(subgoalObj2));
        }
        return node;
    }       
}
