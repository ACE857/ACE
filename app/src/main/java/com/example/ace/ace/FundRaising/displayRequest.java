package com.example.ace.ace.FundRaising;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.ace.ModelAndAdapters.*;
import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.ModelAndAdapters.acenModel;
import com.example.ace.ace.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class displayRequest extends AppCompatActivity {

    TextView username,title,desc,upiid,contact;
    Button donate;
    raiseFundModel pm,dispPet;
    UserDataModel user;
    ImageView imv;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database ;
    DatabaseReference ref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);

        user = (UserDataModel) getIntent().getSerializableExtra("user");        //  details of the user that created the petetion
        pm = (raiseFundModel) getIntent().getSerializableExtra("petetion");      // pm contanins details like pid and decesion maker of petetion to be displayed

        username = findViewById(R.id.displayReqfundName);
        title = findViewById(R.id.displayReqfundTitle);
        desc = findViewById(R.id.displayReqfundDesc);
        upiid = findViewById(R.id.displayReqfundUpiID);
        contact = findViewById(R.id.displayReqfundConatct);
        donate = findViewById(R.id.displayReqfundDonate);
        imv = findViewById(R.id.displayReqfundImage);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("FundRequests").child(pm.userID).child(pm.requestNum);

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(displayRequest.this,fundTransfer.class);
                donationClassModel dm = new donationClassModel();
                dm.name = username.getText()+"";
                dm.upiID = upiid.getText()+"";
                intent.putExtra("user", user);
                intent.putExtra("tran",dm);
                startActivity(intent);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dispPet = dataSnapshot.getValue(raiseFundModel.class);
                setValues();
                loadImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private void loadImage() {

        Toast.makeText(this, ""+pm.userID+" "+pm.requestNum, Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ace28-2e32b.appspot.com").child(pm.userID).child(pm.requestNum);
      /*

        Generate file download URL

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.e("Tuts+", "uri: " + uri.toString());
                Toast.makeText(displayPetetion.this, uri.toString(), Toast.LENGTH_SHORT).show();

                //Handle whatever you're going to do with the URL here
            }
        });*/
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imv.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) { System.out.print(e.getMessage()); }


    }

    private void setValues() {
        username.setText(dispPet.userName);
        title.setText(dispPet.titles);
        desc.setText(dispPet.story);
        upiid.setText(dispPet.upiID);
        contact.setText(dispPet.contact);

    }
}
