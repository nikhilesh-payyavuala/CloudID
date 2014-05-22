package com.cloudid;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfirmIdDialogFragment extends DialogFragment {
	
	//UserID id;
	private TextView fn,ln,ufid,dob;
	
	public static ConfirmIdDialogFragment newInstance(String[] id) {
		ConfirmIdDialogFragment frag = new ConfirmIdDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("UserID", id);
        frag.setArguments(args);
        return frag;
    }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String[] id= getArguments().getStringArray("UserID");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = null;
        if(id[4].equals("U")){
         v= inflater.inflate(R.layout.digital_confirm, null);
        }
        else if(id[4].equals("I")){
        	  v= inflater.inflate(R.layout.digital_confirm_insurance, null);
        	  TextView edate = (TextView)v.findViewById(R.id.expiry);
        	  edate.setText(id[5]);
        }
        else if(id[4].equals("G")){
        	 v= inflater.inflate(R.layout.digital_confirm_gift, null);
        	 TextView edate = (TextView)v.findViewById(R.id.expiry);
        	 TextView rewards = (TextView)v.findViewById(R.id.reward);
        	 edate.setText(id[5]);
        	 rewards.setText(id[6]);
        }
        fn=(TextView)v.findViewById(R.id.fname);
        ln=(TextView)v.findViewById(R.id.lname);
        dob=(TextView)v.findViewById(R.id.dob);
        ufid=(TextView)v.findViewById(R.id.ufid);
        //fn.setText("hello bro");
        fn.setText(id[0]);
        ln.setText(id[1]);
        dob.setText(id[2]);
        ufid.setText(id[3]);
        
       
        builder.setView(v)
        .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Activity a = getActivity();
				
					((AdminActivity)getActivity()).doPositiveClick();
				
			
			}
        	
        })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            	
            	((AdminActivity)getActivity()).doNegitiveClick();
            	
            }
        });
        
       // 
		return builder.create();
	}
	
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		LayoutInflater inflater1 = getActivity().getLayoutInflater();
		View view = inflater1.inflate(R.layout.digital_confirm, container, false);
		return view;
	}
*/
}
