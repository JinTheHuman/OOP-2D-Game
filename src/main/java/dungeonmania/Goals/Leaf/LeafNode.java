package dungeonmania.Goals.Leaf;
import java.io.Serializable;

import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Goals.Node;

public abstract class LeafNode implements Node, Serializable {
    Node leftNode;
    Node rightNode;
   
    public LeafNode() {
        this.leftNode = null;
        this.rightNode = null;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public abstract boolean evaluate(GameMap map, JSONObject config);
}
