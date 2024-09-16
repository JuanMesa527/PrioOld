package unipiloto.edu.co.prio;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnadirProyectoActivity extends AppCompatActivity {
    private EditText editTextDate;
    private DatePicker dpFecha;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anadir_proyecto);
        editTextDate = findViewById(R.id.startTextDate);
        dpFecha = findViewById(R.id.dpFecha);
        dpFecha.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> getDate());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void getDate() {
        int day = dpFecha.getDayOfMonth();
        int month = dpFecha.getMonth() + 1;
        int year = dpFecha.getYear();
        editTextDate.setText(day + "/" + month + "/" + year);
    }
    public void mostrarCalendario(View view) {
        dpFecha.setVisibility(View.VISIBLE);
    }
}