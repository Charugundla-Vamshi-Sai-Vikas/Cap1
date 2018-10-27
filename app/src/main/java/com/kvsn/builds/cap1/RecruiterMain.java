package com.kvsn.builds.cap1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class RecruiterMain extends AppCompatActivity implements DuoMenuView.OnMenuClickListener
{
     private MenuAdapter mMenuAdapter;
     private ViewHolder mViewHolder;
     ImageView header;
     FirebaseAuth mAuth;
     ProgressDialog pd;

     private ArrayList<String> mTitles = new ArrayList<>();

     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_recruiter_main);

	  pd = new ProgressDialog(this);
	  mAuth = FirebaseAuth.getInstance();
	  mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
	  header = findViewById(R.id.image_header);

	  // Initialize the views
	  mViewHolder = new ViewHolder();

	  // Handle toolbar actions
	  handleToolbar();

	  // Handle menu actions
	  handleMenu();

	  // Handle drawer actions
	  handleDrawer();

	  mMenuAdapter.setViewSelected(0 , true);
	  setTitle(mTitles.get(0));
     }

     private void handleToolbar()
     {
	  setSupportActionBar(mViewHolder.mToolbar);
     }

     private void handleDrawer()
     {
	  DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this , mViewHolder.mDuoDrawerLayout , mViewHolder.mToolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close);

	  mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
	  duoDrawerToggle.syncState();

     }

     private void handleMenu()
     {
	  mMenuAdapter = new MenuAdapter(mTitles);

	  mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
	  mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
     }

     @Override
     public void onFooterClicked()
     {
	  pd.setTitle("Logging out");
	  pd.show();
	  mAuth.signOut();
	  startActivity(new Intent(RecruiterMain.this , MainActivity.class));
	  finish();
     }

     @Override
     public void onHeaderClicked()
     {
	  Intent i = new Intent(this , RecruiterProfile.class);
	  ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
	  startActivity(i , actop.toBundle());
	  overridePendingTransition(R.anim.fadein , R.anim.fadeout);
     }

     @Override
     public void onOptionClicked(int position , Object objectClicked)
     {
	  switch(position)
	  {
	       case 1:
		    Intent i = new Intent(this , RecruiterProfile.class);
		    ActivityOptionsCompat actop = ActivityOptionsCompat.makeSceneTransitionAnimation(this , header , ViewCompat.getTransitionName(header));
		    startActivity(i , actop.toBundle());
		    overridePendingTransition(R.anim.fadein , R.anim.fadeout);
	       default:
		    break;
	  }

	  // Close the drawer
	  mViewHolder.mDuoDrawerLayout.closeDrawer();
     }

     private class ViewHolder
     {
	  private DuoDrawerLayout mDuoDrawerLayout;
	  private DuoMenuView mDuoMenuView;
	  private Toolbar mToolbar;

	  ViewHolder()
	  {
	       mDuoDrawerLayout = findViewById(R.id.drawer);
	       mDuoMenuView = (DuoMenuView)mDuoDrawerLayout.getMenuView();
	       mToolbar = findViewById(R.id.toolbar);
	  }
     }

     public void dothis(View v)
     {
	  switch(v.getId())
	  {
	       case R.id.electrician:
		    Toast.makeText(getApplicationContext() , "Electrician" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.carpainter:
		    Toast.makeText(getApplicationContext() , "Carpainter" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.plumber:
		    Toast.makeText(getApplicationContext() , "Plumber" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.bricklayer:
		    Toast.makeText(getApplicationContext() , "BrickLayer" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.painter:
		    Toast.makeText(getApplicationContext() , "Painter" , Toast.LENGTH_SHORT).show();
		    break;
	       case R.id.labour:
		    Toast.makeText(getApplicationContext() , "Labour" , Toast.LENGTH_SHORT).show();
		    break;
	       default:
		    break;
	  }
     }
}
