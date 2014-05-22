package com.cloudid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class QRAdapter extends ArrayAdapter<ID> {
	 
private List<ID> idList;
private Context context;
 
public QRAdapter(List<ID> idList, Context ctx) {
    super(ctx, R.layout.qr_list, idList);
    this.idList = idList;
    this.context = ctx;
}
 
public View getView(int position, View convertView, ViewGroup parent) {
     
    // First let's verify the convertView is not null
    if (convertView == null) {
        // This a new view we inflate the new layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.qr_list, parent, false);
    }
        // Now we can fill the layout with the right values
        TextView fname = (TextView) convertView.findViewById(R.id.fname);
        TextView lname = (TextView) convertView.findViewById(R.id.lname);
        TextView dob = (TextView) convertView.findViewById(R.id.dob);
        TextView ufid = (TextView) convertView.findViewById(R.id.ufid);
        ID p = idList.get(position);
 
        fname.setText(p.getFName());
        lname.setText(" "+p.getLName());
        dob.setText(p.getDOB());
        ufid.setText(p.getUFID());
        
       /* distView.setText("" + p.getDistance());*/
     
     
    return convertView;
}
}