package wilfridlaurier.ianroberts.cp470_w8up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText rFullName, rEmail, rPassword;
    Button rRegisterBtn;
    TextView rLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String userID;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        database = FirebaseDatabase.getInstance();


        rFullName = findViewById(R.id.registerFullName);
        rEmail = findViewById(R.id.registerEmail);
        rPassword = findViewById(R.id.registerPwd);
        rRegisterBtn = findViewById(R.id.registerBtn);
        rLoginBtn = findViewById(R.id.registerLoginBtn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarRegister);

        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        rLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onRegisterBtnClick(View v)
    {
        String email = rEmail.getText().toString().trim();
        String password = rPassword.getText().toString().trim();
        String fullName = rFullName.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            rEmail.setError("Email is Required.");
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            rPassword.setError("Password is Required.");
            return;
        }

        if(password.length() < 6)
        {
            rPassword.setError("Password must be 6 or more characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //register the user in firebase + add user metadata to realtime database

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(RegisterActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                    userID = fAuth.getCurrentUser().getUid();
                    
                    // Map for user metadata
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("fullName",fullName);
                    userData.put("email",email);

                    DatabaseReference myRef = database.getReference("users/" + userID);
                    myRef.setValue(userData);

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Error ! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        hideKeyboard(this);
    }

    private void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}