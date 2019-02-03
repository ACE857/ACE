package com.example.ace.ace.Write;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.ModelAndAdapters.*;
import com.example.ace.ace.R;
import com.example.ace.ace.home;
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



import java.io.File;
import java.io.IOException;

public class displayAnecdote extends AppCompatActivity {

    TextView uid,name,dm,title,desc,signReq,totalSign,displaySignature;
    ImageView img;
    Button sign;

    private Uri filePath;
    private final int PICK_IMAGE_PEQUEST = 71;
    UserDataModel user;
    private String PetID;
    acenModel pm,dispPet;
    int totSigns;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase database ;
    DatabaseReference ref ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_anecdote);

        user = (UserDataModel) getIntent().getSerializableExtra("user");        //  details of the user that created the petetion
        pm = (acenModel) getIntent().getSerializableExtra("petetion");      // pm contanins details like pid and decesion maker of petetion to be displayed

        uid = findViewById(R.id.dispPetUserID);
        name = findViewById(R.id.dispPetUserName);
        dm = findViewById(R.id.dispPetDM);
        signReq = findViewById(R.id.dispNoOfSign);
        title = findViewById(R.id.dispPetTitle);
        desc = findViewById(R.id.dispPetDesc);
        img = findViewById(R.id.dispPetImage);
        sign = findViewById(R.id.dispPetSign);
        //totalSign = findViewById(R.id.dispPetTotalSignatures);
        displaySignature = findViewById(R.id.DispSignatures);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Anecdote").child(pm.userid).child(pm.acenid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dispPet = dataSnapshot.getValue(acenModel.class);
                setValues();
                loadImage();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(displayAnecdote.this)
                        .setTitle("Confirmation")
                        .setMessage("Please Confirm Your Signature")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Anecdote/"+pm.userid+"/"+pm.acenid);
                                //  reference.child("Sign").child(user.getUserid()).setValue(user.getUserid());
                                reference.child("Likes").child(user.getUserid()).setValue(user.getName());
                                Intent intent = new Intent(displayAnecdote.this,home.class);
                                intent.putExtra("user",user);
                                startActivity(intent);
                                //signThis();
                            }})
                        .setNegativeButton("NO", null).show();

            }
        });


    }



    private void loadImage() {

        Toast.makeText(this, ""+pm.userid+" "+pm.acenid, Toast.LENGTH_SHORT).show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ace28-2e32b.appspot.com").child(pm.userid).child(pm.acenid);
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
                    img.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) { System.out.print(e.getMessage()); }


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    private void setValues() {
        uid.setText(dispPet.userid);
        name.setText(dispPet.username);
        dm.setText(dispPet.whathappned);
        title.setText(dispPet.title);
        desc.setText(dispPet.story);
        signReq.setText(dispPet.finalthought);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Anecdote/"+pm.userid+"/"+pm.acenid).child("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totSigns = (int) dataSnapshot.getChildrenCount();
                //Toast.makeText(displayAnecdote.this, ""+totSigns, Toast.LENGTH_SHORT).show();
                displaySignature.setText(""+totSigns);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


}
