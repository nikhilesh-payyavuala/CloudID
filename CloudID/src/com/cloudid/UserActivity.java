package com.cloudid;

import java.io.IOException;
import java.io.InputStream;














import com.cloudid.useridendpoint.Useridendpoint;
import com.cloudid.useridendpoint.model.UserID;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.md5.MD5hash;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends FragmentActivity implements AddIdDialogFragment.AddIdInterface {
	
	private ImageView imgProfilePic;
	private TextView txtName, txtEmail;
	String[] uid= new String[7]; 
	String userreading="";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user);
        
        setContentView(R.layout.background);
        LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
		View theInflatedView = inflater.inflate(R.layout.activity_user, (ScrollView)findViewById(R.id.login_scroll_view));
        
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
    	txtName = (TextView) findViewById(R.id.txtName);
    	txtEmail = (TextView) findViewById(R.id.txtEmail);
    	
    	Intent intent=getIntent();
    	String[] info=intent.getStringArrayExtra(MainActivity.INFO);
    	userreading=info[1];
    	txtName.setText(info[0]);
    	txtEmail.setText(info[1]);
        
        new LoadProfileImage(imgProfilePic).execute(info[2]);
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
			//DialogFragment dialog = new EditNameDialogListener();
    		DialogFragment dialog = new AddIdDialogFragment();
    		dialog.show(fm, "hello");
//			FireMissilesDialogFragment f = new FireMissilesDialogFragment();
//			
//			f.show(fm, "hello");
    	}
    	if(item.getItemId()==R.id.menu){
    		
    		Intent in = new Intent(UserActivity.this,MainActivity.class);
    		in.putExtra("signout", "true");
    		startActivity(in);
    	}
		return false;
    	
    }
	
	public void scanNow(View view) {
		Log.d("test", "button works!"); 

		IntentIntegrator integrator = new IntentIntegrator(UserActivity.this);
		   integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
		   integrator.initiateScan();
		}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent){ 
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		  if (scanResult != null) {
		    // handle scan result
			  String capturedQrValue =scanResult.getContents();
			   intent.getStringExtra("RESULT");
			  Toast.makeText(UserActivity.this,  
					    "Scan Result:" + capturedQrValue, Toast.LENGTH_SHORT)  
					    .show(); 
			  new InsertID().execute(capturedQrValue);
		  }
		}
	
	private class InsertID extends AsyncTask<String, Integer, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			Useridendpoint.Builder useridendpoint=new Useridendpoint.Builder(AndroidHttp.newCompatibleTransport(),
		              new JacksonFactory(),
		              new HttpRequestInitializer() {
		              public void initialize(HttpRequest httpRequest) { }
		              });
			
			Useridendpoint userendpoint =CloudEndpointUtils.updateBuilder(useridendpoint).build();
			try{
			 UserID id= userendpoint.getUserID(params[0]).execute();
			 
			
				/*id.setFName("Nikhilesh");
				id.setLName("Payyavuala");
				id.setDob("07/06/1992");
				id.setUfID("48914676");
				id.setUfHash(MD5hash.md5Java("48914676"));
				id.setTimeStampCreated(System.currentTimeMillis());
				id.setTimeStampLastAccessed(System.currentTimeMillis());*/
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
			
			id.setTimeStampLastAccessed(System.currentTimeMillis());
			id.setLastAccessedBy(userreading);
			userendpoint.updateUserID(id).execute();
			}catch(IOException e){
				// if id doesnt exsist
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
			 DialogFragment newFragment = ConfirmUserDetailsDialogFragment.newInstance(
					 userid);
			    newFragment.show(getSupportFragmentManager(), "dialog1");
			
			
		 }
		
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
	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		View vi=dialog.getView();
		Dialog dialogView = dialog.getDialog();
		//Dialog dialog2 =Dialog.class.cast(dialog);
 	  EditText edit = (EditText) dialogView.findViewById(R.id.username);
 	 //Toast.makeText(getApplicationContext(), "Hi, "+edit.getText().toString(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void getAddId(String uname) {
		// TODO Auto-generated method stub
		
	}
	
	


}
