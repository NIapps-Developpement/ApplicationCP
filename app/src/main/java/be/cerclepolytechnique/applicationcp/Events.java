package be.cerclepolytechnique.applicationcp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Events extends AppCompatActivity {
    TextView mTextMessage;
    ImageView image;
    private static final String TAG = "Events";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trash_layout);
        image = (ImageView)findViewById(R.id.imageView);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("PhotosEvents/6h-cuistax.png");
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(islandRef)
                .into(image);


    }
    public void SetEvents(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> Post = new HashMap<>();
        Post.put("Name", "Ilan Rossler");
        Post.put("PostName", "Event");
        Post.put("PostDescription", "Description");

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
}
