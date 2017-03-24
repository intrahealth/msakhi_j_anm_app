package com.intrahealth.appanm;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
//import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;


public class LazyCursorAdapterCrep extends SimpleCursorAdapter  {

	//private Context context;
	private int layout;
	//public ImageLoader imageLoader;
	private LayoutInflater inflater=null;
	int nameCol,cntCol,cntCol1,slCol;

	
	public LazyCursorAdapterCrep(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		//this.context = context;
		this.layout = layout;
		inflater = LayoutInflater.from(context);
		//imageLoader=new ImageLoader(context);
        nameCol = c.getColumnIndex("head");
        cntCol = c.getColumnIndex("mval");
        cntCol1 = c.getColumnIndex("val");
        slCol = c.getColumnIndex("_id");
		// TODO Auto-generated constructor stub
	}

	@Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View v = inflater.inflate(layout, parent, false);
        Cursor c = getCursor();

        TextView tvName = (TextView)v.findViewById(R.id.txtHead); 
        TextView tvCnt = (TextView)v.findViewById(R.id.mval_entry);
        TextView tvCnt1 = (TextView)v.findViewById(R.id.val_entry);
        String tmp=c.getString(nameCol).trim();
        char ch=tmp.toCharArray()[0];
        tvName.setText(tmp);
        if (Character.isDigit(ch)) { 
        	tvName.setGravity(android.view.Gravity.LEFT);
        	tvName.setTypeface(null, Typeface.BOLD);
        	}
        else { 
        	tvName.setGravity(android.view.Gravity.RIGHT);
        	tvName.setTypeface(null, Typeface.NORMAL);
        	}
        tvCnt.setText(c.getString(cntCol));
        tvCnt1.setText(c.getString(cntCol1));
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

        TextView tvName = (TextView)v.findViewById(R.id.txtHead); 
        TextView tvCnt = (TextView)v.findViewById(R.id.mval_entry);
        TextView tvCnt1 = (TextView)v.findViewById(R.id.val_entry);
        String tmp=c.getString(nameCol).trim();
        char ch=tmp.toCharArray()[0];
        tvName.setText(tmp);
        if (Character.isDigit(ch)) tvName.setGravity(android.view.Gravity.LEFT);
        else tvName.setGravity(android.view.Gravity.RIGHT);
        tvCnt.setText(c.getString(cntCol));
        tvCnt1.setText(c.getString(cntCol1));
    }
	

	
}
