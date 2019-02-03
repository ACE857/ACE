package com.example.ace.ace.SOSsignal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.ModelAndAdapters.sosEmergencyModel;
import com.example.ace.ace.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sosContact extends AppCompatActivity {


    EditText name1,name2,num1,num2;
    Button btn;
    UserDataModel user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_contact);
        user = (UserDataModel) getIntent().getSerializableExtra("user");
        name1 = findViewById(R.id.emerconname1);
        name2= findViewById(R.id.emerconname2);
        num1 = findViewById(R.id.emerconnum1);
        num2 = findViewById(R.id.emerconnnum2);
        btn = findViewById(R.id.update);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("EmergencyContacts");
                sosEmergencyModel sm = new sosEmergencyModel();
                sm.conname1 = name1.getText()+"";
                sm.conname2 = name2.getText()+"";
                sm.connum1 = num1.getText()+"";
                sm.connum2 = num2.getText()+"";
                sm.userid = user.getUserid();
                sm.username = user.getName();
                reference.child(user.getUserid()).setValue(sm);

                Toast.makeText(sosContact.this, "Update Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });




    }
}
