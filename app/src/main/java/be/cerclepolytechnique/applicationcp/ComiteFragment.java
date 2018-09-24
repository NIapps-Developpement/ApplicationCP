package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.ami.fundapter.interfaces.DynamicImageLoader;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class ComiteFragment extends Fragment {
    private static final String TAG = "InfoFragment";
    Map<String,Object> k;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_scrolling_info, container, false);
        GetInfo();
        return myView;
    }

    private void GetInfo() {
        final Context contextnet = this.getActivity().getApplicationContext();

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else {
            Toast toast = Toast.makeText(contextnet, "Aucune connexion internet. Veuillez vous connecter à un réseau avant de réessayer. ", Toast.LENGTH_LONG);
            toast.show();
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Info").orderBy("Tri")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final FirebaseStorage storage = FirebaseStorage.getInstance();
                        if (task.isSuccessful()) {
                            final ArrayList<ItemInfo> iteminf = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + document.getData() + "\n\n\n");
                                ItemInfo msg = new ItemInfo((String) k.get("Name"),(String) k.get("Role"),(String) k.get("Facebook"),(String) k.get("Mail"),(String) k.get("Phone"), (String) k.get("PhotoNbr"));
                                iteminf.add(msg);
                            }


                            BindDictionary<ItemInfo> dictionary = new BindDictionary<>();
                            dictionary.addStringField(R.id.nom, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return iteminf.getName();
                                }
                            });
                            dictionary.addStringField(R.id.role, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return "" + iteminf.getRole();
                                }
                            });
                            dictionary.addStringField(R.id.facebook, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return "" + iteminf.getFacebook();
                                }
                            });
                            dictionary.addStringField(R.id.mail, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return "" + iteminf.getMail();
                                }
                            });
                            dictionary.addStringField(R.id.phone, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return "" + iteminf.getPhone();
                                }
                            });

                            dictionary.addDynamicImageField(R.id.photo,
                                    new StringExtractor<ItemInfo>() {

                                        @Override
                                        public String getStringValue(ItemInfo iteminf, int position) {
                                            return iteminf.getPhotonbr();

                                        }

                                    }, new DynamicImageLoader() {
                                        @Override
                                        public void loadImage(String photonbr, ImageView image) {
                                            image.setClipToOutline(true);
                                            System.out.println("testi");
                                            String url = "gs://application-cp.appspot.com/PhotosMembres/" + photonbr + ".jpg";
                                            StorageReference gsReference = storage.getReferenceFromUrl(url);
                                            Glide.with(getActivity())
                                                    .using(new FirebaseImageLoader())
                                                    .load(gsReference)
                                                    .into(image);
                                        }

                                    });




                           /* dictionary.addStringField(R.id.itemPhoto, new StringExtractor<ItemInfo>() {
                                @Override
                                public String getStringValue(ItemInfo iteminf, int position) {
                                    return "" + iteminf.getPhotonbr();
                                }
                            });
                            */
                            FunDapter adapter = new FunDapter(ComiteFragment.this.getActivity(), iteminf, R.layout.item_layout_info, dictionary);

                            ListView lvItem = (ListView)myView.findViewById(R.id.list_iteminfo);
                            lvItem.setAdapter(adapter);




                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });

    }
}