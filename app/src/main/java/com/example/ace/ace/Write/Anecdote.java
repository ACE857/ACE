package com.example.ace.ace.Write;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ace.ace.ModelAndAdapters.UserDataModel;
import com.example.ace.ace.R;
import com.example.ace.ace.ModelAndAdapters.*;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Anecdote extends AppCompatActivity {

    private String info,date;
    EditText title,sol,signThresh;
    EditText decMaker;
    ImageView photo;
    Button submit,choose;
    private Uri filePath;
    private final int PICK_IMAGE_PEQUEST = 71;
    UserDataModel user;
    private String PetID;
    acenModel am;
    int count;
    FirebaseStorage storage;
    StorageReference storageReference;
    int chk=0;
    FirebaseDatabase database ;
    ArrayList hintName = new ArrayList();
    ArrayList hintID = new ArrayList();
    DatabaseReference req ,newreq;
    String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anecdote);
        user = (UserDataModel) getIntent().getSerializableExtra("user");

        title = findViewById(R.id.newPetTitle);
        decMaker = findViewById(R.id.newPetDM);

        sol = findViewById(R.id.newPetSol);
        photo = findViewById(R.id.newPetImg);
        submit = findViewById(R.id.newPetSumbit);
        signThresh = findViewById(R.id.netPetSignThresh);
        choose = findViewById(R.id.newPetuploadBtn);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        database = FirebaseDatabase.getInstance();
        req = database.getReference("Anecdote");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(peformCheck()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            Anecdote.this).create();
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


    @Override
    protected void onRestart() {
        super.onRestart();
        chk=0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        chk=0;
    }

    private boolean peformCheck() {

        Boolean bl = title.getText().toString().length() <= 0 || decMaker.getText().toString().length() <= 0 || sol.getText().toString().length() <= 0 || filePath == null;

        return bl;


    }

    void cnt(){
        final DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("count");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getValue(Integer.class);
                count--;
                PetID = "" + count;
                r.setValue(count);
                uploadData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void uploadData() {
       // req.child("count").setValue(999);
        final ProgressDialog progressDialog = new ProgressDialog(Anecdote.this);
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


                    am = new acenModel(user.getUserid(), user.getName() ,PetID, title.getText().toString(), decMaker.getText().toString(), sol.getText().toString(), PetID, signThresh.getText().toString());

                    req.child(user.getUserid()).child(PetID).setValue(am);
                    uploadNew(am);
                    Intent intent = new Intent(Anecdote.this, home.class);
                    intent.putExtra("user", user);
                    DatabaseReference.goOffline();
                    Anecdote.this.startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    void uploadNew(acenModel am){
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("NewAcendote");
        r.child(am.acenid).setValue(am);
    }

    private void uploadImage(int count) {

        if(filePath!=null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(Anecdote.this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();

            final StorageReference ref = storageReference.child(user.getUserid()+"/"+PetID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Anecdote.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Anecdote.this, "Upload Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE_PEQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_PEQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                photo.setImageBitmap(bitmap);
            }   catch (IOException e){
                e.printStackTrace();
            }

        }
    }


    public void showInfoTitle(View view){
        info = "\nThis is the first thing people will see about your acendote. Get their attention with a short title that focuses on the main idea you’d like them to convey.\n" +
                "\n1.Keep it short and to the point\n" +
                "\n2.Focus on the key idea.\n" +
                "\n3.Communicate well\n";

        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Title");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoDec(View view){
        info = "Describe what this story is about.\n" +
                "\n1. Who was involved in the story?\n" +
                "\n2. What happened?\n" +
                "\n2. Where did it happen ?\n" +
                "\n3. When did it happen ?\n";
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The W4's");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoSol(View view){
        info = "This is your story tell, tell your experience and everything you want to share with othersn" +
                "\n1. Describe the people involved and the situation you were facing\n" +
                "\n2. Describe the solution\n" +
                "\n3. Make it personal\n" +
                "\n4. Respect others\n";
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Solution");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();

    }
    public void showInfoPhoto(View view){
        info = "Acendote's with a photo receive six times more views than those without. Include one that captures the emotion of your story.\n" +
                "\n1. Choose a photo that captures the emotion of your story\n" +
                "\n2. Try to upload photos that are relevant and appealing\n" +
                "\n3. Keep it friendly for all audiences\n" ;
        AlertDialog alertDialog = new AlertDialog.Builder(
                view.getContext()).create();
        alertDialog.setTitle("The Image");
        alertDialog.setMessage(info);
        alertDialog.setIcon(R.drawable.info);
        alertDialog.show();
    }
}
