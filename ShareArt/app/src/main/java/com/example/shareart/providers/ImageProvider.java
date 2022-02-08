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

    public ImageProvider(){
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    public UploadTask save(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(), 500, 500);
        StorageReference storage = storageReference.child(new Date()+".jpg");
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }
}
