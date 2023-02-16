package dungeonmania.Goals.Composite;
import java.io.Serializable;

import org.json.JSONObject;

import dungeonmania.GameMap;
import dungeonmania.Goals.Node;

public abstract class CompositeNode implements Node, Serializable {
    Node leftNode;
    Node rightNode;
   
    public CompositeNode(Node leftNode, Node rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
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
