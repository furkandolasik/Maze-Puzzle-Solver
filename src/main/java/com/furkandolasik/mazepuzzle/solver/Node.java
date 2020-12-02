package com.furkandolasik.mazepuzzle.solver;

import java.util.*;


public class Node implements InterfaceNode {

    private boolean open = true;
    private boolean visited = false;
    private boolean selected = false;
    private boolean obstacle = false;
    private int x;
    private int y;

    private Node parent;
    private final Collection<Node> children = new ArrayList<Node>();
    private Node predecessor;
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

    public boolean addChild(Node child) {

        return this.children.add(child);
    }



    @Override
    public Collection<Node> getChildren() {
        return this.children;
    }

    public Node getParent(){
        return parent;
    }

    public void setParent(Node parent){
        this.parent = parent;
    }



    @Override
    public Node getPredecessor() {
        return predecessor;
    }


    @Override
    public void setPredecessor(Node node) {
        this.predecessor = node;
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