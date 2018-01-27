package com.siva.needred;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class NeedHelpFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    private View mMainView;
    private RecyclerView mHelpList;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mUsers;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    public NeedHelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_need_help, container, false);
        floatingActionButton = (FloatingActionButton) mMainView.findViewById(R.id.float_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EnquiryActivity.class));
            }
        });

        //init
        mHelpList = (RecyclerView) mMainView.findViewById(R.id.need_recyclerview);
        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Help");
        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        //
        mHelpList.setHasFixedSize(true);
        LinearLayoutManager linearVertical = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mHelpList.setLayoutManager(linearVertical);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(
                mHelpList.getContext(),
                linearVertical.getOrientation()
        );
        mHelpList.addItemDecoration(mDividerItemDecoration);
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Help, NeedHelpFragment.HelpViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Help, NeedHelpFragment.HelpViewHolder>(

                Help.class,
                R.layout.help_single_layout,
                NeedHelpFragment.HelpViewHolder.class,
                mUsersDatabase) {
            @Override
            protected void populateViewHolder(final NeedHelpFragment.HelpViewHolder helpViewHolder, Help help, int i) {

                helpViewHolder.setDate(help.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String blood = dataSnapshot.child("blood_group").getValue().toString();
                        final String phone = dataSnapshot.child("mobile").getValue().toString();
                        String address = dataSnapshot.child("place").getValue().toString();


                        helpViewHolder.setName(userName);
                        helpViewHolder.setBlood(blood);
                        helpViewHolder.setAddress(address);
                        helpViewHolder.setPhone(phone);
                        helpViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Email", "Call"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if (i == 0) {
                                            //

                                        }

                                        if (i == 1) {

                                            String uri = phone;


                                            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                                Intent callIntent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+uri));
                                               // callIntent.setData(Uri.parse("tel:"+uri));
                                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getActivity().startActivity(callIntent);

                                            }

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

        mHelpList.setAdapter(friendsRecyclerViewAdapter);


    }

    // viewholder class..

    public static class HelpViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public HelpViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setBlood(String blood){
            TextView userStatusView = (TextView) mView.findViewById(R.id.help_blood);
            userStatusView.setText(blood.toUpperCase());
        }
        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.help_name);
            userNameView.setText(name.toUpperCase());
        }
        public void setPhone(String phone){

            TextView userNameView = (TextView) mView.findViewById(R.id.help_mobile);
            userNameView.setText(phone);
        }
        public void setAddress(String address) {

            TextView userNameView = (TextView) mView.findViewById(R.id.help_place);
            address.toUpperCase();
            userNameView.setText(address.toUpperCase());
        }
        public void setDate(String date){


        }
    }

}


