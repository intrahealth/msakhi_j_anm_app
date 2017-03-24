package com.intrahealth.appanm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {
	static String link;
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_LMP = "lmp";
	// public static final String ASHA_ID = "asha_id";
	// private static final String TAG = "DBAdapter";
	private static String dbPath = "/data/data/com.intrahealth.msakhi.appanm/databases/";
	public static final String DATABASE_NAME = "appanm.db";
	private static final String DB_PREG_TABLE = "preg_reg";
	private static final String DB_PREG_TABLE_OTHER = "preg_reg_other";
	private static final String DB_ANC_VISIT = "anc_visit";

	public static String resturl = link + "resturl/index.php";

	// private static final int DATABASE_VERSION = 1;
	public static final int qc[] = { 26, 39, 52, 60, 78 };
	public static final String mstr[] = { "JAN", "FEB", "MAR", "APR", "MAY",
			"JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	public static final String hmstr[] = { "जनवरी", "फरवरी", "मार्च", "अप्रैल",
			"मई", "जून", "जुलाई", "अगस्त", "सितम्बर", "अक्टूबर", "नवम्बर",
			"दिसम्बर" };

	private static DBAdapter mInstance = null;
	private final Context context;
	// private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	String seq_str;

	public DBAdapter(Context ctx) {
		super(ctx, DATABASE_NAME, null, 1);
		this.context = ctx;
		link = AppVariables.API(ctx);
	}

	public static DBAdapter getInstance(Context ctx) {
		/**
		 * use the application context as suggested by CommonsWare. this will
		 * ensure that you dont accidentally leak an Activitys context (see this
		 * article for more information:
		 * http://developer.android.com/resources/articles
		 * /avoiding-memory-leaks.html)
		 */
		if (mInstance == null) {
			mInstance = new DBAdapter(ctx.getApplicationContext());
			try {
				mInstance.createDataBase();
				mInstance.openDataBase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mInstance;
	}

	private boolean checkDataBase() {
		File dbFile = new File(dbPath + DATABASE_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {
		try {
			InputStream input = context.getAssets().open(DATABASE_NAME);
			String outPutFileName = dbPath + DATABASE_NAME;
			OutputStream output = new FileOutputStream(outPutFileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			output.flush();
			output.close();
			input.close();
		} catch (IOException e) {
			Log.v("error", e.toString());
		}
	}

	/*
	 * private static class DatabaseHelper extends SQLiteOpenHelper {
	 * 
	 * DatabaseHelper(Context context) { super(context, DATABASE_NAME, null,
	 * DATABASE_VERSION); }
	 * 
	 * @Override public void onCreate(SQLiteDatabase db) { /* try { Log.d(TAG,
	 * "Creating database"); String[] queries = DATABASE_CREATE.split(";");
	 * for(String query : queries){ db.execSQL(query); }
	 * //db.execSQL(DATABASE_CREATE); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int
	 * newVersion) { Log.w(TAG, "Upgrading database from version " + oldVersion
	 * + " to " + newVersion + ", which will destroy all old data");
	 * 
	 * db.execSQL("DROP TABLE IF EXISTS [preg_reg]");
	 * db.execSQL("DROP TABLE IF EXISTS [sch_visits]");
	 * db.execSQL("Drop Trigger If Exists [trg_insert]");
	 * db.execSQL("Drop Trigger If Exists [trg_update]"); onCreate(db); } }
	 */
	// ---opens the database---
	/*
	 * public DBAdapter open() throws SQLException { db =
	 * DBHelper.getWritableDatabase(); return this; }
	 */

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	public void openDataBase() throws SQLException, IOException {
		String fullDbPath = dbPath + DATABASE_NAME;
		db = SQLiteDatabase.openDatabase(fullDbPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public SQLiteDatabase getDB() {
		try {
			openDataBase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return db;
	}

	// ---closes the database---
	@Override
	public synchronized void close() {
		// if( db!=null) db.close();
		// super.close();
	}

	public synchronized void myclose() {

		if (db != null)
			db.close();
		super.close();
		mInstance = null;
		// DBHelper.close();
	}

	public synchronized boolean check_preg(String asha_id, String pid) {
		String sql = String.format(
				"select _id from preg_reg where asha_id=%s and _id=%s",
				asha_id, pid);
		Cursor c = db.rawQuery(sql, null);
		return c.moveToFirst();
	}

	public synchronized void sms_enqeue(String asha_id, String pid,
			String msg_id, String msg, String sql1) {
		String sql = String
				.format("insert into pend_procs(asha_id,pid,msg_id,psql,osql,done) values(%s,%s,%s,\"%s\",\"%s\",0)",
						asha_id, pid, msg_id, msg, sql1);
		// Log.d("info",sql);
		db.execSQL(sql);
	}

	public synchronized void process_sms(String asha_id, String pid) {
		int next_seq = get_last_seq(Integer.parseInt(asha_id)) + 1;
		int curr_seq = 0;
		String sql = String
				.format("select msg_id,psql,osql from pend_procs where done=0 and asha_id=%s order by msg_id",
						asha_id);
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			curr_seq = c.getInt(0);
			if (curr_seq == next_seq) {
				while (!c.isAfterLast()) {
					// curr_seq=c.getInt(c.getColumnIndex("msg_id"));
					String psql = c.getString(1);
					db.execSQL(psql);
					psql = c.getString(2).trim();
					if (!psql.equals(""))
						db.execSQL(psql);// Process osql if it is there
					curr_seq++;
					if (c.moveToNext())
						if (curr_seq != c.getInt(0))
							break;
				}

				if (curr_seq != next_seq) {
					curr_seq--;
					sql = String
							.format(Locale.US,
									"update pend_procs set done=1 where asha_id=%s and msg_id<=%d",
									asha_id, curr_seq);
					db.execSQL(sql);
					sql = String.format(Locale.US,
							"update asha_dets set msg_id=%d where _id=%s",
							curr_seq, asha_id);
					db.execSQL(sql);
				}
			}
		}
		c.close();
		// db.execSQL(String.format("delete from pend_procs where asha_id=%s and pid=%s",asha_id,pid));
	}

	// --New registration Pregnancy--

	public synchronized long insertSms(String msg) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("msg", msg);
		return db.insert("pend_sms", null, initialValues);
	}

	public synchronized boolean updateSms(long rowId, int retry) {
		ContentValues args = new ContentValues();
		args.put("retry", retry);
		return db.update("pend_sms", args, "_id=" + rowId, null) > 0;
	}

	public synchronized boolean updateStat(String msgstat) {
		ContentValues args = new ContentValues();
		args.put("currst", msgstat);
		return db.update("stat", args, null, null) > 0;
	}

	public synchronized String getStat() {
		String tmp = "";
		int n1 = 0, n2 = 0;
		String sql = String.format("select count(*) from preg_reg");
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst())
			n1 = c.getInt(0);
		sql = String.format("select count(*) from sch_visits");
		c = db.rawQuery(sql, null);
		if (c.moveToFirst())
			n2 = c.getInt(0);
		c.close();
		tmp = String.format(Locale.ENGLISH, "%d:%d", n1, n2);
		return tmp;
	}

	public synchronized void execSQL(String sql) {
		db.execSQL(sql);
	}

	public synchronized boolean deleteSms(long rowId) {
		return db.delete("pend_sms", "_id=" + rowId, null) > 0;
	}

	// ---deletes a particular contact---
	public synchronized boolean deletePreg(long rowId) {
		return db.delete(DB_PREG_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves all the contacts---
	public synchronized Cursor getAllPreg() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAllCrep() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select * from crep ";
		return db.rawQuery(sql, null);
	}

	public synchronized void populate(String asha_id) {
		/*
		 * 1 1. कुल प्रसव 2 संस्थागत 3 गृह प्रसव 4 2. नवजात शिशु 5 जन्म पर वजन 6
		 * कम वजन 7 सामान्य वजन 8 बी.सी.जी 9 पोलियो (जीरो डोस) 10 मृत (0-28 दिन)
		 * 11 मृत (29-42 दिन) 12 3. गृह भ्रमण 13 कुल नवजात शिशु 14 शिशु जिनका
		 * गृ.भ्र. हुआ 15 प्रथम ग्र.भ्र 16 पूर्ण 6 ग्र.भ्र. 17 पूर्ण 7 ग्र.भ्र.
		 * 18 4. कुल शिशु संधार्भित 19 5. कुल शिशु उपचारित 20 6. सी.सी.एस.पी
		 * 
		 * 1 1. कुल प्रसव 1 2 संस्थागत 2 3 गृह प्रसव 3 4 2. कुल नवजात शिशु 4 5
		 * जन्म पर वजन 5 6 कम वजन 6 7 सामान्य वजन 7 8 बी.सी.जी 8 9 पोलियो (जीरो
		 * डोस) 9 10 3. कुल गृहभ्रमण लक्ष्य 11 4. कुल गृहभ्रमण प्राप्ति 12 5.
		 * कुल शिशु जिनका गृ.भ्र. होना है 1313 जिनका गृ.भ्र. हुआ 1414
		 * सी.सी.एस.पी. भरा 2015 जो उपचारित हुए 1916 जो संधार्भित हुए 1817 जिनका
		 * पहला गृ.भ्र. हुआ 1518 जिनके 6 गृ.भ्र. हुए 1619 जिनके 7 गृ.भ्र. हुए 17
		 * 20 6. कुल मृत्यु 21 गर्भपात 22 मृत शिशु जन्म23 मृत शिशु (0-28 दिन) 10
		 * 24 मृत शिशु (29-42 दिन) 11
		 * 
		 * 
		 * 9 22, 10 23, 12 11, 13 12, 14 16, 15 17, 16 18, 17 15, 18 14, 19 13
		 * 
		 * select p.*,ip.im_id pol,ib.im_id bcg from preg_reg p left join
		 * imm_det ip on p.asha_id=ip.asha_id and p._id=ip.pid and ip.im_id=2
		 * left join imm_det ib on p.asha_id=ib.asha_id and p._id=ib.pid and
		 * ib.im_id=1
		 */
		int[] mv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0 };
		int[] cv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0 };
		int[] hf = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0 };
		Cursor c;
		// db.execSQL("delete from crep");
		String sql;
		sql = "select count(distinct pid) cnt from sch_visits where asha_id="
				+ asha_id + " and avd is not null and gsumm not like '__---%'";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[13] = c.getInt(0);
		sql += " and strftime('%Y-%m',avd)=strftime('%Y-%m') ";
		c.moveToFirst();
		mv[13] = c.getInt(0);

		sql = "select count(distinct pid) from sch_visits where asha_id="
				+ asha_id + " and avd is not null and gsumm like '__%R%-' ";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[14] = c.getInt(0);
		sql += " and strftime('%Y-%m',avd)=strftime('%Y-%m') ";
		c.moveToFirst();
		mv[14] = c.getInt(0);

		sql = "select count(svd) scnt,count(avd) acnt from sch_visits where asha_id="
				+ asha_id + " and julianday(svd)<=date('NOW')";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[9] = c.getInt(0);
		cv[10] = c.getInt(1);

		sql = "select count(svd) scnt,count(avd) acnt from sch_visits where asha_id="
				+ asha_id
				+ " and julianday(svd)<=date('NOW') and strftime('%Y-%m',svd)=strftime('%Y-%m') ";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		mv[9] = c.getInt(0);
		mv[10] = c.getInt(1);

		sql = "select count(*) cnt from preg_reg where asha_id=" + asha_id
				+ " and c_stat=1";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[19] = c.getInt(0);

		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and c_stat=1 "
				+ "and ((c_death is null and strftime('%Y-%m',dob)=strftime('%Y-%m')) "
				+ "OR (c_death is not null and strftime('%Y-%m',c_death)=strftime('%Y-%m')))";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		mv[19] = c.getInt(0);
		// abortion
		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and c_stat=1 and m_stat=1 and m_death is null and c_death is null";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[20] = c.getInt(0);

		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and c_stat=1 and m_stat=1 and m_death is null and c_death is null "
				+ "and strftime('%Y-%m',dob)=strftime('%Y-%m') ";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		mv[20] = c.getInt(0);
		// still birth
		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and c_stat=1 and m_stat=0 and m_death is null and c_death is null";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[21] = c.getInt(0);

		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and c_stat=1 and m_stat=0 and m_death is null and c_death is null "
				+ "and strftime('%Y-%m',dob)=strftime('%Y-%m') ";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		mv[21] = c.getInt(0);

		sql = "select m_stat,c_stat,dob,pob,weight,hv_str,last_visit,ifnull(julianday(c_death)-julianday(dob),-1) diff, "
				+ "ifnull(ip.im_id,-1) pol,ifnull(ib.im_id,-1) bcg "
				+ "from preg_reg p "
				+ "left join imm_det ip on p.asha_id=ip.asha_id and p._id=ip.pid and ip.im_id=2 "
				+ "left join imm_det ib on p.asha_id=ib.asha_id and p._id=ib.pid and ib.im_id=1 "
				+ "where p.asha_id=" + asha_id + " and dob is not null";
		Log.d("info", sql);
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM",
				Locale.getDefault());
		Calendar cl = Calendar.getInstance(Locale.getDefault());
		String cmy = df.format(cl.getTime());
		boolean cmf = false;
		while (!c.isAfterLast()) {
			cmf = c.getString(c.getColumnIndex("dob")).startsWith(cmy);
			cv[0]++;
			if (cmf)
				mv[0]++;
			if (c.getInt(c.getColumnIndex("pob")) == 1) {
				cv[1]++;
				if (cmf)
					mv[1]++;
			} else {
				cv[2]++;
				if (cmf)
					mv[2]++;
			}
			float weight = c.getFloat(c.getColumnIndex("weight"));
			if (weight > 0) {
				cv[4]++;
				if (cmf)
					mv[4]++;
				if (weight >= 2.5) {
					cv[6]++;
					if (cmf)
						mv[6]++;
				} else {
					cv[5]++;
					if (cmf)
						mv[5]++;
				}
			}
			int bcg = c.getInt(c.getColumnIndex("bcg"));
			int pol = c.getInt(c.getColumnIndex("pol"));
			int diff = c.getInt(c.getColumnIndex("diff"));
			if (bcg > 0) {
				cv[7]++;
				if (cmf)
					mv[7]++;
			}
			if (pol > 0) {
				cv[8]++;
				if (cmf)
					mv[8]++;
			}
			if (diff >= 0) {
				if (diff < 29) {
					cv[22]++;
					if (cmf)
						mv[22]++;
				} else {
					cv[23]++;
					if (cmf)
						mv[23]++;
				}
			}
			String hv_str = c.getString(c.getColumnIndex("hv_str"));
			char charr[] = hv_str.toCharArray();
			int hv_cnt = 0;
			for (int i = 0; i < charr.length; i++)
				if (charr[i] == '1')
					hv_cnt++;
			int lv = c.getInt(c.getColumnIndex("last_visit"));
			if (lv > 0) {
				cv[12]++;
				if (cmf)
					mv[12]++;
				if (hv_str.startsWith("1")) {
					cv[16]++;
					if (cmf)
						mv[16]++;
				}
				if (hv_cnt > 5) {
					cv[17]++;
					if (cmf)
						mv[17]++;
				}
				if (hv_cnt == 7) {
					cv[18]++;
					if (cmf)
						mv[18]++;
				}
			}
			if ((c.getInt(c.getColumnIndex("c_stat")) == 0)
					&& (c.getInt(c.getColumnIndex("m_stat")) == 0)) {
				cv[3]++;
				if (cmf)
					mv[3]++;
			}
			c.moveToNext();
		}

		// cv[11]=cv[0];mv[11]=mv[0];
		sql = "select count(*) cnt from preg_reg where asha_id="
				+ asha_id
				+ " and dob is not null "
				+ "and (julianday(date('NOW'))-julianday(dob))<56 and last_visit<7  and (c_stat=0 and m_stat=0)";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		cv[11] = c.getInt(0);
		sql = "select count(distinct pid) cnt from sch_visits "
				+ "inner join preg_reg on sch_visits.asha_id=preg_reg.asha_id and sch_visits.pid=preg_reg._id "
				+ "where sch_visits.asha_id="
				+ asha_id
				+ " and strftime('%Y-%m',svd)=strftime('%Y-%m') and (c_stat=0 and m_stat=0)";
		c = db.rawQuery(sql, null);
		c.moveToFirst();
		mv[11] = c.getInt(0);
		for (int i = 0; i < 24; i++) {
			if ((mv[i] >= 0) || (cv[i] >= 0)) {
				sql = String.format(Locale.ENGLISH,
						"update crep set mval=%d,val=%d,hflag=%d where _id=%d",
						mv[i], cv[i], hf[i], i + 1);
				Log.d("info", sql);
				db.execSQL(sql);
			}
		}

	}

	public synchronized Cursor getAshaListP() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select asha_id _id,asha_dets.name,count(preg_reg._id) cnt "
				+ "from preg_reg INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id "
				+ "where  preg_reg._id not in (select _id from preg_reg_other where asha_id=preg_reg.asha_id) "
				+ "and dob is null	group by asha_id,asha_dets.name";
		/*
		 * String sql =
		 * "select ad._id,ad.name,count(pr._id) cnt from preg_reg pr " +
		 * "inner join asha_dets ad on ad._id=pr.asha_id " +
		 * "where dob is null group by ad._id,ad.name";
		 */
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAshaListPreg(String from, String to) {
		// Jitendra
		String sql = "	select  asha_id _id, asha_dets.name, (select count(*) from preg_reg where dob is NULL and preg_reg.asha_id=asha_dets._id and preg_reg.dor between '"
				+ from
				+ "' and '"
				+ to
				+ "' )as cnt from preg_reg INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id and preg_reg.dor between '"
				+ from + "' and '" + to + "' 	group by asha_id,asha_dets.name";

		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAshaListnewchild(String from, String to) {
		// jitendra
		String sql = "";
		if (from.equalsIgnoreCase("") && from.equalsIgnoreCase("")) {
			sql = "select pr.asha_id _id,asha_dets.name,count(dob) cnt from preg_reg pr	INNER JOIN asha_dets on pr.asha_id=asha_dets._id left JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id where    dob is not null  group by pr.asha_id";

		} else {
			sql = "select pr.asha_id _id,asha_dets.name,count(dob) cnt from preg_reg pr	INNER JOIN asha_dets on pr.asha_id=asha_dets._id left JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id where    dob is not null  "
					+ " and pr.dor between '"
					+ from
					+ "' and '"
					+ to
					+ "' group by pr.asha_id,asha_dets.name";
		}
		return db.rawQuery(sql, null);

	}

	public synchronized Cursor getAshaListDue() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select asha_id _id,asha_dets.name, count(preg_reg._id) cnt from preg_reg INNER JOIN asha_dets on "
				+ "preg_reg.asha_id=asha_dets._id group by asha_id,asha_dets.name";
		/*
		 * String sql =
		 * "select ad._id,ad.name,count(pr._id) cnt from preg_reg pr " +
		 * "inner join asha_dets ad on ad._id=pr.asha_id " +
		 * "where dob is null group by ad._id,ad.name";
		 */
		return db.rawQuery(sql, null);
	}

	/*
	 * public synchronized Cursor getAshaListVP() { //return
	 * db.rawQuery("select * from preg_reg where dob is null order by edd",
	 * null);
	 * 
	 * String sql=
	 * "select asha_id _id,asha_dets.name,count(preg_reg._id) cnt from preg_reg "
	 * +
	 * "INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id and anm_stat is not null and dob is null "
	 * + "group by asha_id,asha_dets.name"; return db.rawQuery(sql, null); }
	 */
	public synchronized Cursor getAshaListVP() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select asha_id _id, asha_dets.name,count(preg_reg._id) cnt from preg_reg "
				+ "INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id where "
				+

				" preg_reg._id in (select _id from preg_reg_other where asha_id=preg_reg.asha_id) and dob is null group by asha_id,asha_dets.name";

		Log.d("valuessssssss", sql);

		return db.rawQuery(sql, null);
	}

	public synchronized String getAshaListPcnt() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select count(pr._id) cnt from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id = po._id "
				+ "where  pr._id not in (select _id from preg_reg_other where asha_id=pr.asha_id)"
				+ "	and dob is null order by edd desc";
		// +"INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id ";
		// +"group by asha_id,asha_dets.name";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAshaListVPcnt() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		// String
		// sql="select count(preg_reg._id) cnt from preg_reg where anm_stat is not null";
		// +"INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id ";
		// +"group by asha_id,asha_dets.name";
		String sql = "select count(pr._id) cnt from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id = po._id "
				+ "where  pr._id in (select _id from preg_reg_other where asha_id=pr.asha_id)	and dob is null order by edd desc";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getAshaListB() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		/*
		 * String
		 * sql="select asha_id _id,asha_dets.name,count(dob) cnt from preg_reg "
		 * + "INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id where  " +
		 * "preg_reg._id not in (select _id from preg_reg_other where asha_id=preg_reg.asha_id) and dob is not null  group by asha_id,asha_dets.name"
		 * ; return db.rawQuery(sql, null); }
		 */
		String sql = "select pr.asha_id _id,asha_dets.name,count(dob) cnt from preg_reg pr	"
				+ "INNER JOIN asha_dets on pr.asha_id=asha_dets._id  "
				+ "LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id where ((m_stat<>1) and (c_stat<>1)) and dob is not null  "
				+ "and LENGTH(po.child_mcts)=0 group by pr.asha_id,asha_dets.name";
		return db.rawQuery(sql, null);

	}

	public synchronized String getAshaListBcnt() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select count(pr._id) cnt from preg_reg pr LEFT JOIN preg_reg_other po on "
				+ "pr.asha_id =po.asha_id  and pr._id=po._id where ((m_stat<>1) and (c_stat<>1)) "
				+ "and dob is not null  and LENGTH(po.child_mcts)=0 order by dob desc";
		// +"INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id ";
		// +"group by asha_id,asha_dets.name";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	/*
	 * public synchronized Cursor getAshaListVB() { //return
	 * db.rawQuery("select * from preg_reg where dob is null order by edd",
	 * null);
	 * 
	 * String
	 * sql="select asha_id _id,asha_dets.name,count(dob) cnt from preg_reg " +
	 * "INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id and canm_stat is not null "
	 * + "group by asha_id,asha_dets.name"; return db.rawQuery(sql, null); }
	 */

	public synchronized Cursor getAshaListVB() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select pr.asha_id _id,asha_dets.name,count(dob) cnt from preg_reg pr	"
				+ "INNER JOIN asha_dets on pr.asha_id=asha_dets._id  "
				+ "LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id where ((m_stat<>1) and (c_stat<>1)) and dob is not null  "
				+ "and LENGTH(po.child_mcts)>0 group by pr.asha_id,asha_dets.name";
		return db.rawQuery(sql, null);
	}

	public synchronized String getOnlyServerId(String s) {
		Log.i("id", s);
		String sql = "select server_id from preg_reg where _id=" + "'" + s
				+ "'";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();

		return (c.getString(c.getColumnIndex("server_id")));

	}

	public synchronized String getAshaListVBcnt() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select count(pr._id) cnt from preg_reg pr LEFT JOIN preg_reg_other po on "
				+ "pr.asha_id =po.asha_id  and pr._id=po._id where ((m_stat<>1) and (c_stat<>1)) "
				+ "and dob is not null  and LENGTH(po.child_mcts)>0 order by dob desc";
		// +"INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id ";
		// +"group by asha_id,asha_dets.name";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getAshaListD() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select asha_id _id,asha_dets.name name,count(c_death)||':'||count(m_death) cnt from preg_reg "
				+ "INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id "
				+ "where m_death<>0 or c_death<> 0 "
				+ "group by asha_id,asha_dets.name";
		return db.rawQuery(sql, null);
	}

	public synchronized String getAshaListDcnt() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select count(_id) cnt from preg_reg where c_death<>0";
		// +"INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id ";
		// +"group by asha_id,asha_dets.name";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getOpenHVList() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select sv.asha_id _id,ad.name name,count(distinct sv.pid)||':'||count(sv.svd)||':'||count(sv.avd) cnt "
				+ "from sch_visits sv "
				+ "inner join v_ovisit v on sv.asha_id=v.asha_id and sv.pid=v.pid "
				+ "inner join asha_dets ad on sv.asha_id=ad._id "
				+ "where julianday(svd)<=julianday(date('NOW')) "
				+ "group by sv.asha_id,ad.name";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getCloseHVList() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);

		String sql = "select sv.asha_id _id,ad.name name,count(distinct sv.pid)||':'||count(sv.svd)||':'||count(sv.avd) cnt "
				+ "from sch_visits sv "
				+ "inner join v_cvisit v on sv.asha_id=v.asha_id and sv.pid=v.pid "
				+ "inner join asha_dets ad on sv.asha_id=ad._id "
				+ "where julianday(svd)<=julianday(date('NOW')) "
				+ "group by sv.asha_id,ad.name";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getHVCompList() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select p.*,avd,(julianday(date('NOW'))-julianday(svd)) diff from preg_reg p "
				+ "left join sch_visits s on p._id=s.pid and s.seq=7 "
				+ "where (julianday(date('NOW'))-julianday(svd))>15 or last_visit=7 ";
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getDthList() {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select * from preg_reg " + "where m_stat=1 or c_stat=1";
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAllPregList(String asha_id) {
		// return
		// db.rawQuery("select _id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile from preg_reg where asha_id="+asha_id+" and anm_stat is null and dob is null order by edd desc",
		// null);
		// Hero Comment

		// return db
		// .rawQuery(
		// "select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id = po._id "
		// +
		// "where  pr._id not in (select _id from preg_reg_other where asha_id="
		// + asha_id
		// + ") "
		// + "and  pr.asha_id="
		// + asha_id
		// + " and dob is null order by edd desc", null);
		String sql2 = "select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
				+ "from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id = po._id "
				+ "where  pr._id not in (select _id from preg_reg_other where asha_id="
				+ asha_id
				+ ") "
				+ "and  pr.asha_id="
				+ asha_id
				+ " and dob is null order by edd desc";
		// Hero Changes query
		String sql = "select asha_id,_id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile from (select  asha_id,_id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile, (select (1 + count(*)) from preg_reg b where  b.name = a.name and b.edd=a.edd and  a.caste=b.caste and a.asha_id=b.asha_id and b.rowid < a.rowid  ) RANK from preg_reg as a ) where RANK=1 and   _id not in (select _id from preg_reg_other where asha_id='"
				+ asha_id + "') and  asha_id='" + asha_id + "' and dob is null";
		return db.rawQuery(sql, null);
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);
	}

	public synchronized Cursor getVhsndPregList(String asha_id) {
		// return
		// db.rawQuery("select _id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death from preg_reg where asha_id="+asha_id+" and anm_stat is not null and dob is null order by edd desc",
		// null);
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);
		return db
				.rawQuery(
						"select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile"
								+ " from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id "
								+ "where  pr._id in (select _id from preg_reg_other where asha_id="
								+ asha_id
								+ ") and  pr.asha_id="
								+ asha_id
								+ ""
								+ " and dob is null  and po.anc_visit <= 4 order by edd desc",
						null);
	}

	public synchronized Cursor getAllBirthList(String asha_id) {
		/*
		 * return db.rawQuery("select _id,name,case(pob) " +
		 * "when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt,"+
		 * "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile from preg_reg where asha_id="
		 * +asha_id+
		 * " and ((m_stat<>1) and (c_stat<>1)) and dob is not null and canm_stat is null order by dob desc"
		 * , null);
		 */
		// Add Hero
		String sql = "select pr._id,name,case(pob) "
				+ "when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
				+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
				+ "from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id "
				+ "where pr.asha_id="
				+ asha_id
				+ " and ((m_stat<>1) and (c_stat<>1)) "
				+ "and dob is not null  and LENGTH(po.child_mcts)=0 order by dob desc";
		return db.rawQuery(sql, null);
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);
	}

	public synchronized Cursor getVhsndBirthList(String asha_id) {
		/*
		 * return db.rawQuery("select _id,name,case(pob) " +
		 * "when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt,"+
		 * "dob edd,m_stat,c_stat,dob,weight,sex,m_death,c_death from preg_reg where asha_id="
		 * +asha_id+
		 * " and ((m_stat<>1) and (c_stat<>1)) and dob is not null and canm_stat is not null order by dob desc"
		 * , null);
		 */
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);
		String sql = "select pr._id,name,case(pob) "
				+ "when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
				+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
				+ "from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id "
				+ "where pr.asha_id="
				+ asha_id
				+ " and ((m_stat<>1) and (c_stat<>1)) "
				+ "and dob is not null  and LENGTH(po.child_mcts)>0 order by dob desc";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAllDeathList(String asha_id) {
		// return db
		// .rawQuery(
		// "select _id,name,case(pob) "
		// + " when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt,"
		// +
		// "c_death edd,m_stat,c_stat,dob,m_death,c_death from preg_reg where asha_id="
		// + asha_id
		// +
		// " and dob is not null and (m_stat=1 or c_stat=1) order by dob desc",
		// null);//add hname Hero add
		return db
				.rawQuery(
						"select _id,name,hname,case(pob) "
								+ " when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt,"
								+ "c_death edd,m_stat,c_stat,dob,m_death,c_death from preg_reg where asha_id="
								+ asha_id
								+ " and dob is not null and (m_stat=1 or c_stat=1) order by dob desc",
						null);
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);
	}

	public synchronized void custom_qry(String sql) {
		db.execSQL(sql);
	}

	public synchronized Cursor getImmunList() {
		return db.rawQuery("SELECT _id, descr FROM immun", null);
	}

	public synchronized String getImmun(long rowid) {
		String res = "";
		Cursor c = db.rawQuery("select im_id,immun.descr,imdt from imm_det "
				+ "inner join immun on imm_det.im_id=immun._id " + "where pid="
				+ String.valueOf(rowid) + " order by imdt", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			res = res + "|" + c.getString(0) + ":" + c.getString(1) + ":"
					+ strtodate(c.getString(2));
			c.moveToNext();
		}
		return res;
	}

	public synchronized Cursor getPregVisits(String mydt, String asha_id,
			String type) {
		/*
		 * String sql=
		 * "select v._id,v.seq,v.pid,p.name,min(svd) sv_dt,(julianday(svd)-julianday(date("
		 * +mydt+"))) diff,p.hv_str,p.m_stat,p.c_stat from sch_visits v "
		 * +"inner join preg_reg p on p._id=v.pid " +"inner join ( "
		 * +"select pid,min(abs(julianday(svd)-julianday(date("
		 * +mydt+")))) mday "
		 * +" from sch_visits v inner join preg_reg p on p._id=v.pid "
		 * +"where seq>p.last_visit and avd is null "
		 * +"group by pid) m on v.pid=m.pid " +
		 * "where (m_stat<>1 or c_stat<>1) and avd is null and abs(julianday(svd)-julianday(date("
		 * +mydt+")))=mday " +"group by v.pid " +"order by sv_dt";
		 */
		/*
		 * String sql=
		 * "select sc._id,seq,sc.pid,p.name,svd sv_dt,(julianday(svd)-julianday(date("
		 * +mydt+"))) diff,p.hv_str,p.m_stat,p.c_stat "
		 * +"from sch_visits sc inner join preg_reg p on p._id=sc.pid "
		 * +"inner join (select pid,0 ind,max(svd) svdt from sch_visits v "
		 * +"inner join preg_reg p on p._id=v.pid "
		 * +"where avd is null	and seq>p.last_visit and v.asha_id="+asha_id
		 * +" and svd between date("+mydt+",'-15 days') and date("+mydt+") "
		 * +"group by pid " +"UNION "
		 * +"select pid,1 ind,min(svd) svdt from sch_visits where pid not in "
		 * +"(select distinct pid from sch_visits v "
		 * +"inner join preg_reg p on p._id=v.pid "
		 * +"where avd is null	and seq>p.last_visit  and v.asha_id="+asha_id
		 * +" and svd between date("+mydt+",'-15 days') and date("+mydt+")) "
		 * +"and svd>date("+mydt+") "
		 * +"group by pid) tmp on sc.pid=tmp.pid and sc.svd=tmp.svdt "
		 * +"where (m_stat<>1 or c_stat<>1) and avd is null "
		 * +" order by sv_dt";
		 */
		String dstr = "(julianday(svd)-julianday(date(" + mydt + ")))";
		String sql = "";
		if (type.equals("v_cvisit")) {
			dstr = "2";
			// "select p._id _id,p.last_visit seq,p._id pid,
			sql = "select p._id _id,s.seq seq,p._id pid,name,s.svd sv_dt,"
					+ dstr
					+ " diff, hv_str,m_stat,c_stat,server_id "
					+ "from preg_reg p inner join "
					+ type
					+ " v on p._id=v.pid  and p.asha_id=v.asha_id "
					+ "inner join (select asha_id,pid,max(seq) seq,max(svd) svd from sch_visits "
					+ "where julianday(svd)<=julianday(date("
					+ mydt
					+ ")) "
					+ "group by asha_id,pid) s on p.asha_id=s.asha_id and p._id=s.pid "
					// +"left join sch_visits s on p.asha_id=s.asha_id and p._id=s.pid and p.last_visit+1=s.seq "
					+ "where dob is not null and p.asha_id=" + asha_id;
		} else
			sql = "select sc._id,seq,sc.pid,p.name,svd sv_dt,(julianday(svd)-julianday(date("
					+ mydt
					+ "))) diff,p.hv_str,p.m_stat,p.c_stat "
					+ "from sch_visits sc inner join preg_reg p on p._id=sc.pid and sc.asha_id=p.asha_id "
					+ "inner join (select pid,0 ind,max(svd) svdt from sch_visits v "
					+ "inner join preg_reg p on p._id=v.pid and v.asha_id=p.asha_id "
					+ "where avd is null	and seq>p.last_visit "
					+ "and svd between date("
					+ mydt
					+ ",'-15 days') and date("
					+ mydt
					+ ") "
					+ "group by pid UNION "
					+ "select pid,1 ind,min(svd) svdt from sch_visits where asha_id="
					+ asha_id
					+ " and pid not in "
					+ "(select distinct pid from sch_visits v "
					+ "inner join preg_reg p on p._id=v.pid and v.asha_id=p.asha_id "
					+ "where v.asha_id="
					+ asha_id
					+ " and avd is null and seq>p.last_visit "
					+ "and svd between date("
					+ mydt
					+ ",'-15 days') and date("
					+ mydt
					+ ") "
					+ ")  and svd>date("
					+ mydt
					+ ") "
					+ "group by pid "
					+ ") tmp on sc.pid=tmp.pid and sc.svd=tmp.svdt "
					+ "where sc.asha_id="
					+ asha_id
					+ " and (m_stat<>1 or c_stat<>1) and avd is null "
					+ "order by sv_dt";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getQuest(int grp) {
		String sql = "select * from q_bank where grp=" + String.valueOf(grp)
				+ " order by _id";
		return db.rawQuery(sql, null);
	}

	public synchronized int getPregNo() {
		Cursor mCursor = db.rawQuery("Select count(*) From preg_reg", null);
		if (mCursor.moveToFirst())
			return mCursor.getInt(0);
		else
			return 0;
	}

	public synchronized Cursor getNextQuest(int qid, boolean branch, int dstat) {
		String sql = "select * from q_bank where _id=" + String.valueOf(qid);
		if (!branch)
			sql = sql + " and grp<2";
		if (dstat == 1)
			sql = sql + " and grp<>1 and grp<>4";
		else if (dstat == 2)
			sql = sql + " and grp=1";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getCatgr(int cid) {
		String sql = "select * from catgr where _id=" + String.valueOf(cid);
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAllRem(int cid) {
		String sql = "select * from remedy where cid=" + String.valueOf(cid);
		return db.rawQuery(sql, null);
	}

	public synchronized String getpans(int pid, int qid) {
		// int qc[]={26,13,13,8,18,6};
		int pos = qid;
		String res = "", col = "answrg5";

		if (qid <= qc[0])
			col = "answrg0";
		else if (qid <= qc[1]) {
			col = "answrg1";
			pos = pos - qc[0] - 1;
		} else if (qid <= qc[2]) {
			col = "answrg2";
			pos = pos - qc[1] - 1;
		} else if (qid <= qc[3]) {
			col = "answrg3";
			pos = pos - qc[2] - 1;
		} else if (qid <= qc[4]) {
			col = "answrg4";
			pos = pos - qc[3] - 1;
		}
		String sql = "select " + col + " answr,seq from sch_visits where pid="
				+ String.valueOf(pid) + " and avd is not null order by seq";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		seq_str = "";
		while (!c.isAfterLast()) {
			String mstr = c.getString(0);
			String mcharr[] = mstr.split("\\ ");
			res = res + " " + mcharr[pos];
			seq_str = seq_str + " " + c.getInt(1);
			c.moveToNext();
		}
		return res;
	}

	public synchronized Cursor getVisitSummary(int pid, boolean fvisit) {
		// select avd,gsumm from sch_visits where pid=1 and seq=1
		String tblName = "sch_visits";
		if (fvisit)
			tblName = "opt_visits";
		String sql = "select seq,avd,gsumm from " + tblName
				+ " where avd is not null and pid=" + String.valueOf(pid);// +" and seq="+String.valueOf(pid);
		sql = sql + " order by avd desc limit 1";
		return db.rawQuery(sql, null);
	}

	public synchronized boolean regChild(long rowId, String dob, int pob,
			float weight, int m_stat, int c_stat, String sex) {
		ContentValues args = new ContentValues();
		args.put("dob", dob);
		args.put("pob", pob);
		args.put("weight", weight);
		args.put("c_stat", c_stat);
		args.put("m_stat", m_stat);
		args.put("sex", sex);
		return db.update(DB_PREG_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public synchronized boolean regDeath(long rowId, String doDth, int dstat) {
		ContentValues args = new ContentValues();

		switch (dstat) {
		case 0:
			args.put("c_death", doDth);
			args.put("c_stat", 1);
			break;
		case 1:
			args.put("m_death", doDth);
			args.put("m_stat", 1);
			break;
		case 2:
			args.put("m_death", doDth);
			args.put("c_death", doDth);
			args.put("m_stat", 1);
			args.put("c_stat", 1);
			break;
		case 3:
			args.put("m_stat", 1);
			args.put("c_stat", 1);
			break;
		}
		return db.update(DB_PREG_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	// ---retrieves a particular contact---
	public synchronized Cursor getPreg(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DB_PREG_TABLE, new String[] {
				KEY_ROWID, KEY_NAME, KEY_LMP, "EDD", "dob", "pob", "sex",
				"weight" }, KEY_ROWID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public synchronized Cursor getPregInfo(String rowId, String asha_id)
			throws SQLException {
		String sql = "select p._id,name,lmp,edd,dob,m_stat,c_stat,last_visit from preg_reg p "
				+
				// "left join sch_visits v on v.pid=p._id and v.seq=p.last_visit"+
				" where asha_id=" + asha_id + " and p._id=" + rowId;
		Cursor mCursor = db.rawQuery(sql, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}

	// ---updates a contact---
	public synchronized boolean updatePreg(long rowId, String name, String lmp) {
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_LMP, lmp);
		return db.update(DB_PREG_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public synchronized boolean updatePregVisitStr(String asha_id,
			String rowId, String hv_str, String seq) {
		ContentValues args = new ContentValues();
		args.put("hv_str", hv_str);
		args.put("last_visit", seq);
		return db.update(DB_PREG_TABLE, args, KEY_ROWID + "=" + rowId
				+ " and asha_id =" + asha_id, null) > 0;
	}

	public synchronized boolean updateVisit(long rowId, String answrg0,
			String answrg1, String answrg2, String answrg3, String answrg4,
			String gsumm) {
		ContentValues args = new ContentValues();
		Calendar c = Calendar.getInstance(Locale.getDefault());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String avd = df.format(c.getTime());
		args.put("avd", avd);
		args.put("answrg0", answrg0);
		args.put("answrg1", answrg1);
		args.put("answrg2", answrg2);
		args.put("answrg3", answrg3);
		args.put("answrg4", answrg4);
		// args.put("answrg5", answrg5);
		args.put("gsumm", gsumm);
		return db.update("sch_visits", args, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public synchronized boolean optVisit(long pid, String answrg0,
			String answrg1, String answrg2, String answrg3, String answrg4,
			String gsumm) {
		ContentValues args = new ContentValues();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String avd = df.format(c.getTime());
		args.put("pid", pid);
		args.put("svd", avd);
		args.put("seq", 0);
		args.put("avd", avd);
		args.put("answrg0", answrg0);
		args.put("answrg1", answrg1);
		args.put("answrg2", answrg2);
		args.put("answrg3", answrg3);
		args.put("answrg4", answrg4);
		// args.put("answrg5", answrg5);
		args.put("gsumm", gsumm);
		// return db.update("sch_visits", args, KEY_ROWID + "=" + rowId, null) >
		// 0;
		return db.insert("opt_visits", null, args) > 0;
	}

	public synchronized Cursor getSmsList() {
		return db
				.rawQuery(
						"select _id,msg,retry from pend_sms where retry<10 order by retry",
						null);
	}

	@Override
	public synchronized void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub

	}

	public synchronized String imm_txt(int immid) {
		Cursor c = db.rawQuery(
				"select descr from immun where _id=" + String.valueOf(immid),
				null);
		c.moveToFirst();
		return c.getString(0);
	}

	public synchronized String get_stat() {
		Cursor c = db.rawQuery("select currst from stat limit 1", null);
		c.moveToFirst();
		return c.getString(0);
		// return "12";
	}

	public synchronized String get_pend_sms() {
		/*
		 * String sql=
		 * "select p.asha_id asha_id,a.msg_id+1 mn,min(p.msg_id) mx from pend_procs p "
		 * + "inner join asha_dets a on p.asha_id=a._id " +
		 * "group by p.asha_id";
		 */
		String sql = "select a._id asha_id,msg_id,ifnull(mid,-1) mid from asha_dets a "
				+ "left join (select asha_id,min(msg_id) mid from pend_procs "
				+ "where done=0 " + "group by asha_id) pp on a._id=pp.asha_id ";
		Cursor c = db.rawQuery(sql, null);
		String msg = "";
		if (c.moveToFirst())
			while (!c.isAfterLast()) {
				int mn = c.getInt(c.getColumnIndex("msg_id"));
				int mx = c.getInt(c.getColumnIndex("mid"));
				if (mn < mx)
					msg = msg
							+ String.format("%d:%d:%d;",
									c.getInt(c.getColumnIndex("asha_id")), mn,
									mx);
				c.moveToNext();
			}
		return msg;
	}

	public synchronized int get_last_seq(int asha_id) {
		int tmp = 0;
		Cursor c = db.rawQuery("select msg_id from asha_dets where _id="
				+ asha_id, null);
		if (c.moveToFirst())
			tmp = c.getInt(0);
		return tmp;
	}

	public synchronized int msg_flag(String asha_id, String msg_id) {
		if (msg_id.trim().equals(""))
			return -4;
		int mid = Integer.parseInt(msg_id);
		String sql = "select msg_id from asha_dets where _id=" + asha_id;
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			int last_seq = c.getInt(0);
			if (mid > last_seq) {
				sql = "select ifnull(count(*),0) cnt from pend_procs where asha_id="
						+ asha_id + " and msg_id=" + msg_id;
				c = db.rawQuery(sql, null);
				c.moveToFirst();
				if (c.getInt(0) > 0)
					return -3; // duplicate sms pend
				else {
					sql = "select ifnull(max(msg_id),0) mx from pend_procs where asha_id="
							+ asha_id;
					c = db.rawQuery(sql, null);
					c.moveToFirst();
					if (c.getInt(0) > mid)
						return 1; // already updated
				}
			} else
				return -2;// duplicate sms
		} else
			return -1;// asha_id invalid
		return 0;
	}

	public synchronized static String strtodate(String dstr) {
		String tmp = "-";
		if (dstr != null) {
			String dt_str[] = dstr.split("\\-");
			if (dt_str.length > 2)
				tmp = dt_str[2] + "-" + hmstr[Integer.parseInt(dt_str[1]) - 1]
						+ "-" + dt_str[0];
			else
				tmp = dstr;
		}
		return tmp;
	}

	static synchronized boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}

	public synchronized long insertPregother(String asha_id, String _id,
			String mother_mcts, String aadhar_card, String bank_name,
			String branch, String jsy, String bpl, String age, String reg_date,
			String month_reg, String week_reg, String mother_weight,
			String blood_group, String past_health, String total_preg,
			String lastpreg_comp, String lastpreg_result, String ltolpreg_comp,
			String ltolpreg_result, String delivery_plan, String vdrl,
			String hiv) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("asha_id", asha_id);
		initialValues.put(KEY_ROWID, _id);
		initialValues.put("mother_mcts", mother_mcts);
		initialValues.put("aadhar_card", aadhar_card);
		initialValues.put("bank_name", bank_name);
		initialValues.put("branch", branch);
		initialValues.put("jsy", jsy);
		initialValues.put("bpl", bpl);
		initialValues.put("age", age);
		initialValues.put("reg_date", reg_date);
		initialValues.put("month_reg", month_reg);
		initialValues.put("week_reg", week_reg);
		initialValues.put("mother_weight", mother_weight);
		initialValues.put("blood_group", blood_group);
		initialValues.put("past_health", past_health);
		initialValues.put("total_preg", total_preg);
		initialValues.put("lastpreg_result", lastpreg_result);
		initialValues.put("lastpreg_comp", lastpreg_comp);
		initialValues.put("ltolpreg_result", ltolpreg_result);
		initialValues.put("ltolpreg_comp", ltolpreg_comp);
		initialValues.put("delivery_plan", delivery_plan);
		initialValues.put("vdrl", vdrl);
		initialValues.put("hiv", hiv);
		initialValues.put("anc_visit", 0);
		return db.insert(DB_PREG_TABLE_OTHER, null, initialValues);
	}

	public synchronized Cursor getAnmPreg(String asha_id, String id) {
		String sql = "select name, lmp, dob, m_stat, c_stat, dob, pob, weight, last_visit, m_death, c_death, caste, religion, hname, server_id, dor, awc_id from preg_reg where asha_id="
				+ asha_id + " and _id=" + id + "";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getserverid() {
		return db.rawQuery("select server_id  from preg_reg", null);
	}

	public synchronized Cursor getVhsndPreg(String asha_id, String id) {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select mother_mcts, visit1_date, visit2_date, visit3_date, visit4_date, tt1_date, tt2_date, ttbooster_date, anc_visit, reg_date"
				+ " from preg_reg_other where asha_id="
				+ asha_id
				+ " and _id="
				+ id;
		return db.rawQuery(sql, null);
	}

	// Add Hero new method
	public synchronized Cursor getPopUp_VhsndPreg(String asha_id, String id) {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		// String sql =
		// "select visit1_date, visit2_date, visit3_date, visit4_date, tt1_date, tt2_date, ttbooster_date, anc_visit,mother_weight from preg_reg_other where asha_id="
		// + asha_id + " and _id=" + id;
		String sql = "select * from anc_visit where asha_id=" + asha_id
				+ " and _id=" + id;
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getUpdatePrego(String asha_id, String id) {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select * from preg_reg_other where asha_id=" + asha_id
				+ " and _id=" + id;
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getVhsndChild(String asha_id, String id) {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select child_name, bcg, opv1, dpt1, hepb1, opv2, dpt2, hepb2, opv3, dpt3, hepb3, measeals, vitamina, child_mcts from preg_reg_other where asha_id="
				+ asha_id + " and _id=" + id;
		return db.rawQuery(sql, null);
	}

	public synchronized boolean vhsndpreg(String asha_id, String _id,
			String visit1_date, String visit2_date, String visit3_date,
			String visit4_date, String abortion, String tt1_date,
			String tt2_date, String ttbooster_date, String danger_signs,
			String reffered, String anc_visit) {
		ContentValues args = new ContentValues();
		args.put("visit1_date", visit1_date);
		args.put("visit2_date", visit2_date);
		args.put("visit3_date", visit3_date);
		args.put("visit4_date", visit4_date);
		args.put("abortion", abortion);
		args.put("tt1_date", tt1_date);
		args.put("tt2_date", tt2_date);
		args.put("ttbooster_date", ttbooster_date);
		args.put("danger_signs", danger_signs);
		args.put("reffered", reffered);
		args.put("anc_visit", anc_visit);
		return db.update(DB_PREG_TABLE_OTHER, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean vhsndbirth(String asha_id, String _id,
			String child_name, String bcg, String opv1, String dpt1,
			String hepb1, String opv2, String dpt2, String hepb2, String opv3,
			String dpt3, String hepb3, String measeals, String vitamina) {
		ContentValues args = new ContentValues();
		args.put("child_name", child_name);
		args.put("bcg", bcg);
		args.put("opv1", opv1);
		args.put("dpt1", dpt1);
		args.put("hepb1", hepb1);
		args.put("opv2", opv2);
		args.put("dpt2", dpt2);
		args.put("hepb2", hepb2);
		args.put("opv3", opv3);
		args.put("dpt3", dpt3);
		args.put("hepb3", hepb3);
		args.put("measeals", measeals);
		args.put("vitamina", vitamina);
		return db.update(DB_PREG_TABLE_OTHER, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean anmbirth(String asha_id, String _id,
			String child_mcts, String child_name, String breastfeeding,
			String strmid) {
		ContentValues args = new ContentValues();
		args.put("child_mcts", child_mcts);
		args.put("child_name", child_name);
		args.put("breastfeeding", breastfeeding);
		args.put("mother_mcts", strmid);
		return db.update(DB_PREG_TABLE_OTHER, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean updateAnmStat(String asha_id, String _id,
			String name, String hname, String mobile, String awc, String dor) {
		ContentValues args = new ContentValues();
		args.put("anm_stat", 1);
		args.put("name", name);
		args.put("hname", hname);
		args.put("mobile", mobile);
		args.put("dor", dor);
		args.put("awc_id", awc);
		return db.update(DB_PREG_TABLE, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean updateChildAnmStat(String asha_id, String _id,
			String name, String hname, String mobile, String childsex,
			String caste, String pob, String religion, Float weight) {
		ContentValues args = new ContentValues();
		args.put("canm_stat", 1);
		args.put("name", name);
		args.put("hname", hname);
		args.put("mobile", mobile);
		// args.put("sex", childsex);
		// args.put("caste", caste);
		// args.put("pob", pob);
		// args.put("religion",religion);
		// args.put("weight",weight);

		// Activate comment by Hero
		args.put("sex", childsex);
		args.put("caste", caste);
		args.put("pob", pob);
		args.put("religion", religion);
		args.put("weight", weight);
		Boolean value = null;
		try {
			String sql = "Update preg_reg set canm_stat=1,name='" + name
					+ "',hname='" + hname + "',mobile='" + mobile + "',sex='"
					+ childsex + "',caste='" + caste + "',pob='" + pob
					+ "',religion='" + religion + "',weight='" + weight
					+ "'  where asha_id='" + asha_id + "'and _id='" + _id + "'";
			db.execSQL(sql);
			value = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			value = false;
		}

		// return db.update(DB_PREG_TABLE, args, "asha_id = " + asha_id
		// + " and _id = " + _id, null) > 0;
		return value;
	}

	public synchronized long insertBirthOther(String asha_id, String _id,
			String child_mcts, String child_name, String breastfeeding,
			String strmid, String childsex, String caste, String pob,
			String religion, Float weight) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("asha_id", asha_id);
		initialValues.put(KEY_ROWID, _id);
		initialValues.put("child_mcts", child_mcts);
		initialValues.put("child_name", child_name);
		initialValues.put("breastfeeding", breastfeeding);
		initialValues.put("mother_mcts", strmid);
		// Comment Hero
		// initialValues.put("sex", childsex);
		// initialValues.put("caste", caste);
		// initialValues.put("pob", pob);
		// initialValues.put("religion", religion);
		// initialValues.put("weight", weight);

		// Add Hero
		String sql1 = "select count(*) from preg_reg_other where _id='" + _id
				+ "' and asha_id='" + asha_id + "'";
		int aa = getMaxRecord(sql1);
		String sql = "";
		int rtnvalue = 0;
		try {
			if (aa > 0) {
				sql = "Update preg_reg_other set child_mcts='" + child_mcts
						+ "',child_name='" + child_name + "',breastfeeding='"
						+ breastfeeding + "',mother_mcts='" + strmid
						+ "'  where asha_id='" + asha_id + "'and _id='" + _id
						+ "'";
				rtnvalue = 2;
			} else {
				sql = "insert into preg_reg_other (asha_id,_id,child_mcts,child_name,breastfeeding,mother_mcts) values('"
						+ asha_id
						+ "','"
						+ _id
						+ "','"
						+ child_mcts
						+ "','"
						+ child_name
						+ "','"
						+ breastfeeding
						+ "','"
						+ strmid
						+ "')";
				// add Hero
				rtnvalue = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rtnvalue = 2;
		}
		db.execSQL(sql);
		return rtnvalue;
		// return db.insert(DB_PREG_TABLE_OTHER, null, initialValues);
	}

	// hero add
	public int getMaxRecord(String Sql) {
		int iIntegerValue = 0;
		Cursor cursor = null;
		try {

			cursor = db.rawQuery(Sql, null);
			if (cursor != null) {
				cursor.moveToFirst();
				while (cursor.isAfterLast() == false) {
					iIntegerValue = cursor.getInt(0);
					cursor.moveToNext();
				}
				cursor.close();
			}
		} catch (Exception exception) {
			Log.e("DataProvider",
					"Error in getMaxRecord :: " + exception.getMessage());
		}
		return iIntegerValue;

	}

	public synchronized long insertAncVisit1(String asha_id, String _id,
			String weight1, String bp1, String hb1, String urine1, String ifa1) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("asha_id", asha_id);
		initialValues.put(KEY_ROWID, _id);
		initialValues.put("weight1", weight1);
		initialValues.put("bp1", bp1);
		initialValues.put("hb1", hb1);
		initialValues.put("urine1", urine1);
		initialValues.put("ifa1", ifa1);
		return db.insert(DB_ANC_VISIT, null, initialValues);
	}

	public synchronized boolean updateAncVisit2(String asha_id, String _id,
			String weight2, String bp2, String hb2, String urine2, String ifa2) {
		ContentValues args = new ContentValues();
		args.put("weight2", weight2);
		args.put("bp2", bp2);
		args.put("hb2", hb2);
		args.put("urine2", urine2);
		args.put("ifa2", ifa2);
		return db.update(DB_ANC_VISIT, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean updateAncVisit3(String asha_id, String _id,
			String weight3, String bp3, String hb3, String urine3, String ifa3) {
		ContentValues args = new ContentValues();
		args.put("weight3", weight3);
		args.put("bp3", bp3);
		args.put("hb3", hb3);
		args.put("urine3", urine3);
		args.put("ifa3", ifa3);
		return db.update(DB_ANC_VISIT, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized boolean updateAncVisit4(String asha_id, String _id,
			String weight4, String bp4, String hb4, String urine4, String ifa4) {
		ContentValues args = new ContentValues();
		args.put("weight4", weight4);
		args.put("bp4", bp4);
		args.put("hb4", hb4);
		args.put("urine4", urine4);
		args.put("ifa4", ifa4);
		return db.update(DB_ANC_VISIT, args, "asha_id = " + asha_id
				+ " and _id = " + _id, null) > 0;
	}

	public synchronized Cursor getAncVisit(String asha_id, String id) {
		// return
		// db.rawQuery("select * from preg_reg where dob is null order by edd",
		// null);
		String sql = "select * from anc_visit where asha_id=" + asha_id
				+ " and _id=" + id;
		return db.rawQuery(sql, null);
	}

	public synchronized boolean sendGPRS(String url, String msg, int reqtype) {
		ContentValues initialValues = new ContentValues();
		initialValues.put("url", url);
		initialValues.put("msg", msg);
		initialValues.put("rtype", reqtype);
		Log.d("info", url + "#" + msg);
		return db.insert("pend_web", null, initialValues) > 0;
	}

	public synchronized long updateAshaData(String[] str) {

		for (int i = 0; i < str.length - 1; i++) {
			Log.e("ERROR", str[i]);
			db.execSQL(str[i]);
		}
		return 1;
	}

	public synchronized Cursor getPdataList() {
		return db.rawQuery(
				"select _id,msg,url,retry,rtype from pend_web where sent=0",
				null);
	}

	public Cursor rawQuery(String sql) {
		return db.rawQuery(sql, null);
	}

	public synchronized long logAct(String info) {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		String dtime = df.format(c.getTime());
		ContentValues initialValues = new ContentValues();
		initialValues.put("info", info);
		initialValues.put("dtime", dtime);
		return db.insert("actlog", null, initialValues);
	}

	public synchronized boolean deleteWeb(long rowId) {
		ContentValues args = new ContentValues();
		args.put("sent", 1);
		return db.update("pend_web", args, "_id= " + rowId, null) > 0;
		// return db.delete("pend_sms", "_id=" + rowId, null) > 0;
	}

	public synchronized Cursor getAshaReportP() {

		// String sql =
		// "select  asha_id _id, asha_dets.name, (select count(*) from preg_reg where dob is NULL and preg_reg.asha_id=asha_dets._id )as cnt1, "
		// +
		// "(select count(*) from preg_reg_other where child_mcts is null and preg_reg_other.asha_id=asha_dets._id )as cnt2 "
		// +
		// "from preg_reg INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id GROUP BY asha_dets.name";
		// // Jitendra
		String sql = "select  asha_id _id, asha_dets.name, (select count(*) from preg_reg where dob is NULL and preg_reg.asha_id=asha_dets._id )as cnt1, (select count (*)from preg_reg pr inner JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id 	where  pr.asha_id=po.asha_id and dob is null and po.asha_id=asha_dets._id )as cnt2  from preg_reg INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id GROUP BY asha_dets.name";

		return db.rawQuery(sql, null);
	}

	public synchronized String getAshaReportPcnt() {

		// String
		// //
		// sql="select count(preg_reg._id) cnt from preg_reg where dob is null";
		// String sql =
		// "select count(pr._id) cnt from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id = po._id "
		// +
		// "where  pr._id not in (select _id from preg_reg_other where asha_id=pr.asha_id)"
		// + "	and dob is null order by edd desc";
		// jitendra
		String sql = "select count(pr._id) as cnt1	from preg_reg pr Left JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id where  dob is null order by edd desc";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAshaReportPRcnt() {

		// String sql =
		// "select count(preg_reg_other._id) cnt from preg_reg_other where child_mcts is null";
		// Jitendra
		String sql = "select count (*) cnt from preg_reg pr inner JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id where  pr.asha_id=po.asha_id and dob is null ";
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getAshaReportB() {

		String sql = "select  asha_id _id, asha_dets.name, (select count(*) from preg_reg where dob is not NULL and preg_reg.asha_id=asha_dets._id )as cnt1, "
				+ "(select count(*) from preg_reg_other where child_mcts is not null and preg_reg_other.asha_id=asha_dets._id )as cnt2 "
				+ "from preg_reg INNER JOIN asha_dets on preg_reg.asha_id=asha_dets._id GROUP BY asha_dets.name";

		return db.rawQuery(sql, null);
	}

	public synchronized String getAshaReportBcnt() {

		String sql = "select count(preg_reg._id) cnt from preg_reg where dob is not null";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAshaReportBRcnt() {

		String sql = "select count(preg_reg_other._id) cnt from preg_reg_other where child_mcts is not null";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getBCG(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 45 and bcg is null AND pg.asha_id = "
		// + asha_id;
		// Hero Changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 45 and (bcg is null or bcg='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getDPT1(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and dpt1 is null AND pg.asha_id = "
		// + asha_id;
		// Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and (dpt1 is null or dpt1='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getDPT2(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and dpt2 is null AND pg.asha_id = "
		// + asha_id;

		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (dpt2 is null or dpt2='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getDPT3(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and dpt3 is null AND pg.asha_id = "
		// + asha_id;
		// Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (dpt3 is null or dpt3='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getHepb1(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and hepb1 is null AND pg.asha_id = "
		// + asha_id;
		// Add hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and (hepb1 is null or hepb1='')  AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getHepb2(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and hepb2 is null AND pg.asha_id = "
		// + asha_id;
		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (hepb2 is null or hepb2='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getHepb3(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and hepb3 is null AND pg.asha_id = "
		// + asha_id;
		// Addd Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (hepb3 is null or hepb3='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getMeaseals(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 270 and julianday('now') - julianday( pg.dob) < 365 and measeals is null AND pg.asha_id = "
		// + asha_id;
		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 270 and julianday('now') - julianday( pg.dob) < 365 and (measeals is null or measeals='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getVitamina(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 480 and julianday('now') - julianday( pg.dob) < 720 and vitamina is null AND pg.asha_id = "
		// + asha_id;
		// Add hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 480 and julianday('now') - julianday( pg.dob) < 720 and (vitamina is null or vitamina='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getOpv1(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 72 and opv1 is null AND pg.asha_id = "
		// + asha_id;
		// Aaa Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 72 and (opv1 is null or opv1='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getOpv2(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and opv2 is null AND pg.asha_id = "
		// + asha_id;
		// Addd Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (opv2 is null or opv2='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getOpv3(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and opv3 is null AND pg.asha_id = "
		// + asha_id;
		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
				+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (opv3 is null or opv3='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getRegPregList(String asha_id) {
		// return
		// db.rawQuery("select _id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death from preg_reg where asha_id="+asha_id+" and anm_stat is not null and dob is null order by edd desc",
		// null);
		// String
		// sql="select * from preg_reg where last_visit=0 and ((m_stat<>1) and (c_stat<>1)) order by edd ";
		// return db.rawQuery(sql, null);

		// return db
		// .rawQuery(
		// "select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile, "
		// +
		// "po.mother_mcts, po.mother_weight, po.reg_date, po.past_health, po.anc_visit"
		// +
		// " from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id "
		// + "where  pr._id in (select _id from preg_reg_other where asha_id="
		// + asha_id
		// + ") and  pr.asha_id="
		// + asha_id
		// + ""
		// + " and dob is null order by edd desc", null);
		// jitendra
		String sql = "select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile, po.mother_mcts, po.mother_weight, po.reg_date, po.past_health, po.anc_visit	from preg_reg pr Left JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id where  pr.asha_id="
				+ asha_id + " and dob is null order by edd desc";

		return db.rawQuery(sql, null);
	}

	// jitendra
	public synchronized Cursor getRegPregListreport(String asha_id,
			String from, String to) {

		String sql = "select pr.asha_id,pr._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile, po.mother_mcts, po.mother_weight, po.reg_date, po.past_health, po.anc_visit	from preg_reg pr Left JOIN preg_reg_other po on pr.asha_id =po.asha_id and pr._id=po._id where  pr.asha_id="
				+ asha_id
				+ " and dob is null and pr.dor between '"
				+ from
				+ "' and '" + to + "' order by edd desc ";
		return db.rawQuery(sql, null);
	}

	public synchronized Cursor getAshaListnewchildreport(String asha_id,
			String from, String to) {
		// jitendra
		String sql = "";
		if (from.equalsIgnoreCase("") && from.equalsIgnoreCase("")) {
			sql = "select pr.asha_id,pr._id,(case when po.child_name is null then '----' else po.child_name end) as name,''||pr.name rslt,dob as edd,'' hname,m_stat,c_stat,m_death,c_death from preg_reg pr left JOIN preg_reg_other po on pr._id =po._id and pr.asha_id =po.asha_id where   pr.asha_id='"
					+ asha_id
					+ "'  and pr.dob is not null  order by pr.dob desc ";
		} else {
			sql = "select pr.asha_id,pr._id,(case when po.child_name is null then '----' else po.child_name end) as name,''||pr.name rslt,dob as edd,'' hname,m_stat,c_stat,m_death,c_death from preg_reg pr left JOIN preg_reg_other po on pr._id =po._id and pr.asha_id =po.asha_id where   pr.asha_id='"
					+ asha_id
					+ "'  and pr.dob is not null and pr.dor between '"
					+ from
					+ "' and '" + to + "' order by pr.dob desc ";
		}
		return db.rawQuery(sql, null);

	}

	public synchronized Cursor getRegBirthList(String asha_id) {

		return db
				.rawQuery(
						"select pr._id,name,case(pob) "
								+ "when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile, po.child_mcts, po.child_name "
								+ "from preg_reg pr LEFT JOIN preg_reg_other po on pr.asha_id =po.asha_id  and pr._id=po._id "
								+ "where pr.asha_id="
								+ asha_id
								+ " and ((m_stat<>1) and (c_stat<>1)) "
								+ "and dob is not null  and po.child_mcts is not NULL order by dob desc",
						null);
	}

	public synchronized Cursor getBcgList(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.dob) > 45 and bcg is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
								+ "where julianday('now') - julianday( pg.dob) > 45 and (bcg is null or bcg='') AND pg.asha_id = "
								+ asha_id, null);
	}

	public synchronized Cursor getDPT1List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and dpt1 is null AND pg.asha_id = "
		// + asha_id, null);
		// Addd Hero Changes

		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and (dpt1 is null or dpt1='') AND pg.asha_id = "
								+ asha_id, null);
	}

	public synchronized Cursor getDPT2List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and dpt2 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero Chnages
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (dpt2 is null or dpt2='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getDPT3List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and dpt3 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (dpt3 is null or dpt3='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getHepb1List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and hepb1 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 70 and (hepb1 is null or hepb1='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getHepb2List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and hepb2 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (hepb2 is null or hepb2='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getHepb3List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and hepb3 is null AND pg.asha_id = "
		// + asha_id, null);
		// Addd Hero changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (hepb3 is null or hepb3='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getMeasealsList(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 270 and julianday('now') - julianday( pg.dob) < 365 and measeals is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero CHanges
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 270 and julianday('now') - julianday( pg.dob) < 365 and (measeals is null or measeals='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getVitaminaList(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 480 and julianday('now') - julianday( pg.dob) < 720 and vitamina is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero Changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 480 and julianday('now') - julianday( pg.dob) < 720 and (vitamina is null or vitamina='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getOpv1List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 72 and opv1 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Changes Hero
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 42 and julianday('now') - julianday( pg.dob) < 72 and (opv1 is null or opv1='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getOpv2List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and opv2 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero Changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 70 and julianday('now') - julianday( pg.dob) < 98 and (opv2 is null or opv2='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getOpv3List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
		// +
		// "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
		// +
		// "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and opv3 is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero Changes
		return db
				.rawQuery(
						"select  po.asha_id, po._id, name,case(pob) when 1 then 'I' else 'H' end||' '||sex||' '||weight rslt, "
								+ "dob edd,m_stat,c_stat,dob,weight,sex,anm_stat,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id where "
								+ "julianday('now') - julianday( pg.dob) > 98 and julianday('now') - julianday( pg.dob) < 270 and (opv3 is null or opv3='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized String getAnc1(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 56 and visit1_date is null and dob is null AND pg.asha_id = "
		// + asha_id;
		// Hero add Chnages
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
				+ "where julianday('now') - julianday( pg.lmp) > 56 and (visit1_date is null or visit1_date='') and (dob is null or dob='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAnc2(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 168 and visit2_date is null and dob is null AND pg.asha_id = "
		// + asha_id;
		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
				+ "where julianday('now') - julianday( pg.lmp) > 168 and (visit2_date is null or visit2_date='') and (dob is null or dob='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAnc3(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 244 and visit3_date is null and dob is null AND pg.asha_id = "
		// + asha_id;
		// Add hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
				+ "where julianday('now') - julianday( pg.lmp) > 244 and (visit3_date is null or visit3_date='') and (dob is null or dob='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized String getAnc4(String asha_id) {

		// String sql =
		// "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 252 and visit4_date is null and dob is null AND pg.asha_id = "
		// + asha_id;
		// Add Hero changes
		String sql = "select  count(*) as cnt from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
				+ "where julianday('now') - julianday( pg.lmp) > 252 and (visit4_date is null or visit4_date='') and (dob is null or dob='') AND pg.asha_id = "
				+ asha_id;

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

	public synchronized Cursor getAnc1List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 56 and visit1_date is null and dob is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
								+ "where julianday('now') - julianday( pg.lmp) > 56 and (visit1_date is null or visit1_date='') and (dob is null or dob='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getAnc2List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 168 and visit2_date is null and dob is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
								+ "where julianday('now') - julianday( pg.lmp) > 168 and (visit2_date is null or visit2_date='') and (dob is null or dob='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getAnc3List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 244 and visit3_date is null and dob is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero changes
		return db
				.rawQuery(
						"select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
								+ "where julianday('now') - julianday( pg.lmp) > 244 and (visit3_date is null or visit3_date='') and (dob is null or dob='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized Cursor getAnc4List(String asha_id) {

		// return db
		// .rawQuery(
		// "select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
		// +
		// "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
		// +
		// "where julianday('now') - julianday( pg.lmp) > 252 and visit4_date is null and dob is null AND pg.asha_id = "
		// + asha_id, null);
		// Add Hero Changes
		return db
				.rawQuery(
						"select  pg.asha_id,pg._id,name,'' rslt,edd,m_stat,c_stat,dob,m_death,c_death,hname,caste,religion,mobile "
								+ "from preg_reg pg inner join preg_reg_other po on pg.asha_id=po.asha_id and pg._id=po._id "
								+ "where julianday('now') - julianday( pg.lmp) > 252 and (visit4_date is null or visit4_date='') and (dob is null or dob='') AND pg.asha_id = "
								+ asha_id, null);

	}

	public synchronized String getPendWeb() {

		String sql = "SELECT count(*) cnt FROM pend_web where sent = 0";

		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		return String.valueOf(c.getInt(0));
	}

}