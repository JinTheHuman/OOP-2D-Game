package dungeonmania.Goals;

import dungeonmania.Goals.Leaf.*;

import java.io.Serializable;

import dungeonmania.Goals.Composite.*;

public class NodeFactory {
    public static Node createNode(String type, Node leftNode, Node rightNode) {
        switch (type) {
            case "exit":
                return new ExitLeaf();
            case "treasure":
                return new TreasureLeaf();
            case "boulders":
                return new BouldersLeaf();
            case "enemies":
                return new EnemiesLeaf();
            case "AND":
                return new ANDNode(leftNode, rightNode);
            case "OR":
                return new ORNode(leftNode, rightNode);
            default:
                return null;
        }
    }
}
