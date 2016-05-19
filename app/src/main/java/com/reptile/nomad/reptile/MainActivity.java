package com.reptile.nomad.reptile;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.reptile.nomad.reptile.Adapters.NewsFeedFragmentPagerAdapter;
import com.reptile.nomad.reptile.Fragments.BlankFragment;
import com.reptile.nomad.reptile.Fragments.FragmentNewsFeed;
import com.reptile.nomad.reptile.Fragments.FragmentNewsFeed;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.Models.User;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public static String TAG = "Main Activity";
     TextView nameTextView;
    ProfilePictureView profilePicture;
    @Override
    protected void onResume() {
        super.onResume();
        if(Profile.getCurrentProfile()!=null) {
            nameTextView.setText(Profile.getCurrentProfile().getName());
            profilePicture.setProfileId(Profile.getCurrentProfile().getId());


        }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container2);
        tabLayout = (TabLayout) findViewById(R.id.tabs);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mViewPager = (ViewPager) findViewById(R.id.container2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2)
                {
                 fab.setVisibility(View.VISIBLE);
                }
                else
                {
                    fab .setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(0);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startActivity(new Intent(getApplicationContext(),CreateTaskActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.NameTextView);
        profilePicture = (ProfilePictureView) navigationView.getHeaderView(0).findViewById(R.id.profilePicture);


        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                Log.d(TAG, "Facebook Initialized");
                if (AccessToken.getCurrentAccessToken() == null || Profile.getCurrentProfile() == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {

                    nameTextView.setText(Profile.getCurrentProfile().getName());
                    profilePicture.setProfileId(Profile.getCurrentProfile().getId());
                    Reptile.Instance.facebookLogin();
                }
            }
        });

        List<FragmentNewsFeed> fragmentList = new ArrayList<FragmentNewsFeed>();

//        fragmentList.add(FragmentNewsFeed.newInstance("Feed", new ArrayList<Task>()));
        fragmentList.add(new FragmentNewsFeed());
        fragmentList.add(new FragmentNewsFeed());
        fragmentList.add(new FragmentNewsFeed());

        NewsFeedFragmentPagerAdapter NewsFeedPagerAdapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(NewsFeedPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //TODO Replace with Switch Case
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id == R.id.logout_menu_item)
        {
            LoginManager.getInstance().logOut();
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.remove(QuickPreferences.tokenExpiry);
            editor.remove(QuickPreferences.facebookToken);
            editor.remove(QuickPreferences.facebookProfile);
            editor.commit();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();


    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return BlankFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
//    public List<Task> generateRandomTasks()
//    {
//        List<Task> tasks = new ArrayList<>();
//        User Sankar = new User("Sankar","Manoj");
//        User Subrat = new User ("Subrat","");
//        User Prudhvi = new User("Prudhvi","Rampey");
//        tasks.add(new Task(Sankar,"Go to Sleep",Task.Status.ACTIVE,2,3,null,null,null));
//        tasks.add(new Task(Subrat,"Go Home", Task.Status.ACTIVE,2,3,null,null,null));
//        tasks.add(new Task(Prudhvi,"Wake up", Task.Status.DONE,2,3,null,null,null));
//        tasks.add(new Task(Sankar,"Finish Reptile", Task.Status.MISSED,2,3,null,null,null));
//        return tasks;
//    }

}
