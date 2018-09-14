package be.cerclepolytechnique.applicationcp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static String name;
    public static String photoNbr;
    Map<String, Object> k;
    EditText login;
    final List<String> CodeList = new ArrayList<>();
    final List<String> NameList = new ArrayList<>();
    final List<String> PhotoNbrList = new ArrayList<>();
    final CharSequence text1 = "Votre code n'est pas correct";
    final int duration = Toast.LENGTH_SHORT;



    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_news);
        final Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        Button confirm = findViewById(R.id.confirm_login);
        Button retour = findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent mainIntent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(mainIntent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              GetCode(v);
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void GetCode(final Vibrator v){
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
                                NameList.add((String) k.get("Name"));
                                PhotoNbrList.add((String) k.get("PhotoNbr"));

                                Log.d(TAG, String.valueOf(CodeList));
                            }
                        } else {
                        }
                        final Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, text1, duration);
                        CheckCode(toast, v);
                    }
                });
    }
    private void CheckCode(Toast toast, Vibrator v) {
        int i = 0;
        EditText login = findViewById(R.id.login);
        String UserCode = login.getText().toString();
        final Context context = getApplicationContext();
        for (String E : CodeList){
            v.cancel();
            i += 1;
            if(UserCode.equals(E)){
                setName(NameList.get(i-1));
                setPhotoNbr(PhotoNbrList.get(i-1));

                final Intent mainIntent = new Intent(Login.this, NewsPost.class);
                Login.this.startActivity(mainIntent);

                toast.cancel();
            }
            else {
                v.vibrate(500);
                toast.show();}
        }

    }
    public static String getName(){
        return name;
    }
    public void setName(String nom){
           name = nom;
    }
    public static String getPhotoNbr(){
        return photoNbr;
    }
    public void setPhotoNbr(String photoNombre){
        photoNbr = photoNombre;
    }

}


