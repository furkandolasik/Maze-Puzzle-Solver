package com.furkandolasik.mazepuzzle.algorithms.bfs;

import java.util.*;

public interface InterfaceMazeSolver {

    void setConnections();
    void setHeuristicManhattan();
    void setHeuristicZero();
    void setHeuristicChebyshev();
    void setHeuristicOctile();
    void setHeuristicEuclidean();


    Stack<MyNode> searchPath(boolean algorithm);
}
