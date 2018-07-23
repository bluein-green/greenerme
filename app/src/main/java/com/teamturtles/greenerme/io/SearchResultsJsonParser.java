package com.teamturtles.greenerme.io;

import com.teamturtles.greenerme.model.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsJsonParser {

    private ItemJsonParser itemParser = new ItemJsonParser();
    public List<Item> parseResults(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        List<Item> results = new ArrayList<>();
        JSONArray hits = jsonObject.optJSONArray("hits");
        if (hits == null) {
            return null;
        }

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) {
                continue;
            }

            Item item = itemParser.parse(hit);
            if (item == null) {
                continue;
            }

            results.add(item);
        }
        return results;
    }
}