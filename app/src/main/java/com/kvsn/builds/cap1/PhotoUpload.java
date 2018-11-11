package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PhotoUpload extends AppCompatActivity
{

     int SELECT_PICTURE = 100;
     CircleImageView image;
     Uri filepath;
     private FirebaseAuth mAuth;
     private ProgressDialog pd;
     private DatabaseReference rdatabase;
     private String ruserid;
     private FirebaseStorage rstorage;
     private StorageReference rstorageReference;
     private DatabaseReference msubref;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_photo_upload);
	  setTitle("Select Image");
	  image = findViewById(R.id.uploadimage);
	  pd = new ProgressDialog(this);
	  mAuth = FirebaseAuth.getInstance();
	  rstorage = FirebaseStorage.getInstance();
	  ruserid = mAuth.getCurrentUser().getUid().toString();
	  rdatabase = FirebaseDatabase.getInstance().getReference("Users").child(ruserid);
	  msubref = FirebaseDatabase.getInstance().getReference("seeker");
	  rstorageReference = rstorage.getReference().child("Images").child("RecruiterImages");
     }

     public void chooseimage(View v)
     {
	  Intent i = new Intent();
	  i.setType("image/*");
	  i.setAction(Intent.ACTION_GET_CONTENT);
	  startActivityForResult(Intent.createChooser(i , "Select Picture") , SELECT_PICTURE );
     }

     public void uploadimage(View v)
     {
          final ProgressDialog pd = new ProgressDialog(this);
          pd.setTitle("Uploading....");
          pd.show();

	  StorageReference ref = rstorageReference.child(mAuth.getCurrentUser().getUid().toString());
	  ref.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
	  {
	       @Override
	       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
	       {
		    Handler handler = new Handler();
		    handler.postDelayed(new Runnable()
		    {
			 @Override
			 public void run()
			 {
			      pd.dismiss();
			 }
		    } , 500);
		    String urlToImage = taskSnapshot.getDownloadUrl().toString();
		    rdatabase.child("urlToImage").setValue(urlToImage);

		    Picasso.get().load(urlToImage).transform(new CropCircleTransformation()).into(image);

		    Toast.makeText(getApplicationContext() , "Uploaded" , Toast.LENGTH_SHORT).show();
	       }
	  }).addOnFailureListener(new OnFailureListener()
	  {
	       @Override
	       public void onFailure(@NonNull Exception e)
	       {
		    pd.dismiss();
		    Toast.makeText(getApplication() , "Uploading failed" + e.getMessage() , Toast.LENGTH_SHORT).show();
	       }
	  }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
	  {
	       @Override
	       public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
	       {
		    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
		    pd.setMessage("Uploaded" + (int)progress + "%");
	       }
	  });
     }
}
