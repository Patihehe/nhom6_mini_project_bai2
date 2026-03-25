package com.example.nhom6_mini_project_bai2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom6_mini_project_bai2.MainActivity;
import com.example.nhom6_mini_project_bai2.R;
import com.example.nhom6_mini_project_bai2.dal.AppDatabase;
import com.example.nhom6_mini_project_bai2.entities.User;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = AppDatabase.getInstance(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String user = etUsername.getText().toString();
            String pass = etPassword.getText().toString();

            User loggedInUser = db.shoppingDao().login(user, pass);
            if (loggedInUser != null) {
                SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                prefs.edit().putInt("user_id", loggedInUser.userId).apply();
                prefs.edit().putBoolean("is_logged_in", true).apply();

                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Instead of always going to MainActivity, just return to the previous screen
                // if there is one. If we came from a 'Select' action, finishing will take us back.
                if (isTaskRoot()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
