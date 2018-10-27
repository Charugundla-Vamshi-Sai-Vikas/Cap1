package com.kvsn.builds.cap1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class SeekerProfile extends AppCompatActivity
{
     FirebaseAuth mAuth;
     DatabaseReference mDatabase, msubref;
     int gallerypicture = 1;
     TextView sname, smail, saadhaar, smobile, sstate, scity, sporfession, saddress;
     StorageReference UserProfileImagesReference;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_seeker_profile);

	  sname = findViewById(R.id.name_seeker);
	  smail = findViewById(R.id.mail_seeker);
	  saddress = findViewById(R.id.seeker_address);
	  saadhaar = findViewById(R.id.seeker_aadhaar);
	  scity = findViewById(R.id.seeker_city);
	  smobile = findViewById(R.id.seeker_mobile);
	  sporfession = findViewById(R.id.seeker_profession);
	  sstate = findViewById(R.id.seeker_state);

	  mDatabase = FirebaseDatabase.getInstance().getReference();
	  mAuth = FirebaseAuth.getInstance();
	  UserProfileImagesReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

	  Toast.makeText(this , "Test In Profile" , Toast.LENGTH_SHORT).show();
	  retrieve();
     }

     public void retrieve()
     {
	  FirebaseUser user = mAuth.getCurrentUser();
	  msubref = mDatabase.child("Users").child(user.getUid());
	  msubref.addListenerForSingleValueEvent(new ValueEventListener()
	  {
	       @Override
	       public void onDataChange(@NonNull DataSnapshot dataSnapshot)
	       {
		    sname.setText(dataSnapshot.child("Name").getValue(String.class));
		    smail.setText(dataSnapshot.child("Email").getValue(String.class));
		    saddress.setText(dataSnapshot.child("Address").getValue(String.class));
		    saadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
		    scity.setText(dataSnapshot.child("City").getValue(String.class));
		    smobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
		    sporfession.setText(dataSnapshot.child("Profession").getValue(String.class));
		    sstate.setText(dataSnapshot.child("State").getValue(String.class));
	       }

	       @Override
	       public void onCancelled(@NonNull DatabaseError databaseError)
	       {

	       }
	  });
     }
     
     @Override
     public void onBackPressed()
     {
	  super.onBackPressed();
	  finish();
     }
}
