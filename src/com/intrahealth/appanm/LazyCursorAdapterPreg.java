package com.intrahealth.appanm;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
//import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.intrahealth.msakhi.appanm.R;

public class LazyCursorAdapterPreg extends SimpleCursorAdapter {

	// private Context context;
	private int layout;
	private LayoutInflater inflater = null;
	int nameCol, sv_dtCol, diffCol, slCol, slHvInd, pid, mstInd, cstInd,
			rsltCol, hnameCol;
	int mdtCol, cdtCol, mst, cst;
	boolean mdth = false, cdth = false;
	int child = 0;

	public LazyCursorAdapterPreg(Context context, int layout, Cursor c,
			String[] from, int[] to, int aa) {
		super(context, layout, c, from, to);
		// this.context = context;
		this.layout = layout;
		this.child = aa;
		inflater = LayoutInflater.from(context);
		nameCol = c.getColumnIndex("name");
		hnameCol = c.getColumnIndex("hname");
		sv_dtCol = c.getColumnIndex("edd");
		slCol = c.getColumnIndex("_id");
		// pid = c.getColumnIndex("pid");
		// diffCol = c.getColumnIndex("diff");
		// slHvInd=c.getColumnIndex("hv_str");
		mstInd = c.getColumnIndex("m_stat");
		cstInd = c.getColumnIndex("c_stat");
		rsltCol = c.getColumnIndex("rslt");
		mdtCol = c.getColumnIndex("m_death");
		cdtCol = c.getColumnIndex("c_death");
		// TODO Auto-generated constructor stub
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		String tmp = "";
		View v = inflater.inflate(layout, parent, false);
		Cursor c = getCursor();
		// cpos=c.getInt(c.getColumnIndex("seq"));
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvRslt = (TextView) v.findViewById(R.id.rslt);
		tvRslt.setText(c.getString(rsltCol));

		// ImageView thumb_image=(ImageView)v.findViewById(R.id.list_image);
		mst = c.getInt(mstInd);
		cst = c.getInt(cstInd);
		mdth = !c.isNull(mdtCol);
		cdth = !c.isNull(cdtCol);

		if ((!mdth && !cdth) && (mst == 1 && cst == 1))
			tmp = "(गर्भपात)";
		else if (!cdth && cst == 1)
			tmp = "(मृत शिशु)";
		else if (mdth && cdth)
			tmp = "(मृत्यु दोनों)";
		else if (mdth)
			tmp = "(मृत्यु माँ)";
		else if (cdth)
			tmp = "(मृत्यु शिशु)";

		if (child == 11) {
			tvName.setText(tmp + c.getString(nameCol));
		} else {

			String aa = tmp + c.getString(nameCol) + " W/O "
					+ c.getString(hnameCol);
			tvName.setText(tmp + c.getString(nameCol) + " W/O "
					+ c.getString(hnameCol));
		}

		if (tmp.equals("(मृत्यु माँ)"))
			tvEdd.setText(DBAdapter.strtodate(c.getString(mdtCol)));
		else if (tmp.equals("(मृत्यु शिशु)") || tmp.equals("(मृत्यु दोनों)"))
			tvEdd.setText(DBAdapter.strtodate(c.getString(cdtCol)));
		else
			tvEdd.setText(DBAdapter.strtodate(c.getString(sv_dtCol)));
		// tvSlno.setText(c.getString(slCol));
		tvSlno.setText("" + (c.getPosition() + 1));

		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor cursor) {

		String tmp = "";
		Cursor c = getCursor();
		// cpos=c.getInt(c.getColumnIndex("seq"));
		TextView tvName = (TextView) v.findViewById(R.id.name_entry);
		TextView tvEdd = (TextView) v.findViewById(R.id.edd_entry);
		TextView tvSlno = (TextView) v.findViewById(R.id.slno);
		TextView tvRslt = (TextView) v.findViewById(R.id.rslt);
		tvRslt.setText(c.getString(rsltCol));

		mst = c.getInt(mstInd);
		cst = c.getInt(cstInd);
		mdth = !c.isNull(mdtCol);
		cdth = !c.isNull(cdtCol);
		if ((!mdth && !cdth) && (mst == 1 && cst == 1))
			tmp = "(गर्भपात)";
		else if (!cdth && cst == 1)
			tmp = "(मृत शिशु)";
		else if (mdth && cdth)
			tmp = "(मृत्यु दोनों)";
		else if (mdth)
			tmp = "(मृत्यु माँ)";
		else if (cdth)
			tmp = "(मृत्यु शिशु)";
		tvName.setText(tmp + c.getString(nameCol) + " W/O "
				+ c.getString(hnameCol));
		if (tmp.equals("(मृत्यु माँ)"))
			tvEdd.setText(DBAdapter.strtodate(c.getString(mdtCol)));
		else if (tmp.equals("(मृत्यु शिशु)") || tmp.equals("(मृत्यु दोनों)"))
			tvEdd.setText(DBAdapter.strtodate(c.getString(cdtCol)));
		else
			tvEdd.setText(DBAdapter.strtodate(c.getString(sv_dtCol)));
		if (child == 11) {
			if(cst==1){
				tvName.setTextColor(Color.RED);
			}
			
			tvName.setText(c.getString(nameCol));
		} else {

			tvName.setText(tmp + c.getString(nameCol) + " W/O "
					+ c.getString(hnameCol));
		}
		// tvEdd.setText(DBAdapter.strtodate(c.getString(sv_dtCol)));
		// tvSlno.setText(c.getString(slCol));
		tvSlno.setText("" + (c.getPosition() + 1));

	}
	/*
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) { View currView = super.getView(position, convertView, parent);
	 * Object currItem = getItem(position); if (currItem.isItemSelected) {
	 * currView.setBackgroundColor(Color.RED); } else {
	 * currView.setBackgroundColor(Color.BLACK); } return currView; }
	 */

}
