package comcesar1287.github.tagyou.view;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.Person;
import comcesar1287.github.tagyou.controller.util.Utility;

public class PersonsDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_details);

        Person person = (Person) getIntent().getSerializableExtra(Utility.KEY_CONTENT_EXTRA_COMPANY);

        final ImageView nav_image = (ImageView) findViewById(R.id.image_details_person);
        Glide.with(this).load(person.getProfilePic())
                .asBitmap().into(new BitmapImageViewTarget(nav_image) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                nav_image.setImageDrawable(circularBitmapDrawable);
            }
        });
        TextView tvName = (TextView) findViewById(R.id.name_details_person);
        tvName.setText(person.getName());

        TextView tvEmail = (TextView) findViewById(R.id.email_details_person);
        tvEmail.setText(person.getEmail());

        TextView tvBirth = (TextView) findViewById(R.id.birth_details_person);
        tvBirth.setText(person.getBirth());

        TextView tvPhone = (TextView) findViewById(R.id.phone_details_person);
        tvPhone.setText(person.getPhone());

    }

}
