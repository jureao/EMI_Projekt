

package com.example.user.studytracker;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.view.Menu.NONE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar myToolbar;
    List<Subject> subjectList = new ArrayList<Subject>();
    String file_subjectArray = " \\subj_array.txt";
    NavigationView navigationView;
    int selected = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);



        myToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        getFileInput();
        buildNavigationDrawer();
        buildTabs();

    }


    @Override
    /**
     * loads the subjectList from a textfile that has been created when the activity was last closed
     *
     */
    protected void onStart(){

        super.onStart();
        Intent intent = getIntent();
        if(intent.hasExtra("subjectChanged")){
            int pos = intent.getIntExtra("position", 0);
            subjectList.set(pos, (Subject) intent.getSerializableExtra( "subjectChanged"));
            onStop();
            startActivity(new Intent(this, MainActivity.class));
        }
        if(intent.hasExtra("subjectsChanged")){
            subjectList = (List<Subject>) intent.getSerializableExtra("subjectsChanged");
            onStop();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void getFileInput(){
        try {
            File file = new File(getFilesDir() + file_subjectArray);
            if (file.exists()) {
                FileInputStream fIS = new FileInputStream(file);
                ObjectInputStream oIS = new ObjectInputStream(fIS);

                subjectList = (List<Subject>) oIS.readObject();
            }
        }
        catch (Exception ex) {
            if(!(ex instanceof EOFException)){
                Toast.makeText(this, getString(R.string.txt_oops), Toast.LENGTH_SHORT).show();
            }

        }



        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra("subject")){
            Subject subj = (Subject) receivedIntent.getSerializableExtra("subject");
            subjectList.add(subj);
        }
    }

    /** sets up the navigationDrawer
     *  assigns each item in the drawer a unique ID that can be used to identify which index of
     *  the subjectList should be given as intent to the other activities.
     *  Will need to set up the actual intents with Masha though to make sure it's compatible with
     *  Masha's code
     */
    private void buildNavigationDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        myToolbar.setTitle(getString(R.string.all_subjects));
        SubMenu n= m.addSubMenu(R.id.nav_group_subjects, R.id.submenu_java, NONE, "subjects");
        MenuItem it = n.add(NONE, R.id.all_subjects, NONE, getString(R.string.all_subjects));

        int index = 0;
        for(Subject s:subjectList) {
            n.add(NONE, index, NONE, s.getName());
            index++;
        }
    }

    @Override
    /**
     * stores the subjectList in a textfile upon closing the activity
     */
    protected void onStop() {
        try {
            File file = new File(getFilesDir() + file_subjectArray);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fOS = new FileOutputStream(file);
            ObjectOutputStream oOS = new ObjectOutputStream(fOS);
            oOS.writeObject(subjectList);
            oOS.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // creates the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // when you click on the settings-button in the toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // handles what happens if you click the navigationDrawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setCheckable(true);
        if (item.isChecked()) item.setChecked(false);
        else item.setChecked(true);



        if (id == R.id.nav_new){
            startActivity(new Intent(this, AddActivity.class));
        }
        else{
            myToolbar.setTitle(item.getTitle());
            if(id>100){
                selected=1000;
            }
            else{
                selected = id;
            }
        }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }

    // builds the UI for the tabs on the bottom
    public void buildTabs(){
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        LectureFragment tab1 = new LectureFragment();
                        return tab1;
                    case 1:
                        StatisticsFragment tab2 = new StatisticsFragment();
                        return tab2;
                    case 2:
                        CalendarFragment tab3 = new CalendarFragment();
                        return tab3;
                    default:
                        return null;
                }
            }


            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

