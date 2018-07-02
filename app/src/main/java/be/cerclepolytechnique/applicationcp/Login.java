package be.cerclepolytechnique.applicationcp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    Map<String, Object> k;
    EditText login;
    final List<String> CodeList = new ArrayList<>();


    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_news);

        Button confirm = findViewById(R.id.confirm_login);
        Button retour = findViewById(R.id.retour);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  AsyncCaller asy = new AsyncCaller();
                asy.execute();
                */
              GetCode();
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void GetCode(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Codes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k = document.getData();
                                Log.d(TAG, document.getId() + " => " + k + "\n\n\n");
                                CodeList.add((String) k.get("Code"));
                                Log.d(TAG, String.valueOf(CodeList));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        CheckCode();
                    }
                });
    }
    private void CheckCode() {
        EditText login = findViewById(R.id.login);
        String UserCode = login.getText().toString();
        if (CodeList.contains(UserCode)) {
            final Intent mainIntent = new Intent(Login.this, NewsPost.class);
            Login.this.startActivity(mainIntent);
        } else {
            Log.d(TAG, "WAAAAAAAAH");
        }
    }

}


