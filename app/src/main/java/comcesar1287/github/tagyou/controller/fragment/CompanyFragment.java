package comcesar1287.github.tagyou.controller.fragment;

import android.app.ProgressDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.adapter.CompanyAdapter;
import comcesar1287.github.tagyou.controller.domain.Company;
import comcesar1287.github.tagyou.controller.interfaces.RecyclerViewOnClickListenerHack;
import comcesar1287.github.tagyou.controller.util.Utility;
import comcesar1287.github.tagyou.view.CompanyDetailsActivity;

public class CompanyFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    RecyclerView mRecyclerView;
    public List<Company> mList;
    public CompanyAdapter adapter;

    ArrayList<Company> companiesList;

    private DatabaseReference mDatabase;

    Query company;

    ValueEventListener valueEventListener;
    ValueEventListener singleValueEventListener;

    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mList = new ArrayList<>();
        adapter = new CompanyAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerView, this ));

        dialog = ProgressDialog.show(getActivity(),"", "Carregando empresas...", true, false);

        getCompaniesList();

        return view;

    }

    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(getActivity(), CompanyDetailsActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_COMPANY, mList.get(position));
        startActivity(intent);
    }

    public void loadList(){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                companiesList = new ArrayList<>();

                Company p;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    p = new Company();
                    p.setAddress((String)postSnapshot.child("address").getValue());
                    p.setBanner((String)postSnapshot.child("banner").getValue());
                    p.setDescription((String)postSnapshot.child("description").getValue());
                    p.setEmail((String)postSnapshot.child("email").getValue());
                    p.setHashtag((String)postSnapshot.child("hashtag").getValue());
                    p.setLatitude((Double) postSnapshot.child("latitude").getValue());
                    p.setLogo((String)postSnapshot.child("logo").getValue());
                    p.setLongitude((Double) postSnapshot.child("longitude").getValue());
                    p.setName((String)postSnapshot.child("name").getValue());
                    p.setPhone((String)postSnapshot.child("phone").getValue());
                    p.setQuantity((Long) postSnapshot.child("quantity").getValue());
                    p.setSite((String)postSnapshot.child("site").getValue());

                    companiesList.add(p);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                //Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                //finish();
            }
        };

        singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                mList.clear();
                mList.addAll(companiesList);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                //Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                //finish();
            }
        };

        company.addValueEventListener(valueEventListener);

        company.addListenerForSingleValueEvent(singleValueEventListener);
    }

    public List<Company> getCompaniesList() {

        company = mDatabase
                .child("companies")
                .orderByChild("name");

        loadList();

        return companiesList;
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
