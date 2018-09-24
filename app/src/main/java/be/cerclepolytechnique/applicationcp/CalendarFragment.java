package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.ami.fundapter.interfaces.DynamicImageLoader;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CalendarFragment extends Fragment{
    private static final String TAG = "CalFragment";
    Map<String,Object> k;

    View myView;
    ImageButton button;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scrolling_cal, container, false);

        ImageButton fragment_relative= myView.findViewById(R.id.testbutcal);
        fragment_relative.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                final Intent mainIntent = new Intent(getActivity(), CalendarLogin.class);
                getActivity().startActivity(mainIntent);
                return true;

            }

        });
        GetCalendar();

        return myView;
    }

    private void GetCalendar() {
        final Context contextnet = this.getActivity().getApplicationContext();

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else {
            Toast toast = Toast.makeText(contextnet, "Aucune connexion internet. Veuillez vous connecter à un réseau avant de réessayer. ", Toast.LENGTH_SHORT);
            toast.show();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events").orderBy("Date")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final FirebaseStorage storage = FirebaseStorage.getInstance();
                        if (task.isSuccessful()) {
                            final ArrayList<ItemCal> itemsnf = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData() + "\n\n\n");
                                ItemCal msg = new ItemCal((String) k.get("Name"),(String) k.get("Date"),(String) k.get("Descr"), (String) k.get("PhotoNbr"));
                                itemsnf.add(msg);
                            }

                            Collections.reverse(itemsnf);

                            BindDictionary<ItemCal> dictionary = new BindDictionary<>();
                            dictionary.addStringField(R.id.itemTitre, new StringExtractor<ItemCal>() {
                                @Override
                                public String getStringValue(ItemCal itemsnf, int position) {
                                    return itemsnf.getName();
                                }
                            });
                            dictionary.addStringField(R.id.itemDate, new StringExtractor<ItemCal>() {
                                @Override
                                public String getStringValue(ItemCal itemsnf, int position) {
                                    return "" + itemsnf.getDate();
                                }
                            });
                            dictionary.addStringField(R.id.itemText, new StringExtractor<ItemCal>() {
                                @Override
                                public String getStringValue(ItemCal itemsnf, int position) {
                                    return "" + itemsnf.getMessage();
                                }
                            });

                            dictionary.addDynamicImageField(R.id.itemPhoto,
                                    new StringExtractor<ItemCal>() {

                                        @Override
                                        public String getStringValue(ItemCal itemsnf, int position) {
                                            return itemsnf.getName();

                                        }

                                    }, new DynamicImageLoader() {
                                        @Override
                                        public void loadImage(String name, ImageView image) {
                                            image.setClipToOutline(true);
                                            System.out.println("testical" + name);

                                            String url = "gs://application-cp.appspot.com/PhotosEvents/" + name;
                                            StorageReference gsReference = storage.getReferenceFromUrl(url);
                                            Glide.with(getActivity())
                                                    .using(new FirebaseImageLoader())
                                                    .load(gsReference)
                                                    .into(image);
                                        }

                                    });




                           /* dictionary.addStringField(R.id.itemPhoto, new StringExtractor<ItemCal>() {
                                @Override
                                public String getStringValue(ItemCal itemsnf, int position) {
                                    return "" + itemsnf.getPhotonbr();
                                }
                            });
                            */
                            FunDapter adapter = new FunDapter(CalendarFragment.this.getActivity(), itemsnf, R.layout.item_layout_cal, dictionary);

                            ListView lvItem = (ListView)myView.findViewById(R.id.list_itemcal);
                            lvItem.setAdapter(adapter);




                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }


}