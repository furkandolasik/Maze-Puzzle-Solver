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
        boolean[][] matrix = new boolean[nodeData.length()][nodeData.getJSONArray(0).length()];
        for (int i = 0; i < nodeData.length(); i++) {
            JSONArray temp = nodeData.getJSONArray(i);
            for (int j = 0; j < temp.length(); j++) {
                matrix[i][j] = temp.getInt(j) == 1;
            }
        }

        MazeSolver aStar = new MazeSolver(matrix, startX, startY, endX, endY);
        Stack<Node> shortestPath = aStar.getPath();
        HashMap visited = aStar.getVisited();

        Map<String, Object> response = new HashMap<>();

        List<String> visitedList = new ArrayList<>();
        for (Node node : shortestPath) {
            System.out.println(node.getX() + "," + node.getY());
            visitedList.add(node.getX() + "," + node.getY());
        }
        response.put("shortestPath", visitedList);
        //System.out.println(response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
