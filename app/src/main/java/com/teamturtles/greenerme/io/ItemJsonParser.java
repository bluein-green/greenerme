
package com.teamturtles.greenerme.io;

import com.teamturtles.greenerme.model.Item;

import org.json.JSONObject;

public class ItemJsonParser {
    public Item parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String name = jsonObject.optString("name");
        String category = jsonObject.optString("category");
        int id = jsonObject.optInt("objectID", -1);

        if (name != null && category != null && id >= 0) {
            Item item = new Item();
            item.setName(name);
            item.setCategory(category);
            item.setId(id);
            return item;
        }

        return null;
    }


}
