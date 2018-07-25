package com.teamturtles.greenerme.io;

import com.teamturtles.greenerme.Highlight;
import com.teamturtles.greenerme.HighlightedResult;
import com.teamturtles.greenerme.model.Item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsJsonParser {

    private ItemJsonParser itemParser = new ItemJsonParser();

    /**
     * Parse the root result JSON object into a list of results.
     *
     * @param jsonObject The result's root object.
     * @return A list of results (potentially empty), or null in case of error.
     */
    public List<HighlightedResult<Item>> parseResults(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        List<HighlightedResult<Item>> results = new ArrayList<>();
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

            // System.out.println(item.getName() + " " + item.getCategory() + " " + item.getId());

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null) {
                continue;
            }

            JSONObject highlightName = highlightResult.optJSONObject("name");
            if (highlightName == null)
                continue;
            String value = highlightName.optString("value");
            if (value == null)
                continue;
            HighlightedResult<Item> result = new HighlightedResult<>(item);
            result.addHighlight("name", new Highlight("name", value));

            results.add(result);
        }
        return results;
    }
}