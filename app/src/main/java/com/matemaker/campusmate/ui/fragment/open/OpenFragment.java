package com.matemaker.campusmate.ui.fragment.open;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.matemaker.campusmate.R;
import com.matemaker.campusmate.database.MoimDTO;
import com.matemaker.campusmate.ui.activity.MainActivity;

import java.io.File;


public class OpenFragment extends Fragment {

    View root;
    RadioGroup groupGender;
    Button create , image;
    long timestamp;

    Intent data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_open, container, false);
        groupGender = root.findViewById(R.id.category_group);
        timestamp = System.currentTimeMillis();
        create = root.findViewById(R.id.button_open_create);
        image = root.findViewById(R.id.button_open_image);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = ((EditText)root.findViewById(R.id.edit_open_title)).getText().toString();
                final String subtitle = ((EditText)root.findViewById(R.id.edit_open_subtitle)).getText().toString();
                final String age_max = ((EditText)root.findViewById(R.id.edit_open_age2)).getText().toString();
                final String age_min = ((EditText)root.findViewById(R.id.edit_open_age)).getText().toString();
                final String category = ((RadioButton)root.findViewById(groupGender.getCheckedRadioButtonId())).getText().toString();

                if (MainActivity.uid != null) {
                    MoimDTO moimDTO = new MoimDTO(MainActivity.uid, timestamp, title, subtitle, "", age_max, age_min, category);
                    createMoim(moimDTO);
                }
                getActivity().onBackPressed();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,10);
            }
        });

        return root;
    }


    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }


    public void uploadImage(final String uid, Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        final StorageReference logoRef = storageRef.child("logos").child(uid).child(uri.getLastPathSegment());

        UploadTask uploadTask = logoRef.putFile(uri);

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return logoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    FirebaseDatabase.getInstance().getReference().child("moim").child(uid).child("image").setValue(downloadUri.getPath());
                }else{

                }
            }
        });
    /*addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("업로드 실패 ");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("업로드 성공 ");
                String downloadUrl = logoRef.getDownloadUrl().toString();
                FirebaseDatabase.getInstance().getReference().child("moim").child(uid).child("image").setValue(downloadUrl);
            }
        });*/


    }

    public void createMoim(MoimDTO moimDTO) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("moim");
        String uid = ref.push().getKey();
        ref.child(uid).setValue(moimDTO.toMap());

        uploadImage(uid,data.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data != null){
                this.data = data;
            }

        }
    }
}