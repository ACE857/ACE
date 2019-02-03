package com.example.ace.ace.FundRaising;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Toast;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.ace.ace.ModelAndAdapters.*;

import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.ModelAndAdapters.raiseFundModel;
import com.example.ace.ace.ModelAndAdapters.recyclerAcen;
import com.example.ace.ace.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class homeReqFund extends AppCompatActivity {

    FirebaseRecyclerAdapter<raiseFundModel, recyclerAcen> adapter;
    FirebaseRecyclerOptions<raiseFundModel> options;
    UserDataModel user;
    RecyclerView recyclerView;
    Intent intent;
    DatabaseReference mDatabase,query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_req_fund);

        user = (UserDataModel) getIntent().getSerializableExtra("user");
        recyclerView = findViewById(R.id.homerecyclerRaiseFund);

        user = (UserDataModel) getIntent().getSerializableExtra("user");
        Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewFundRequest");
        //mDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Recycler View

        query = FirebaseDatabase.getInstance().getReference().child("NewFundRequest");
        options = new FirebaseRecyclerOptions.Builder<raiseFundModel>()
                .setQuery(query, raiseFundModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<raiseFundModel, recyclerAcen>(
                options) {
            @Override
            public recyclerAcen onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view, parent, false);


                return new recyclerAcen(v);
            }

            @Override
            protected void onBindViewHolder(recyclerAcen holder, final int position, final raiseFundModel current) {


                holder.setName(current.userName);
                holder.setTitle(current.titles);
                holder.setImage(getApplicationContext(),current.requestNum,current.userID);
                Toast.makeText(homeReqFund.this, ""+current.userID, Toast.LENGTH_SHORT).show();
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(homeReqFund.this, ""+current.requestNum, Toast.LENGTH_SHORT).show();
                        intent = new Intent(homeReqFund.this,displayRequest.class);
                        intent.putExtra("user",user);
                        raiseFundModel openPet = new raiseFundModel();
                        openPet.userID = current.userID;
                        openPet.requestNum = current.requestNum;
                        intent.putExtra("petetion",openPet);
                        homeReqFund.this.startActivity(intent);
                    }
                });

                holder.mview.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {


                        return true;
                    }
                });
            }


        };
        //Populate Item into Adapter
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    private void displayMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
