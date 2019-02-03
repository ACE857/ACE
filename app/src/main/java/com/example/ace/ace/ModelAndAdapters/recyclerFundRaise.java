package com.example.ace.ace.ModelAndAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ace.ace.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class recyclerFundRaise extends RecyclerView.ViewHolder {


    public View mview;
    public recyclerFundRaise(View itemView) {
        super(itemView);
        mview = itemView;

    }
    public void setName(String name){
        TextView recName = mview.findViewById(R.id.recUsrName);
        recName.setText(name);
    }
    public void setTitle(String title){
        TextView t = mview.findViewById(R.id.recUsrTitle);
        t.setText(title);
    }
    public void setImage(Context ctx, String image, String user){
        final ImageView iv = mview.findViewById(R.id.recImg);
        /*Picasso.with(ctx).load(image).into(iv);*/

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(user+"/"+image);
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    iv.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}

    }

}

