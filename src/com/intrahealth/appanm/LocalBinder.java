package com.intrahealth.appanm;

import java.io.FileDescriptor;
import java.lang.ref.WeakReference;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class LocalBinder<S> extends Binder {
//  private String TAG = "LocalBinder";
	
  private  WeakReference<S> mService;
  
  public LocalBinder(S service){
      mService = new WeakReference<S>(service);
  }  
  public S getService() {
      return mService.get();
  }
  
}