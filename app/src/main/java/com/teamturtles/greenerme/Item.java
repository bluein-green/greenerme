package com.teamturtles.greenerme;

public class Item {
    private String name;
    private int recyclableStatus;   // 0: no, 1: general yes, 2: yes
    private String procedure;
    private boolean hdb_recyclable;
    private boolean separated_recyclable;
    private boolean others_recyclable;


    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public Item() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setRecyclability(int recyclabileStatus) {
        this.recyclableStatus = recyclabileStatus;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public void setStatus(boolean hdb_recyclable, boolean separated_recyclable, boolean others_recyclable) {
        this.hdb_recyclable = hdb_recyclable;
        this.separated_recyclable = separated_recyclable;
        this.others_recyclable = others_recyclable;
    }

    public String getName() {
        return name;
    }

    public int getRecyclability() {
        return recyclableStatus;
    }

    public String getProcedure() {
        return procedure;
    }

    public boolean getHdbRecyclable() {
        return hdb_recyclable;
    }

    public boolean getSeparatedRecyclable() {
        return separated_recyclable;
    }

    public boolean getOthersRecyclable() {
        return others_recyclable;
    }

    @Override
    public String toString() {
        String disp_result = "";
        disp_result += name + ": " + Integer.toString(recyclableStatus);
        return disp_result;
    }

}
