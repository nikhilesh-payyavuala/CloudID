package com.cloudid;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;



public class AddIdDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	View V;
	EditText edit;
	public interface AddIdInterface{
		 public void onDialogPositiveClick(DialogFragment dialog);
		 public void getAddId(String uname);
	}
	
	AddIdInterface AddId;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            AddId = (AddIdInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        LayoutInflater inflater = getActivity().getLayoutInflater();
         View V1 = inflater.inflate(R.layout.digital_signin, null);
       // edit = (EditText) V.findViewById(R.id.username);
        Spinner spin;
        spin = (Spinner)V1.findViewById(R.id.spinner1);
        DialogInterface.OnClickListener l;
        
        List<String> list = new ArrayList<String>();
        list.add("UFID");
        list.add("Health Insurance");
        list.add("Macy's Shopping");
        
    
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(dataAdapter);
        
        builder.setView(V1)
               .setPositiveButton("FIRE", AddIdDialogFragment.this)
               .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
       
        // Create the AlertDialog object and return it
        return builder.create();
    }
	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		//String pls= edit.getText().toString();
		//AddId.getAddId(edit.getText().toString());
		AddId.onDialogPositiveClick(AddIdDialogFragment.this);
	}

}
