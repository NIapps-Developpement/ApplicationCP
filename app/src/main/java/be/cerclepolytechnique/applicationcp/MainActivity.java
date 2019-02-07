package be.cerclepolytechnique.applicationcp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> wlist;
    String[] listItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    Map<String,Object> k;


    private static final String TAG = "MainActivity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            ImageButton button = findViewById(R.id.send_button);
            ImageButton parambutton = findViewById(R.id.param_button);
            switch (item.getItemId()) {


                case R.id.navigation_home:


                    parambutton.setBackground(getResources().getDrawable(android.R.drawable.ic_menu_manage));
                    button.setBackground(getResources().getDrawable(R.drawable.ic_send));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Intent mainIntent = new Intent(MainActivity.this, Login.class);
                            MainActivity.this.startActivity(mainIntent);
                        }
                    });
                    parambutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            mBuilder.setTitle(R.string.dialog_title);
                            mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                                    if(isChecked){
                                        mUserItems.add(position);
                                    }else{
                                        mUserItems.remove((Integer.valueOf(position)));
                                    }
                                }
                            });
                            mBuilder.setCancelable(false);
                            mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    String item = "";
                                    for (int i = 0; i < mUserItems.size(); i++) {
                                        //  System.out.println(listItems[mUserItems.get(i)]);
                                        if (i != mUserItems.size() - 1) {
                                            item = item + ", ";
                                        }
                                    }
                                    SaveData(mUserItems);
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame
                                                    , new NewsFragment())
                                            .commit();
                                }
                            });

                            mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    ArrayList<String> testlist = LoadData();
                                    System.out.println(testlist);
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame
                                                    , new NewsFragment())
                                            .commit();
                                }
                            });

                            mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    for (int i = 0; i < checkedItems.length; i++) {
                                        checkedItems[i] = false;
                                        mUserItems.clear();
                                        SaveData(mUserItems);
                                        android.app.FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.content_frame
                                                        , new NewsFragment())
                                                .commit();
                                    }
                                }
                            });

                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }
                    });
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new NewsFragment())
                            .commit();
                    return true;


                case R.id.navigation_dashboard:

                    parambutton.setBackgroundResource(0);
                    button.setBackground(getResources().getDrawable(R.drawable.ic_note_add));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Intent mainIntent = new Intent(MainActivity.this, CalendarLogin.class);
                            MainActivity.this.startActivity(mainIntent);
                        }
                    });
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new CalendarFragment())
                            .commit();
                    return true;


                case R.id.navigation_notifications:
                    parambutton.setBackgroundResource(0);
                    button.setBackgroundResource(0);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new ComiteFragment())
                            .commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listItems = getResources().getStringArray(R.array.shopping_item);
        checkedItems = new boolean[listItems.length];
        //RGEIRPGHEGBE
        LoadData();
        ImageButton button = findViewById(R.id.send_button);
        ImageButton parambutton = findViewById(R.id.param_button);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new NewsFragment())
                .commit();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(mainIntent);
            }
        });
        parambutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle(R.string.dialog_title);
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
//                        if (isChecked) {
//                            if (!mUserItems.contains(position)) {
//                                mUserItems.add(position);
//                            }
//                        } else if (mUserItems.contains(position)) {
//                            mUserItems.remove(position);
//                        }
                        if(isChecked){
                            mUserItems.add(position);
                        }else{
                            mUserItems.remove((Integer.valueOf(position)));
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                          //  System.out.println(listItems[mUserItems.get(i)]);
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        SaveData(mUserItems);
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame
                                        , new NewsFragment())
                                .commit();
                    }
                });

                mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        ArrayList<String> testlist = LoadData();
                        System.out.println(testlist);
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame
                                        , new NewsFragment())
                                .commit();
                    }
                });

                mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkedItems.length; i++) {
                            checkedItems[i] = false;
                            mUserItems.clear();
                            SaveData(mUserItems);
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.content_frame
                                            , new NewsFragment())
                                    .commit();
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }
    public void SaveData(ArrayList<Integer> wlist){
        SharedPreferences.Editor editor = getSharedPreferences("PARAMDELEG", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(wlist);
        editor.putString("PARAMDELEGLIST", json);
        editor.apply();
    }
    public ArrayList<String> LoadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("PARAMDELEG", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("PARAMDELEGLIST", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        wlist = gson.fromJson(json, type);

        if (wlist == null){
            wlist = new ArrayList<>();
        }
        return wlist;

    }
    public void SetNews(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> Post = new HashMap<>();
        Post.put("Name", "Ilan Rossler");
        Post.put("Post", "QUEDESKEHS2");
        Post.put("Date", "01/07");
        Post.put("PhotoNbr", "1");

// Add a new document with a generated ID
        db.collection("News")
                .add(Post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public void GetNews(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + k.get("Post")+ "\n\n\n");

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }
}