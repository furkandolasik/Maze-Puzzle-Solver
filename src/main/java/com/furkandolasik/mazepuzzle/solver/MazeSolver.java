package com.furkandolasik.mazepuzzle.solver;

import java.util.*;

public class MazeSolver implements InterfaceMazeSolver {

    private final Node[][] matrix;
    private HashMap<Node, Double> visited;
    private final Stack<Node> path;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    ArrayList<Node> potential;

    public MazeSolver(boolean[][] arr,int heuristicNo, int startX, int startY, int endX, int endY) {
        matrix = new Node[arr.length][arr[0].length];
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        for (int i = 0; i < matrix.length; i++) { // obstacle ya da değil init
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = new Node();
                matrix[i][j].setX(i);
                matrix[i][j].setY(j);
                matrix[i][j].setObstacle(arr[i][j]);
            }
        }
        if(heuristicNo==0){
            setHeuristicManhattan();
        }
        else if(heuristicNo==1){
            setHeuristicEuclidean();
        }
        else if(heuristicNo==2){
            setHeuristicOctile();
        }
        else if(heuristicNo==3){
            setHeuristicChebyshev();
        }
        else {
            setHeuristicZero();
        }
        setConnections();
        path = searchPath();
    }
    public ArrayList<Node> getVisited() {
        ArrayList<Node> visitedArraylist = new ArrayList<Node>();
        for(Node nodes : visited.keySet()){
            visitedArraylist.add(nodes);
        }
        return visitedArraylist;
    }
    public ArrayList<Node> getPotential(){
        return potential;
    }
    public Stack<Node> getPath() {
        return path;
    }
    public Stack<Node> searchPath() {

        visited = new HashMap<Node, Double>();
        potential = new ArrayList<Node>();
        Stack<Node> path = new Stack<Node>();
        boolean cont = true;
        int solverCounter = 0;
        Node temp = matrix[startX][startY];
        matrix[startX][startY].setCost(0);

        while (cont) {
            double minCost = Integer.MAX_VALUE;
            visited.put(temp, temp.getHeuristic());
            for (Node nodes : temp.getChildren()) {
                if (!visited.containsKey(nodes)) {
                    nodes.setParent(temp);
                    nodes.setCost(nodes.getParent().getCost()+1);
                }
            }
            temp.getChildren().forEach(child -> {
                if (!visited.containsKey(child) && !potential.contains(child)) {
                    potential.add(child);
                }
            });
            for (Node child : potential) {
                if (child.getHeuristic() + child.getCost() < minCost) {
                    minCost = child.getHeuristic() + child.getCost();
                }
            }
            for (Node child : potential) {
                if (child.getHeuristic() + child.getCost() == minCost) {
                    temp = child;
                }
            }
            potential.remove(temp);
            if (temp.getX() == endX && temp.getY() == endY) {
                System.out.println("FOUND!");
                while (!temp.equals(matrix[startX][startY])) {
                    path.push(temp);
                    temp = temp.getParent();
                }
                path.push(temp);
                break;
            }
            if (solverCounter > (matrix.length * matrix[0].length * matrix.length * matrix.length)) {
                System.out.println("can't find a path.");
                break;
            }
            solverCounter++;
        }
        return path;
    }
    public void setConnections() {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {

                if (matrix[i][j].isObstacle()) {
                    continue;
                } else if (!matrix[i][j].isObstacle() && i == 0 && j == 0) {  //sol üst
                    if (!matrix[i][j + 1].isObstacle()) { // sol üstün sağı için
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i + 1][j].isObstacle()) { //sol üstün altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                } else if (!matrix[i][j].isObstacle() && i == 0 && j == (matrix[0].length - 1)) { // sağ üst

                    if (!matrix[i + 1][j].isObstacle()) { // sağ üstün altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //sol üstün solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                } else if (!matrix[i][j].isObstacle() && i == (matrix.length - 1) && j == 0) { // sol alt

                    if (!matrix[i][j + 1].isObstacle()) { // sol altın sağı
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i - 1][j].isObstacle()) { //sol altın üstü
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                } else if (!matrix[i][j].isObstacle() && i == (matrix.length - 1) && j == (matrix[0].length - 1)) { // sağ alt

                    if (!matrix[i - 1][j].isObstacle()) { //sağ altın üstü
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //sağ altın solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                } else if (!matrix[i][j].isObstacle() && i == 0) { // üst satır

                    if (!matrix[i + 1][j].isObstacle()) { // üst satırın altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                    if (!matrix[i][j + 1].isObstacle()) { // üst satırın sağı
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //üst satırın solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                } else if (!matrix[i][j].isObstacle() && i == (matrix.length - 1)) { // alt satır

                    if (!matrix[i][j + 1].isObstacle()) { // alt satırın sağı
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i - 1][j].isObstacle()) { //alt satırın üstü
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //alt satırın solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                } else if (!matrix[i][j].isObstacle() && j == 0) { // sol sütun

                    if (!matrix[i + 1][j].isObstacle()) { // sol sütun altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                    if (!matrix[i][j + 1].isObstacle()) { // sol sütun sağı
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i - 1][j].isObstacle()) { //sol sütun üst
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                } else if (!matrix[i][j].isObstacle() && j == (matrix[0].length - 1)) { // sağ sütun

                    if (!matrix[i + 1][j].isObstacle()) { // sağ sütun altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                    if (!matrix[i - 1][j].isObstacle()) { //sağ sütun üstü
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //sağ sütun solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                } else { // orta kare

                    if (!matrix[i + 1][j].isObstacle()) { // orta kare altı
                        matrix[i][j].addChild(matrix[i + 1][j]);
                    }
                    if (!matrix[i][j + 1].isObstacle()) { // orta kare sağı
                        matrix[i][j].addChild(matrix[i][j + 1]);
                    }
                    if (!matrix[i - 1][j].isObstacle()) { //orta kare üstü
                        matrix[i][j].addChild(matrix[i - 1][j]);
                    }
                    if (!matrix[i][j - 1].isObstacle()) { //orta kare solu
                        matrix[i][j].addChild(matrix[i][j - 1]);
                    }
                }
            }
        }
    }
    public void setHeuristicManhattan() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setHeuristic(Math.abs(i - endX) + Math.abs(j - endY));

            }
        }
    }
    public void setHeuristicEuclidean() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setHeuristic(Math.sqrt((i - endX)*(i - endX) + (j - endY)*(j - endY)));

            }
        }
    }
    public void setHeuristicOctile() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setHeuristic(Math.max(Math.abs(i - endX),Math.abs(j - endY)));

            }
        }
    }
    public void setHeuristicChebyshev() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setHeuristic((Math.max(Math.abs(i - endX) , Math.abs(j - endY)) + Math.sqrt(2)-1*Math.min(Math.abs(i - endX) , Math.abs(j - endY))));

            }
        }
    }
    public void setHeuristicZero() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j].setHeuristic(Math.abs(i - endX) + Math.abs(j - endY));

            }
        }
    }
}