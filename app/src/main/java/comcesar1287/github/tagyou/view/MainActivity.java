package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.fragment.CompanyFragment;
import comcesar1287.github.tagyou.controller.fragment.PersonFragment;
import comcesar1287.github.tagyou.controller.util.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private String database;

    private NavigationView navigationView;

    private TabLayout tabLayout;

    private SharedPreferences sharedPreferences;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        verifyUserIsLogged();

        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        database = getIntent().getStringExtra(Utility.KEY_CONTENT_EXTRA_DATABASE);
        if(database==null){
            database = sharedPreferences.getString(Utility.KEY_CONTENT_EXTRA_DATABASE, "");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupUI();
    }

    public void verifyUserIsLogged(){
        if (user == null) {
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(this, CategoryRegisterActivity.class);
            startActivity(intent);
            finish();
        }else{
            for (UserInfo userTestProvider: FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
                if (userTestProvider.getProviderId().equals("password") && !user.isEmailVerified()) {
                    Intent intent = new Intent(this, CategoryRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompanyFragment(), "Empresas");
        adapter.addFragment(new PersonFragment(), "Pessoas");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupUI() {
        View hView = navigationView.getHeaderView(0);

        if(user != null) {
            if (user.getPhotoUrl() != null) {
                String profilePic = user.getPhotoUrl().toString();
                final ImageView nav_image = (ImageView) hView.findViewById(R.id.imageView);
                Glide.with(this).load(profilePic)
                        .asBitmap().into(new BitmapImageViewTarget(nav_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        nav_image.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }

            String name = user.getDisplayName();
            TextView nav_nome = (TextView) hView.findViewById(R.id.header_name);
            nav_nome.setText(name);
        }
    }

    public void signOut(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        LoginManager.getInstance().logOut();
        mAuth.signOut();
        startActivity(new Intent(this, CategoryRegisterActivity.class));
        finish();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_empresas) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
        } else if (id == R.id.nav_pessoas) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        } else if (id == R.id.nav_editar_preferencias) {
            startActivity(new Intent(this, TagsFilterActivity.class));
        } else if (id == R.id.nav_editar_cadastro) {
            if(database.equals(FirebaseHelper.FIREBASE_DATABASE_USERS)){
                startActivity(new Intent(this, EditPersonActivity.class));
            }else{
                startActivity(new Intent(this, EditCompanyActivity.class));
            }
        } else if (id == R.id.nav_chat) {
            startActivity(new Intent(this, UserActivity.class));
        } else if (id == R.id.nav_indication) {
            startActivity(new Intent(this, IndicationPersonActivity.class));
        } else if (id == R.id.nav_duvidas) {

        } else if (id == R.id.nav_fale_conosco) {
            startActivity(new Intent(this, ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
