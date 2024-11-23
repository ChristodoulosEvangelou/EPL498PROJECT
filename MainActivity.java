package com.example.project498;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Βεβαιώσου ότι το σωστό layout είναι συνδεδεμένο

        // Ενεργοποίηση Toolbar ως ActionBar
        setSupportActionBar(findViewById(R.id.toolbar)); // Υποθέτω ότι υπάρχει Toolbar στο layout σου

        // Ρύθμιση NavController για το navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Λήψη FCM Registration Token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Πάρε το FCM Registration Token
                        String token = task.getResult();

                        // Προσωρινή ενέργεια: Εμφάνιση του token με Log
                        String msg = "FCM Registration token: " + token;
                        Log.d(TAG, msg);
                        // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show(); // Ενεργοποίησε το αν χρειάζεται
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Αν χρειάζεσαι menu, κάνε uncomment και αντικατέστησε το R.menu.menu_items με το δικό σου menu
        // getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Χειρισμός clicks στο μενού της ActionBar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Επιστροφή στο προηγούμενο fragment
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Ρύθμιση του navigate up για το NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
