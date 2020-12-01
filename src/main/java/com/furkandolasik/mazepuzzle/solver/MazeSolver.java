package com.furkandolasik.mazepuzzle.solver;

import java.util.*;

public class MazeSolver implements InterfaceMazeSolver{

    private Node[][] matrix;
    private HashMap<Node,Integer> visited;
    private Stack<Node> path;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public MazeSolver(boolean[][] arr,int startX,int startY,int endX,int endY){
        //System.out.println("constructer maze solver girdi.");
        matrix = new Node[arr.length][arr[0].length];
        this.startX=startX;
        this.startY = startY;
        this.endX = endX;
        this.endY=endY;

        for(int i = 0; i<matrix.length;i++){ // obstacle ya da değil init
            for(int j=0;j<matrix[0].length;j++){
                matrix[i][j] = new Node();
                matrix[i][j].setX(i);
                matrix[i][j].setY(j);
                matrix[i][j].setObstacle(arr[i][j]);
            }
        }
        setHeuristic();
        setConnections();
        path = searchPath();

        for (Node nodes : path){
            System.out.print(" --> "+nodes.getX()+" "+nodes.getY());

        }
    }

    public HashMap<Node,Integer> getVisited (){
        return visited;
    }
    public Stack<Node> getPath(){
        return path;
    }

    public Stack<Node> searchPath(){

        visited = new HashMap<Node, Integer>();
        ArrayList<Node> potential = new ArrayList<Node>();
        Stack<Node> path = new Stack<Node>();
        boolean cont = true;
        int counter = 0;
        int solverCounter=0;
        Node temp = matrix[startX][startY];

        while (cont){
            int minHeuristic = Integer.MAX_VALUE;
            visited.put(temp,temp.getHeuristic());
            for(Node nodes : temp.getChildren()){
                if(!visited.containsKey(nodes)){
                    nodes.setParent(temp);
                }
            }
            temp.getChildren().forEach(child -> {
                if(!visited.containsKey(child) && !potential.contains(child)){
                    potential.add(child);
                    //System.out.println("+ + + + + + +  + + +");
                   // System.out.println(child.getX()+" "+child.getY());
                   // System.out.println("+ + + + + + +  + + +");
                }
            });
            for (Node child : potential){
                if(child.getHeuristic()<minHeuristic){
                    minHeuristic = child.getHeuristic();
                }
            }
            for (Node child : potential){
                if(child.getHeuristic()==minHeuristic){
                    temp=child;
                }
            }
            //System.out.println("P P P P P P P");
            potential.remove(temp);
            //System.out.println(temp.getX()+" "+temp.getY());
            //System.out.println("P P P P P P P P ");
            if(temp.getX()==endX && temp.getY()==endY){
                System.out.println("FOUND!");
                cont=false;
                while (!temp.equals(matrix[startX][startY])){
                    path.push(temp);
                    temp = temp.getParent();
                }
                path.push(temp);
            }
            if(solverCounter>(matrix.length*matrix[0].length*matrix.length*matrix.length)){
                System.out.println("can't find a path.");
                break;
            }
            solverCounter++;
        }
        /*
        for(Node node : visited.keySet()){
            System.out.println(node.getX()+" "+node.getY());
        }
        System.out.println("- - -- - - - -- -- -- - - -  --");
        for(Node node : potential){
            System.out.println(node.getX()+" "+node.getY());
        }
         */
        return path;
    }
    public void setConnections(){

        for(int i = 0; i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++){

                if(matrix[i][j].isObstacle()){
                       continue;
                }
                else if(!matrix[i][j].isObstacle() && i==0 && j==0){  //sol üst
                    if(!matrix[i][j+1].isObstacle()){ // sol üstün sağı için
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i+1][j].isObstacle()){ //sol üstün altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && i==0&& j==(matrix[0].length -1) ){ // sağ üst

                    if(!matrix[i+1][j].isObstacle()){ // sağ üstün altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //sol üstün solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && i==(matrix.length -1)&& j==0 ){ // sol alt

                    if(!matrix[i][j+1].isObstacle()){ // sol altın sağı
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i-1][j].isObstacle()){ //sol altın üstü
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && i==(matrix.length -1)&& j==(matrix[0].length -1) ){ // sağ alt

                    if(!matrix[i-1][j].isObstacle()){ //sağ altın üstü
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //sağ altın solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && i==0){ // üst satır

                    if(!matrix[i+1][j].isObstacle()){ // üst satırın altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                    if(!matrix[i][j+1].isObstacle()){ // üst satırın sağı
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //üst satırın solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && i==(matrix.length -1)){ // alt satır

                    if(!matrix[i][j+1].isObstacle()){ // alt satırın sağı
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i-1][j].isObstacle()){ //alt satırın üstü
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //alt satırın solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && j==0){ // sol sütun

                    if(!matrix[i+1][j].isObstacle()){ // sol sütun altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                    if(!matrix[i][j+1].isObstacle()){ // sol sütun sağı
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i-1][j].isObstacle()){ //sol sütun üst
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                }
                else if (!matrix[i][j].isObstacle() && j==(matrix[0].length -1)){ // sağ sütun

                    if(!matrix[i+1][j].isObstacle()){ // sağ sütun altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                    if(!matrix[i-1][j].isObstacle()){ //sağ sütun üstü
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //sağ sütun solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
                else { // orta kare
                    //System.out.println(""+i+" "+j);

                    if(!matrix[i+1][j].isObstacle()){ // orta kare altı
                        matrix[i][j].addChild(matrix[i+1][j]);
                    }
                    if(!matrix[i][j+1].isObstacle()){ // orta kare sağı
                        matrix[i][j].addChild(matrix[i][j+1]);
                    }
                    if(!matrix[i-1][j].isObstacle()){ //orta kare üstü
                        matrix[i][j].addChild(matrix[i-1][j]);
                    }
                    if(!matrix[i][j-1].isObstacle()){ //orta kare solu
                        matrix[i][j].addChild(matrix[i][j-1]);
                    }
                }
            }
        }
    }
    public void setHeuristic(){
        for(int i = 0; i<matrix.length;i++){
            for(int j=0;j<matrix[0].length;j++) {
                matrix[i][j].setHeuristic(Math.abs(i-endX) + Math.abs(j-endY));
            }
        }
    }
    public static void main(String args[]){  // tryout dummy main
        boolean arr[][] = new boolean[6][6];
        for(int i = 0; i<arr.length;i++){
            for(int j=0;j<arr[0].length;j++) {
                arr[i][j]=false;
            }
        }
        arr[2][2]=true;
        arr[2][3]=true;
        arr[1][3]=true;
        arr[3][3]=true;
        arr[4][3]=true;
        arr[0][3]=true;


        MazeSolver m = new MazeSolver(arr,2,1,2,4);
    }
}
