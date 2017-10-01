package hezra.wingsnsides.Main;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hezra.wingsnsides.AccountDetails.AccountFragment;
import hezra.wingsnsides.AccountDetails.AccountFragmentUpdateMobile;
import hezra.wingsnsides.AccountDetails.AccountFragmentUpdatePassword;
import hezra.wingsnsides.AccountDetails.SignInFragment;
import hezra.wingsnsides.AccountDetails.SignUpFragment;
import hezra.wingsnsides.ChickenWings.AddChickenItemFragment;
import hezra.wingsnsides.ChickenWings.ChickenWings;
import hezra.wingsnsides.Contact.ContactFragment;
import hezra.wingsnsides.DesertnDrinks.DesertnDrinksFragment;
import hezra.wingsnsides.Extras.ExtrasFragment;
import hezra.wingsnsides.OrderHere.AddItemFragment;
import hezra.wingsnsides.OrderHere.OrderFragment;
import hezra.wingsnsides.OrderHere.OrderPackage;
import hezra.wingsnsides.R;
import hezra.wingsnsides.Sides.SidesFragment;
import hezra.wingsnsides.TurkeyWings.TurkeyWingsFragment;
import hezra.wingsnsides.Utils.Preferences;
import hezra.wingsnsides.Utils.Utils;

import static hezra.wingsnsides.Utils.Constants.EMAIL;
import static hezra.wingsnsides.Utils.Constants.FNAME;
import static hezra.wingsnsides.Utils.Constants.ISLOGGEDIN;
import static hezra.wingsnsides.Utils.Constants.LNAME;

public class MainNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ChickenWings.OnFragmentInteractionListener, ContactFragment.OnFragmentInteractionListener,
        View.OnClickListener, SignUpFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener, ExtrasFragment.OnFragmentInteractionListener,
        OrderFragment.OnFragmentInteractionListener, SidesFragment.OnFragmentInteractionListener,
        TurkeyWingsFragment.OnFragmentInteractionListener, DesertnDrinksFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, SignInFragment.OnFragmentInteractionListener,
        AddItemFragment.OnFragmentInteractionListener, AddChickenItemFragment.OnFragmentInteractionListener,
        AccountFragmentUpdatePassword.OnFragmentInteractionListener, AccountFragmentUpdateMobile.OnFragmentInteractionListener {


    private ImageView fab;
    private TextView LoginLink, username_header, email_header;
    public static String cartCount = "";
    public static List<OrderPackage> orderPackage;
    /*public static SignUpFragment signUpFragment;
    public static SignInFragment signInFragment;
    public static AccountFragment accountFragment;
    public static TurkeyWingsFragment turkeyWingsFragment;
    public static ExtrasFragment extrasFragment;
    public static SidesFragment sidesFragment;
    public static OrderFragment orderFragment;
    public static DesertnDrinksFragment desertnDrinksFragment;
    public static ChickenWings chickenWings;
    public static MainFragment mainFragment;*/
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setVisibility(View.GONE);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        LoginLink = (TextView) header.findViewById(R.id.login_link);
        LoginLink.setOnClickListener(this);

        username_header = (TextView) header.findViewById(R.id.username_tv);
        email_header = (TextView) header.findViewById(R.id.email_tv);
        username_header.setOnClickListener(this);
        email_header.setOnClickListener(this);
        setRemovePref();
        if (savedInstanceState == null) {
            setFragment(fragment = new MainFragment());
            fragment = null;
        }
        orderPackage = new ArrayList<>();

        if (Utils.isNetworkAvailable(MainNavigationActivity.this)) {
            Dialog dialog = new Dialog(MainNavigationActivity.this);
            dialog.setTitle(getString(R.string.check_your_internet_connection));
            dialog.setCancelable(true);
        }
    }

    private void setRemovePref() {
        if (!Preferences.getBooleanPreference(MainNavigationActivity.this, ISLOGGEDIN)) {
            username_header.setVisibility(View.GONE);
            email_header.setVisibility(View.GONE);
            LoginLink.setVisibility(View.VISIBLE);
        } else if (Preferences.getBooleanPreference(MainNavigationActivity.this, ISLOGGEDIN)) {
            username_header.setVisibility(View.VISIBLE);
            username_header.setText(Preferences.getStringPreference(MainNavigationActivity.this, FNAME)
                    + " " + Preferences.getStringPreference(MainNavigationActivity.this, LNAME));
            email_header.setVisibility(View.VISIBLE);
            email_header.setText(Preferences.getStringPreference(MainNavigationActivity.this, EMAIL));
            LoginLink.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment != null) {
                setFragment(fragment = new MainFragment());
                fragment = null;
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.badge_menu_item);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        tv.setText(cartCount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.badge) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chicken_wings) {
            setFragment(fragment = new ChickenWings());
        } else if (id == R.id.nav_turkey_wings) {
            setFragment(fragment = new TurkeyWingsFragment());
        } else if (id == R.id.nav_sides) {
            setFragment(fragment = new SidesFragment());
        } else if (id == R.id.nav_extra) {
            setFragment(fragment = new ExtrasFragment());
        } else if (id == R.id.nav_drink_n_desert) {
            setFragment(fragment = new DesertnDrinksFragment());
        } else if (id == R.id.nav_order_here) {
            setFragment(fragment = new OrderFragment());
        } else if (id == R.id.nav_loyalty_points) {

        } else if (id == R.id.nav_contact_us) {
            fab.setVisibility(View.GONE);
            setFragment(fragment = new ContactFragment());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void setFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void removeFragment(Fragment fragment) {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction() {
        try {
            //check for username etc
            setRemovePref();
            invalidateOptionsMenu();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFragmentClosed(boolean result) {
        if (result) {
            removeFragment(fragment);
            setRemovePref();
            setFragment(fragment = new MainFragment());
            fragment = null;
        }
    }

    @Override
    public void onClick(View view) {
        DrawerLayout drawer;
        switch (view.getId()) {
            case R.id.login_link:
                setFragment(fragment = new SignInFragment());

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;

            case R.id.username_tv:
            case R.id.email_tv:
                setFragment(fragment = new AccountFragment());

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            fragment = null;
            /*signUpFragment = null;
            signInFragment = null;
            accountFragment = null;
            turkeyWingsFragment = null;
            extrasFragment = null;
            sidesFragment = null;
            orderFragment = null;
            desertnDrinksFragment = null;
            chickenWings = null;
            mainFragment = null;*/
        } catch (Exception ex) {
        }
    }
}
