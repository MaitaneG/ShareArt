package com.example.shareart.providers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shareart.utils.CompressorBitmapImage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageProvider {

    private final StorageReference storageReference;
    private String childRefernce="";

    public ImageProvider(){
        storageReference= FirebaseStorage.getInstance().getReference();
    }

    public UploadTask saveArgitalpenaFirebasen(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(), 500, 500);
        childRefernce= "Argitalpena "+ new Date()+".jpg";
        StorageReference storage = storageReference.child(childRefernce);
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public UploadTask saveProfilArgazkiaFirebasen(Context context, File file){
        byte[] imageByte = CompressorBitmapImage.getImage(context,file.getPath(), 500, 500);
        childRefernce= "Profila "+ new Date()+".jpg";
        StorageReference storage = storageReference.child(childRefernce);
        UploadTask task = storage.putBytes(imageByte);
        return task;
    }

    public StorageReference getArgazkiarenKokapena(){
        return storageReference.child(childRefernce);
    }

    public void deleteArgazkia(String url){
        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        reference.delete();
    }
}
