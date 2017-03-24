package com.intrahealth.appanm;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
//import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;


public class LazyCursorAdapter extends SimpleCursorAdapter  {

	//private Context context;
    private int layout;
	//public ImageLoader imageLoader;
	private LayoutInflater inflater=null;
	int nameCol,cntCol,slCol;

	
	public LazyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		//this.context = context;
		this.layout = layout;
		inflater = LayoutInflater.from(context);
		//imageLoader=new ImageLoader(context);
        nameCol = c.getColumnIndex("name");
        cntCol = c.getColumnIndex("cnt");
        slCol = c.getColumnIndex("_id");
		// TODO Auto-generated constructor stub
	}

	@Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
		 Log.d("In newView ","for other list");
        View v = inflater.inflate(layout, parent, false);       
        Cursor c = getCursor();
        TextView tvName = (TextView)v.findViewById(R.id.name_entry);        
        TextView tvCnt = (TextView)v.findViewById(R.id.number_entry);       
        TextView tvSlno = (TextView)v.findViewById(R.id.slno);        
        //ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image);
        tvName.setText(c.getString(nameCol));
      
        
        
        //   		String dt_str[]=plmp.split("\\-");
        //		dtPicker.init(Integer.parseInt(dt_str[0]),Integer.parseInt(dt_str[1])-1, Integer.parseInt(dt_str[2]), new OnDateChangedListener() {
        tvSlno.setText(""+(c.getPosition()+1));
        //tvSlno.setText(c.getString(slCol));
        String tmp=c.getString(cntCol);
       
        
        
        String cnt_str="";
        String starr[]=tmp.split("\\:");
        String clr[]={"#8DB600","0xff00ff","#007FFF","0xff0000"};
        if (starr.length>0)
        	for(int i=0;i<starr.length;i++) {        	
        			cnt_str+=String.format(Locale.getDefault(),"<font color=%s> %3d</font>",clr[i],Integer.parseInt(starr[i]));
        	}
        else cnt_str=tmp;
        tvCnt.setText(Html.fromHtml(cnt_str));
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

    	TextView tvName = (TextView)v.findViewById(R.id.name_entry); 
        TextView tvCnt = (TextView)v.findViewById(R.id.number_entry); 
        TextView tvSlno = (TextView)v.findViewById(R.id.slno); 
        // ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image);
        tvName.setText(c.getString(nameCol));        
        //tvSlno.setText(c.getString(slCol));
        tvSlno.setText(""+(c.getPosition()+1));
        String tmp=c.getString(cntCol);
        String cnt_str="";
        String starr[]=tmp.split("\\:");
        String clr[]={"#8DB600","0xff00ff","#007FFF","0xff0000"};
        if (starr.length>0)
        	for(int i=0;i<starr.length;i++) {        	
        			cnt_str+=String.format(Locale.getDefault(),"<font color=%s> %3d</font>",clr[i],Integer.parseInt(starr[i]));
        	}
        else cnt_str=tmp;
        tvCnt.setText(Html.fromHtml(cnt_str));
    }
	

	
}
