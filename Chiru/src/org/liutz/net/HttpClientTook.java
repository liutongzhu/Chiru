package org.liutz.net;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class HttpClientTook {
	private HttpClient httpClient = null;
	
	private HttpClientTook(){
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); 
		HttpProtocolParams.setUseExpectContinue(params, true);
		ConnManagerParams.setMaxTotalConnections(params,100);
		HttpConnectionParams.setConnectionTimeout(params,3000);
		HttpConnectionParams.setSoTimeout(params,3000);
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schReg);
		httpClient = new DefaultHttpClient(cm, params);
	}
	
	private static class HttpClientHolder{
		private static HttpClientTook httpClient = new HttpClientTook();
	}
	
	public static HttpClientTook getInstance(){
		return HttpClientHolder.httpClient;
	}
	
	public void post(String url,Map<String,String> params,String data,IHttpResponse responseListener){
		HttpPost httppost = null;
		try {
			httppost = new HttpPost(url);
			if(params != null){
				Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
				while (iterator.hasNext()) {
				    Map.Entry<String,String> entry = (Map.Entry) iterator.next();
				    String key = entry.getKey();
				    String val = entry.getValue();
				    httppost.addHeader(key,val);			   
				}
				
			}
			httppost.addHeader("Content-Encoding", "gzip");
			if(null != data){
				StringEntity reqEntity = new StringEntity(data);			
				httppost.setEntity(reqEntity);
			}
			HttpResponse response = httpClient.execute(httppost);
			InputStream gis = null;
			if (matchIgnoreCase(response.getHeaders("Content-Encoding"), "gzip")) {
				gis = new GZIPInputStream(response.getEntity().getContent());
			} else {
				gis = response.getEntity().getContent();
			}		
			int code  = response.getStatusLine().getStatusCode();
			String body = inputStream2String(gis,"UTF-8");
			responseListener.onResult(code, body);
		} catch (Exception e) {
			httppost.abort();
			e.getCause();
			responseListener.onError(e.getLocalizedMessage());
		}
		
					
	}
	
	public void get(String url,IHttpResponse responseListener){	
		HttpGet httpget = null;
		try {			
			httpget = new HttpGet(url);
			httpget.addHeader("accept-encoding", "gzip");
			HttpResponse response = httpClient.execute(httpget);				
			InputStream gis = null;
			if (matchIgnoreCase(response.getHeaders("Content-Encoding"), "gzip")) {
				gis = new GZIPInputStream(response.getEntity().getContent());
			} else {
				gis = response.getEntity().getContent();
			}		
			int code  = response.getStatusLine().getStatusCode();			
			String body = inputStream2String(gis,"UTF-8");
			responseListener.onResult(code, body);
		} catch (Exception e) {
			e.getCause();
			httpget.abort();
			responseListener.onError(e.getLocalizedMessage());
		}
		
	}
	
	public interface IHttpResponse {
        public void onResult(int resCode, String body);
        public void onError(String err);
    }
	
	public static boolean matchIgnoreCase(Header[] headers,String parameter){
		if (headers == null || parameter == null) {
			return false;
		}
		for(Header header:headers){
			if(parameter.equalsIgnoreCase(header.getValue())){
				return true;
			}
		}
		return false;
	}
	
	public static String  inputStream2String  (InputStream  in , String encoding)throws Exception  {    	
    	StringBuffer out = new StringBuffer();    	
        InputStreamReader inread = new InputStreamReader(in,encoding);          
        char[] b = new char[4096];  
        for(int n; (n  = inread.read(b)) != -1;){  
        	out.append(new  String(b,  0,  n));  
        }
        return out.toString();  
    }
	
}


