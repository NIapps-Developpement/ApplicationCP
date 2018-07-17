package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.ami.fundapter.interfaces.DynamicImageLoader;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    Map<String,Object> k;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scrolling, container, false);
        ImageButton button = getActivity().findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(getActivity(), Login.class);
                getActivity().startActivity(mainIntent);
            }
        });
        GetNews();

        // Instanciating an array list (you don't need to do this,
        // you already have yours).

        return myView;
    }
    public void GetNews(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("News").orderBy("Date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final FirebaseStorage storage = FirebaseStorage.getInstance();
                        if (task.isSuccessful()) {
                            final ArrayList<Item> itemsnf = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData() + "\n\n\n");
                                Item msg = new Item((String) k.get("Name"),(String) k.get("Date"),(String) k.get("Post"), (String) k.get("PhotoNbr"));
                                itemsnf.add(msg);
                            }

                            Collections.reverse(itemsnf);

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

                            dictionary.addDynamicImageField(R.id.itemPhoto,
                                    new StringExtractor<Item>() {

                                        @Override
                                        public String getStringValue(Item itemsnf, int position) {
                                            return itemsnf.getPhotonbr();

                                        }
                                    }, new DynamicImageLoader() {
                                        @Override
                                        public void loadImage(String photonbr, ImageView image) {
                                            System.out.println("testi");
                                            String url = "gs://application-cp.appspot.com/PhotosEvents/" + photonbr + ".jpg";
                                            StorageReference gsReference = storage.getReferenceFromUrl(url);
                                            Glide.with(getActivity())
                                                    .using(new FirebaseImageLoader())
                                                    .load(gsReference)
                                                    .into(image);
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