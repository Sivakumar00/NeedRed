package com.siva.needred;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import static android.R.attr.thumb;


/**
 * A simple {@link Fragment} subclass.
 */
public class DonorListFragment extends Fragment {

    private RecyclerView mDonorList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    public DonorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_donor_list, container, false);

        mDonorList = (RecyclerView)mMainView.findViewById(R.id.donor_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);


        mDonorList.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
       mDonorList.setLayoutManager(linearVertical);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mDonorList.getContext(),
                linearVertical.getOrientation()
        );
        mDonorList.addItemDecoration(mDividerItemDecoration);

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Donors, DonorsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Donors, DonorsViewHolder>(

                Donors.class,
                R.layout.users_single_layout,
                DonorsViewHolder.class,
                mUsersDatabase


        ) {
            @Override
            protected void populateViewHolder(final DonorsViewHolder donorsViewHolder, Donors donors, int i) {

                donorsViewHolder.setDate(donors.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String blood=dataSnapshot.child("blood_group").getValue().toString();
                        String phone=dataSnapshot.child("mobile").getValue().toString();
                        String address=dataSnapshot.child("place").getValue().toString();


                        donorsViewHolder.setName(userName);
                        donorsViewHolder.setBlood(blood);
                        donorsViewHolder.setAddress(address);
                        donorsViewHolder.setPhone(phone);
                        final String uri=phone;
                        donorsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Email", "Call"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if(i == 0){

                                          /*  Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);*/

                                        }

                                        if(i == 1){
                                          Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+uri));
                                            // callIntent.setData(Uri.parse("tel:"+uri));
                                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            getActivity().startActivity(callIntent);

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mDonorList.setAdapter(friendsRecyclerViewAdapter);


    }

    // viewholder class..

    public static class DonorsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public DonorsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBlood(String blood){
            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_blood);
            userStatusView.setText(blood.toUpperCase());
        }
        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name.toUpperCase());
        }
        public void setPhone(String phone){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_phone);
            userNameView.setText(phone);
        }
        public void setAddress(String address) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_address);
            address.toUpperCase();
            userNameView.setText(address.toUpperCase());
        }
        public void setDate(String date){


        }
    }

}
