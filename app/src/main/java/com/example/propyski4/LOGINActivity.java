package com.example.propyski4;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LOGINActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button Prosmotor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validate(username, password)) {
                    // Переход к другой активности
                    Intent intent = new Intent(LOGINActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Показать сообщение об ошибке
                    Toast.makeText(LOGINActivity.this, "Неверные учетные данные", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ButtonProsmotr();

    }
    public  void ButtonProsmotr(){
        Prosmotor = (Button) findViewById(R.id.Prosmotor);
        Prosmotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LOGINActivity.this, Excele_Vivod_Dannix.class);
                startActivity(intent);
            }
        });

    }

    private boolean validate(String username, String password) {
        // Здесь вы можете проверить учетные данные пользователя
        return ("Камленок".equals(username) && "1234".equals(password)) ||
                ("Соловей".equals(username) && "4321".equals(password));
    }

}

