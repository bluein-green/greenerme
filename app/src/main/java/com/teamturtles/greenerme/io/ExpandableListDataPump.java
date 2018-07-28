package com.teamturtles.greenerme.io;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturtles.greenerme.model.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static LinkedHashMap<String, List<Pair>> getData() {
        final LinkedHashMap<String, List<Pair>> expandableListDetail = new LinkedHashMap<String, List<Pair>>();

        // set firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference();
        DatabaseReference catRef = dbReference.child("Item Categories");

        List<String> itemCategories = new ArrayList<>();
        itemCategories.add("Plastic");
        itemCategories.add("Paper");
        itemCategories.add("Metal");
        itemCategories.add("Glass");
        itemCategories.add("E-Waste");
        itemCategories.add("Others");

        for (int i = 0; i < itemCategories.size(); i++) {
            DatabaseReference specificCat = catRef.child(itemCategories.get(i));
            final List<Pair> items = new ArrayList<>();
            specificCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> itemSnapshots = dataSnapshot.getChildren();

                    for (DataSnapshot itemSnap : itemSnapshots) {
                        String itemName = itemSnap.getValue(String.class);
                        int id = Integer.parseInt(itemSnap.getKey());
                        items.add(new Pair(itemName, id));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            expandableListDetail.put(itemCategories.get(i).toUpperCase(), items);
        }

        return expandableListDetail;

        // TODO: organise items based on certain order (usage -- others at the bottom) [DONE]
        // TODO: pull from database properly -.- [DONE]
        // TODO: add the rule to optimise queries
        // TODO: refactor and use functions instead of hardcoding the order of display [DONE]
        // TODO: glass category [DONE]

    }
}




/*
        // HARDCODED WAY -------------------------------------
        DatabaseReference plasticCat = catRef.child("Plastic");
        DatabaseReference paperCat = catRef.child("Paper");
        DatabaseReference metalCat = catRef.child("Metal");
        DatabaseReference eWasteCat = catRef.child("E-waste");
        DatabaseReference othersCat = catRef.child("Others");


        // for paper
        final List<Pair> paper = new ArrayList<Pair>();
        paperCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    int id = Integer.parseInt(itemSnap.getKey());
                    paper.add(new Pair(itemName, id));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("PAPER", paper);
*/
        /*
        catRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> categories = dataSnapshot.getChildren();

                for (DataSnapshot cat : categories) {
                    // handle children - each category
                    String catName = cat.getKey();
                    System.out.println(catName);
                    List<String> itemsInCat = new ArrayList<String>();


                    for (DataSnapshot item : cat.getChildren()) {
                        Item actItem = item.getValue(Item.class);
                        String itemName = actItem.getName();
                        itemsInCat.add(itemName);
                        Log.d("adding", itemName);
                    }

                    expandableListDetail.put(catName, itemsInCat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
/*
        // for plastics
        final List<Pair> plastic = new ArrayList<Pair>();
        plasticCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    int id = Integer.parseInt(itemSnap.getKey());
                    plastic.add(new Pair(itemName, id));

                    System.out.println("id = " + id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("PLASTICS", plastic);


        // for metals
        final List<Pair> metal = new ArrayList<Pair>();
        metalCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    int id = Integer.parseInt(itemSnap.getKey());
                    metal.add(new Pair(itemName, id));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("METAL", metal);


        // for e-waste
        final List<Pair> eWaste = new ArrayList<Pair>();
        eWasteCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    int id = Integer.parseInt(itemSnap.getKey());
                    eWaste.add(new Pair(itemName, id));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("E-WASTE", eWaste);


        // for others
        final List<Pair> others = new ArrayList<Pair>();
        othersCat.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    int id = Integer.parseInt(itemSnap.getKey());
                    others.add(new Pair(itemName, id));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("OTHERS", others);
*/
