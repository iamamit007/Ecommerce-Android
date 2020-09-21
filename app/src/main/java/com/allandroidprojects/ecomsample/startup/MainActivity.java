package com.allandroidprojects.ecomsample.startup;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allandroidprojects.ecomsample.R;
import com.allandroidprojects.ecomsample.fragments.ImageListFragment;
import com.allandroidprojects.ecomsample.login.CustomerDetailResponse;
import com.allandroidprojects.ecomsample.miscellaneous.EmptyActivity;
import com.allandroidprojects.ecomsample.notification.NotificationCountSetClass;
import com.allandroidprojects.ecomsample.options.CartListActivity;
import com.allandroidprojects.ecomsample.options.MyAccountActivity;
import com.allandroidprojects.ecomsample.options.SearchResultActivity;
import com.allandroidprojects.ecomsample.options.WishlistActivity;
import com.allandroidprojects.ecomsample.utility.ApiClient;
import com.allandroidprojects.ecomsample.utility.ApiInterface;
import com.allandroidprojects.ecomsample.utility.Catagories;
import com.allandroidprojects.ecomsample.utility.NetworkCallBack;
import com.allandroidprojects.ecomsample.utility.NetworkResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.velectico.rbm.network.callbacks.NetworkError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int notificationCountCart = 0;
    static ViewPager viewPager;
    static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

         viewPager = (ViewPager) findViewById(R.id.viewpager);
         tabLayout = (TabLayout) findViewById(R.id.tabs);




      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        callApiList();
        callProfileApiList();
    }




    public void callApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<List<Catagories>> responseCall = apiInterface.getCatagories();
        responseCall.enqueue(callBack);

    }

        List<String> titles= new ArrayList<>();
    List<Catagories> responseData = new ArrayList<>();

    private NetworkCallBack callBack = new NetworkCallBack<List<Catagories>>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            responseData = (List<Catagories>) response.getData();
            for (Catagories i:responseData) {
                titles.add(i.getName());
                if (viewPager != null) {
                    setupViewPager(viewPager);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

    public void callProfileApiList(){

        ApiInterface apiInterface = ApiClient.getInstance().getClient().create(ApiInterface.class);
        Call<CustomerDetailResponse> responseCall = apiInterface.getCustomerProfile();
        responseCall.enqueue(callBack2);

    }



    private NetworkCallBack callBack2 = new NetworkCallBack<CustomerDetailResponse>() {
        @Override
        public void onSuccessNetwork(@Nullable Object data, @NotNull NetworkResponse response) {
            Log.d("ZINGAKART",response.toString());
            Toast.makeText(MainActivity.this,"Profile UP."+response.getData().toString(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailureNetwork(@Nullable Object data, @NotNull NetworkError error) {

        }
    };

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
            return true;
        }else if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(MainActivity.this, CartListActivity.class));

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        }else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager(),titles);
        for (Catagories i:responseData
             ) {
            ImageListFragment fragment = new ImageListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i.getId());
            fragment.setArguments(bundle);
            adapter.addFragment(fragment,i.getName());
        }

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
            }
        });

//        ImageListFragment fragment = new ImageListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", 1);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_1));
//        fragment = new ImageListFragment();
//        bundle = new Bundle();
//        bundle.putInt("type", 2);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_2));
//        fragment = new ImageListFragment();
//        bundle = new Bundle();
//        bundle.putInt("type", 3);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_3));
//        fragment = new ImageListFragment();
//        bundle = new Bundle();
//        bundle.putInt("type", 4);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_4));
//        fragment = new ImageListFragment();
//        bundle = new Bundle();
//        bundle.putInt("type", 5);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_5));
//        fragment = new ImageListFragment();
//        bundle = new Bundle();
//        bundle.putInt("type", 6);
//        fragment.setArguments(bundle);
//        adapter.addFragment(fragment, getString(R.string.item_6));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_item1) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_item2) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_item3) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_item4) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_item5) {
            viewPager.setCurrentItem(4);
        }else if (id == R.id.nav_item6) {
            viewPager.setCurrentItem(5);
        }else if (id == R.id.my_wishlist) {
            startActivity(new Intent(MainActivity.this, WishlistActivity.class));
        }
        else if (id == R.id.my_account) {
            startActivity(new Intent(MainActivity.this, MyAccountActivity.class));
        }
        else {
            startActivity(new Intent(MainActivity.this, EmptyActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
         List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm ,List<String> mFragmentTitles) {
            super(fm);
            this.mFragmentTitles = mFragmentTitles;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


}
