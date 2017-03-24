package com.intrahealth.appanm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;









import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends Service
{		
	 String link;
	 Context context;
	 String anm_id2="";
	 long timeInMilliseconds;
	 int j;
	 Date DateMs;
	 String str = "";
	 int i=0;
	 String[] array_of_server_id=new String[5000];
	 Cursor c1;
	 String server_id,image_folder;
	 private DBAdapter mydb;
	 SharedPreferences sharedpreferences;
	 public static final String MyPREFERENCES = "MyPrefs" ;
	 public static final String TIMESTAMPMS = "utime";
	 public static final String LastTime = "ltime";
	 String TimeStamp = "2013-01-23 10:20:56";
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.e("Sservice","start");	
		 SharedPreferences prefs=PreferenceManager
					.getDefaultSharedPreferences(this);     
		 context=getApplicationContext();		 
		 link=AppVariables.API(context);
		 anm_id2=prefs.getString("id", "1").trim();
				 
		 final Timer timer = new Timer ();
	     TimerTask hourlyTask = new TimerTask () {
	         @Override
	         public void run () {	
	        	 i++;
	        	 if(i>=2)
	        	 {
	        		 Log.e("TIMER CANCEL","IN SERVICE");
	        		 timer.cancel();
	        	 }
	        	 else
	        	 {
	        	 Log.e("Sservice","Timer start");	
	        	 mydb=DBAdapter.getInstance(getApplicationContext());
	             sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

	             if (sharedpreferences.contains(TIMESTAMPMS))
	             {	             	
	             	TimeStamp = sharedpreferences.getString(LastTime, "");
	             }
	             
	        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 				try {
						DateMs = sdf.parse(TimeStamp);
						Log.e("date",DateMs+"");
						timeInMilliseconds = DateMs.getTime();
						System.out.println("timeinMilliseconds"+timeInMilliseconds);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 				//showAlert(TimeStamp+" "+Long.toString(timeInMilliseconds));
	 				 TimeStamp = (DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
	 			
	 				 String n  = TimeStamp;
	 				 
	 		   	      Editor editor = sharedpreferences.edit();
	 		   	      editor.putString(TIMESTAMPMS, "Update("+n+")");
	 		   	      editor.putString(LastTime, n);
	 		   	      editor.commit(); 				
	 				new JSONParse().execute();
	        	 }
	     	}
	     };
	     timer.schedule(hourlyTask, 0l,600000); 	
	    return START_NOT_STICKY;
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("Sservice","stopped");
		super.onDestroy();
			
	}
	
	
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
	     
	      protected void onPreExecute() {
	            super.onPreExecute();	             
	           
	      }
	      @Override
	        protected JSONObject doInBackground(String... args) {
	       
	    	  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		        StrictMode.setThreadPolicy(policy);
				
				JSONObject json = null;
				//String str = "";
				HttpResponse response;
		        HttpClient myClient = new DefaultHttpClient();
		       // HttpPost myConnection = new HttpPost("http://msakhi.org/webget_anmdata.php?anm_id="+anm_id+"&dtime="+timeInMilliseconds);
         
	            String s=link+"webget_anmdata.php?anm_id="+anm_id2+"&dtime="+timeInMilliseconds;	        
		        HttpPost myConnection = new HttpPost(
		       s);		        
		        System.out.println(
		        		 s);		        
		        try {
		        	response = myClient.execute(myConnection);
		            str = EntityUtils.toString(response.getEntity(), "UTF-8");
		            
		            Log.d("Sservice","data and time="+timeInMilliseconds);
		            Log.d("Sservice","parsed data"+ str);
		            
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }		        
		        try {
	        	    JSONArray jArray = new JSONArray(str);
		        	json = jArray.getJSONObject(0);		        			      	
		        	String stmt = json.getString("stmt");
		        	String [] str_array = stmt.split(";");
		        	for(int inte=0;inte<str_array.length;inte++)
		        	{
		        	System.out.println(str_array[inte]);
		        	}
		        	//showAlert(stmt);
		        	mydb.updateAshaData(str_array);	   
		        	//c=mydb.getAshaListP();
		        	//int key_id = c.getInt(c.getColumnIndex("asha_id"));
	                //   int  asha_id=key_id;
		        	 c1=mydb.getserverid();
                 //  for(int j=0;j<=str_array.length;j++)
                 //  {       	  
            if (c1.moveToFirst())
			{	
         	   do{  							
         	    server_id=(c1.getString(c1.getColumnIndex("server_id")));  
         	    array_of_server_id[j]=server_id;    
         	    downlaodfile2(link+"download.php",server_id);
         	    j++;
         	    c1.moveToNext();
			}while(c1.moveToNext());       	   
			}                 
         //  downlaodfile("http://192.168.1.34/msakhi/download.php",array_of_server_id[j]);              
         //}  
          
	        } catch ( JSONException e) {
	        	
		        	e.printStackTrace();		        	
		        }	
		        
			return json;
	      }
	       @Override
	         protected void onPostExecute(JSONObject json) {
	       
		                    
	       }
	    }			
	public String downlaodfile2(String FileDownloadPath, String fileName) {
		   try {		   
		       File root = android.os.Environment.getExternalStorageDirectory();
		       File dir = new File(Environment.getExternalStorageDirectory()+"/"+"mcare_hindi"+"/pdata/");
		       if(dir.exists() == false){
		            dir.mkdirs();  
		       }
		            File file =new File(dir,fileName+".jpg");
		       HttpClient httpclient = new DefaultHttpClient();
		       
		            // 2. make POST request to the given URL
		            HttpPost httpPost = new HttpPost(FileDownloadPath);
		 
		            String json = "";		 		            
		            // 3. build jsonObject		           		           		          
		            // 4. convert JSONObject to JSON to String		           		 		            
		            long startTime = System.currentTimeMillis();
		            System.out.println(json);
		            // ** Alternative way to convert Person object to JSON string usin Jackson Lib 
		            // ObjectMapper mapper = new ObjectMapper();
		            // json = mapper.writeValueAsString(person); 		 
		            // 5. set json to StringEntity
		            StringEntity se = new StringEntity(fileName);		 
		            // 6. set httpPost Entity
		            httpPost.setEntity(se);		 
		            // 7. Set some headers to inform server about the type of the content   
		            httpPost.setHeader("Accept", "application/json");
		            httpPost.setHeader("Content-type", "application/json");
		 
		            // 8. Execute POST request to the given URL
		            HttpResponse httpResponse = httpclient.execute(httpPost);
		 
		            // 9. receive response as inputStream
		         InputStream   inputStream = httpResponse.getEntity().getContent();		   		       
		         BufferedInputStream bufferinstream = new BufferedInputStream(inputStream);

		       ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		       int current = 0;
		       while((current = bufferinstream.read()) != -1){
		           baf.append((byte) current);
		       }
		       FileOutputStream fos = new FileOutputStream( file);
		       fos.write(baf.toByteArray());
		       fos.flush();
		       fos.close();
		       
		       Log.d("DownloadManager" , "download ready in" + ((System.currentTimeMillis() - startTime)/1000) + "sec");
		       int dotindex = fileName.lastIndexOf('.');
		       if(dotindex>=0){
		           fileName = fileName.substring(0,dotindex);		  
		   }
		   }		       
		   catch(IOException e) {
		       Log.d("DownloadManager" , "Error:" + e);
		   }
		   catch(Exception e )
		   {
		    e.printStackTrace();
		   }
		return fileName;	
		}		
	

}
