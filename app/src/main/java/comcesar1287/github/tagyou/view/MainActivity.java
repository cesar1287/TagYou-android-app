package comcesar1287.github.tagyou.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.login.LoginManager;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.NearbyPermissions;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.beacon.DeviceAdapter;
import comcesar1287.github.tagyou.controller.beacon.DumpTask;
import comcesar1287.github.tagyou.controller.domain.IBeacon;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.fragment.CompanyFragment;
import comcesar1287.github.tagyou.controller.fragment.PersonFragment;
import comcesar1287.github.tagyou.controller.util.BeaconMessageReceiver;
import comcesar1287.github.tagyou.controller.util.BleUtil;
import comcesar1287.github.tagyou.controller.util.ScannedDevice;
import comcesar1287.github.tagyou.controller.util.Utility;
import de.hdodenhof.circleimageview.CircleImageView;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BluetoothAdapter.LeScanCallback {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private String database;

    private NavigationView navigationView;

    private TabLayout tabLayout;

    private SharedPreferences sharedPreferences;

    private ViewPager viewPager;

    private static final String TAG_LICENSE = "license";
    private BluetoothAdapter mBTAdapter;
    private DeviceAdapter mDeviceAdapter;
    private DumpTask mDumpTask;
    private boolean mIsScanning;

    private boolean bobsShowed = false;
    private boolean mcDonalsShowed = false;
    private boolean saraivaShowed = false;

    CircleImageView photoCompany, photoMultiplier, photoPerson;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoCompany = (CircleImageView) findViewById(R.id.photoCompany);
        photoMultiplier = (CircleImageView) findViewById(R.id.photoMultiplier);
        photoPerson = (CircleImageView) findViewById(R.id.photoPerson);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        verifyUserIsLogged();

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                /*mMessagesClient = Nearby.getMessagesClient(this, new MessagesOptions.Builder()
                    .setPermissions(NearbyPermissions.BLE)
                    .build());*/
        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

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

        setActionBar();

        init();

        startScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init() {
        // BLE check
        if (!BleUtil.isBLESupported(this)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // BT check
        BluetoothManager manager = BleUtil.getManager(this);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
        if (mBTAdapter == null) {
            Toast.makeText(this, R.string.bt_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // init listview
//        ListView deviceListView = (ListView) findViewById(R.id.list);
//        mDeviceAdapter = new DeviceAdapter(this, R.layout.listitem_device,
//                new ArrayList<ScannedDevice>());
//        deviceListView.setAdapter(mDeviceAdapter);
        //stopScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startScan() {
        if ((mBTAdapter != null) && (!mIsScanning)) {
            mBTAdapter.startLeScan(this);
            mIsScanning = true;
            setProgressBarIndeterminateVisibility(true);
            invalidateOptionsMenu();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void stopScan() {
        if (mBTAdapter != null) {
            mBTAdapter.stopLeScan(this);
        }
        mIsScanning = false;
        setProgressBarIndeterminateVisibility(false);
        invalidateOptionsMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupViewPager(viewPager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "O app depende dessa permissão para funcionar corretamente", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CompanyFragment(), "Empresas");
        adapter.addFragment(new PersonFragment(), "Pessoas");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //String summary = mDeviceAdapter.update(device, rssi, scanRecord);
                if(scanRecord!=null) {
                    IBeacon iBeacon = IBeacon.fromScanData(scanRecord, rssi);
                    if(iBeacon != null){
                        Log.i(TAG, iBeacon.getProximityUuid());
                        if(iBeacon.getProximityUuid().
                                equals("aa0ba7b7-5800-4285-b2da-336298781ab9") && !bobsShowed) {
                            createNotificationBeacon("Bobs", "Promoção Bob's",
                                    "Venha conferir a nova oferta do Bob's");
                            bobsShowed = true;
                        }else if(iBeacon.getProximityUuid().
                                equals("db0e7935-ef5d-49b1-a7d5-3996c3a82ce5") && !mcDonalsShowed){
                            createNotificationBeacon("McDonals", "PromoMac",
                                    "Venha conferir a nova oferta do McDonals");
                            mcDonalsShowed = true;
                        }else if(iBeacon.getProximityUuid().
                                equals("bd858f4b-86e8-4215-b8d8-b346651988d8") && !saraivaShowed){
                            createNotificationBeacon("Saraiva", "Liquidação Saraiva",
                                    "Venha conferir a nova oferta da Saraiva");

                            saraivaShowed = true;
                        }
                    }
                }
                /*if (summary != null) {
                    getActionBar().setSubtitle(summary);
                }*/
            }
        });
    }

    private void createNotificationBeacon(String loja, String titulo, String texto) {
        int id = -1;
        //Assign Big Picture style notification
        NotificationCompat.BigPictureStyle bigPictureStyle=new NotificationCompat.BigPictureStyle();
        if(loja.equals("Bobs")) {
            id = 0;
            bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.tagyou_image_notification_bobs)).build();
        }else if(loja.equals("McDonals")) {
            id = 1;
            bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.tagyou_image_notification_mcdonalds)).build();
        }else if(loja.equals("Saraiva")) {
            id = 2;
            bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.tagyou_image_notification_saraiva)).build();
        }

        //gets instance of notification manager service
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //set intents and pending intents to call activity on click of "show activity" action button of notification
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,(int) Calendar.getInstance().getTimeInMillis(),intent,0);


        // build notification
        NotificationCompat.Builder builder=(NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(texto)
                .setStyle(bigPictureStyle);

        PendingIntent.getActivity(getApplicationContext(),0,getIntent(),0,null);

        // post notification on the notification bar
        notificationManager.notify(id,builder.build());
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
        } else if (id == R.id.nav_config) {
            startActivity(new Intent(this, ConfigActivity.class));
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

    public void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);

        actionBar.setCustomView(v);
    }
}
