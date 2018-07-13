package be.cerclepolytechnique.applicationcp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewsPost extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_news);
        final String name = Login.getName();
        final EditText message = findViewById(R.id.message);
        Button send = findViewById(R.id.send_message);
        Button retour = findViewById(R.id.retour_message);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(NewsPost.this, MainActivity.class);
                NewsPost.this.startActivity(mainIntent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MMM  H:m");
                String date = df.format(c);
                String post = String.valueOf(message.getText());
                SetNews(name, post, date, "1");
            }
        });

    }
    public void SetNews(String name, String post, String date, String photonbr){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> Post = new HashMap<>();
        Post.put("Name", name);
        Post.put("Post", post);
        Post.put("Date", date);
        Post.put("PhotoNbr", photonbr);

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
        final Intent mainIntent = new Intent(NewsPost.this, MainActivity.class);
        NewsPost.this.startActivity(mainIntent);
    }
}
