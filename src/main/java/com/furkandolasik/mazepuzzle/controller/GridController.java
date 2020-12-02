package com.furkandolasik.mazepuzzle.controller;

import com.furkandolasik.mazepuzzle.solver.MazeSolver;
import com.furkandolasik.mazepuzzle.solver.Node;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class GridController {

    @PostMapping("/nodeData")
    public ResponseEntity<Map> getNodeData(@RequestBody String input) {
        JSONObject data = new JSONObject(input); //jsona çevirdik
        JSONArray nodeData = data.getJSONArray("nodes");

        int startX = data.getInt("startX");
        int startY = data.getInt("startY");
        int endX = data.getInt("endX");
        int endY = data.getInt("endY");

        int heuristicNo = data.getInt("heuristic");

        System.out.println("Heuristic: " + heuristicNo);

        int ROWS = nodeData.length();
        int COLS = nodeData.getJSONArray(0).length();


        boolean[][] matrix = new boolean[ROWS][COLS];
        for (int i = 0; i < nodeData.length(); i++) {
            JSONArray temp = nodeData.getJSONArray(i);
            for (int j = 0; j < temp.length(); j++) {
                matrix[i][j] = temp.getInt(j) == 1;
            }
        }

        long startTime = System.currentTimeMillis();
        MazeSolver aStar = new MazeSolver(matrix, heuristicNo, startX, startY, endX, endY);
        long endTime = System.currentTimeMillis();
        System.out.println("duration: " + (endTime - startTime));

        Stack<Node> shortestPath = aStar.getPath();
        List<String> shortestPathList = new ArrayList<>();
        if (shortestPath.size() != 0) {
            for (Node node : shortestPath) {
                //System.out.println(node.getX() + "," + node.getY());
                shortestPathList.add(node.getX() + "," + node.getY());
            }
        }

        ArrayList<Node> closedNodesList = aStar.getVisited();
        ArrayList<String> closedNodes = new ArrayList<>();
        for (Node node : closedNodesList) {
            closedNodes.add((node.getX() + "," + node.getY()));
        }


        ArrayList<Node> openNodesList = aStar.getPotential();
        ArrayList<String> openNodes = new ArrayList<>();
        for (Node node : openNodesList) {
            openNodes.add((node.getX() + "," + node.getY()));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("shortestPath", shortestPathList);
        response.put("openNodes", openNodes);
        response.put("closedNodes", closedNodes);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
