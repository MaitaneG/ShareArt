package com.example.shareart.providers;

import android.content.Context;

import com.example.shareart.utils.CompressorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageProvider {

    private StorageReference storageReference;
    private String childRefernce="";

    public ImageProvider(){
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    public UploadTask gordeFirebasen(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(), 500, 500);
        childRefernce= "Argitarapena "+ new Date()+".jpg";
        StorageReference storage = storageReference.child(childRefernce);
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public StorageReference lortuArgazkiarenKokapena(){
        return storageReference.child(childRefernce);
    }
}
