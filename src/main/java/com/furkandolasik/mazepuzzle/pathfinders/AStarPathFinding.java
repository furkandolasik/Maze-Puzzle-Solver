package com.furkandolasik.mazepuzzle.pathfinders;

import java.awt.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class AStarPathFinding {

    private static final int TILE_SIZE = 1; //cost

    private final int COLS;
    private final int ROWS;
    private final Node[][] grid;
    private final boolean[][] obstaclesGrid;
    private final int startX;
    private final int startY;
    private final int targetX;
    private final int targetY;
    private int heuristic;
    private Graph<Point> graph;


    public AStarPathFinding(boolean[][] matrix, int heuristicNo, int startX, int startY, int targetX, int targetY) {
        this.obstaclesGrid = matrix;
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.heuristic = heuristicNo;

        ROWS = matrix.length;
        COLS = matrix[0].length;
        grid = new Node[ROWS][COLS];

    }

    public List<Node<Point>> solve() {
        graph = new Graph<>((start, target, current) -> {

            int dx = target.getObj().x - current.getObj().x;
            int dy = target.getObj().y - current.getObj().y;

            if (this.heuristic == 0) {   // manhattan
                return (dx + dy);
            } else if (this.heuristic == 1) {    // euclidian
                return Math.sqrt(dx * dx + dy * dy);
            } else if (this.heuristic == 2) {    // octile
                return (Math.max(Math.abs(dx), Math.abs(dy)));
            } else if (this.heuristic == 3) {    // chebyshev
                return (Math.max(Math.abs(dx), Math.abs(dy)) + Math.sqrt(2) - 1 * Math.min(Math.abs(dx), Math.abs(dy)));
            }else {
                return 0;
            }
            // heuristic = 0 -> equivalent to Dijkstra
            //return 0;
        });

        for (int i = 0; i < ROWS; i++) { // fill the grid
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new Node(new Point(i, j));
                graph.addNode(grid[i][j]);             // add nodes to graph
            }
        }

        //Vertical Linking

        for (int i = 0; i < ROWS - 1; i++) {
            for (int j = 0; j < COLS; j++) {
                Node top = grid[i][j];
                Node bottom = grid[i + 1][j];
                if (!obstaclesGrid[i][j] && !obstaclesGrid[i + 1][j]) {
                    graph.link(top, bottom, TILE_SIZE);
                }
            }
        }

        //Horizontal
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS - 1; j++) {
                Node left = grid[i][j];
                Node right = grid[i][j + 1];
                if (!obstaclesGrid[i][j] && !obstaclesGrid[i][j + 1]) {
                    graph.link(left, right, TILE_SIZE);
                }
            }
        }

        Node<Point> startNode = grid[startX][startY];
        Node<Point> targetNode = grid[targetX][targetY];

        List<Node<Point>> path = new ArrayList<>(); //final path

        graph.findPath(startNode, targetNode, path);

        //path.forEach(node -> System.out.println(node.getObj()));

        return path;

    }

    public List<Node<Point>> getNodes() {
        return graph.getNodes();
    }
}