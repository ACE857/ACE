package com.example.ace.ace.FundRaising;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.example.ace.ace.ModelAndAdapters.raiseFundModel;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.ModelAndAdapters.acenModel;
import com.example.ace.ace.R;
import com.example.ace.ace.Write.Anecdote;
import com.example.ace.ace.home;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class requestFunds extends AppCompatActivity {

    UserDataModel user;
    EditText title, desc, upiID, contact;
    Button request,choose;
    ImageView imv;
    int count;
    String userID,username,reqID;
    raiseFundModel am;
    FirebaseStorage storage;
    private final int PICK_IMAGE_PEQUEST = 71;
    StorageReference storageReference;
    FirebaseDatabase database ;
    DatabaseReference req ,newreq;
    int chk=0;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_funds);
        user = (UserDataModel) getIntent().getSerializableExtra("user");

        title = findViewById(R.id.raisefundtitle);
        desc = findViewById(R.id.raiseFundStory);
        upiID = findViewById(R.id.raiseFundUPI);
        request = findViewById(R.id.raiseFundRequest);
        imv = findViewById(R.id.raiseFundImage);
        choose = findViewById(R.id.raiseFundDocs);
        contact = findViewById(R.id.raiseFundContact);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        req = database.getReference("FundRequests");

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(peformCheck()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            requestFunds.this).create();
                    alertDialog.setTitle("oop's Empty Fields");
                    alertDialog.setMessage("Please fill all fields and select an image.");
                    alertDialog.setIcon(R.drawable.info);
                    alertDialog.show();

                }
                else
                    cnt();


            }
        });

        

    }
    private boolean peformCheck() {

        Boolean bl = title.getText().toString().length() <= 0 || upiID.getText().toString().length() <= 0  || desc.getText().toString().length() <= 0 || contact.getText().toString().length() <= 0 || filePath == null;

        return bl;


    }

    void cnt(){
        final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("fundRequestCount");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class);
                count--;
                reqID = "RequestFund" + count;
                r.setValue(count);
                uploadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_PEQUEST);


    }

    private void uploadData() {
        // req.child("count").setValue(999);
        final ProgressDialog progressDialog = new ProgressDialog(requestFunds.this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        req.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                if(chk==0) {
                    chk = 1;
                    // getting a petetion id for this petetion  count is totoal no of petetion till now

                    //  updating value of count in database
                    uploadImage(count);


                    am = new raiseFundModel(user.getUserid(), user.getName() ,reqID, desc.getText().toString(), title.getText().toString(), upiID.getText().toString(), contact.getText().toString());

                    req.child(user.getUserid()).child(reqID).setValue(am);
                    uploadNew(am);
                    Intent intent = new Intent(requestFunds.this, home.class);
                    intent.putExtra("user", user);
                    DatabaseReference.goOffline();
                    requestFunds.this.startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(int count) {

        if(filePath!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(requestFunds.this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();
            final StorageReference ref = storageReference.child(user.getUserid()+"/"+reqID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(requestFunds.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(requestFunds.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100*((float)taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount()));
                            progressDialog.setMessage("Uploaded  "+progress+"%");




                        }
                    });


        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_PEQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imv.setImageBitmap(bitmap);
            }   catch (IOException e){
                e.printStackTrace();
            }

        }
    }


    void uploadNew(raiseFundModel am){
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("NewFundRequest");
        r.child(am.requestNum).setValue(am);
    }


}
