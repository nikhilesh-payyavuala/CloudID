package com.cloudid;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.md5.MD5hash;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayQRDialogFragment extends DialogFragment{
	
	public static DisplayQRDialogFragment newInstance(String id,String last_user, String last_time) {
		DisplayQRDialogFragment frag = new DisplayQRDialogFragment();
        Bundle args1 = new Bundle();
        args1.putString("UserID", id);
        args1.putString("LastUser", last_user);
        args1.putString("LastTime", last_time);     
        frag.setArguments(args1);
        return frag;
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String id=getArguments().getString("UserID");
		String last_user=getArguments().getString("LastUser");
		String last_time=getArguments().getString("LastTime");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
   
        Date resultdate = new Date(Long.valueOf(last_time));
        sdf.format(resultdate);
        System.out.println(sdf.format(resultdate));
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v= inflater.inflate(R.layout.digital_qr, null);
        ImageView imageView=(ImageView)v.findViewById(R.id.qrCode);
        TextView userlast = (TextView)v.findViewById(R.id.lastuser);
        userlast.setText(last_user);
        TextView userlasttime = (TextView)v.findViewById(R.id.lastseen);
        userlasttime.setText(sdf.format(resultdate));
        String qrData = MD5hash.md5Java(id) ;
		int qrCodeDimention = 500;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
		        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);

		try {
		    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		    imageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
		    e.printStackTrace();
		}
		
        
        builder.setView(v).setPositiveButton("CONFIRM", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				((AdminActivity)getActivity()).doQRPositiveClick();
			}
        	
        });
        
        
        return builder.create();
	}

}
