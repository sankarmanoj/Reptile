package com.reptile.nomad.reptile;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
//import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.iid.FirebaseInstanceId;
import com.reptile.nomad.reptile.Adapters.NewsFeedFragmentPagerAdapter;
import com.reptile.nomad.reptile.Adapters.SearchUserRecyclerAdapter;
import com.reptile.nomad.reptile.Fragments.BlankFragment;
import com.reptile.nomad.reptile.Fragments.FragmentNewsFeed;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
     ViewPager mViewPager;
    TabLayout tabLayout;

    HashMap<String,User> searchedUsers;
    Timer searchUserTimer;
    public static String TAG = "Main Activity";
    public static GoogleApiClient mGoogleApiClient;
     TextView nameTextView;
    ProfilePictureView profilePicture;
    SoftKeyboard softKeyboard;
   static SearchEditText searchingEditText;
   private static boolean searchingToggle;
    @Override
    protected void onResume() {
        super.onResume();
        if(!Reptile.mSocket.connected())
        {
            Reptile.mSocket.connect();
        }
        if(Profile.getCurrentProfile()!=null&&Reptile.loginMethod()==Reptile.FACEBOOK_LOGIN) {
            nameTextView.setText(Profile.getCurrentProfile().getName());
            profilePicture.setProfileId(Profile.getCurrentProfile().getId());
        }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Got Activity Result with Result Code "+ String.valueOf(resultCode)+ "And Request Code " + String.valueOf(requestCode));
        if(requestCode==9010&&resultCode==RESULT_OK)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Reptile.mGoogleAccount = result.getSignInAccount();
            String name = result.getSignInAccount().getDisplayName();
            Log.d(TAG,"Name is "+name);
            if (name==null)
                throw new AssertionError("Got a Null Name");
            nameTextView.setText(name);
            Reptile.googleLogin(Reptile.mGoogleAccount);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchingToggle=false;
        CoordinatorLayout mainLayout =(CoordinatorLayout
                ) findViewById(R.id.main_layout); // You must use your root layout
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        softKeyboard = new SoftKeyboard(mainLayout,im);



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
        profilePicture = (com.reptile.nomad.reptile.ProfilePictureView) navigationView.getHeaderView(0).findViewById(R.id.profilePicture);
        if(Reptile.mGoogleAccount!=null)
        {
            nameTextView.setText(Reptile.mGoogleAccount.getDisplayName());
        }

        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                Log.d(TAG, "Facebook Initialized");
                if (!Reptile.checkLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    if(Reptile.loginMethod()==Reptile.FACEBOOK_LOGIN) {
                        nameTextView.setText(Profile.getCurrentProfile().getName());
                        profilePicture.setProfileId(Profile.getCurrentProfile().getId());
                        Reptile.facebookLogin();
                    }

                }
            }
        });


        List<FragmentNewsFeed> fragmentList = new ArrayList<FragmentNewsFeed>();
        fragmentList.add(FragmentNewsFeed.newInstance(FragmentNewsFeed.FEED));
        fragmentList.add(FragmentNewsFeed.newInstance(FragmentNewsFeed.FOLLOWING));
        fragmentList.add(FragmentNewsFeed.newInstance(FragmentNewsFeed.PROFILE));

        NewsFeedFragmentPagerAdapter NewsFeedPagerAdapter = new NewsFeedFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setAdapter(NewsFeedPagerAdapter);
        mViewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(mViewPager);
        GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestIdToken(getString(R.string.google_server_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.e(TAG,"Connection Failed");
                        Log.e(TAG,connectionResult.getErrorMessage());
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,GSO)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if(Reptile.loginMethod()==Reptile.GOOGLE_LOGIN&& Reptile.mGoogleAccount==null) {
                            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                            startActivityForResult(signInIntent, 9010);
                            Log.d(TAG, "Starting Activity for Google Login");
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();


        ImageView fbImage = ( ( ImageView)profilePicture.getChildAt( 0));
        Bitmap    bitmapToSave  = ( (BitmapDrawable) fbImage.getDrawable()).getBitmap(); // for saving own copy
        Bitmap resizedProfilePictureBitmap = getResizedBitmap(bitmapToSave,500);
        String profilePictureString = BitMapToString(resizedProfilePictureBitmap);
        new SendProfilePicture().execute(profilePictureString);

        setupTabIcons();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(searchingToggle)
        {
            final ActionBar actionBar = getSupportActionBar();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchingEditText.getWindowToken(), 0);
            searchingToggle=false;
            actionBar.setDisplayShowCustomEnabled(false); //disable a customRadioButton view inside the actionbar
            actionBar.setDisplayShowTitleEnabled(true); //show the title in the action bar
        }
        else {
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
        final ActionBar actionBar = getSupportActionBar();
        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_settings:
                return true;
            case R.id.searchIcon:
//                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                final View dialogView = this.getLayoutInflater().inflate(R.layout.search_dialog,null);
//                builder.setView(dialogView);
//                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                         searchedUsers=null;
//                    }
//                });
//                final EditText searchEditText = (EditText)dialogView.findViewById(R.id.searchEditText);
//                final Button Done = (Button)dialogView.findViewById(R.id.doneSearchButton);
//                final RecyclerView userListRecycler = (RecyclerView)dialogView.findViewById(R.id.userRecyclerVeiw);
//                searchedUsers = new HashMap<>();
//                final SearchUserRecyclerAdapter searchUserRecyclerAdapter = new SearchUserRecyclerAdapter(new ArrayList<>(searchedUsers.values()));
//                userListRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                userListRecycler.setAdapter(searchUserRecyclerAdapter);
//                Reptile.mSocket.on("user-search", new Emitter.Listener() {
//                    @Override
//                    public void call(Object... args) {
//                        try {
//                            JSONArray inputArray = new JSONArray((String) args[0]);
//                            Log.d(TAG,"From Server is "+args[0]);
//                            for(int i = 0; i<inputArray.length();i++)
//                            {
//                                JSONObject input = inputArray.getJSONObject(i);
//                                User.addUserToHashMap(input,searchedUsers);
//                            }
//
//                            searchUserRecyclerAdapter.userList = new ArrayList<User>(searchedUsers.values());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    searchUserRecyclerAdapter.notifyDataSetChanged();
//                                }
//                            });
//
//
//                        }
//                        catch (JSONException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                searchUserTimer = new Timer();
//                searchEditText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        searchUserTimer.purge();
//                        searchUserTimer.cancel();
//                        searchUserTimer = new Timer();
//                        searchUserTimer.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//
//                                    Reptile.mSocket.emit("user-search", searchEditText.getText().toString());
//                                   // Log.d(TAG, searchEditText.getText().toString());
//
//                                }
//                        },250);
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//
//                    }
//                });
//                final AlertDialog dialog = builder.create();
//                Done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                     dialog.dismiss();
//                    }
//                });
//                dialog.show();
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

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_followers) {

        } else if (id == R.id.nav_following) {

        }
        else if(id == R.id.nav_groups)
        {
            startActivity(new Intent(this,ManageGroups.class));
        }

        else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this,PreferencesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_create_task) {

        } else if(id == R.id.logout_menu_item)
        {
            if(Reptile.loginMethod()==Reptile.FACEBOOK_LOGIN) {
                LoginManager.getInstance().logOut();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.remove(QuickPreferences.tokenExpiry);
                editor.remove(QuickPreferences.facebookToken);
                editor.remove(QuickPreferences.facebookProfile);
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
            else
            {
                if(Reptile.loginMethod()==Reptile.GOOGLE_LOGIN)
                {
                 Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                     @Override
                     public void onResult(Status status) {
                         startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                     }
                 });
                }
            }
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

    public List<Task> generateRandomTasks()
    {
        List<Task> tasks = new ArrayList<>();
        User Sankar = new User("Sankar","Manoj");
        User Subrat = new User ("Subrat","");
        User Prudhvi = new User("Prudhvi","Rampey");
        tasks.add(new Task(Sankar,"Go to Sleep", Calendar.getInstance(),Calendar.getInstance()));
        tasks.add(new Task(Subrat,"adsf", Calendar.getInstance(),Calendar.getInstance()));
        tasks.add(new Task(Prudhvi,"Gadfp", Calendar.getInstance(),Calendar.getInstance()));
        return tasks;
    }
    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_public_white_24dp,
                R.drawable.ic_people_outline_white_24dp,
                R.drawable.ic_whatshot_white_24dp
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private class SendProfilePicture extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... image) {
            JSONObject dpSend = new JSONObject();
            try {
                dpSend.put("data",image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dpSend.put("user",Reptile.mUser.id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Reptile.mSocket.emit("saveProfileImage", dpSend);
            Long val;
            val = new Long(55);
            return val;
        }


    }


}


