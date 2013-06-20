package org.liutz.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

public class Interrupt {
	
	private static final String TAG = "Interrupt";
	
	public void run(){
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					synchronized (this) {
						try {
							wait(200);
							Log.d(TAG,"run..");
						} catch (InterruptedException e) {
						}
					}
				}
				
			}
		});
		Log.d(TAG,"Interrupt start");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		es.shutdown();
		Log.d(TAG,"Interrupt started.");
		
	}
}
