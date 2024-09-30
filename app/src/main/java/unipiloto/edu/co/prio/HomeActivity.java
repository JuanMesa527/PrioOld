package unipiloto.edu.co.prio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private PrioDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        dbHelper = new PrioDatabaseHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        ArrayList<Project> projects = dbHelper.getAllProjects();
        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<Project> listAdapter = new ArrayAdapter<Project>(this, R.layout.card_item, projects) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
                }

                Project currentItem = getItem(position);

                ImageView logoImageView = convertView.findViewById(R.id.logoImageView);
                TextView titleTextView = convertView.findViewById(R.id.titleTextView);
                TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);

                logoImageView.setImageResource(currentItem.getLogoResId());
                titleTextView.setText(currentItem.getTitle());
                descriptionTextView.setText(currentItem.getDescription());

                convertView.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ProjectActivity.class);
                    intent.putExtra("item", currentItem);
                    startActivity(intent);
                });
                return convertView;
            }
        };
        listView.setAdapter(listAdapter);

    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("userEmail");
        editor.apply();

        Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(loginIntent);
        finish();
    }
}