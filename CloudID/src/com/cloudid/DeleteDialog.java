package com.cloudid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteDialog extends DialogFragment {

	public static DeleteDialog newInstance(String id) {
		DeleteDialog frag = new DeleteDialog();
        Bundle args1 = new Bundle();
        args1.putString("UserID", id);
        frag.setArguments(args1);
        return frag;
    }
	
	 public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
		  
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Are you sure you want to delete the id?")
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   String id1=getArguments().getString("UserID");
	                	   ((AdminActivity)getActivity()).dodeletePositiveClick(id1);	                   }
	               })
	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                	   
	                	   
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	
}
