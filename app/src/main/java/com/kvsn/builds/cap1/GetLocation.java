package com.kvsn.builds.cap1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLocation extends AppCompatActivity
{
     CardView currLocation;
     Button otherLocation;
     int PLACE_PICKER_REQUEST = 1;
     FusedLocationProviderClient fusedLocationProviderClient;
     SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;
     Location lastlocation;

     public void goPlacePicker(View view)
     {
	  PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
	  try
	  {
	       startActivityForResult(builder.build(GetLocation.this) , PLACE_PICKER_REQUEST);
	  }catch(GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e)
	  {
	       e.printStackTrace();
	  }

     }
     @Override
     protected void onActivityResult(int requestCode , int resultCode , Intent data)
     {
	  //super.onActivityResult(requestCode, resultCode, data);
	  if(requestCode == PLACE_PICKER_REQUEST)
	  {
	       if(resultCode == RESULT_OK)
	       {
		    Place place = PlacePicker.getPlace(GetLocation.this , data);
		    Geocoder geocoder = new Geocoder(this);
		    try
		    {
			 List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude , place.getLatLng().longitude , 1);
			 String address = addresses.get(0).getAddressLine(0);
			 //String city = addresses.get(0).getAddressLine();
			 String city = addresses.get(0).getLocality();
			 //String country = addresses.get(0).getAddressLine(2)
			 Toast.makeText(GetLocation.this , "AddressPlacePicker" + address , Toast.LENGTH_LONG).show();
			 Toast.makeText(GetLocation.this , "CityPlacePicker" + city , Toast.LENGTH_SHORT).show();

			 startActivity(new Intent(GetLocation.this , AvailableWorkers.class));

		    }catch(IOException e)
		    {

			 e.printStackTrace();
		    }
		    //tv.setText(place.getAddress());
		    //Toast.makeText(GetLocation.this, "Address" + place.getAddress() + "\n", Toast.LENGTH_SHORT).show();
	       }
	  }
     }
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {

	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_get_location);
	  currLocation = findViewById(R.id.curr_Location);
	  otherLocation = findViewById(R.id.other_Location);
	  fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GetLocation.this);
	  sharedPreferences = getSharedPreferences("Categories" , Context.MODE_PRIVATE);
	  editor = sharedPreferences.edit();

     }

     public void dothis(View v)
     {
	  getlocation();
     }

     void getlocation()
     {
	  if(ActivityCompat.checkSelfPermission(this , android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
	  {
	       ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 1);
	  }
	  else
	  {
	       Log.d("TAG" , "getLocation: permissions granted");
	       fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
	       {
		    @Override
		    public void onSuccess(Location location)
		    {

			 if(location != null)
			 {
			      lastlocation = location;
			      double latitude = lastlocation.getLatitude();
			      double longitue = lastlocation.getLongitude();

			      //tvlatitue.setText(""+latitude);
			      //tvlongitude.setText(""+longitue);
			      Geocoder geocoder = new Geocoder(GetLocation.this , Locale.getDefault());


			      try
			      {
				   //List<Address> locationlist = geocoder.getFromLocation(latitude,longitue,1);
				   List<Address> addresses = geocoder.getFromLocation(latitude , longitue , 1);

				   Toast.makeText(GetLocation.this , "Inside try" , Toast.LENGTH_SHORT).show();

				   if(addresses.size() > 0)
				   {
					// Address address = locationlist.get(0);
					String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
					String city1 = addresses.get(0).getLocality();
					String state1 = addresses.get(0).getAdminArea();
					String country1 = addresses.get(0).getCountryName();
					String postalCode1 = addresses.get(0).getPostalCode();
					String knownName1 = addresses.get(0).getFeatureName();
                                /*address.setText(address1);
                                city.setText(city1);
                                state.setText(state1);
                                country.setText(country1);
                                postalcode.setText(postalCode1);
                                knownplace.setText(knownName1);*/

					editor.putString("City" , city1);
					editor.commit();

					Toast.makeText(GetLocation.this , "Address : " + address1 , Toast.LENGTH_SHORT).show();
					Toast.makeText(GetLocation.this , "City : " + city1 , Toast.LENGTH_SHORT).show();


					startActivity(new Intent(GetLocation.this , AvailableWorkers.class));

					// tvphysicaladdress.setText("Address is:"+ address);
				   }

			      }catch(IOException e)
			      {
				   e.printStackTrace();
			      }
			 }
		    }
	       });
	  }
     }
}

