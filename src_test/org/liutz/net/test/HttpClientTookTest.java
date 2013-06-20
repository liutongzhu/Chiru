package org.liutz.net.test;

import java.util.ArrayList;
import java.util.List;
import org.liutz.net.HttpClientTook;
import org.liutz.net.HttpClientTook.IHttpResponse;

import android.test.AndroidTestCase;
import android.util.Log;

public class HttpClientTookTest extends AndroidTestCase {
	
	private static final String TAG ="HttpClientTookTest";
	
	public void testInstance() throws Throwable {
		for(int i=0;i<55;i++){
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpClientTook hct = HttpClientTook.getInstance();
					addRef(hct);
					
					hct.get("http://hq.sinajs.cn/list=sh000001", new IHttpResponse() {
						
						@Override
						public void onResult(int resCode, String body) {
							Log.d(TAG,"resCode:"+resCode);
							
						}
						
						@Override
						public void onError(String err) {
							Log.d(TAG,"onError:"+err);
							
						}
					});
					
					
				}
			});
			t.start();
			t.join();
		}
		Object t = null;
		for(Object o:ref){
			if( t != null && !o.equals(t)){
				Log.d(TAG,"Multiple objects...");
			}else{
				Log.d(TAG,"some objects...");
			}
			t = o;			
		}
	}
	
	private List ref = new ArrayList();
	
	public synchronized void addRef(Object obj){
		ref.add(obj);
	}
	
	public void testGet() throws Throwable {

	}
	
	public void testPost() throws Throwable {

	}
}
