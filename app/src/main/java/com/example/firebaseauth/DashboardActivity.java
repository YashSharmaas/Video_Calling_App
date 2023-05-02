package com.example.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;


import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {


    EditText codeBox;
    TextView hellotxt;
    Button logoutBtn,joinBtn,ShareBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logoutBtn=findViewById(R.id.logout_btn);
        joinBtn=findViewById(R.id.joinbtn);
        ShareBtn=findViewById(R.id.sharebtn);
        codeBox=findViewById(R.id.roomcode);
        hellotxt=findViewById(R.id.helloTxt);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        hellotxt.setText("Email: "+user.getEmail());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");

            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)

                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code;
                code = String.valueOf(codeBox.getText());

                if (code.length() > 0 ){
//                    Toast.makeText(DashboardActivity.this, "Please Enter valid room Code or minimum six character", Toast.LENGTH_SHORT).show();
                }

                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(codeBox.getText().toString())
                        .build();

                JitsiMeetActivity.launch(DashboardActivity.this, options);
            }
        });

        ShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = codeBox.getText().toString();
                Intent intent= new Intent();
                intent.setAction(intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,string);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });

    }
}