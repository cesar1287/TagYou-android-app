package comcesar1287.github.tagyou.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.domain.UserDetails;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView usersList;
    TextView noUsersText;
    HashMap<String, String> users;
    ArrayList<String> usersNames;
    int totalUsers = 0;
    ProgressDialog pd;

    Query user;

    ValueEventListener valueEventListener;
    ValueEventListener singleValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignWithActivity.class));
            finish();
            return;
        } else {
            UserDetails.username = mFirebaseUser.getUid();
            UserDetails.name = mFirebaseUser.getDisplayName();
        }

        setContentView(R.layout.activity_user);

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        user = mDatabase
                .child("users");

        loadList();

        pd = new ProgressDialog(UserActivity.this);
        pd.setMessage("Carregando...");
        pd.show();

        usersList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        UserDetails.chatWithName = usersNames.get(i);
        UserDetails.chatWith = users.get(usersNames.get(i));
        startActivity(new Intent(UserActivity.this, ChatActivity.class));
    }

    public void loadList(){

        users = new HashMap<>();
        usersNames = new ArrayList<>();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if(!UserDetails.username.equals(postSnapshot.getKey())) {
                        users.put((String)postSnapshot.child("name").getValue(), postSnapshot.getKey());
                        usersNames.add((String)postSnapshot.child("name").getValue());
                    }
                    totalUsers++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        };

        singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(totalUsers <=1){
                    noUsersText.setVisibility(View.VISIBLE);
                    usersList.setVisibility(View.GONE);
                }
                else{
                    noUsersText.setVisibility(View.GONE);
                    usersList.setVisibility(View.VISIBLE);
                    usersList.setAdapter(new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1, usersNames));
                }

                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        };

        user.addValueEventListener(valueEventListener);

        user.addListenerForSingleValueEvent(singleValueEventListener);
    }
}
