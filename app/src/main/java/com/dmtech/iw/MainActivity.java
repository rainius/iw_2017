package com.dmtech.iw;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);

        mToolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        mDrawer=findViewById(R.id.drawer);
//        //创建抽屉按钮
//        mDrawerToggle=new ActionBarDrawerToggle(this,
//                mDrawer,
//                mToolbar,
//                R.string.open,
//                R.string.close);

        //创建抽屉按钮
        mDrawerToggle=new ActionBarDrawerToggle(this,
                mDrawer,
                mToolbar,
                R.string.open,
                R.string.close);

        mDrawer.addDrawerListener(mDrawerToggle);

        mDrawer.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.menu_item_add) {
            Toast.makeText(this, "增加新位置", Toast.LENGTH_SHORT).show();
            Log.d("iWeather", "增加新位置");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
