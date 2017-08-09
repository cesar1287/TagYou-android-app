package comcesar1287.github.tagyou.view;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.fragment.MapViewFragment;
import comcesar1287.github.tagyou.controller.util.Utility;

public class CompanyDetailsActivity extends AppCompatActivity {

    MapViewFragment frag;

    Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        company = getIntent().getParcelableExtra(Utility.KEY_CONTENT_EXTRA_COMPANY);

        setupUI();
    }

    public void setupUI(){

        frag = (MapViewFragment) getSupportFragmentManager().findFragmentByTag(Utility.KEY_MAP_FRAGMENT);
        if(frag == null) {
            frag = new MapViewFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(Utility.KEY_CONTENT_EXTRA_COMPANY, company);
            frag.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.map_fragment_container, frag, Utility.KEY_MAP_FRAGMENT);
            ft.commit();
        }

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(company.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView ivBanner = (ImageView) findViewById(R.id.company_details_banner);
        Glide.with(this)
                .load(company.getBanner())
                .centerCrop()
                .into(ivBanner);
        TextView tv_name = (TextView) findViewById(R.id.company_details_name);
        tv_name.setText(company.getName());
        TextView tv_description = (TextView) findViewById(R.id.company_details_description);
        tv_description.setText(company.getDescription());
        TextView tv_address = (TextView) findViewById(R.id.company_details_address);
        tv_address.setText(company.getAddress());
        TextView tv_phone = (TextView) findViewById(R.id.company_details_phone);
        tv_phone.setText(company.getPhone());
        TextView tv_site = (TextView) findViewById(R.id.company_details_site);
        tv_site.setText(company.getSite());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
