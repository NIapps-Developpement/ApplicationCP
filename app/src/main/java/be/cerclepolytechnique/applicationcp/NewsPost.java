package be.cerclepolytechnique.applicationcp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NewsPost extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_news);
        final String name = Login.getName();
        final String photoNbr = Login.getPhotoNbr();
        final EditText message = findViewById(R.id.message);
        final CharSequence text2 = "Veuillez entrer un message";
        final int duration = Toast.LENGTH_SHORT;
        final Context context = getApplicationContext();
        final String[] check = new String[1];
        Button send = findViewById(R.id.send_message);
        Button retour = findViewById(R.id.retour_message);
        String service;

        retour.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(NewsPost.this, MainActivity.class);
                NewsPost.this.startActivity(mainIntent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check[0] = message.getText().toString();

                if(check[0].isEmpty()){
                    Toast toast = Toast.makeText(context, text2, duration);
                    toast.show();
                }
                else {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MMM  H:m");
                String date = df.format(c);
                String post = String.valueOf(message.getText());
                SetNews(name, post, date, photoNbr);}

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
                        String SENDER_ID = "93750736124";
                        FirebaseMessaging fm = FirebaseMessaging.getInstance();
                        AtomicInteger msgId = new AtomicInteger();
                        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                                .setMessageId(Integer.toString(msgId.incrementAndGet()))
                                .addData("my_message", "Hello World")
                                .addData("my_action","SAY_HELLO")
                                .build());
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
