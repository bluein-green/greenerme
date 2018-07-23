
package com.teamturtles.greenerme.io;

import com.teamturtles.greenerme.model.Item;

import org.json.JSONObject;

public class ItemJsonParser {
    public Item parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String name = jsonObject.optString("itemName");
        int id = jsonObject.optInt("objectID");

        if (name != null) {
            Item item = new Item();
            item.setName(name);
            item.setId(id);
            return item;
        } else {
            return null;
        }
    }


}
