package be.cerclepolytechnique.applicationcp;

import android.app.Fragment;
import android.media.Image;
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

import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.extractors.StringExtractor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

    public class ComiteFragment extends Fragment {

        View myView;
        ImageView ilan;
        ImageView maxime;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myView = inflater.inflate(R.layout.activity_scrolling_info, container, false);
            ilan = (ImageView) myView.findViewById(R.id.photo);
            ilan.setClipToOutline(true);
            maxime = (ImageView) myView.findViewById(R.id.photo2);
            maxime.setClipToOutline(true);
            return myView;
        }
    }