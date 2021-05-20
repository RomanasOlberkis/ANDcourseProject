package com.example.chatproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin, passEtLogin;
    Button LoginBtn, registerBtn;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;


protected void onStart(){
    super.onStart();
    firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

    if(firebaseUser !=null){
        Intent i = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        userETLogin = findViewById(R.id.editText);
        passEtLogin = findViewById(R.id.editText3);
        LoginBtn = findViewById(R.id.Loginbtn);
        registerBtn=findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser !=null){
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }



        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Login_Activity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
            String email_text = userETLogin.getText().toString();
            String pass_text = passEtLogin.getText().toString();

            if (TextUtils.isEmpty(email_text)|| TextUtils.isEmpty(pass_text)){
                Toast.makeText(Login_Activity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            }
            else{ auth.signInWithEmailAndPassword(email_text, pass_text)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(Login_Activity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            Toast.makeText(Login_Activity.this, "Login failure", Toast.LENGTH_SHORT).show();
                        }

                     });
            }
            }
        });
    }
}