package org.liutz.service;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class ConnectionServer extends Service {

	Thread mLongThread = null;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (!isNetworkConnection()) {
			showWithoutNetWorkDialog();
			stopSelf();
		}
		mLongThread = new BackgroundThread();

	}

	class BackgroundThread extends Thread {
		List<String> dataList = new ArrayList<String>();
		Handler handler = null;

		public BackgroundThread() {
			handler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					dataList.add((String) msg.obj);
					super.handleMessage(msg);
				}

			};
		}

		public void sendDownloadMsg(String url) {
			Message msg = new Message();
			msg.obj = url;
			handler.sendMessage(msg);
		}

		public void run() {
			while (true) {
				if (dataList.size() > 0) {
					DownloadEnventTask task = new DownloadEnventTask();
					task.equals(dataList.remove(0));
				}
			}
		}
	}

	class DownloadEnventTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Toast.makeText(ConnectionServer.this, "开始下载", Toast.LENGTH_LONG)
					.show();//
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
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

	boolean isNetworkConnection() {
		return false;
	}

}
