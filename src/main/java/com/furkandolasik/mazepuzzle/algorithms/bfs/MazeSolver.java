package com.furkandolasik.mazepuzzle.algorithms.bfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;



public class MazeSolver implements InterfaceMazeSolver {

    private final MyNode[][] matrix;
    private HashMap<MyNode, Double> visited;
    private ArrayList<MyNode> visitedBFS;
    private final Stack<MyNode> path;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    ArrayList<MyNode> potential;
    private int searchNo;

    public MazeSolver(boolean[][] arr, int searchNo, int heuristicNo, int startX, int startY, int endX, int endY) { //searchNo 0 ise A* 1 ise BestFirstSearch bşkaysa BFS yapar
        matrix = new MyNode[arr.length][arr[0].length];
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.searchNo=searchNo;

        for (int i = 0; i < matrix.length; i++) { // obstacle ya da değil init
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = new MyNode();
                matrix[i][j].setX(i);
                matrix[i][j].setY(j);
                matrix[i][j].setObstacle(arr[i][j]);
            }
        }
        setConnections();

        if (searchNo<2){ // AStar or Dijkstra
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
            if(searchNo==0){
                path = searchPath(true);
            }
            else{
                path = searchPath(false);
            }
        }
        else { // BFS
            path = searchPathBFS();
        }
    }
    public Stack<MyNode> searchPathBFS() {

        Stack<MyNode> path = new Stack<MyNode>();
        MyNode start = matrix[startX][startY];
        visitedBFS = new ArrayList<MyNode>();
        potential = new ArrayList<MyNode>();
        ArrayList<MyNode> tempPotential = new ArrayList<MyNode>();
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
            potential = (ArrayList<MyNode>) tempPotential.clone();
            tempPotential.clear();
            potential.forEach( p -> {
                if(p.equals(matrix[endX][endY]) && flag.get()){
                    MyNode iter = p;
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
    public ArrayList<MyNode> getVisited() {
        if(searchNo==0){
            ArrayList<MyNode> visitedArraylist = new ArrayList<MyNode>();
            for(MyNode nodes : visited.keySet()){
                visitedArraylist.add(nodes);
            }
            return visitedArraylist;
        }
        else{
            return visitedBFS;
        }
    }
    public ArrayList<MyNode> getPotential(){
        return potential;
    }
    public Stack<MyNode> getPath() {
        return path;
    }
    public Stack<MyNode> searchPath(boolean algorithm /* if true aStar else greedy BFS */ ) {

        visited = new HashMap<MyNode, Double>();
        potential = new ArrayList<MyNode>();
        Stack<MyNode> path = new Stack<MyNode>();
        boolean cont = true;
        int solverCounter = 0;
        MyNode temp = matrix[startX][startY];
        matrix[startX][startY].setCost(0);

        while (cont) {
            double minCost = Integer.MAX_VALUE;
            visited.put(temp, temp.getHeuristic());
            for (MyNode nodes : temp.getChildren()) {
                if (!visited.containsKey(nodes)) {
                    nodes.setParent(temp);
                    if(algorithm){
                        nodes.setCost(nodes.getParent().getCost()+1);
                    }
                }
            }
            temp.getChildren().forEach(child -> {
                if (!visited.containsKey(child) && !potential.contains(child)) {
                    potential.add(child);
                }
            });
            for (MyNode child : potential) {
                if (child.getHeuristic() + child.getCost() < minCost) {
                    minCost = child.getHeuristic() + child.getCost();
                }
            }
            for (MyNode child : potential) {
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

        boolean[][] maze = new boolean[32][32];

        for(int i =0;i<maze.length;i++){
            for(int j=0;j<maze[0].length;j++){
                maze[i][j]=false;
            }
        }
        maze[0][2]=true;
        maze[1][1]=true;
        maze[2][2]=true;
        maze[13][2]=true;
        maze[10][2]=true;
        maze[11][1]=true;
        maze[12][2]=true;
        maze[31][2]=true;
        maze[30][2]=true;
        maze[13][1]=true;
        maze[22][2]=true;
        maze[16][2]=true;
        maze[0][22]=true;
        maze[1][12]=true;
        maze[2][23]=true;
        maze[3][23]=true;
        maze[0][24]=true;
        maze[1][15]=true;
        maze[2][25]=true;
        maze[3][25]=true;

        MazeSolver m = new MazeSolver(maze,1,123,0,0,30,30);
        System.out.println("PATH NODES - - - - -");
        for(MyNode nodes : m.getPath()){
            System.out.println(nodes.getX()+" "+nodes.getY());
        }
        System.out.println("VISITED NODES - - - - -");
        for(MyNode nodes : m.getVisited()){
            System.out.println(nodes.getX()+" "+nodes.getY());
        }
        System.out.println("POTENTIAL NODES - - - - -");
        for(MyNode nodes : m.getPath()){
            if(!m.getVisited().contains(nodes))
                System.out.println(nodes.getX()+" "+nodes.getY());
        }
    }
}
