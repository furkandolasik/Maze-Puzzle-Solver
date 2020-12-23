package com.furkandolasik.mazepuzzle.algorithms.astarpathfinding;

import java.util.*;

/**
 * pathFinding.Graph class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Graph<T> {

    private final List<Node<T>> nodes = new ArrayList<>();
    private final Heuristic<T> heuristic;

    public Graph(Heuristic<T> heuristic) {
        this.heuristic = heuristic;
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public List<Node<T>> getNodes() {
        return nodes;
    }

    public Heuristic<T> getHeuristic() {
        return heuristic;
    }

    public void link(Node a, Node b, double cost) {
        Edge edge = new Edge(cost, a, b);
        a.addEdge(edge);
        b.addEdge(edge);
    }

    public void findPath(Node<T> start, Node<T> target, List<Node<T>> path) {
        nodes.forEach(node -> {
            node.setState(Node.State.UNVISITED);
            node.setBackPathNode(null);
            node.setG(Double.MAX_VALUE);
        });

        start.setG(0);
        start.setH(heuristic.calculate(start, target, start));

        PriorityQueue<Node<T>> activeNodes = new PriorityQueue<>();
        activeNodes.add(start);

        while (!activeNodes.isEmpty()) {
            Node<T> currentNode = activeNodes.poll();
            currentNode.setState(Node.State.CLOSED);

            // target node found !
            if (currentNode == target) {
                path.clear();
                target.retrievePath(path);
                return;
            }

            currentNode.getEdges().forEach((edge) -> {
                Node<T> neighborNode = edge.getOppositeNode(currentNode);
                double neighborG = currentNode.getG() + edge.getG();
                if (!neighborNode.isBlocked()
                        && neighborG < neighborNode.getG()) {

                    neighborNode.setBackPathNode(currentNode);
                    neighborNode.setG(neighborG);
                    double h = heuristic.calculate(start, target, neighborNode);
                    neighborNode.setH(h);
                    if (!activeNodes.contains(neighborNode)) {
                        activeNodes.add(neighborNode);
                        neighborNode.setState(Node.State.OPEN);
                    }
                }
            });
        }
    }

}
