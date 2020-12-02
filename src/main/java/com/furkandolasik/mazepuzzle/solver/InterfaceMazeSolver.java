package com.furkandolasik.mazepuzzle.solver;

import java.util.*;

public interface InterfaceMazeSolver {

    void setConnections();
    void setHeuristicManhattan();
    void setHeuristicZero();
    void setHeuristicChebyshev();
    void setHeuristicOctile();
    void setHeuristicEuclidean();


    Stack<Node> searchPath();
}
