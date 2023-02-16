package dungeonmania.Goals;

import org.json.JSONObject;

import dungeonmania.GameMap;

public interface Node {
    public boolean evaluate(GameMap map, JSONObject config);

    public Node getLeftNode();
    public void setLeftNode(Node leftNode);

    public Node getRightNode();
    public void setRightNode(Node rightNode);
}
