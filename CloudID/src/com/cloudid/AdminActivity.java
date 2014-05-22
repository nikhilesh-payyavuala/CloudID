package com.cloudid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

import com.cloudid.useridendpoint.Useridendpoint;
import com.cloudid.useridendpoint.model.UserID;
import com.cloudid.userinfoendpoint.Userinfoendpoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.md5.MD5hash;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends FragmentActivity implements AddIdDialogFragment.AddIdInterface, ConnectionCallbacks, OnConnectionFailedListener, ActionBar.TabListener {
	
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	 ViewPager mViewPager;
	    
	//public static final String SIGN_OUT = "";
	String[] uid= new String[7]; 
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.background);
        
        
        Intent intent=getIntent();
    	String[] info=intent.getStringArrayExtra(MainActivity.INFO);
    	
        
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(),info);
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        
        
       
    	
        
        //new LoadProfileImage(imgProfilePic).execute(info[2]);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId()==R.id.action_new){
    		FragmentManager fm = getSupportFragmentManager();
			
    		DialogFragment dialog = new AddIdDialogFragment();
    	
    		dialog.show(fm, "hello");
    	}
    	if(item.getItemId()==R.id.menu){
    	//MainActivity.mGoogleApiClient.disconnect();
    	Plus.AccountApi.clearDefaultAccount(MainActivity.mGoogleApiClient);
		Plus.AccountApi.revokeAccessAndDisconnect(MainActivity.mGoogleApiClient)
				.setResultCallback(new ResultCallback<Status>() {
					@Override
					public void onResult(Status arg0) {
						//Log.e(TAG, "User access revoked!");
						MainActivity.mGoogleApiClient.connect();
						//updateUI(false);
					}

				});
    		Intent in = new Intent(AdminActivity.this,MainActivity.class);
    		in.putExtra("signout", "true");
    		startActivity(in);
    	}
		return false;
    	
    }
	
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	private class LoadLastSeen extends AsyncTask<String , Integer, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			String[] s =new String[2];
			Useridendpoint.Builder useridendpoint=new Useridendpoint.Builder(AndroidHttp.newCompatibleTransport(),
		              new JacksonFactory(),
		              new HttpRequestInitializer() {
		              public void initialize(HttpRequest httpRequest) { }
		              });
			
			Useridendpoint userendpoint =CloudEndpointUtils.updateBuilder(useridendpoint).build();
			try{
				UserID id= userendpoint.getUserID(params[0]).execute();
				s[0]=id.getLastAccessedBy();
				s[1]=String.valueOf(id.getTimeStampLastAccessed());
			}
			catch(IOException e){
				
				
			}
			return s;
		}
		
	}
	
	private class InsertID extends AsyncTask<String, Integer, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			Useridendpoint.Builder useridendpoint=new Useridendpoint.Builder(AndroidHttp.newCompatibleTransport(),
		              new JacksonFactory(),
		              new HttpRequestInitializer() {
		              public void initialize(HttpRequest httpRequest) { }
		              });
			
			Useridendpoint userendpoint =CloudEndpointUtils.updateBuilder(useridendpoint).build();
			try{
			 UserID id= userendpoint.getUserID(params[0]).execute();
			 uid[0]= id.getFName();
				uid[1]=id.getLName();
				uid[2]=id.getDob();
				uid[3]=id.getId();
				uid[4]=id.getType();
				if(uid[4].equals("I")){
					uid[5]=id.getExpiryDate();
				}
				else if(uid[4].equals("G")){
					uid[5]=id.getExpiryDate();
					uid[6]=id.getRewardPoints();
				}
				
				/* REGISTERING A NEW USER
				/*UserID id=new UserID();
				id.setUfHash(params[0]);*/
				//userendpoint.removeUserID(MD5hash.md5Java("19997953")).execute();
			 
				/*UserID id =new UserID();
				id.setFName("Nikhilesh");
				id.setLName("Payyavuala");
				id.setDob("07/06/1992");
				id.setId("88888888");
				id.setRewardPoints("1240");
				id.setIdhash(MD5hash.md5Java("88888888"));
				id.setUserName("nikhileshp92@gmail.com");
				id.setType("G");
				
				id.setTimeStampCreated(System.currentTimeMillis());
				id.setTimeStampLastAccessed(System.currentTimeMillis());
				userendpoint.insertUserID(id).execute();*/
			
			/* DialogFragment newFragment = ConfirmIdDialogFragment.newInstance(
					 uid);
			    newFragment.show(getSupportFragmentManager(), "dialog");*/
				
			}catch(IOException e){
				// if ID doesnt exsist
				uid[0]="";
				uid[1]="";
				uid[2]="";
				uid[3]="";
				
			}
			catch(NullPointerException e){
				uid[0]="Not Present";
				 
			}
			return uid;
		}
		@Override
		 protected void onPostExecute(String[] userid) {
			if(userid[0].equals("Not Present")){
				 Toast.makeText(getApplicationContext(), " No Such ID Found  ", Toast.LENGTH_SHORT).show();
				 return;
				 
			}
			 DialogFragment newFragment = ConfirmIdDialogFragment.newInstance(
					 userid);
			    newFragment.show(getSupportFragmentManager(), "dialog");
		
			
		 }
		
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		
		View vi=dialog.getView();
		Dialog dialogView = dialog.getDialog();
		//Dialog dialog2 =Dialog.class.cast(dialog);
 	  EditText edit = (EditText) dialogView.findViewById(R.id.username);
 	  if(edit.getText().toString().equals("")){
 		 Toast.makeText(getApplicationContext(), " Enter Refernce Number  ", Toast.LENGTH_SHORT).show();
 		 return;
 	  }
 	  Spinner type = (Spinner)dialogView.findViewById(R.id.spinner1);
 	  String  t = type.getSelectedItem().toString();
 	  
 	 
 	 
 	 new InsertID().execute(MD5hash.md5Java(edit.getText().toString()));
 	
 	 
	}

	@Override
	public void getAddId(String uname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void doPositiveClick() { //second dialog
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		int l=db.insertID(uid[3], uid[0], uid[1], uid[2], MD5hash.md5Java(uid[3]));
		 if(l==0){
			 Toast.makeText(getApplicationContext(), "ID already exsists",
						Toast.LENGTH_SHORT).show();
			 return;
		 }
		 Fragment qr =new QRCodeFragment();
		 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, qr) .commit();
		
		Toast.makeText(getApplicationContext(), "And you have your ID!! ", Toast.LENGTH_SHORT).show();
	}

	public void doNegitiveClick() {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	
		 mViewPager.setCurrentItem(tab.getPosition());
		 if(tab.getPosition()==1){
			 //Toast.makeText(getApplicationContext(), "second tab "+ tab.getPosition(), Toast.LENGTH_SHORT).show();
			 //new QRCodeFragment().loadid(getApplicationContext());
			 Fragment qr =new QRCodeFragment();
			 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, qr) .commit();
		 }
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
         String[] info1= new String[4];
        public AppSectionsPagerAdapter(FragmentManager fm, String[] info) {
            super(fm);
            this.info1=info;
        }

       @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    Fragment userinfo= new UserDetailsFragment();
                    Bundle args1 = new Bundle();
                    args1.putStringArray("info", info1);
                    userinfo.setArguments(args1);
                    return userinfo;
                    
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    Fragment idinfo= new QRCodeFragment();
                    //Bundle args2 = new Bundle();
                    //args2.putStringArray("info", info1);
                    //userinfo.setArguments(args1);
                    return idinfo;
                    
                    

                default:
                	
                    Fragment fragment = new LastUsedSectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(LastUsedSectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	switch (position) {
        	case 0: 
        		return "User Details";
        	case 1:
        		return "ID Details";
        	case 2:
        		return "Last Updated";
        	default:
        		return "Section " + (position + 1);
        	}
            
        }

		
    }
	public static class UserDetailsFragment extends Fragment {
	
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
			 //LayoutInflater inflater = LayoutInflater.from(get);
			Bundle args = getArguments();
				View theInflatedView = inflater.inflate(R.layout.activity_admin, container, false);//(ScrollView)findViewById(R.id.login_scroll_view));
		      ImageView imgProfilePic = (ImageView)theInflatedView.findViewById(R.id.imgProfilePic);
		       TextView	txtName = (TextView) theInflatedView.findViewById(R.id.txtName);
		       TextView	txtEmail = (TextView) theInflatedView.findViewById(R.id.txtEmail);
		       txtName.setText(args.getStringArray("info")[0]);
		    	txtEmail.setText(args.getStringArray("info")[1]);
		    	new AdminActivity().new LoadProfileImage(imgProfilePic).execute(args.getStringArray("info")[2]);
			
			return theInflatedView;
		}
		
	}

	
	public static class QRCodeFragment extends Fragment  {

        public static final String ARG_SECTION_NUMBER = "section_number";
        private List<String> mDataSourceList = new ArrayList<String>();  
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        	super.onCreateView(inflater, container, savedInstanceState);
        	View v= inflater.inflate(R.layout.list_layout, container, false);
        
        	
        	return v;
        }
        @Override 
        public void onActivityCreated(Bundle savedInstanceState) {  
            super.onActivityCreated(savedInstanceState);  
            
            ListView listView = (ListView) getActivity().findViewById(R.id.listnew);  
            DatabaseHandler db = new DatabaseHandler(getActivity());
            List<ID>dbid=db.getAllID();
            //listView.setAdapter(new ArrayAdapter(getActivity(), R.layout.qr_list, mDataSourceList));
            listView.setAdapter(new QRAdapter(dbid,getActivity()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String last=""; 
					long last_time=0;
					String[] s = {"",""};
					ID item = (ID) parent.getItemAtPosition(position);
					try {
						 s= new AdminActivity().new LoadLastSeen().execute(MD5hash.md5Java(item.getUFID())).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					 DialogFragment newFragment1 = DisplayQRDialogFragment.newInstance(
	      					 item.getUFID(),s[0],s[1]);
	      			    newFragment1.show(getFragmentManager(), "qrdialog");
				}
            	
			});
               
        }
        @Override
        public void onResume(){
        	super.onResume();
        	//Toast.makeText(getActivity(), "in resue ", Toast.LENGTH_SHORT).show();
        	ListView listView = (ListView) getActivity().findViewById(R.id.listnew);  
            DatabaseHandler db = new DatabaseHandler(getActivity());
            List<ID>dbid=db.getAllID();
            //listView.setAdapter(new ArrayAdapter(getActivity(), R.layout.qr_list, mDataSourceList));
            listView.setAdapter(new QRAdapter(dbid,getActivity()));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String[] s = {"",""};
					ID item = (ID) parent.getItemAtPosition(position);
					try {
						 s= new AdminActivity().new LoadLastSeen().execute(MD5hash.md5Java(item.getUFID())).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					 DialogFragment newFragment1 = DisplayQRDialogFragment.newInstance(
	      					 item.getUFID(),s[0],s[1]);
	      			    newFragment1.show(getFragmentManager(), "qrdialog");
				}
            	
			});
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					ID item = (ID) parent.getItemAtPosition(position);
					//Toast.makeText(getActivity(), " Long click ", Toast.LENGTH_LONG).show();
					DialogFragment newFragment1 = DeleteDialog.newInstance(
	      					item.getUFID());
	      			    newFragment1.show(getFragmentManager(), "qrdialog");
					return true;
				}
            	
            });
        }
        
           }
	
	public static class LastUsedSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.qr_list, container, false);
            Bundle args = getArguments();
            
           // new AdminActivity().new LoadLastSeen().execute(args.getStringArray("info")[2]); 
            /*((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));*/
            return rootView;
        }
    }

	public void doQRPositiveClick() {
		// TODO Auto-generated method stub
		
	}

	public void dodeletePositiveClick(String dlabel) {
		// TODO Auto-generated method stub
		//Toast.makeText(getApplicationContext(), "yezss ", Toast.LENGTH_LONG).show();
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.deleteID(dlabel);
		Fragment qr =new QRCodeFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, qr) .commit();
	}

}
