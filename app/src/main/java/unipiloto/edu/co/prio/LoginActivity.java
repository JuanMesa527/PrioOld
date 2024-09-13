package unipiloto.edu.co.prio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private PrioDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new PrioDatabaseHelper(this);
    }

    public void login(View v) {
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();

        boolean isCorrect = dbHelper.getLogin(emailText, passwordText);
        if (isCorrect) {
            // Guardar el estado de inicio de sesión en SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userEmail", emailText);
            editor.apply();

            Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(loginIntent);
            finish(); // Finalizar LoginActivity para que no se pueda volver con el botón atrás
        } else {
            Toast.makeText(this, "Credenciales incorrectas o usuario inexistente", Toast.LENGTH_SHORT).show();
        }
    }
}