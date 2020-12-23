package com.furkandolasik.mazepuzzle.algorithms;

import java.util.*;

public interface InterfaceMazeSolver {

    void setConnections();
    void setHeuristicZero();
    void setHeuristicChebyshev();
    void setHeuristicOctile();
    void setHeuristicEuclidean();


    Stack<Node> searchPath(boolean algorithm);
}
