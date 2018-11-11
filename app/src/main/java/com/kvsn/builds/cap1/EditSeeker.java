package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditSeeker extends AppCompatActivity
{

     FirebaseAuth mAuth;
     DatabaseReference mDatabase, msubref;
     EditText name, mobile, state, city, address , profession;
     TextView mail, aadhaar;
     CircleImageView profile;
     ProgressDialog pd;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_edit_seeker);

	  name = (EditText)findViewById(R.id.name_seeker_edit);
	  mail = (TextView)findViewById(R.id.mail_seeker_edit);
	  address = (EditText)findViewById(R.id.seeker_address_edit);
	  aadhaar = (TextView)findViewById(R.id.seeker_aadhaar_edit);
	  city = (EditText)findViewById(R.id.seeker_city_edit);
	  mobile = (EditText)findViewById(R.id.seeker_mobile_edit);
	  state = (EditText)findViewById(R.id.seeker_state_edit);
	  profession = (EditText) findViewById(R.id.profesion_seeker_edit);
	  profile = (CircleImageView)findViewById(R.id.profile_image_seeker_edit);
	  pd = new ProgressDialog(this);

	  mDatabase = FirebaseDatabase.getInstance().getReference();
	  mAuth = FirebaseAuth.getInstance();
	  profile.setOnClickListener(new View.OnClickListener()
	  {
	       @Override
	       public void onClick(View v)
	       {
		    Toast.makeText(EditSeeker.this , "Avneesh Chutiya" , Toast.LENGTH_SHORT).show();
		    startActivity(new Intent(getApplicationContext() , PhotoActivity.class));
	       }
	  });

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
		    name.setText(dataSnapshot.child("Name").getValue(String.class));
		    mail.setText(dataSnapshot.child("Email").getValue(String.class));
		    address.setText(dataSnapshot.child("Address").getValue(String.class));
		    aadhaar.setText(dataSnapshot.child("Aadhaar").getValue(String.class));
		    city.setText(dataSnapshot.child("City").getValue(String.class));
		    mobile.setText(dataSnapshot.child("Mobile").getValue(String.class));
		    state.setText(dataSnapshot.child("State").getValue(String.class));
		    profession.setText(dataSnapshot.child("Profession").getValue(String.class));
		    if(dataSnapshot.hasChild("urlToImage"))
		    {
			 Picasso.get().load(dataSnapshot.child("urlToImage").getValue().toString()).transform(new CropCircleTransformation()).into(profile);
		    }
	       }

	       @Override
	       public void onCancelled(@NonNull DatabaseError databaseError)
	       {

	       }
	  });
     }

     public void updatedata(View v)
     {
	  FirebaseUser user = mAuth.getCurrentUser();
	  msubref = mDatabase.child("Users").child(user.getUid());


	  msubref.child("Name").setValue(name.getText().toString());
	  msubref.child("Street number").setValue(address.getText().toString());
	  msubref.child("city").setValue(city.getText().toString());
	  msubref.child("State").setValue(state.getText().toString());
	  msubref.child("Mobile").setValue(mobile.getText().toString());
	  msubref.child("Profession").setValue(profession.getText().toString());

	  Toast.makeText(this , "Data Updated" , Toast.LENGTH_SHORT).show();
	  startActivity(new Intent(EditSeeker.this , SeekerProfile.class));
	  finish();

     }
}

