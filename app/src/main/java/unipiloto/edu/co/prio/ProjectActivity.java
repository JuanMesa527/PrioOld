package unipiloto.edu.co.prio;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProjectActivity extends AppCompatActivity implements OnMapReadyCallback {

    private PrioDatabaseHelper dbHelper;
    private double lat, lng;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        dbHelper = new PrioDatabaseHelper(this);

        ImageView logoImageView = findViewById(R.id.logoImageView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView budgetTextView = findViewById(R.id.budgetTextView);
        TextView datesTextView = findViewById(R.id.datesTextView);
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        TextView localityTextView = findViewById(R.id.localityTextView);

        Project item = getIntent().getParcelableExtra("item");

        if (item != null) {
            logoImageView.setImageResource(item.getLogoResId());
            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
            budgetTextView.setText("Presupuesto: " + item.getBudget());
            datesTextView.setText("Fechas: " + item.getStartDate() + " - " + item.getEndDate());
            categoryTextView.setText("Categoría: " + dbHelper.getCategoryName(item.getCategoryId()));
            localityTextView.setText("Localidad: " + dbHelper.getLocalityName(item.getLocalityId()));
        }

        String localizacion = item.getAddress();
        String salida = localizacion.substring(10, localizacion.indexOf(')'));
        String[] partes = salida.split(",");
        lat=Double.parseDouble(partes[0]);
        lng=Double.parseDouble(partes[1]);
        title = item.getTitle();


        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng initialLocation = new LatLng(lat, lng);
        googleMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("Localizacion Proyecto" + title));
    }


}