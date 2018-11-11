package com.kvsn.builds.cap1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class Show_Available_worker_Profile extends AppCompatActivity
{

     TextView tv_name1, tv_profession1, tv_phone1, tv_alter_phone1, tv_rating_num1, tv_experience_num1, tv_email_id;
     LinearLayout l1;
     Button b1;

     ImageView img_profile1;
     private DatabaseReference database;
     private DatabaseReference mref;
     private DatabaseReference msubref;
     private FirebaseAuth mAuth;
     private String UrlToImg;
     private String key;

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_show__available_worker__profile);
	  SharedPreferences sharedPreferences = getSharedPreferences("Getkey" , Context.MODE_PRIVATE);
	  key = sharedPreferences.getString("key" , "");
	  Toast.makeText(getApplicationContext() , "key in profile" + key , Toast.LENGTH_SHORT).show();
	  tv_name1 = (TextView)findViewById(R.id.tv_name);

	  b1 = (Button)findViewById(R.id.finish);
	  b1.setClickable(false);
	  l1 = (LinearLayout)findViewById(R.id.confirmandfinish);
	  tv_profession1 = (TextView)findViewById(R.id.tv_profession);

	  tv_phone1 = (TextView)findViewById(R.id.tv_phone);

	  tv_alter_phone1 = (TextView)findViewById(R.id.tv_alter_phone);

	  tv_rating_num1 = (TextView)findViewById(R.id.tv_rating_num);

	  tv_experience_num1 = (TextView)findViewById(R.id.tv_experience_num);

	  img_profile1 = (ImageView)findViewById(R.id.img_profile);
	  tv_email_id = (TextView)findViewById(R.id.tv_email_id);
	  displayProfile();
     }

     public void displayProfile()
     {
	  database = FirebaseDatabase.getInstance().getReference();
	  //  Toast.makeText(getApplicationContext(),"id"+FirebaseAuth.getInstance().getCurrentUser().getUid().toString(),Toast.LENGTH_SHORT).show();
	  database.child("user").child(key).addValueEventListener(new ValueEventListener()
	  {
	       @Override
	       public void onDataChange(DataSnapshot dataSnapshot)
	       {
		    if(dataSnapshot.exists())
		    {

			 tv_name1.setText(dataSnapshot.child("Name").getValue().toString());
			 tv_profession1.setText(dataSnapshot.child("Profession").getValue().toString());
			 tv_phone1.setText(dataSnapshot.child("Contact number").getValue().toString());
			 tv_alter_phone1.setText(dataSnapshot.child("Alternate contact number").getValue().toString());
			 tv_email_id.setText(dataSnapshot.child("Email").getValue().toString());
			 if(dataSnapshot.hasChild("urlToImage"))
			 {
			      UrlToImg = dataSnapshot.child("urlToImage").getValue().toString();
			      if(! UrlToImg.isEmpty())
			      {

				   Picasso.get().load(UrlToImg).transform(new CropCircleTransformation()).into(img_profile1);

			      }
			 }


		    }

		    else
		    {
			 Toast.makeText(getApplicationContext() , "Data does not exit" , Toast.LENGTH_SHORT).show();
		    }
	       }

	       @Override
	       public void onCancelled(DatabaseError databaseError)
	       {
		    Toast.makeText(getApplicationContext() , "Data loading failed" , Toast.LENGTH_SHORT).show();
	       }
	  });


     }

     public void showconfirm(View v)
     {

	  String number = tv_phone1.getText().toString();
	  Intent i = new Intent(Intent.ACTION_DIAL);
	  i.setData(Uri.parse("tel:+91" + number));
	  startActivity(i);
	  l1.setVisibility(View.VISIBLE);
	  b1.setClickable(false);
     }

     public void showdone(View v)
     {
	  b1.setClickable(true);
     }

     public void review(View v)
     {
	  final AlertDialog.Builder builder = new AlertDialog.Builder(Show_Available_worker_Profile.this);
	  LayoutInflater inflater = getLayoutInflater();
	  builder.setTitle("Please Review The Work..");
	  View dialogLayout = inflater.inflate(R.layout.activity_reviewdailog , null);
	  final RatingBar ratingBar = (RatingBar)dialogLayout. findViewById(R.id.ratingbar);
	  builder.setView(dialogLayout);
	  builder.show();
     }

     public void submitreview(View v)
     {

     }
}
