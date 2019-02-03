package com.example.ace.ace;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.ace.FundRaising.homeReqFund;
import com.example.ace.ace.FundRaising.requestFunds;
import com.example.ace.ace.ModelAndAdapters.*;
import com.example.ace.ace.FundRaising.donateFund;
import com.example.ace.ace.FundRaising.fundTransfer;
import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.Write.*;
import com.example.ace.ace.SOSsignal.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    ImageView write,donfund,needfund,sos,nav;
    UserDataModel user;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView textView;
    Intent intent;
    FirebaseRecyclerAdapter<acenModel, recyclerAcen> adapter;
    FirebaseRecyclerOptions<acenModel> options;
    RecyclerView recyclerView;
    DatabaseReference mDatabase,query;
    ArrayList<acenModel> list;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = (UserDataModel) getIntent().getSerializableExtra("user");
        Toast.makeText(this, ""+user.getName(), Toast.LENGTH_SHORT).show();
        write = findViewById(R.id.navwrite);
        donfund = findViewById(R.id.homedonatefunds);
        needfund = findViewById(R.id.navneedfund);
        sos = findViewById(R.id.navsos);
        nav = findViewById(R.id.homenav);

        needfund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,requestFunds.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,Anecdote.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,sosSig.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        donfund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this,homeReqFund.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout navDrawer = findViewById(R.id.homeDrawer);
                // If navigation drawer is not open yet open it else close it.
                if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(Gravity.START);
                else navDrawer.closeDrawer(Gravity.END);
            }
        });

        recyclerView = findViewById(R.id.homeRecView);

        user = (UserDataModel) getIntent().getSerializableExtra("user");
        Toast.makeText(this, user.getName(), Toast.LENGTH_SHORT).show();
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.homeDrawer);
        navigationView = findViewById(R.id.homeNavView);

        View head = navigationView.getHeaderView(0);
        textView = head.findViewById(R.id.headerUserId);
        textView.setText(user.getName());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("NewAcendote");
        //mDatabase.keepSynced(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));





        // assigning click listners to the navigation items
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.homeNavnewAnecdote : item.setChecked(true);
                        intent = new Intent(home.this,Anecdote.class);
                        intent.putExtra("user",user);

                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.homeDonate : item.setChecked(true);

                        intent = new Intent(home.this,homeReqFund.class);
                        intent.putExtra("user",user);

                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.homeMyProfile : item.setChecked(true);
                        displayMessage("My Profile Selected");

                        intent = new Intent(home.this,sosContact.class);
                        intent.putExtra("user",user);

                        home.this.startActivity(intent);

                        drawerLayout.closeDrawers();
                        return true;




                    case R.id.homeLogout : item.setChecked(true);

                        intent = new Intent(home.this,MainActivity.class);
                        home.this.startActivity(intent);


                        displayMessage("Logged Out");
                        drawerLayout.closeDrawers();
                        return true;


                }

                return false;
            }
        });

        // Recycler View

        query = FirebaseDatabase.getInstance().getReference().child("NewAcendote");
        options = new FirebaseRecyclerOptions.Builder<acenModel>()
                .setQuery(query, acenModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<acenModel, recyclerAcen>(
                options) {
            @Override
            public recyclerAcen onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_view, parent, false);


                return new recyclerAcen(v);
            }

            @Override
            protected void onBindViewHolder(recyclerAcen holder, final int position, final acenModel current) {


                holder.setName(current.username);
                holder.setTitle(current.title);
                holder.setImage(getApplicationContext(),current.acenid,current.userid);
                Toast.makeText(home.this, ""+current.userid, Toast.LENGTH_SHORT).show();
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(home.this, ""+current.acenid, Toast.LENGTH_SHORT).show();
                        intent = new Intent(home.this,displayAnecdote.class);
                        intent.putExtra("user",user);
                        acenModel openPet = new acenModel();
                        openPet.userid = current.userid;
                        openPet.acenid = current.acenid;
                        intent.putExtra("petetion",openPet);

                        home.this.startActivity(intent);
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
