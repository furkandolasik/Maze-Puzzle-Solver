package com.furkandolasik.mazepuzzle.solver;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MazeSolver implements InterfaceMazeSolver {

    private final Node[][] matrix;
    private HashMap<Node, Double> visited;
    private ArrayList<Node> visitedBFS;
    private final Stack<Node> path;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    ArrayList<Node> potential;
    private int searchNo;

    public MazeSolver(boolean[][] arr, int searchNo, int heuristicNo, int startX, int startY, int endX, int endY) {
        matrix = new Node[arr.length][arr[0].length];
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.searchNo=searchNo;

        for (int i = 0; i < matrix.length; i++) { // obstacle ya da değil init
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = new Node();
                matrix[i][j].setX(i);
                matrix[i][j].setY(j);
                matrix[i][j].setObstacle(arr[i][j]);
            }
        }
        setConnections();

        if (searchNo==0){ // AStar or Dijkstra
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
            path = searchPath();
        }
        else { // BFS
            path = searchPathBFS();
        }
    }
    public Stack<Node> searchPathBFS() {

        Stack<Node> path = new Stack<Node>();
        Node start = matrix[startX][startY];
        visitedBFS = new ArrayList<Node>();
        potential = new ArrayList<Node>();
        ArrayList<Node> tempPotential = new ArrayList<Node>();
        AtomicBoolean flag= new AtomicBoolean(true);
        AtomicBoolean potentialFlag= new AtomicBoolean(true);

        visitedBFS.add(start);
        start.getChildren().forEach( child ->{
            child.setParent(start);
            if(!visitedBFS.contains(child) && !potential.contains(child)){
                tempPotential.add(child);
            }
        });

        while(potentialFlag.get()){
            potential = (ArrayList<Node>) tempPotential.clone();
            tempPotential.clear();
            potential.forEach( p -> {
                if(p.equals(matrix[endX][endY]) && flag.get()){
                    Node iter = p;
                    path.add(iter);
                    flag.set(false);
                    potentialFlag.set(false);
                    while(!iter.getParent().equals(matrix[startX][startY])){
                        path.add(iter.getParent());
                        iter = iter.getParent();
                    }
                    path.add(iter.getParent());
                }
                else{
                    p.getChildren().forEach(child -> {
                        if(!visitedBFS.contains(child) && !tempPotential.contains(child)){
                            child.setParent(p);
                            tempPotential.add(child);
                        }
                    });
                    visitedBFS.add(p);
                }
            });
        }

        return path;
    }
    public ArrayList<Node> getVisited() {
        if(searchNo==0){
            ArrayList<Node> visitedArraylist = new ArrayList<Node>();
            for(Node nodes : visited.keySet()){
                visitedArraylist.add(nodes);
            }
            return visitedArraylist;
        }
        else{
            return visitedBFS;
        }
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
                matrix[i][j].setHeuristic(0);

            }
        }
    }
    public static void main(String args[]){

        boolean[][] maze = new boolean[5][5];

        for(int i =0;i<maze.length;i++){
            for(int j=0;j<maze[0].length;j++){
                maze[i][j]=false;
            }
        }
        maze[0][2]=true;
        maze[1][1]=true;
        maze[2][2]=true;
        maze[3][2]=true;

        MazeSolver m = new MazeSolver(maze,1,123,0,0,1,3);
        System.out.println("PATH NODES - - - - -");
        for(Node nodes : m.getPath()){
            System.out.println(nodes.getX()+" "+nodes.getY());
        }
        System.out.println("VISITED NODES - - - - -");
        for(Node nodes : m.getVisited()){
            System.out.println(nodes.getX()+" "+nodes.getY());
        }
        System.out.println("POTENTIAL NODES - - - - -");
        for(Node nodes : m.getPath()){
            System.out.println(nodes.getX()+" "+nodes.getY());
        }
    }
}