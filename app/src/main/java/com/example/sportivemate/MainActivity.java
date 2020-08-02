package com.example.sportivemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sportivemate.UI.HomeFragment;
import com.example.sportivemate.UI.HomeFragmentDirections;
import com.example.sportivemate.UI.SportPostsListFragment;
import com.example.sportivemate.model.AppLocalDb;
import com.example.sportivemate.model.Post;
import com.example.sportivemate.model.Sport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements HomeFragment.Delegate, SportPostsListFragment.Delegate {

    private Button logoutBtn;
    private FirebaseAuth mAuth;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(this, R.id.nav_host_home);
    }

    public void updateUI() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_activity_menu_logout: {
                mAuth.signOut();
                Log.d("TAG", "Logged out");
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemSelected(Sport sport) {
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToSportPostsListFragment(sport));
    }

    @Override
    public void OnItemSelected(Post post) {
        //navController.navigate(SpotReportsListFragmentDirections.actionGlobalReportDetailsFragment(report));
    }

}