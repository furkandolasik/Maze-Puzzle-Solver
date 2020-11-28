package com.furkandolasik.mazepuzzle.controller;

import com.furkandolasik.mazepuzzle.model.Grid;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GridController {

    @GetMapping("/")
    public ResponseEntity<Grid> getGridInfo() {
        Grid myGrid = new Grid();
        myGrid.setName("Onur");
        myGrid.setSurname("Albayrak");
        return new ResponseEntity<>(myGrid, HttpStatus.OK);
    }

    @PostMapping("/nodeData")
    public void getNodeData(@RequestBody Map<String, Object> map) {
        JSONObject data = new JSONObject(map); //jsona çevirdik
        JSONArray nodeData = data.getJSONArray("nodes");
        for (int i = 0; i < nodeData.length(); i++) {
            System.out.println(nodeData.get(i));
        }
    }
}
