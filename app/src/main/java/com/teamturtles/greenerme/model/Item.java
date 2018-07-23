package com.teamturtles.greenerme.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Item {
    // implements Parcelable
    private String name;
    private String category;
    private int recyclableStatus;   // 0: no, 1: yes, 2: yes with special notes (and exceptions), 3: no with special notes
    private String procedure;
    private boolean hdb_recyclable;
    private boolean separated_recyclable;
    private boolean others_recyclable;
    private ArrayList<String> test;
    private int id;


    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public Item() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) { this.category = category; }

    public void setRecyclability(int recyclableStatus) {
        this.recyclableStatus = recyclableStatus;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public void setStatus(boolean hdb_recyclable, boolean separated_recyclable, boolean others_recyclable) {
        this.hdb_recyclable = hdb_recyclable;
        this.separated_recyclable = separated_recyclable;
        this.others_recyclable = others_recyclable;
    }

    public void setHdbRecyclable(boolean hdb_recyclable) {
        this.hdb_recyclable = hdb_recyclable;
    }

    public void setOthersRecyclable(boolean others_recyclable) {
        this.others_recyclable = others_recyclable;
    }

    public void setSeparatedRecyclable(boolean separated_recyclable) {
        this.separated_recyclable = separated_recyclable;
    }

    public void setId(int id) {
        this.id = id;
    }

    // public getters
    public String getName() {
        return name;
    }

    public String getCategory() { return category; }

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

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        String disp_result = "";
        disp_result += name + ": " + Integer.toString(recyclableStatus);
        return disp_result;
    }

    public void setTest(ArrayList<String> test) {
        this.test = test;
    }

    public ArrayList<String> getTest() {
        return test;
    }

    /*
    // Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    // constructor that takes a Parcel and gives you an object populated with its values
    private Item(Parcel in) {
        this.name = in.readString();
        this.recyclableStatus = in.readInt();
        this.procedure = in.readString();
        this.hdb_recyclable = in.readInt() != 0;
        this.separated_recyclable = in.readInt() != 0;
        this.others_recyclable = in.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write each field into the parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeString(name);
        dest.writeInt(recyclableStatus);
        dest.writeString(procedure);
        dest.writeInt((int) (hdb_recyclable ? 1 : 0));
        dest.writeInt((int) (separated_recyclable ? 1 : 0));
        dest.writeInt((int) (others_recyclable ? 1 : 0));
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

*/


}
