package com.furkandolasik.mazepuzzle.solver;

import java.util.*;

public interface InterfaceMazeSolver {

    void setConnections();
    void setHeuristic();
    Stack<Node> searchPath();
}
