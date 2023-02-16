package dungeonmania.Goals.Composite;

import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Goals.Node;

public class ANDNode extends CompositeNode {
    public ANDNode(Node leftNode, Node rightNode) {
        super(leftNode, rightNode);
    }

    public boolean evaluate(GameMap map, JSONObject config) {
        return (leftNode.evaluate(map, config) && rightNode.evaluate(map, config));
    }
}
