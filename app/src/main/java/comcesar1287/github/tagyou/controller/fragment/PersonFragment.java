package comcesar1287.github.tagyou.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comcesar1287.github.tagyou.R;
import comcesar1287.github.tagyou.controller.adapter.PersonAdapter;
import comcesar1287.github.tagyou.controller.domain.Person;
import comcesar1287.github.tagyou.controller.domain.Tag;
import comcesar1287.github.tagyou.controller.firebase.FirebaseHelper;
import comcesar1287.github.tagyou.controller.interfaces.RecyclerViewOnClickListenerHack;

public class PersonFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    RecyclerView mRecyclerViewTag1, mRecyclerViewTag2, mRecyclerViewTag3;
    public List<Person> mListTag1, mListTag2, mListTag3;

    ArrayList<Person> peopleList, affinity, group, segment;
    ArrayList<Tag> tagsList;

    HashMap<String, Tag> mapTags;
    HashMap<String, Person> mapPeople;

    public PersonAdapter adapter1, adapter2, adapter3;

    private DatabaseReference mDatabase;

    Query person, tags;

    ValueEventListener valueEventListener;
    ValueEventListener singleValueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setupRecyclerViewAffinity(view);

        setupRecyclerViewGroup(view);

        setupRecyclerViewSegment(view);

        getPeopleList();

        return view;

    }

    private void setupRecyclerViewAffinity(View view) {
        mRecyclerViewTag1 = (RecyclerView) view.findViewById(R.id.rv_list_tag1);
        mRecyclerViewTag1.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewTag1.setLayoutManager(linearLayoutManager);

        mListTag1 = new ArrayList<>();
        adapter1 = new PersonAdapter(getActivity(), mListTag1);
        adapter1.setRecyclerViewOnClickListenerHack(this);
        mRecyclerViewTag1.setAdapter( adapter1 );

        mRecyclerViewTag1.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerViewTag1, this ));
    }

    private void setupRecyclerViewGroup(View view) {
        mRecyclerViewTag2 = (RecyclerView) view.findViewById(R.id.rv_list_tag2);
        mRecyclerViewTag2.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewTag2.setLayoutManager(linearLayoutManager);

        mListTag2 = new ArrayList<>();
        adapter2 = new PersonAdapter(getActivity(), mListTag2);
        adapter2.setRecyclerViewOnClickListenerHack(this);
        mRecyclerViewTag2.setAdapter( adapter2 );

        mRecyclerViewTag2.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerViewTag2, this ));
    }

    private void setupRecyclerViewSegment(View view) {
        mRecyclerViewTag3 = (RecyclerView) view.findViewById(R.id.rv_list_tag3);
        mRecyclerViewTag3.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewTag3.setLayoutManager(linearLayoutManager);

        mListTag3 = new ArrayList<>();
        adapter3 = new PersonAdapter(getActivity(), mListTag3);
        adapter3.setRecyclerViewOnClickListenerHack(this);
        mRecyclerViewTag3.setAdapter( adapter3 );

        mRecyclerViewTag3.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerViewTag3, this ));
    }

    public void loadList(){

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                peopleList = new ArrayList<>();
                mapPeople = new HashMap<>();

                Person p;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    p = new Person();
                    p.setProfilePic((String)postSnapshot.child("profile_pic").getValue());
                    p.setName((String)postSnapshot.child("name").getValue());
                    p.setHashtag((String)postSnapshot.child("hashtag").getValue());
                    p.setEmail((String)postSnapshot.child("email").getValue());
                    p.setBirth((String)postSnapshot.child("birth").getValue());
                    p.setPhone((String)postSnapshot.child("phone").getValue());
                    p.setSex((String)postSnapshot.child("sex").getValue());

                    mapPeople.put(postSnapshot.getKey(), p);
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
                
                getListTags();
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

    private void loadListTags() {

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tagsList = new ArrayList<>();
                mapTags = new HashMap<>();

                Tag tag;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    tag = new Tag();
                    tag.setAffinity((String)postSnapshot.child("affinity").getValue());
                    tag.setGroup((String)postSnapshot.child("group").getValue());
                    tag.setSegment((String)postSnapshot.child("segment").getValue());

                    mapTags.put(postSnapshot.getKey(), tag);
                    tagsList.add(tag);
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

                setupInfoRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /*dialog.dismiss();
                Toasty.error(PartnerCategoryActivity.this, getResources().getString(R.string.error_loading_partners), Toast.LENGTH_SHORT, true).show();
                finish();*/
            }
        };

        tags.addValueEventListener(valueEventListener);

        tags.addListenerForSingleValueEvent(singleValueEventListener);
    }

    private void setupInfoRecyclerView() {
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        affinity = new ArrayList<>();
        group = new ArrayList<>();
        segment = new ArrayList<>();

        String []tagsAffinity, tagsGroup, tagsSegment;
        if(mapTags.containsKey(Uid)){
            Tag me = mapTags.get(Uid);

            for(Map.Entry<String, Tag> entry: mapTags.entrySet()){
                String affinityList = entry.getValue().getAffinity();
                tagsAffinity = affinityList.split(";");

                String groupList = entry.getValue().getGroup();
                tagsGroup = groupList.split(";");

                String segmentList = entry.getValue().getSegment();
                tagsSegment = segmentList.split(";");

                for(String tagEntry: tagsAffinity){
                    if(me.getAffinity().contains(tagEntry) && !entry.getKey().equals(Uid)){
                        affinity.add(mapPeople.get(entry.getKey()));
                        break;
                    }
                }

                for(String tagEntry: tagsGroup){
                    if(me.getGroup().contains(tagEntry) && !entry.getKey().equals(Uid)){
                        group.add(mapPeople.get(entry.getKey()));
                        break;
                    }
                }

                for(String tagEntry: tagsSegment){
                    if(me.getSegment().contains(tagEntry) && !entry.getKey().equals(Uid)){
                        segment.add(mapPeople.get(entry.getKey()));
                        break;
                    }
                }
            }

            setupLists();
        }else{
            setupLists();
        }
    }

    private void setupLists() {
        mListTag1.clear();
        mListTag1.addAll(affinity);
        adapter1.notifyDataSetChanged();

        mListTag2.clear();
        mListTag2.addAll(group);
        adapter2.notifyDataSetChanged();

        mListTag3.clear();
        mListTag3.addAll(segment);
        adapter3.notifyDataSetChanged();
        //dialog.dismiss();
    }

    private List<Tag> getListTags() {

        tags = mDatabase
                .child(FirebaseHelper.FIREBASE_DATABASE_TAGS);

        loadListTags();

        return tagsList;
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
        /*Intent intent = new Intent(getActivity(), PersonsDetailsActivity.class);
        intent.putExtra(Utility.KEY_CONTENT_EXTRA_COMPANY, mListTag1.get(position));
        startActivity(intent);*/
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