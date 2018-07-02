package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    Map<String,Object> k;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scrolling, container, false);
        GetNews();
        // Instanciating an array list (you don't need to do this,
        // you already have yours).

        return myView;
}
    public void GetNews(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("News")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Item> itemsnf = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData() + "\n\n\n");
                                Item msg = new Item((String) k.get("Name"),(String) k.get("Date"),(String) k.get("Post"), 1);
                                itemsnf.add(msg);
                            }


                            BindDictionary<Item> dictionary = new BindDictionary<>();
                            dictionary.addStringField(R.id.itemNom, new StringExtractor<Item>() {
                                @Override
                                public String getStringValue(Item itemsnf, int position) {
                                    return itemsnf.getName();
                                }
                            });
                            dictionary.addStringField(R.id.itemDate, new StringExtractor<Item>() {
                                @Override
                                public String getStringValue(Item itemsnf, int position) {
                                    return "" + itemsnf.getDate();
                                }
                            });
                            dictionary.addStringField(R.id.itemPost, new StringExtractor<Item>() {
                                @Override
                                public String getStringValue(Item itemsnf, int position) {
                                    return "" + itemsnf.getMessage();
                                }
                            });
                           /* dictionary.addStringField(R.id.itemPhoto, new StringExtractor<Item>() {
                                @Override
                                public String getStringValue(Item itemsnf, int position) {
                                    return "" + itemsnf.getPhotonbr();
                                }
                            });
                            */
                            FunDapter adapter = new FunDapter(NewsFragment.this.getActivity(), itemsnf, R.layout.item_layout, dictionary);

                            ListView lvItem = (ListView)myView.findViewById(R.id.list_itemnf);
                            lvItem.setAdapter(adapter);



                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }
}
