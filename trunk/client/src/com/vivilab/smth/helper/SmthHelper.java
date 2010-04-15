package com.vivilab.smth.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.vivilab.smth.model.Article;
import com.vivilab.smth.model.Board;

import android.util.Log;

public class SmthHelper {
	private static final String TAG = "SmthHelper";  

	private static String sessionKey;
	//private final static String gwUrl = "http://vivilab.com/";
	private final static String gwUrl = "http://192.168.1.88:8000/";
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
			Log.e("SmthHelper","httpGet,login error", e);
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
				Log.i(TAG, "login,loginOK,key="+result);
				sessionKey=result;
				currentUser=id;
				return 1;
			}
			else
			{
				Log.i(TAG, "login,result is null");
				return 0;
			}
		}catch(Exception e)
		{
			Log.e(TAG,"login,login error", e);
			return 0;			
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
}
