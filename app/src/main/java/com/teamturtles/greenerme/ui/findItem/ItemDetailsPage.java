package com.teamturtles.greenerme.ui.findItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.teamturtles.greenerme.ui.main.HomePage_loggedin;
import com.teamturtles.greenerme.model.Item;
import com.teamturtles.greenerme.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemDetailsPage extends AppCompatActivity {
    // displays
    private ImageButton home_btn;
    private TextView itemName_txt;
    private TextView category_txt;
    private ImageView recyclable_icon;
    private TextView recyclable_txt;
    private TextView howto_txt;
    private ImageView hdb_result;
    private ImageView sep_result;
    private ImageView sep_icon;
    private ImageView item_pic;
    private List<TextView> procedure_txts;
    private TextView procedure_txt1;
    private TextView procedure_txt2;
    private TextView procedure_txt3;
    private TextView procedure_txt4;
    private TextView procedure_txt5;    // MAX: 5 lines in text view

    // firebase references
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private DatabaseReference itemRef;
    private FirebaseStorage storage;
    private StorageReference stoRef;
    private StorageReference picRef;

    // local file for picture
    private DisplayImageOptions displayImageOptions;
    private ImageLoader imageLoader;
    private File localfile;
    private String picId;
    private int itemId;

    // item instance
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_page);

        // get intent that started this activity and extract item id
        Intent intent = getIntent();
        itemId = intent.getIntExtra("ITEM_ID", 0);

        // set firebase references to pull item details
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference();
        itemRef = dbReference.child("Items").child(Integer.toString(itemId));
        storage = FirebaseStorage.getInstance();
        stoRef = storage.getReference();

        picId = Integer.toString(itemId) + ".png";
        picRef = stoRef.child(picId);

        // set View references
        setViewRefs();

/*
        // Configure Universal Image Loader.
        displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLoader = ImageLoader.getInstance();
                ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(ItemDetailsPage.this)
                        .memoryCacheSize(2 * 1024 * 1024)
                        .memoryCacheSizePercentage(13) // default
                        .build();
                imageLoader.init(configuration);

                item_pic.post(new Runnable() {
                    @Override
                    public void run() {
                        imageLoader.displayImage(result.getResult().getImage(), posterImageView, displayImageOptions);
                    }
                })
            }
        }).start();
*/




        // get item from database
        itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item = dataSnapshot.getValue(Item.class);

                // display item name
                itemName_txt.setText(item.getName());

                // display category name
                String category = item.getCategory();
                category_txt.setText(category);

                // display item picture
                // TODO: upload item pictures + pull and display
                // TODO: set the procedures!!!

                // display procedures
                List<String> procedure = item.getProcedure();
                initProcedureViewRefs(procedure.size());
                displayProcedures(item.getProcedure());

                // set recyclable icon
                int recyclability = item.getRecyclability(); // 0: no, 1: yes, 2: yes with special notes (and exceptions), 3: no with special notes

                if (recyclability == 0 || recyclability == 3) {   // not recyclable
                    displayNotRecyclable();

                    // if there is a special note, toggle the "special note" message

                } else {    // recyclable
                    displayGeneralRecyclable();

                    // display HDB & separated bins recyclable status
                    displayHdbBinStatus(item.getHdbRecyclable());
                    displaySepBinStatus(item.getSeparatedRecyclable());

                    // if category is one of the common ones, display indiv separated bin
                    if (category.equals("Paper") || category.equals("Plastic")
                            || category.equals("Glass") || category.equals("Metal")) {  // TODO: take note "metal" recycling is mostly for cans, need to indicate in the sep recyclable!!
                        displayIndivSepBin(category);
                    }

                    // if there is a special note, toggle the "special note" message
                    // TODO: create special note UI and figure out placement
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // display item picture
        // displayItemPic();




        // set click listener for BACK TO HOME
        setBackToHomeClickListener();
    }

    private void displayNotRecyclable() {
        recyclable_icon.setImageResource(R.drawable.cross);
        recyclable_txt.setText(R.string.Det_txt_notRecyclable);
        recyclable_txt.setTextColor(Color.parseColor("#F05228"));
        howto_txt.setText(R.string.Det_whattodo);
        displayHdbBinStatus(false);
        displaySepBinStatus(false);
    }


    private void displayGeneralRecyclable() {
        recyclable_icon.setImageResource(R.drawable.checked);
        recyclable_txt.setText(R.string.Det_txt_recyclable);
    }

    private void displayIndivSepBin(String category) {
        if (category.equals("Plastic")) {
            sep_icon.setImageResource(R.drawable.plastic_recycling_bin);
        } else if (category.equals("Paper")) {
            sep_icon.setImageResource(R.drawable.paper_recycling_bin);
        } else if (category.equals("Glass")) {
            sep_icon.setImageResource(R.drawable.glass_recycling_bin);
        } else if (category.equals("Metal")) {
            sep_icon.setImageResource(R.drawable.cans_recycling_bin);
        }
    }

    private void displayHdbBinStatus(boolean isHdbRecyclable) {
        if (isHdbRecyclable) {
            hdb_result.setImageResource(R.drawable.checked);
        } else {
            hdb_result.setImageResource(R.drawable.cross);
        }
    }

    private void displaySepBinStatus(boolean isSepRecyclable) {
        if (isSepRecyclable) {
            sep_result.setImageResource(R.drawable.checked);
        } else {
            sep_result.setImageResource(R.drawable.cross);
        }
    }

    // TODO: can try using Glide instead??
    private void displayItemPic() {
        if (fileExist(picId)) {
            // load the picture from file
            File file = getBaseContext().getCacheDir();
            loadPicture(file);
            System.out.println("I have ");
        } else {
            downloadItemPic();
        }
    }

    // TODO: these don't work!!!!
    private boolean fileExist(String fileName) {
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    private void loadPicture(File fileName) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName.getAbsolutePath());
        item_pic.setImageBitmap(bitmap);
    }

    private void downloadItemPic() {
        try {
            localfile = File.createTempFile("image", "png");       // TODO: 820 x 500 pics
            picRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // loaded file
                    loadPicture(localfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // handle any errors
                    System.out.println("error in downloading pic");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewRefs() {
        itemName_txt = (TextView) findViewById(R.id.Det_item_name_txt);
        category_txt = (TextView) findViewById(R.id.Det_cat_name_txt);
        recyclable_icon = (ImageView) findViewById(R.id.Det_recyclable_icon);
        recyclable_txt = (TextView) findViewById(R.id.Det_recyclable_txt);
        howto_txt = (TextView) findViewById(R.id.Det_how_txt);
        hdb_result = (ImageView) findViewById(R.id.Det_hdb_result);
        sep_result = (ImageView) findViewById(R.id.Det_sep_result);
        sep_icon = (ImageView) findViewById(R.id.Det_sep_icon);
        home_btn = (ImageButton) findViewById(R.id.Det_backtohome_btn);
        item_pic = (ImageView) findViewById(R.id.Det_itempic);

        // procedure texts
        procedure_txt1 = (TextView) findViewById(R.id.Det_procedure1);
        procedure_txt2 = (TextView) findViewById(R.id.Det_procedure2);
        procedure_txt3 = (TextView) findViewById(R.id.Det_procedure3);
        procedure_txt4 = (TextView) findViewById(R.id.Det_procedure4);
        procedure_txt5 = (TextView) findViewById(R.id.Det_procedure5);

        procedure_txts = new ArrayList<>();
        procedure_txts.add(procedure_txt1);
        procedure_txts.add(procedure_txt2);
        procedure_txts.add(procedure_txt3);
        procedure_txts.add(procedure_txt4);
        procedure_txts.add(procedure_txt5);

    }

    private void initProcedureViewRefs(int size) {
        for (int i = size; i < 5; i++) {
            procedure_txts.get(i).setVisibility(View.GONE);
            System.out.println("hiding procedure: " + i);
        }
    }

    private void displayProcedures(List<String> procedure) {
        for (int i = 0; i < procedure.size(); i++) {
            procedure_txts.get(i).setText(Html.fromHtml(procedure.get(i)));
        }
    }

    private void setBackToHomeClickListener() {
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailsPage.this, HomePage_loggedin.class);
                startActivity(intent);
            }
        });
    }
}

