package com.kvsn.builds.cap1;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class PhotoActivity extends AppCompatActivity
{

     ImageView iv;
     StorageReference UserProfileImagesReference;
     FirebaseUser currentUserId;
     FirebaseAuth mauth;
     DatabaseReference RootRef;
     int gallerypicture = 77;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_photo);

	  iv = findViewById(R.id.iv);
	  currentUserId = FirebaseAuth.getInstance().getCurrentUser();
	  UserProfileImagesReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
	  RootRef = FirebaseDatabase.getInstance().getReference();
	  mauth = FirebaseAuth.getInstance();

     }

     public void image(View v)
     {
	  Intent i = new Intent();
	  i.setAction(Intent.ACTION_GET_CONTENT);
	  i.setType("image/*");
	  startActivityForResult(i , gallerypicture);
     }

     @Override
     protected void onActivityResult(int requestCode , int resultCode , Intent data)
     {
	  super.onActivityResult(requestCode , resultCode , data);

	  if(requestCode == gallerypicture && resultCode == RESULT_OK && data != null)
	  {
	       Uri imageuri = data.getData();

	       //crop activity from library
	       CropImage.activity().setAspectRatio(1 , 1).start(this);
	  }

	  if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
	  {
	       CropImage.ActivityResult result = CropImage.getActivityResult(data);

	       if(resultCode == RESULT_OK)
	       {
		    Uri resultri = result.getUri();

		    final StorageReference filepath = UserProfileImagesReference.child(currentUserId + ".jpg");
		    filepath.putFile(resultri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
		    {
			 @Override
			 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
			 {
			      if(task.isSuccessful())
			      {
				   Toast.makeText(PhotoActivity.this , "Pic uploaded successfully" , Toast.LENGTH_SHORT).show();
				   final String downloadUrl = filepath.getDownloadUrl().toString();

				   final String uid = currentUserId.getUid().toString();

				   RootRef.child("Users").child(uid).child("Images").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>()
				   {
					@Override
					public void onComplete(@NonNull Task<Void> task)
					{
					     if(task.isSuccessful())
					     {
						  Toast.makeText(PhotoActivity.this , "Image Save In Database" , Toast.LENGTH_SHORT).show();

						  Picasso.get().load(downloadUrl).into(iv);
					     }
					     else
					     {
						  String message = task.getException().toString();
						  Toast.makeText(PhotoActivity.this , "Error :" + message , Toast.LENGTH_SHORT).show();

					     }
					}
				   });
			      }
                              else
				   {
					String message = String.valueOf(task.getException());
					Toast.makeText(PhotoActivity.this , "Error " + message , Toast.LENGTH_SHORT).show();
				   }
			      }
			 });
		    }
	       }
	  }
     }
