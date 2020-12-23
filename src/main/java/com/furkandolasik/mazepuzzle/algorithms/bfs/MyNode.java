package com.furkandolasik.mazepuzzle.algorithms.bfs;

import java.util.*;


public class MyNode implements InterfaceNode {

    private boolean open = true;
    private boolean visited = false;
    private boolean selected = false;
    private boolean obstacle = false;
    private int x;
    private int y;

    private MyNode parent;
    private final Collection<MyNode> children = new ArrayList<MyNode>();
    private MyNode predecessor;
    private int cost = 0;
    private double heuristic = 0;


    public void reset() {
        predecessor = null;
        cost = 0;
        open = true;
        visited = false;
        selected = false;
        obstacle = false;
    }

    public void setX(int x){
        this.x=x;

    }
    public void setY(int y){
        this.y=y;

    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    @Override
    public boolean isOpen() {
        return open;
    }


    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }


    @Override
    public boolean isObstacle() {
        return obstacle;
    }


    @Override
    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    @Override
    public boolean isVisited() {
        return visited;
    }


    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    @Override
    public boolean isSelected() {
        return selected;
    }


    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean addChild(MyNode child) {

        return this.children.add(child);
    }



    @Override
    public Collection<MyNode> getChildren() {
        return this.children;
    }

    public MyNode getParent(){
        return parent;
    }

    public void setParent(MyNode parent){
        this.parent = parent;
    }



    @Override
    public MyNode getPredecessor() {
        return predecessor;
    }


    @Override
    public void setPredecessor(MyNode myNode) {
        this.predecessor = myNode;
    }


    @Override
    public int getCost() {
        return this.cost;
    }


    @Override
    public void setCost(int cost) {
        this.cost = cost;
    }


    @Override
    public double getHeuristic() {
        return heuristic;
    }


    @Override
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(heuristic + cost);
        sb.append("(");
        sb.append(cost);
        sb.append(",");
        sb.append(heuristic);
        sb.append(")");;
        return sb.toString();
    }

}
