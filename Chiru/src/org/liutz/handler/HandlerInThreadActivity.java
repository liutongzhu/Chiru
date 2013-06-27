package org.liutz.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * Eorro~~!!!
 * create Handler in Thread.
 * @author zeus
 *
 */
public class HandlerInThreadActivity extends Activity{
	
	HandlerInThread handlerInThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handlerInThread = new HandlerInThread();
		handlerInThread.sendMessage("liutz");
		showWithoutNetWorkDialog();
	}

	class HandlerInThread extends Thread{
		Handler handler = null;
		public HandlerInThread(){
			handler = new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String message = (String) msg.obj;
					Log.i("HandlerInThread","Message:"+message);
				}
				
			};
		}
		public void sendMessage(String message) {
			Message msg = new Message();
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
	
	void showWithoutNetWorkDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setButton("设置", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});

		dialog.show();
	}

}