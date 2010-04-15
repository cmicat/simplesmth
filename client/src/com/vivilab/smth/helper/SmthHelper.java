package com.vivilab.smth.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vivilab.smth.model.Article;
import com.vivilab.smth.model.Board;

import android.util.Log;

public class SmthHelper {
	public static final int STATE_LOGIN_OK=1;
	public static final int STATE_LOGIN_FAIL=0;
	public static final int STATE_SESSION_OK=1;
	public static final int STATE_SESSION_FAIL=0;
	
	private static final String TAG = "SmthHelper";  

	private static String sessionKey;
	//private final static String gwUrl = "http://vivilab.com/";
	private final static String gwUrl = "http://smth.vivilab.info:8000/";
	private static String currentUser;
	
	static private String httpGet(String url)
	{
		try {
			URL getUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer result=new StringBuffer();
			String oneline;
			while ((oneline = reader.readLine()) != null) 
			{
				result.append(oneline);
			}
			reader.close();
			connection.disconnect();
			return result.toString();
		}
		catch(Exception e)
		{
			Log.e(TAG,"httpGet,httpGet error", e);
			return null;
		}
		
	}

	static private String httpPost(String url, List namevalue) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost postUrl = new HttpPost(url);
		try {
			postUrl.setEntity(new UrlEncodedFormEntity(namevalue,HTTP.UTF_8));
			HttpResponse response;
			response = httpclient.execute(postUrl);
			if (response.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(response.getEntity());
				return strResult;
			} else {
				Log.e(TAG, "httpPost response error");
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "httpPost exception:", e);
			return null;
		}
	}
	
	static public int login(String id,String passwd)
	{
		try{
			String loginUrl = gwUrl+"login?id="+URLEncoder.encode(id, "utf-8")+"&passwd="+URLEncoder.encode(passwd, "utf-8");
			String result=httpGet(loginUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				Log.i(TAG, "login,info="+result);
				String key = json.getString("k");
				if(!key.equals("0"))
				{
					sessionKey=key;
					currentUser=id;
					return STATE_LOGIN_OK;
				}
				else
					return STATE_LOGIN_FAIL;
			}
			else
			{
				Log.i(TAG, "login,result is null");
				return STATE_LOGIN_FAIL;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"login,login error", e);
			return STATE_LOGIN_FAIL;			
		}
	}
	
	static public String getCurrentUser()
	{
		return currentUser;
	}
	
	static public List getFavorate()
	{
		try{
			String favUrl = gwUrl+"getfav?key="+URLEncoder.encode(sessionKey, "utf-8");
			String result=httpGet(favUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"getFavorate session fail");
					return null;
				}
				JSONArray a =json.getJSONArray("r");
				Log.i(TAG, "getfav r:"+a);
				List resultList = new ArrayList();
				
				for(int i=0;i<a.length();i++)
				{
					JSONObject b = a.getJSONObject(i);
					Log.i(TAG,"we are adding"+b.toString());
					Iterator it = b.keys();
					
					resultList.add((String)it.next());
				}
				return resultList;
			}
			else
				return null;
		}catch(Exception e)
		{
			Log.e(TAG,"getFavorate error", e);
			return null;
		}
		
	}
	
	
	static public Board getwz(String name,String pageaddition)
	{
		try{
			String boardUrl;
			if(pageaddition!=null)
			{
				boardUrl =gwUrl+"getboard?ftype=1&key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8")+"&page="+pageaddition;
			}else
				boardUrl = gwUrl+"getboard?ftype=1&key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8");
			String result=httpGet(boardUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"getWz session fail");
					return null;
				}
				JSONArray a =json.getJSONArray("r");
				Log.i(TAG, "getwz r:"+a);
				List resultList = new ArrayList();
				for(int i=0;i<a.length();i++)
				{
					JSONObject b = a.getJSONObject(i);
					Article at = new Article();
					at.setId(b.getString("id"));
					at.setInfo(b.getString("info"));
					at.setTitle(b.getString("title"));
					resultList.add(at);
				}
				Board board = new Board();
				board.setArticles(resultList);
				board.setPpage(json.getInt("pp"));
				board.setNpage(json.getInt("np"));
				board.setFtype(1);
				return board;
			}
			else
			{
				Log.w(TAG,"getwz no result");
				return null;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"getwz error", e);
			return null;
		}
		
	}

	static public Board getmark(String name,String pageaddition)
	{
		try{
			String boardUrl;
			if(pageaddition!=null)
			{
				boardUrl =gwUrl+"getboard?ftype=3&key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8")+"&page="+pageaddition;
			}else
				boardUrl = gwUrl+"getboard?ftype=3&key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8");
			String result=httpGet(boardUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"getMark session fail");
					return null;
				}
				JSONArray a =json.getJSONArray("r");
				Log.i(TAG, "getmark r:"+a);
				List resultList = new ArrayList();
				for(int i=0;i<a.length();i++)
				{
					JSONObject b = a.getJSONObject(i);
					Article at = new Article();
					at.setId(b.getString("id"));
					at.setInfo(b.getString("info"));
					at.setTitle(b.getString("title"));
					resultList.add(at);
				}
				Board board = new Board();
				board.setArticles(resultList);
				board.setPpage(json.getInt("pp"));
				board.setNpage(json.getInt("np"));
				board.setFtype(3);
				return board;
			}
			else
			{
				Log.w(TAG,"getmark no result");
				return null;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"getwz error", e);
			return null;
		}
		
	}

	
	static public Board getboard(String name,String pageaddition)
	{
		try{
			String boardUrl;
			if(pageaddition!=null)
			{
				boardUrl =gwUrl+"getboard?key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8")+"&page="+pageaddition;
			}else
				boardUrl = gwUrl+"getboard?key="+sessionKey+"&board="+URLEncoder.encode(name, "utf-8");
			String result=httpGet(boardUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"getBoard session fail");
					return null;
				}
				JSONArray a =json.getJSONArray("r");
				Log.i(TAG, "getboard r:"+a);
				List resultList = new ArrayList();
				for(int i=0;i<a.length();i++)
				{
					JSONObject b = a.getJSONObject(i);
					Article at = new Article();
					at.setId(b.getString("id"));
					at.setInfo(b.getString("info"));
					at.setTitle(b.getString("title"));
					resultList.add(at);
				}
				Board board = new Board();
				board.setArticles(resultList);
				board.setPpage(json.getInt("pp"));
				board.setNpage(json.getInt("np"));
				board.setFtype(0);
				return board;
			}
			else
			{
				Log.w(TAG,"getboard no result");
				return null;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"getboard error", e);
			return null;
		}
		
	}
	
	static public Article article(String board,String id,String p)
	{
		try{
			String articleUrl;
			if(p!=null)
				articleUrl = gwUrl+"article?key="+sessionKey+"&board="+URLEncoder.encode(board, "utf-8")+"&id="+id+"&p="+p;
			else
				articleUrl = gwUrl+"article?key="+sessionKey+"&board="+URLEncoder.encode(board, "utf-8")+"&id="+id;
			String result=httpGet(articleUrl);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"article session fail");
					return null;
				}
				String content = json.getString("r");
				Log.i(TAG, "article r:"+content);
				Article article = new Article();
				article.setContent(content);
				article.setTitle(json.getString("t"));
				article.setTopid(json.getString("tid"));
				article.setId(json.getString("id"));
				return article;
			}
			else
			{
				Log.w(TAG,"article no result");
				return null;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"article error", e);
			return null;
		}
		
	}
	
	static public int post(String board,String title,String content,String reid)
	{
		try{
			Log.i(TAG,"ready to post:"+title+","+content);
			String postUrl;
			postUrl = gwUrl+"post?key="+sessionKey+"&board="+URLEncoder.encode(board, "utf-8")+"&reid="+reid;
			List<NameValuePair> namevalue=new ArrayList <NameValuePair>();
			namevalue.add(new BasicNameValuePair("title", title));
			namevalue.add(new BasicNameValuePair("content", content));
			String result=httpPost(postUrl,namevalue);
			if(result!=null)
			{
				JSONObject json = new JSONObject(result);
				int login = json.getInt("l");
				if(login != STATE_SESSION_OK)
				{
					Log.w(TAG,"post session fail");
					return 1;
				}

				int state = json.getInt("r");
				Log.i(TAG, "post r:"+state);
				return state;
			}
			else
			{
				Log.w(TAG,"article no post");
				return 1;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"article post error", e);
			return 1;
		}
		
	}
}
