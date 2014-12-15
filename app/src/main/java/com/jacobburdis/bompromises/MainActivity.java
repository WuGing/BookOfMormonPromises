package com.jacobburdis.bompromises;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    private ActionBar actionBar;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseWriter.WriteDataBase(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("JOSHUA", "onTabSelected at "+" position "+position+" from "+positionOffset+" with number of pixels = "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
                //Log.d("JOSHUA", "onTabSelected at "+" position "+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*if(state == ViewPager.SCROLL_STATE_IDLE){
                    Log.d("JOSHUA", "onPageScrollStateChanged Idle");
                }
                if(state == ViewPager.SCROLL_STATE_DRAGGING){
                    Log.d("JOSHUA", "onpageScrollStateChanged Dragging");
                }
                if(state == ViewPager.SCROLL_STATE_SETTLING){
                    Log.d("JOSHUA", "onPageScrollStateChanged Setting");
                }*/
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab0 = actionBar.newTab();
        tab0.setText("Favorites")
            .setTabListener(this);

        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("What I Promise\nto Do");
        tab1.setTabListener(this);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("What God\nPromises Me")
                .setTabListener(this);

        ActionBar.Tab tab3 = actionBar.newTab();
        tab3.setText("Promises by\nBook in Order")
                .setTabListener(this);

        actionBar.addTab(tab0);
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);

        viewPager.setCurrentItem(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_info:
                info();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void info(){
        Intent infoActivity = new Intent(this, InfoActivity.class);
        startActivity(infoActivity);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Log.d("JOSHUA", "onTabSelected at "+" position "+tab.getPosition()+" name "+tab.getText());
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Log.d("JOSHUA", "onTabUnselected at "+" position "+tab.getPosition()+" name "+tab.getText());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //Log.d("JOSHUA", "onTabReselected at "+" position "+tab.getPosition()+" name "+tab.getText());
    }
}
