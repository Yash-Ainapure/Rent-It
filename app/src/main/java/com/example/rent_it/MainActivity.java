package com.example.rent_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button register,login;
    EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=findViewById(R.id.register);
        login=findViewById(R.id.login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        register.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            String email = username.getText().toString();
            String pass =password.getText().toString();

            // Check if email and password are not empty
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                // Use Firebase Authentication to sign in
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, Home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // Display a message if email or password is empty
                Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}