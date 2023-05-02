package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText signMail,signPass,nameBox;
    Button reg_btn;
    TextView t2;
    ProgressBar progressBar2;
    FirebaseAuth mAuth;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signMail=findViewById(R.id.reg_email);
        signPass=findViewById(R.id.regg_pass);
        nameBox  = findViewById(R.id.name);

        reg_btn=findViewById(R.id.regbtn);
        t2=findViewById(R.id.accou);

        progressBar2=findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);

                String email,password,namee;
                email = String.valueOf(signMail.getText());
                password=String.valueOf(signPass.getText());
                namee=String.valueOf(nameBox.getText());

                User user = new User(email, password, namee);
                user.setEmail(email);
                user.setPass(password);
                user.setName(namee);


                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)  && TextUtils.isEmpty(namee) ){
                    progressBar2.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "You have entered wrong credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar2.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    database.collection("Users")
                                            .add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(RegisterActivity.this, "Successfully Account Created", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });

            }
        });

    }
}