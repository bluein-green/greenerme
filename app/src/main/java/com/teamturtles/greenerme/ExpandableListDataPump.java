package com.teamturtles.greenerme;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListDataPump {
//    private static HashMap<String, Integer> nameToId;

    public static LinkedHashMap<String, List<String>> getData() {
        final LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();
//        nameToId = new HashMap<String, Integer>();

        // set firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference();
        DatabaseReference catRef = dbReference.child("Item Categories");

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
        // HARDCODED WAY -------------------------------------
        DatabaseReference plasticCat = catRef.child("Plastic");
        DatabaseReference paperCat = catRef.child("Paper");
        DatabaseReference metalCat = catRef.child("Metal");
        DatabaseReference eWasteCat = catRef.child("E-waste");
        DatabaseReference othersCat = catRef.child("Others");


        // for paper
        final List<String> paper = new ArrayList<String>();
        paperCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    // String itemName = item.getName();
                    paper.add(itemName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("PAPER", paper);
        // for plastics
        final List<String> plastic = new ArrayList<String>();
        plasticCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    String id = itemSnap.getKey();
                    //String itemName = item.getName();
                    plastic.add(itemName);

                    System.out.println("id = " + id);

                    // int id = Integer.parseInt(itemSnap.getKey());
                    // nameToId.put(itemName, id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("PLASTICS", plastic);


        // for metals
        final List<String> metal = new ArrayList<String>();
        metalCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    // String itemName = item.getName();
                    metal.add(itemName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("METAL", metal);


        // for e-waste
        final List<String> eWaste = new ArrayList<String>();
        eWasteCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    //String itemName = item.getName();
                    eWaste.add(itemName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("E-WASTE", eWaste);


        // for others
        final List<String> others = new ArrayList<String>();
        othersCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> items = dataSnapshot.getChildren();

                for (DataSnapshot itemSnap : items) {
                    String itemName = itemSnap.getValue(String.class);
                    // String itemName = item.getName();
                    others.add(itemName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expandableListDetail.put("OTHERS", others);

        return expandableListDetail;

        // TODO: organise items based on certain order (usage -- others at the bottom)
        // TODO: pull from database properly -.-







/*
        List<String> cricket = new ArrayList<String>();
        cricket.add("India");
        cricket.add("Pakistan");
        cricket.add("Australia");
        cricket.add("England");
        cricket.add("South Africa");

        List<String> football = new ArrayList<String>();
        football.add("Brazil");
        football.add("Spain");
        football.add("Germany");
        football.add("Netherlands");
        football.add("Italy");

        List<String> basketball = new ArrayList<String>();
        basketball.add("United States");
        basketball.add("Spain");
        basketball.add("Argentina");
        basketball.add("France");
        basketball.add("Russia");

        expandableListDetail.put("CRICKET TEAMS", cricket);
        expandableListDetail.put("FOOTBALL TEAMS", football);
        expandableListDetail.put("BASKETBALL TEAMS", basketball);
*/
//        List<String> plastics = new ArrayList<String>();
//        plastics.add("plastic bag");
//        plastics.add("water bottle");
//        expandableListDetail.put("PLASTICS", plastics);


//        for (Map.Entry<String, List<String>> entry : expandableListDetail.entrySet()) {
//            List<String> actList = entry.getValue();
//            for (String item : actList) {
//                Log.d("help", entry.getKey() + ": " + item);
//            }
//        }



    }
}