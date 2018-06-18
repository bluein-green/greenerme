package com.teamturtles.greenerme;

public class Item {
    public String name;
    public String category;
    public int recyclability;   // 0: no, 1: general yes, 2: yes
    public String procedure;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public Item() {
    }

    public Item (String name, String category) {
        this.name = name;
        this.category = category;
    }

}
