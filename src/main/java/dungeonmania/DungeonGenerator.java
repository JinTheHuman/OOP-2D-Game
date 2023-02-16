package dungeonmania;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;

import com.google.gson.JsonObject;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class DungeonGenerator {
    public static JSONObject generateDungeon(int xStart, int yStart, int xEnd, int yEnd) {
        Random random = new Random(System.currentTimeMillis());
        int width = xEnd - xStart;
        int height = yEnd - yStart;
        Position start = new Position(xStart, yStart);
        Position end = new Position(xEnd, yEnd);

        Map<Position, Boolean> maze = new HashMap<Position, Boolean>();

        // populate maze
        for (int x = xStart - 1; x <= xEnd + 1; x++) {
            for (int y = yStart - 1; y <= yEnd + 1; y++) {
                maze.put(new Position(x, y), false);
            }
        }
        maze.put(start, true);

        List<Position> options = new ArrayList<Position>();
        options.addAll(getNeighbours(maze, start, xStart, yStart, xEnd, yEnd, false));

        while (!options.isEmpty()) {
            int randIdx;
            if (options.size() == 1) {
                randIdx = 0;
            } else {
                randIdx = random.nextInt(options.size() - 1);
            }
            Position next = options.remove(randIdx);

            List<Position> neighbours = getNeighbours(maze, next, xStart, yStart, xEnd, yEnd, true);
            if (!neighbours.isEmpty()) {
                Position neighbour;
                if (neighbours.size() == 1) {
                    neighbour = neighbours.get(0);
                } else {
                    neighbour = neighbours.get(random.nextInt(neighbours.size() - 1));
                }
                maze.put(next, true);
                maze.put(getBetween(next, neighbour), true);
                maze.put(neighbour, true);
            }

            options.addAll(getNeighbours(maze, next, xStart, yStart, xEnd, yEnd, false));
        }

        // printMaze(start, end, maze);
        // check that end is accessible
        if (!maze.get(end)) {
            maze.put(end, true);

            List<Position> neighbours = getCloseNeighbours(maze, end, xStart, yStart, xEnd, yEnd);
            if (!neighbours.stream().anyMatch((e) -> (maze.get(e)))) {
                Position neighbour;
                if (neighbours.size() == 1) {
                    neighbour = neighbours.get(0);
                } else {
                    neighbour = neighbours.get(random.nextInt(neighbours.size() - 1));
                }
                maze.put(neighbour, true);
            }
        }

        // createJsonMaze(start, end, maze);
        return createJsonMaze(start, end, maze);
    }

    private static JSONObject createJsonMaze(Position start, Position end, Map<Position, Boolean> maze) {
        JSONObject dungeon = new JSONObject();
        List<JSONObject> entitiesList = new ArrayList<JSONObject>();

        for (Position p : maze.keySet()) {
            JSONObject entity = new JSONObject();
            if (start.equals(p)) {
                entity.put("type", "player");
            } else if (end.equals(p)) {
                entity.put("type", "exit");
            } else if (!maze.get(p)) {
                entity.put("type", "wall");
            } else {
                continue;
            }
            entity.put("x", p.getX());
            entity.put("y", p.getY());
            entitiesList.add(entity);
        }

        JSONObject goal = new JSONObject();
        goal.put("goal", "exit");

        dungeon.put("entities", entitiesList);
        dungeon.put("goal-condition", goal);

        try {
            FileWriter file = new FileWriter("src/main/resources/dungeons/random.json", false);
            file.write(dungeon.toString(1));
            file.flush();
            file.close();
        } catch (IOException e) {
            System.out.println("Lol it broke");
        }
        return dungeon;
    }

    private static void printMaze(Position start, Position end, Map<Position, Boolean> maze) {
        for (int y = 0; y < 11; y++) {
            for (int x = 0; x < 11; x++) {
                if (start.equals(new Position(x, y))) {
                    System.out.print("S");
                } else if (end.equals(new Position(x, y))) {
                    System.out.print("E");
                } else if (maze.get(new Position(x, y))) {
                    System.out.print(" ");
                } else {
                    System.out.print("O");
                }
            }
            System.out.println("");
        }
    }

    private static List<Position> getNeighbours(Map<Position, Boolean> maze, Position p, int xStart, int yStart,
            int xEnd, int yEnd, boolean wantEmpty) {

        List<Position> ret = new ArrayList<Position>();
        for (Direction d : Direction.values()) {
            Position outerPos = p.translateBy(d).translateBy(d);
            if (isWithinRange(outerPos, xStart, yStart, xEnd, yEnd) && (maze.get(outerPos) == wantEmpty)) {
                ret.add(outerPos);
            }
        }
        return ret;
    }

    private static List<Position> getCloseNeighbours(Map<Position, Boolean> maze, Position p, int xStart, int yStart,
            int xEnd, int yEnd) {

        List<Position> ret = new ArrayList<Position>();
        for (Direction d : Direction.values()) {
            Position outerPos = p.translateBy(d);
            if (isWithinRange(outerPos, xStart, yStart, xEnd, yEnd)) {
                ret.add(outerPos);
            }
        }
        return ret;
    }

    private static boolean isWithinRange(Position p, int xStart, int yStart, int xEnd, int yEnd) {
        return !(p.getX() > xEnd || p.getX() < xStart || p.getY() > yEnd || p.getY() < yStart);
    }

    private static Position getBetween(Position a, Position b) {
        return new Position((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }

    // public static void main(String[] args) {
    // Scanner s = new Scanner(System.in);
    // int input = s.nextInt();
    // while (input != 0) {
    // DungeonGenerator.generateDungeon(1, 1, 9, 9);
    // input = s.nextInt();
    // }
    // }
}
