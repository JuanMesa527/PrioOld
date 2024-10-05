package unipiloto.edu.co.prio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private PrioDatabaseHelper dbHelper;
    private ArrayAdapter<Project> listAdapter;
    private DrawerLayout drawerLayout;

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
        SearchView searchView = findViewById(R.id.busqueda);
        Button filterButton = findViewById(R.id.filter_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        listAdapter = new ArrayAdapter<Project>(this, R.layout.card_item, projects) {
            private List<Project> originalList = new ArrayList<>(projects);
            private List<Project> filteredList = new ArrayList<>(projects);

            @Override
            public int getCount() {
                return filteredList.size();
            }

            @Override
            public Project getItem(int position) {
                return filteredList.get(position);
            }

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

            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        if (constraint == null || constraint.length() == 0) {
                            results.values = originalList;
                            results.count = originalList.size();
                        } else {
                            List<Project> filteredItems = new ArrayList<>();
                            String filterPattern = constraint.toString().toLowerCase().trim();
                            for (Project item : originalList) {
                                if (item.getTitle().toLowerCase().contains(filterPattern) ||
                                        String.valueOf(item.getLocalityId()).contains(filterPattern)) {
                                    filteredItems.add(item);
                                }
                            }
                            results.values = filteredItems;
                            results.count = filteredItems.size();
                        }
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        filteredList = (List<Project>) results.values;
                        if (filteredList == null) {
                            filteredList = new ArrayList<>();
                        }
                        notifyDataSetChanged();
                    }
                };
            }
        };
        listView.setAdapter(listAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });

        filterButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String id = String.valueOf(dbHelper.getLocalityId(item.getTitle().toString()));
                listAdapter.getFilter().filter(id);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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