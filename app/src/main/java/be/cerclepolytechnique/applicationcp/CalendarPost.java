package be.cerclepolytechnique.applicationcp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.protobuf.StringValue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CalendarPost extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_calendar);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final String name = Login.getName();
        final String photoNbr = Login.getPhotoNbr();
        final EditText eventDescr = findViewById(R.id.event_text);
        final EditText eventName = findViewById(R.id.event_name);
        final EditText dateday = findViewById(R.id.even_day);
        final EditText datemonth = findViewById(R.id.even_month);
        final EditText dateyear = findViewById(R.id.even_year);

        Button send = findViewById(R.id.send_message);
        Button retour = findViewById(R.id.retour_message);
        Button addPhoto = findViewById(R.id.add_pic);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(CalendarPost.this, MainActivity.class);
                CalendarPost.this.startActivity(mainIntent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postName = String.valueOf(eventName.getText());
                String postDesc = String.valueOf(eventDescr.getText());
                String Date = String.valueOf(dateday.getText() + "/" + datemonth.getText() + "/" + dateyear.getText());
                upload(postName, postDesc, Date);
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });

    }
    private void choose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();

        }
    }
    private void upload(final String postName, final String postDesc, final String date) {

        if(filePath != null)
        {
            System.out.println("BAAAAAAAAAAH");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("PhotosEvents/"+ postName);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> Post = new HashMap<>();
                            Post.put("Name", postName);
                            Post.put("Descr", postDesc);
                            Post.put("Date", date);

// Add a new document with a generated ID
                            db.collection("Events")
                                    .add(Post)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressDialog.dismiss();
                                            Toast.makeText(CalendarPost.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                            final Intent mainIntent = new Intent(CalendarPost.this, MainActivity.class);
                                            CalendarPost.this.startActivity(mainIntent);
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
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CalendarPost.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}
