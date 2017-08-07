package comcesar1287.github.tagyou.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.adapter.PersonAdapter;
import comcesar1287.github.tagyou.controller.domain.Person;
import comcesar1287.github.tagyou.controller.interfaces.RecyclerViewOnClickListenerHack;
import comcesar1287.github.tagyou.controller.util.Utility;
import comcesar1287.github.tagyou.view.CompanyDetailsActivity;
import comcesar1287.github.tagyou.view.PersonsDetailsActivity;

public class PersonFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    RecyclerView mRecyclerView;
    public List<Person> mList;

    ArrayList<Person> peopleList;

    public PersonAdapter adapter;

    private DatabaseReference mDatabase;

    Query person;

    ValueEventListener valueEventListener;
    ValueEventListener singleValueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mList = new ArrayList<>();
        adapter = new PersonAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerView, this ));

        getPeopleList();

        return view;

    }

    public void loadList(){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                peopleList = new ArrayList<>();

                Person p;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    p = new Person();
                    p.setProfilePic((String)postSnapshot.child("profile_pic").getValue());
                    p.setName((String)postSnapshot.child("name").getValue());

                    peopleList.add(p);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*dialog.dismiss();
                Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();*/
            }
        };

        singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                mList.clear();
                mList.addAll(peopleList);
                adapter.notifyDataSetChanged();
                //dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*dialog.dismiss();
                Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();*/
            }
        };

        person.addValueEventListener(valueEventListener);

        person.addListenerForSingleValueEvent(singleValueEventListener);
    }

    public List<Person> getPeopleList() {

        person = mDatabase
                .child("users")
                .orderByChild("name");

        loadList();

        return peopleList;
    }

    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(getActivity(), PersonsDetailsActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_COMPANY, mList.get(position));
        startActivity(intent);
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildAdapterPosition(cv) );
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
