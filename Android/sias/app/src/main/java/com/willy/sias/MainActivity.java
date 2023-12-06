package com.willy.sias;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import com.willy.sias.ui.home.HomeFragment;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout drawerLayout;
  private ActionBarDrawerToggle toggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drawerLayout = findViewById(R.id.drawer_layout);

    // 設置ActionBar的HomeButton為可用
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    // 創建ActionBarDrawerToggle對象
    toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
    // 設置ActionBarDrawerToggle對象為DrawerLayout的監聽器
    drawerLayout.addDrawerListener(toggle);
    // 同步狀態
    toggle.syncState();

    // 設置點擊菜單項目時的監聽器
    NavigationView navigationView = findViewById(R.id.navigation_view);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 在這裡實現點擊菜單項目的操作
        return false;
      }
    });
  }

  // 當HomeButton被點擊時，呼叫ActionBarDrawerToggle的onOptionsItemSelected()方法
  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (toggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}