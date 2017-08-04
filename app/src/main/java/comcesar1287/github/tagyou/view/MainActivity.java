package comcesar1287.github.tagyou.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.fragment.CompanyFragment;
import comcesar1287.github.tagyou.controller.fragment.PersonFragment;
import comcesar1287.github.tagyou.controller.util.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;

    private FirebaseAuth mAuth;

    String name, profilePic;

    ArrayList<Company> companiesList;

    NavigationView navigationView;

    CompanyFragment frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupUI();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompanyFragment(), "Empresas");
        adapter.addFragment(new PersonFragment(), "Pessoas");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupUI() {

        sharedPreferences = getSharedPreferences(Utility.LOGIN_SHARED_PREF_NAME, MODE_PRIVATE);
        name = sharedPreferences.getString("name","Carregando...");
        profilePic = sharedPreferences.getString("profile_pic","Carregando...");

        View hView =  navigationView.getHeaderView(0);
        final ImageView nav_image = (ImageView)hView.findViewById(R.id.imageView);
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
        TextView nav_nome = (TextView)hView.findViewById(R.id.header_name);
        nav_nome.setText(name);

        companiesList = new ArrayList<>();

        Company extra = new Company();

        extra.setName("Extra");
        Uri uriExtra = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_extra);
        extra.setBanner(uriExtra.toString());
        Uri uriExtraLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_extra);
        extra.setLogo(uriExtraLogo.toString());
        extra.setQuantity(2);
        extra.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        extra.setPhone("(37) 99935-2949");
        extra.setSite("www.facebook.com/churrascaria100");
        extra.setLatitude(-20.4673771);
        extra.setLongitude(-45.4316553);
        companiesList.add(extra);

        Company mcDonalds = new Company();
        mcDonalds.setName("Mc Donalds");
        Uri uriMcDonalds = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_mc_donalds);
        mcDonalds.setBanner(uriMcDonalds.toString());
        Uri uriMcLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_mc_donalds);
        extra.setLogo(uriMcLogo.toString());
        mcDonalds.setQuantity(5);
        mcDonalds.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        mcDonalds.setPhone("(37) 99935-2949");
        mcDonalds.setSite("www.facebook.com/churrascaria100");
        mcDonalds.setLatitude(-20.4673771);
        mcDonalds.setLongitude(-45.4316553);
        companiesList.add(mcDonalds);

        Company leroyMerlin = new Company();
        leroyMerlin.setName("Leroy Merlin");
        Uri uriLeroyMerlin = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_leroy_merlin);
        leroyMerlin.setBanner(uriLeroyMerlin.toString());
        Uri uriLeroyLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_leroy_merlin);
        extra.setLogo(uriLeroyLogo.toString());
        leroyMerlin.setQuantity(3);
        leroyMerlin.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        leroyMerlin.setPhone("(37) 99935-2949");
        leroyMerlin.setSite("www.facebook.com/churrascaria100");
        leroyMerlin.setLatitude(-20.4673771);
        leroyMerlin.setLongitude(-45.4316553);
        companiesList.add(leroyMerlin);

        Company habbibs = new Company();
        habbibs.setName("Habbib's");
        Uri uriHabbibs = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_habibs);
        habbibs.setBanner(uriHabbibs.toString());
        Uri uriHabibsLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_habibs);
        extra.setLogo(uriHabibsLogo.toString());
        habbibs.setQuantity(7);
        habbibs.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        habbibs.setPhone("(37) 99935-2949");
        habbibs.setSite("www.facebook.com/churrascaria100");
        habbibs.setLatitude(-20.4673771);
        habbibs.setLongitude(-45.4316553);
        companiesList.add(habbibs);

        Company ceA = new Company();
        ceA.setName("C&A");
        Uri uriCEA = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_riachuelo);
        ceA.setBanner(uriCEA.toString());
        Uri uriRiachueloLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_richuelo);
        extra.setLogo(uriRiachueloLogo.toString());
        ceA.setQuantity(9);
        ceA.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        ceA.setPhone("(37) 99935-2949");
        ceA.setSite("www.facebook.com/churrascaria100");
        ceA.setLatitude(-20.4673771);
        ceA.setLongitude(-45.4316553);
        companiesList.add(ceA);

        Company pagueMenos = new Company();
        pagueMenos.setName("Pague Menos");
        Uri uriPagueMenos = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_pague_menos);
        pagueMenos.setBanner(uriPagueMenos.toString());
        Uri uriPagueLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_pague_menos);
        extra.setLogo(uriPagueLogo.toString());
        pagueMenos.setQuantity(8);
        pagueMenos.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        pagueMenos.setPhone("(37) 99935-2949");
        pagueMenos.setSite("www.facebook.com/churrascaria100");
        pagueMenos.setLatitude(-20.4673771);
        pagueMenos.setLongitude(-45.4316553);
        companiesList.add(pagueMenos);

        Company carrefour = new Company();
        carrefour.setName("Carrefour");
        Uri uriCarrefour = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_carrefour);
        carrefour.setBanner(uriCarrefour.toString());
        Uri uriCarrefourLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_carrefour);
        extra.setLogo(uriCarrefourLogo.toString());
        carrefour.setQuantity(4);
        carrefour.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        carrefour.setPhone("(37) 99935-2949");
        carrefour.setSite("www.facebook.com/churrascaria100");
        carrefour.setLatitude(-20.4673771);
        carrefour.setLongitude(-45.4316553);
        companiesList.add(carrefour);

        Company pizzaHut = new Company();
        pizzaHut.setName("Pizza Hut");
        Uri uriPizzaHut = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_empresa_pizza_hut);
        pizzaHut.setBanner(uriPizzaHut.toString());
        Uri uriPizzaLogo = Uri.parse(Utility.URI_PACKAGE + R.drawable.tagyou_banner_pizza_hut);
        extra.setLogo(uriPizzaLogo.toString());
        pizzaHut.setQuantity(3);
        pizzaHut.setDescription("Av. Guiomar García Neto, 175, Formiga - MG");
        pizzaHut.setPhone("(37) 99935-2949");
        pizzaHut.setSite("www.facebook.com/churrascaria100");
        pizzaHut.setLatitude(-20.4673771);
        pizzaHut.setLongitude(-45.4316553);
        companiesList.add(pizzaHut);
    }

    public void signOut(View view){
        LoginManager.getInstance().logOut();
        mAuth.signOut();
        startActivity(new Intent(this, SignWithActivity.class));
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
        //if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*// Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_empresas) {
            // Handle the camera action
        } else if (id == R.id.nav_pessoas) {

        } else if (id == R.id.nav_editar_preferencias) {

        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_duvidas) {

        } else if (id == R.id.nav_fale_conosco) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Company> getCompaniesList() {
        return companiesList;
    }
}
