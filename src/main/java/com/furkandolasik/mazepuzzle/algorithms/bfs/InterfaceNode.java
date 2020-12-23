package com.furkandolasik.mazepuzzle.algorithms.bfs;

import java.util.*;



public interface InterfaceNode {

    Collection<MyNode> getChildren();

    MyNode getPredecessor();

    void setPredecessor(MyNode myNode);

    public boolean isOpen();

    public void setOpen(boolean open);

    public boolean isObstacle();

    public void setObstacle(boolean obstacle);

    public boolean isVisited();

    public void setVisited(boolean visited);

    public boolean isSelected();

    public MyNode getParent();

    public void  setParent(MyNode parent);

    public void setSelected(boolean selected);

    double getHeuristic();

    void setHeuristic(double heuristic);

    int getCost();

    void setCost(int cost);

}