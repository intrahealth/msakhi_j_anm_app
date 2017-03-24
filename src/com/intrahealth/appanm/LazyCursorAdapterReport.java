package com.intrahealth.appanm;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class LazyCursorAdapterReport extends SimpleCursorAdapter  {

	//private Context context;
	private int layout;
	//public ImageLoader imageLoader;
	private LayoutInflater inflater=null;
	int nameCol,cntCol,slCol, cntCol1;

	
	public LazyCursorAdapterReport(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		//this.context = context;
		this.layout = layout;
		inflater = LayoutInflater.from(context);
		//imageLoader=new ImageLoader(context);
        nameCol = c.getColumnIndex("name");
        cntCol = c.getColumnIndex("cnt1");
        cntCol1 = c.getColumnIndex("cnt2");
        slCol = c.getColumnIndex("_id");
		// TODO Auto-generated constructor stub
	}

	@Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {


        View v = inflater.inflate(layout, parent, false);
        Cursor c = getCursor();

        TextView tvName = (TextView)v.findViewById(R.id.name_entry);
        
        TextView tvCnt = (TextView)v.findViewById(R.id.number_entry);
        TextView tvCnt1 = (TextView)v.findViewById(R.id.number_entry1);
        
        TextView tvSlno = (TextView)v.findViewById(R.id.slno);
        
        //ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image);
        tvName.setText(c.getString(nameCol));
        tvSlno.setText(""+(c.getPosition()+1));
        tvCnt.setText(c.getString(cntCol));
        tvCnt1.setText(c.getString(cntCol1));
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

    	TextView tvName = (TextView)v.findViewById(R.id.name_entry); 
        TextView tvCnt = (TextView)v.findViewById(R.id.number_entry); 
        TextView tvSlno = (TextView)v.findViewById(R.id.slno); 
        TextView tvCnt1 = (TextView)v.findViewById(R.id.number_entry1);
       // ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image);
        tvName.setText(c.getString(nameCol));
        tvSlno.setText(""+(c.getPosition()+1));
        tvCnt.setText(c.getString(cntCol));
        tvCnt1.setText(c.getString(cntCol1));
    }
	

	
}

