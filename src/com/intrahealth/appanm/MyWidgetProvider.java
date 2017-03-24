
package com.intrahealth.appanm;


//import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
//import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.intrahealth.msakhi.appanm.R;

public class MyWidgetProvider extends AppWidgetProvider {

  private static final String SMS_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
  public static String MY_WIDGET_CLICK = "MY_WIDGET_CLICK";
  //int number=0;
  //public static final int DURATION = 45000;
  public static final int DURATION = 3600000;
  //public static final int DURATION = 14400000; //Every 4 hour
  //public static final int DURATION = 43200000; //Every 12 hour
  private Handler mHandler = new Handler();

  private DBAdapter mydb;
  String pktHeader="";
  String phoneNumber="";
  public boolean send_sms=false;
  private Intent mIntent;
  private Context mContext;
  
  @Override
  public void onEnabled(Context context)
  {
	  mydb=DBAdapter.getInstance(context);
	  mHandler.postDelayed(periodicTask, DURATION);
	  SharedPreferences prefs=PreferenceManager
				.getDefaultSharedPreferences(context);
	  String anm_id=prefs.getString("id", "1").trim();
	  send_sms=prefs.getBoolean("send_sms", false);
	  pktHeader=context.getString(R.string.sms_prefix)+" =^ "+anm_id;
	  phoneNumber=context.getString(R.string.smsno);

  }

  @Override
  public void onDisabled(Context context)
  {
	  mydb.close();
  }
  
  
  
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {
	  super.onUpdate(context, appWidgetManager, appWidgetIds);
    // Get all ids
    ComponentName thisWidget = new ComponentName(context,
        MyWidgetProvider.class);
    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    if (mydb==null) mydb=DBAdapter.getInstance(context);
    /*
    {
        mydb = new DBAdapter(context);
	    try {
	    	mydb.createDataBase();
			mydb.openDataBase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }*/
    try {
        String starr[] = mydb.get_stat().split("\\:");
        String preg = starr[0];
        String del_arr[] = starr[1].split("\\,");
        String shv_arr[] = starr[2].split("\\,");
        String av_arr[] = starr[3].split("\\,");
        String sc_arr[] = starr[4].split("\\,");

        for (int widgetId : allWidgetIds) {
          // Create some random data
        //  int number = (new Random().nextInt(100));

          RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
              R.layout.widget_front);  	
          //remoteViews.setTextViewText(R.id.tvHVY, mydb.get_stat());
          
          remoteViews.setTextViewText(R.id.tvPregY, preg);
          //remoteViews.setTextViewText(R.id.tvPregM, "");
         // remoteViews.setTextViewText(R.id.tvPreg, "");
          
         remoteViews.setTextViewText(R.id.tvDelY, del_arr[0]);
         remoteViews.setTextViewText(R.id.tvDelM, del_arr[1]);
         remoteViews.setTextViewText(R.id.tvDel, del_arr[2]);
          
         remoteViews.setTextViewText(R.id.tvHVY, shv_arr[0]);
         remoteViews.setTextViewText(R.id.tvHVM, shv_arr[1]);
         remoteViews.setTextViewText(R.id.tvHV, shv_arr[2]);

         remoteViews.setTextViewText(R.id.tvAVY, av_arr[0]);
         remoteViews.setTextViewText(R.id.tvAVM, av_arr[1]);
         remoteViews.setTextViewText(R.id.tvAV, av_arr[2]);

         remoteViews.setTextViewText(R.id.tvSCY, sc_arr[0]);
//         remoteViews.setTextViewText(R.id.tvSCM, "");
         remoteViews.setTextViewText(R.id.tvSC, sc_arr[1]);
         Intent intent = new Intent(context, MainActivity.class);
         //Intent intent = new Intent(context, MyWidgetProvider.class); 
         //intent.setAction(MY_WIDGET_CLICK);
          intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
          intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
          //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
          PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
          remoteViews.setOnClickPendingIntent(R.id.llTop, pendingIntent);
          appWidgetManager.updateAppWidget(widgetId, remoteViews);
          
        }
		
	} catch (Exception e) {
		// TODO: handle exception
		Log.d("error", e.getMessage());
	}
          
  }


  
	@Override
	public void onReceive(Context context, Intent intent) {
		//number = (new Random().nextInt(100));
		mContext = context;
        mIntent = intent;
		super.onReceive(context, intent);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), MyWidgetProvider.class.getName());
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_front);
//		remoteViews.setTextViewText(R.id.tvHVY, String.valueOf(number)); 
		if (intent.getAction().equals(MY_WIDGET_CLICK))
		{
			Intent mintent = new Intent(context, MainActivity.class);
			context.startActivity(mintent);
		}
		
		if (intent.getAction().equals(SMS_RECEIVED)) {
			/*
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++)
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);*/
				SmsMessage[] messages = getMessagesFromIntent(mIntent);
				String fmsg=""; 
				for (SmsMessage message : messages) fmsg+=message.getMessageBody().toString();
				fmsg=fmsg.replaceAll("(\\r|\\n)", "");
				{		
				    Log.d("info",fmsg);
				    //Toast.makeText(context, String.format ("\\u%04x \\u%04x", (int)fmsg.charAt(0),(int)fmsg.charAt(1)),
				    //		Toast.LENGTH_LONG).show();
					if (fmsg.startsWith("=^")||fmsg.startsWith("=Λ")) 
					{
						if (mydb==null) mydb=DBAdapter.getInstance(context);
					    String msg=fmsg.substring(2).trim();
					    //Log.d("info",message.getMessageBody());
					    String msgar[]=msg.split("\\@");
					    String statmsg=msgar[1];
						//mydb.updateStat(statmsg);
						//for(int i=0;i<msgar.length;i++)
						//	Log.d("info", msgar[i]);
						if (msgar.length>2)
						{
						String tmp_msg=msgar[0].trim();
						String msg_id=msgar[2].trim();
						if (!tmp_msg.equals(""))
							updateData(tmp_msg,msg_id,context,statmsg);
						}
						//=^ 1:PA:45:सूर्योदय:2012-10-05:|19:17,0,0:112,23,0:4,0,0:1,0
							appWidgetManager.updateAppWidget(thisAppWidget, remoteViews);      
							onUpdate(context, appWidgetManager, appWidgetIds);

					     //Toast.makeText(context, msgar[0], Toast.LENGTH_LONG).show();
						
					abortBroadcast();
					}
			}
			}
		}
	//}
	
	private Runnable periodicTask = new Runnable() {
		public void run() {	
			 SmsManager sms = SmsManager.getDefault();
			 String longMessage=String.format("%s:AN:SYNC:%s",pktHeader,mydb.get_pend_sms());			 
		        if (send_sms)
		        	sms.sendTextMessage(phoneNumber, null, longMessage, null, null);
				Log.d("info",longMessage);
		        mHandler.postDelayed(periodicTask, DURATION);
		}
	};
	
	 public static SmsMessage[] getMessagesFromIntent(Intent intent) {
	        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
	        byte[][] pduObjs = new byte[messages.length][];

	        for (int i = 0; i < messages.length; i++) {
	            pduObjs[i] = (byte[]) messages[i];
	        }
	        byte[][] pdus = new byte[pduObjs.length][];
	        int pduCount = pdus.length;
	        SmsMessage[] msgs = new SmsMessage[pduCount];
	        for (int i = 0; i < pduCount; i++) {
	            pdus[i] = pduObjs[i];
	            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
	        }
	        return msgs;
	    }
	 
	public synchronized void updateData(String msg,String msg_id,Context context,String statmsg)
	  {
		  //1:PA:45:सूर्योदय:2012-10-05:
		  String msgar[]=msg.split("\\:");
		  String asha_id=msgar[0];
		  String pid=msgar[2];
		  String sql="";
		  String sql1="";
		  String cmd=msgar[1].trim();
		  Log.d("info",msg);
		  try {
			  //int last_msg_id=mydb.get_last_seq(Integer.parseInt(asha_id));
			
			  //if (Integer.parseInt(msg_id)>last_msg_id)   
			  if (msg_id.equals("")) msg_id="0";
			  int mf=mydb.msg_flag(asha_id, msg_id);
			  if (mf>=0)
			  {
				  if (mf==0) mydb.updateStat(statmsg);	  
			  //String msg_id="0";
			  //boolean upd_flag=mydb.check_preg(asha_id, pid);
			  //boolean pa_flag=cmd.equals("PA");
			  //Cursor c=mydb.getPregInfo(pid, asha_id);
			  if (cmd.equals("PA")) sql=String.format("insert into preg_reg(asha_id,_id,name,lmp,hv_str,last_visit,sex,m_stat,c_stat) " + 
			  			  			  "values(%s,%s,'%s','%s','-------',0,'B',0,0)",asha_id,pid,msgar[3],msgar[4]);
			  	  else if (cmd.equals("IA")) sql=String.format("insert into imm_det(asha_id,pid,im_id,imdt) " + 
			  			  "values(%s,%s,%s,'%s')",asha_id,pid,msgar[3],msgar[4]);
				  else if (cmd.equals("PM")) sql=String.format("update preg_reg set name='%s',lmp='%s' where asha_id=%s and _id=%s "  
			  			  ,msgar[3],msgar[4],asha_id,pid);
				  else if (cmd.equals("IM")) sql=String.format("update imm_det set imdt='%s' where asha_id=%s,pid=%s and im_id=%s "  
		  			  ,msgar[4],asha_id,pid,msgar[3]);
				  else if ((cmd.equals("CA")) || (cmd.equals("CM"))) sql=String.format("update preg_reg set dob='%s',weight=%s,pob=%s,sex='%s' where asha_id=%s and _id=%s "  
		  			  ,msgar[5],msgar[3],msgar[4],msgar[6],asha_id,pid);
				  //asha_id:cmd:pid:seq:avd:gsumm:hvstr
				  else if (cmd.equals("H1")) {
					  String avdt=msgar[4].replace(".", ":");
					  sql=String.format("update sch_visits set avd='%s',gsumm='%s' where asha_id=%s and _id=%s and seq=%s"  
					  		,avdt,msgar[5],asha_id,pid,msgar[3]);
					  sql1=String.format("update preg_reg set hv_str='%s',last_visit=%s where asha_id=%s and _id=%s",
							  	msgar[6],msgar[3],asha_id,pid);
				  	//mydb.updatePregVisitStr(asha_id,pid, msgar[6],msgar[3]);	
			  		}
				  else if (cmd.equals("H2")||cmd.equals("O1")||cmd.equals("O2")) sql="update preg_reg set m_stat=0 where 1=2";
				  else if (cmd.equals("DA")) {
					  String qry;
					  String dod=msgar[4];
					  int d_state=Integer.parseInt(msgar[3]);
					  if (d_state==0) qry=String.format("c_stat=1,c_death='%s'",dod);
					  else if (d_state==1) qry=String.format("m_stat=1,m_death='%s'",dod);
					  else if (d_state==2) qry=String.format("m_stat=1,m_death='%s',c_stat=1,c_death='%s'",dod,dod);
					  else qry="m_stat=1,c_stat=1";
					  sql=String.format("update preg_reg set %s where asha_id=%s and _id=%s", qry,asha_id,pid);
				  }
			  
			
			  Toast.makeText(context, sql, Toast.LENGTH_SHORT).show();
			  
			  if (!sql.equals("")) {
				  
				  mydb.sms_enqeue(asha_id,pid,msg_id,sql,sql1);
				  //if ((pa_flag)||(upd_flag)) mydb.execSQL(sql); else mydb.sms_enqeue(asha_id,pid,msg_id,sql,sql1);
				  //if (pa_flag) 
					  mydb.process_sms(asha_id,pid);
			  }
			  } else Toast.makeText(context, "Invalid/Duplicate SMS "+msg_id+" "+String.valueOf(mf), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.d("error", e.getMessage());
		}
		  
	  }	
} 
